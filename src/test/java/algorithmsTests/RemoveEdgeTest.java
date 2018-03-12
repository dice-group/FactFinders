package algorithmsTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import elasticSearch.SearchResult;
import factFinders.AverageLog;
import factFinders.InitializeBeliefs;
import factFinders.Investment;
import factFinders.Sums;
import factFinders.Truthfinder;
import graphPlotter.CreateGraph;
import trainingGraph.TrainingResponse;

public class RemoveEdgeTest {

	public String claim = new String();
	public ArrayList<String> sources = new ArrayList<String>();
	public HashMap<String, ArrayList<String>> training = new HashMap<String, ArrayList<String>>();
	public TrainingResponse response = new TrainingResponse();
	public InitializeBeliefs beliefs = new InitializeBeliefs();
	public CreateGraph newEdge = new CreateGraph();
	public SearchResult result = new SearchResult();
	public Sums sums = new Sums();
	public AverageLog avg = new AverageLog();
	public Truthfinder tf = new Truthfinder();
	public Investment inv = new Investment();
	
	@Before
	public void plotDummyGraph(){
		/**
		 * TrainingWithSearch
		 */
		claim = "c1";
		sources.add("s1");
		sources.add("s3");
		sources.add("s2");
		training.put(claim, sources);
		
		CreateGraph create = new CreateGraph(training);
		response.graph = create.getGraph();
		response.claims = create.getClaims();
		response.sources = create.getSources();
		
		response.graph = beliefs.initialize(response.graph, response.claims, "training");
		
		/**
		 * BulkClaimTest
		 */
		result.claim = "c2";
		result.sources.add("s1");
		result.sources.add("s2");
		result.sources.add("s3");
		response = newEdge.addEdge(response, result);
		response.graph = beliefs.initialize(response.graph, result.claim, "testing");
	}
	
	@Test
	public void edgeRemovalTest() {
		response = newEdge.removeEdge(response, result);
		assertNotNull(response.graph.vertexKeys().size());
		assertNotNull(response.graph.getEdges().size());
		assertEquals(response.claims.size(), 1);
		assertEquals(response.sources.size(), 3);
		assertEquals(response.graph.vertexKeys().size(), 4);
		assertEquals(response.graph.getEdges().size(), 3);
	}

}
