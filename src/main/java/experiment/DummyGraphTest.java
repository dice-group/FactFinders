package experiment;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import elasticSearch.SearchResult;
import factfinders.AverageLog;
import factfinders.InitializeBeliefs;
import factfinders.Investment;
import factfinders.Sums;
import factfinders.Truthfinder;
import graphConstruct.Vertex;
import graphPlotter.CreateGraph;
import trainingGraph.TrainingResponse;

/**
 * Minimal graph experiment just as Proof of concept for algorithms
 * Variants of this dummy test are also presented as JUnit tests
 * @author Hussain
 *
 */
public class DummyGraphTest {
	public static void main(String[] args) throws IOException {
		String claim = new String();
		Set<String> sources = new LinkedHashSet<String>();
		HashMap<String, Set<String>> training = new HashMap<String, Set<String>>();
		TrainingResponse response = new TrainingResponse();
		InitializeBeliefs beliefs = new InitializeBeliefs();
		CreateGraph newEdge = new CreateGraph();
		SearchResult result = new SearchResult();
		FileWriter writer = new FileWriter("./src/main/resources/data/clueweb_graph.tsv");
		Sums sums = new Sums();
		AverageLog avg = new AverageLog();
		Truthfinder tf = new Truthfinder();
		Investment inv = new Investment();

		//		String domain = getDomainName("https://en.wikipedia.org/wiki/Albert_Einstein");

		/**
		 * Adding training data
		 */
		claim = "c1";
		sources.add("s1");
		sources.add("s2");
		sources.add("s3");
		training.put(claim, sources);

		claim = new String();
		sources = new LinkedHashSet<String>();

		claim = "c2";
		sources.add("s1");
		sources.add("s4");
		sources.add("s5");
		training.put(claim, sources);

		claim = new String();
		sources = new LinkedHashSet<String>();

		claim = "c3";
		sources.add("s1");
		sources.add("s2");
		sources.add("s6");
		training.put(claim, sources);

		CreateGraph create = new CreateGraph(training);
		response.graph = create.getGraph();
		response.claims = create.getClaims();
		response.sources = create.getSources();

		/**
		 * Initializing claims Belief Score for training claims to 1.0
		 */
		response.graph = beliefs.initialize(response.graph, response.claims);

		/**
		 * Populating test data in the graph
		 */

		result.claim = "c4";
		result.sources.add("s1");
		result.sources.add("s7");
		result.sources.add("s8");
		response = newEdge.addEdge(response, result);
		response.graph = beliefs.initialize(response.graph, result.claim);

		result = new SearchResult();

		result.claim = "c5";
		result.sources.add("s9");
		result.sources.add("s10");
		result.sources.add("s11");
		response = newEdge.addEdge(response, result);
		response.graph = beliefs.initialize(response.graph, result.claim);

		result = new SearchResult();

		result.claim = "c6";
		result.sources.add("s2");
		result.sources.add("s5");
		result.sources.add("s8");
		response = newEdge.addEdge(response, result);
		
		/**
		 * Initializing Belief Scores for test data to be 0.5 
		 */
		response.graph = beliefs.initialize(response.graph, result.claim);

		/**
		 * Generating proof of Vertices with multiple edges
		 */
		for(String eachClaim : response.claims) {
			if(response.graph.containsVertex(eachClaim)) {
				writer.append(response.graph.getVertex(eachClaim).getLabel());
				writer.append("\t");
				writer.append(Double.toString(response.graph.getVertex(eachClaim).getScore()));
				writer.append("\t");
				writer.append(Double.toString(response.graph.getVertex(eachClaim).getNeighborCount()));
				writer.append("\t");
				writer.append(response.graph.getVertex(eachClaim).getNeighbors().toString());
				writer.append("\n");
				for(int x = 0; x < response.graph.getVertex(eachClaim).getNeighborCount(); x++) {
					writer.append(response.graph.getVertex(eachClaim).getNeighbor(x).getSource().getLabel());
					writer.append("\t");
					writer.append(response.graph.getVertex(response.graph.getVertex(eachClaim).getNeighbor(x).getSource().getLabel()).getNeighbors().toString());
					writer.append("\n");
				}
			}
		}

		writer.flush();
		writer.close();

		System.out.println("Total Sources: " + response.sources.size());
		System.out.println("Total Claims: " + response.claims.size());
		System.out.println("Total Edges: " + response.graph.getEdges().size());

		/**
		 * Run Algorithm
		 */
		inv.trustScore(response.graph, response.sources);
		for(int i = 0; i < 3; i++) {
			inv.trustScore(response.graph, response.sources);
			inv.beliefScore(response.graph, response.claims);
		}

		System.out.println("Finalized trust scores are");

		/**
		 * Print the finalized Belief Scores of test claims after the execution of algorithms
		 */
		for(Vertex v : response.graph.vertices()) {
			if(response.claims.contains(v))
				System.out.println(v.getLabel() +"="+ v.getScore());
		}

	}
}
