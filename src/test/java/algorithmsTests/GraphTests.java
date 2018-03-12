package algorithmsTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import elasticSearch.SearchResult;
import factfinders.AverageLog;
import factfinders.InitializeBeliefs;
import factfinders.Investment;
import factfinders.Sums;
import factfinders.Truthfinder;
import graphPlotter.CreateGraph;
import trainingGraph.TrainingResponse;

public class GraphTests {

	public String claim = new String();
	public Set<String> sources = new LinkedHashSet<String>();
	public HashMap<String, Set<String>> training = new HashMap<String, Set<String>>();
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
		sources.add("s6");
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
		result.sources.add("s4");
		response = newEdge.addEdge(response, result);
		response.graph = beliefs.initialize(response.graph, result.claim, "testing");
	}
	
	@Test
	public void graphTest() {
		assertNotNull(response.graph.vertexKeys().size());
		assertNotNull(response.graph.getEdges().size());
		assertEquals(response.claims.size(), 2);
		assertEquals(response.sources.size(), 5);
		assertEquals(response.graph.vertexKeys().size(), 7);
		assertEquals(response.graph.getEdges().size(), 8);
	}

}
