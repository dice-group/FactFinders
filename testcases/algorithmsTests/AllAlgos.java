package algorithmsTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import demo.CreateGraph;
import elasticSearch.Result;
import experiment.TrainingResponse;
import factFinders.AverageLog;
import factFinders.InitializeBeliefs;
import factFinders.Investment;
import factFinders.Sums;
import factFinders.Truthfinder;
import graphPlotter.Vertex;

public class AllAlgos {

	public String claim = new String();
	public ArrayList<String> sources = new ArrayList<String>();
	public HashMap<String, ArrayList<String>> training = new HashMap<String, ArrayList<String>>();
	public TrainingResponse response = new TrainingResponse();
	public InitializeBeliefs beliefs = new InitializeBeliefs();
	public CreateGraph newEdge = new CreateGraph();
	public Result result = new Result();
	public Sums sums = new Sums();
	public AverageLog avg = new AverageLog();
	public Truthfinder tf = new Truthfinder();
	public Investment inv = new Investment();
	
	@Before
	public void plotDummyGraph(){
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
		
		result = new Result();
		
		result.claim = "c5";
		result.sources.add("s9");
		result.sources.add("s10");
		result.sources.add("s11");
		response = newEdge.addEdge(response, result);
		response.graph = beliefs.initialize(response.graph, result.claim, "testing");
		
		result = new Result();
		
		result.claim = "c6";
		result.sources.add("s2");
		result.sources.add("s5");
		result.sources.add("s8");
		response = newEdge.addEdge(response, result);
		response.graph = beliefs.initialize(response.graph, result.claim, "testing");
	}
	
	@Test
	public void graphTest() {
		assertNotNull(response.graph.vertexKeys().size());
		assertNotNull(response.graph.getEdges().size());
		assertEquals(response.claims.size(), 6);
		assertEquals(response.sources.size(), 11);
		assertEquals(response.graph.vertexKeys().size(), 17);
		assertEquals(response.graph.getEdges().size(), 18);
	}
	
	@Test
	public void sumsTest() {
		for(int i = 0; i < 20; i++) {
	 	   	sums.trustScore(response.graph, response.sources);
	        sums.beliefScore(response.graph, response.claims);
			}
		System.out.println("Finalized Sums trust scores are");

		  for(Vertex v : response.graph.vertices()) {
			   if(response.claims.contains(v))
				  System.out.println(v.getLabel() +"="+ v.getScore());
		  }
	}
	
	@Test
	public void AvgLogTest() {
		for(int i = 0; i < 20; i++) {
	 	   	avg.trustScore(response.graph, response.sources);
	        avg.beliefScore(response.graph, response.claims);
			}
		System.out.println("Finalized Avg-Log trust scores are");

		  for(Vertex v : response.graph.vertices()) {
			   if(response.claims.contains(v))
				  System.out.println(v.getLabel() +"="+ v.getScore());
		  }
	}
	
	@Test
	public void investmnetTest() {
		for(int i = 0; i < 20; i++) {
	 	   	inv.trustScore(response.graph, response.sources);
	        inv.beliefScore(response.graph, response.claims);
			}
		System.out.println("Finalized Investmnets trust scores are");

		  for(Vertex v : response.graph.vertices()) {
			   if(response.claims.contains(v))
				  System.out.println(v.getLabel() +"="+ v.getScore());
		  }
	}
	
	@Test
	public void tfTest() {
		for(int i = 0; i < 20; i++) {
	 	   	tf.trustScore(response.graph, response.sources);
	        tf.beliefScore(response.graph, response.claims);
			}
		System.out.println("Finalized TruthFinder trust scores are");

		  for(Vertex v : response.graph.vertices()) {
			   if(response.claims.contains(v))
				  System.out.println(v.getLabel() +"="+ v.getScore());
		  }
	}

}
