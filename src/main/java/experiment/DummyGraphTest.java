package experiment;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import demo.CreateGraph;
import elasticSearch.SearchResult;
import factFinders.AverageLog;
import factFinders.InitializeBeliefs;
import factFinders.Investment;
import factFinders.PooledInvestment;
import factFinders.Sums;
import factFinders.Truthfinder;
import graphPlotter.Vertex;

/**
 * Minimal graph experiment just as Proof of concept for algorithms
 * @author Hussain
 *
 */
public class DummyGraphTest {
	public static void main(String[] args) throws IOException {
		String claim = new String();
		ArrayList<String> sources = new ArrayList<String>();
		HashMap<String, ArrayList<String>> training = new HashMap<String, ArrayList<String>>();
		TrainingResponse response = new TrainingResponse();
		InitializeBeliefs beliefs = new InitializeBeliefs();
		CreateGraph newEdge = new CreateGraph();
		SearchResult result = new SearchResult();
		FileWriter writer = new FileWriter("./src/main/resources/clueweb_graph.tsv");
		Sums sums = new Sums();
		AverageLog avg = new AverageLog();
		Truthfinder tf = new Truthfinder();
		Investment inv = new Investment();
		PooledInvestment pool = new PooledInvestment();
		
//		String domain = getDomainName("https://en.wikipedia.org/wiki/Albert_Einstein");
		
		/**
		 * Training
		 */
		claim = "c1";
		sources.add("s1");
		sources.add("s2");
		sources.add("s3");
		training.put(claim, sources);
		
		claim = new String();
		sources = new ArrayList<String>();
		
		claim = "c2";
		sources.add("s1");
		sources.add("s4");
		sources.add("s5");
		training.put(claim, sources);
		
		claim = new String();
		sources = new ArrayList<String>();
		
		claim = "c3";
		sources.add("s1");
		sources.add("s2");
		sources.add("s6");
		training.put(claim, sources);
		
		CreateGraph create = new CreateGraph(training);
		response.graph = create.getGraph();
		response.claims = create.getClaims();
		response.sources = create.getSources();
		
		response.graph = beliefs.initialize(response.graph, response.claims, "training");
		
		/**
		 * Testing
		 */
		
		result.claim = "c4";
		result.sources.add("s1");
		result.sources.add("s7");
		result.sources.add("s8");
		response = newEdge.addEdge(response, result);
		response.graph = beliefs.initialize(response.graph, result.claim, "testing");
		
		result = new SearchResult();
		
		result.claim = "c5";
		result.sources.add("s9");
		result.sources.add("s10");
		result.sources.add("s11");
		response = newEdge.addEdge(response, result);
		response.graph = beliefs.initialize(response.graph, result.claim, "testing");
		
		result = new SearchResult();
		
		result.claim = "c6";
		result.sources.add("s2");
		result.sources.add("s5");
		result.sources.add("s8");
		response = newEdge.addEdge(response, result);
		response.graph = beliefs.initialize(response.graph, result.claim, "testing");
		
		for(Vertex eachClaim : response.claims) {
		if(response.graph.containsVertex(eachClaim)) {
			writer.append(response.graph.getVertex(eachClaim.getLabel()).getLabel());
			writer.append("\t");
			writer.append(Double.toString(response.graph.getVertex(eachClaim.getLabel()).getScore()));
			writer.append("\t");
			writer.append(Double.toString(response.graph.getVertex(eachClaim.getLabel()).getNeighborCount()));
			writer.append("\t");
			writer.append(response.graph.getVertex(eachClaim.getLabel()).getNeighbors().toString());
			writer.append("\n");
			for(int x = 0; x < response.graph.getVertex(eachClaim.getLabel()).getNeighborCount(); x++) {
				writer.append(response.graph.getVertex(eachClaim.getLabel()).getNeighbor(x).getSource().getLabel());
				writer.append("\t");
				writer.append(response.graph.getVertex(response.graph.getVertex(eachClaim.getLabel()).getNeighbor(x).getSource().getLabel()).getNeighbors().toString());
				writer.append("\n");
			}
		}
	}
	
	writer.flush();
	writer.close();
	
	System.out.println("Total Sources: " + response.sources.size());
	System.out.println("Total Claims: " + response.claims.size());
	System.out.println("Total Edges: " + response.graph.getEdges().size());
	
	inv.trustScore(response.graph, response.sources);
	for(int i = 0; i < 3; i++) {
 	   	inv.trustScore(response.graph, response.sources);
        inv.beliefScore(response.graph, response.claims);
		}
	
	System.out.println("Finalized trust scores are");

	  for(Vertex v : response.graph.vertices()) {
		   if(response.claims.contains(v))
			  System.out.println(v.getLabel() +"="+ v.getScore());
	  }
	
	}
	
	public static String getDomainName(String url) throws MalformedURLException{
	    if(!url.startsWith("http") && !url.startsWith("https")){
	         url = "http://" + url;
	    }        
	    URL netUrl = new URL(url);
	    String host = netUrl.getHost();
	    if(host.startsWith("www")){
	        host = host.substring("www".length()+1);
	    }
	    return host;
	}
}
