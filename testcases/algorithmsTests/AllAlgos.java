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
		sources.add("s3");
		training.put(claim, sources);
		
		CreateGraph create = new CreateGraph(training);
		response.graph = create.getGraph();
		response.claims = create.getClaims();
		response.sources = create.getSources();
		
		response.graph = beliefs.initialize(response.graph, response.claims, "training");
		
		/**
		 * Testing
		 */
		result.claim = "c2";
		result.sources.add("s1");
		result.sources.add("s2");
		response = newEdge.addEdge(response, result);
		response.graph = beliefs.initialize(response.graph, result.claim, "testing");
	}
	
	@Test
	public void graphTest() {
		assertNotNull(response.graph.vertexKeys().size());
		assertNotNull(response.graph.getEdges().size());
		assertEquals(response.claims.size(), 2);
		assertEquals(response.sources.size(), 3);
		assertEquals(response.graph.vertexKeys().size(), 5);
		assertEquals(response.graph.getEdges().size(), 4);
	}
	
	@Test
	public void sumsTest() {
		for(int i = 0; i < 2; i++) {
	 	   	sums.trustScore(response.graph, response.sources);
	        sums.beliefScore(response.graph, response.claims);
			}
		
		assertEquals(response.graph.getVertex("c1").getScore(), 1, 0.01);
		assertEquals(response.graph.getVertex("c2").getScore(), 0.93, 0.01);
	}
	
	@Test
	public void AvgLogTest() {
		for(int i = 0; i < 2; i++) {
	 	   	avg.trustScore(response.graph, response.sources);
	        avg.beliefScore(response.graph, response.claims);
			}
		
		assertEquals(response.graph.getVertex("c1").getScore(), 1, 0.01);
		assertEquals(response.graph.getVertex("c2").getScore(), 1, 0.01);
	}
	
	@Test
	public void investmnetTest() {
		inv.trustScore(response.graph, response.sources);
		for(int i = 0; i < 2; i++) {
	 	   	inv.trustScore(response.graph, response.sources);
	        inv.beliefScore(response.graph, response.claims);
			}
		
		assertEquals(response.graph.getVertex("c1").getScore(), 1, 0.01);
		assertEquals(response.graph.getVertex("c2").getScore(), 0.22, 0.01);
	}
	
	@Test
	public void tfTest() {
		for(int i = 0; i < 2; i++) {
	 	   	tf.trustScore(response.graph, response.sources);
	        tf.beliefScore(response.graph, response.claims);
			}
		assertEquals(response.graph.getVertex("c1").getScore(), 1, 0.01);
		assertEquals(response.graph.getVertex("c2").getScore(), 0.99, 0.01);
	}

}
