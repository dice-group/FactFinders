package algorithmsTests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.graph.DefaultDirectedGraph;
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

/**
 * This simple JUnit Test verifies the creation of directed graph by 
 * JGraphT library
 * @author Hussain
 *
 */
public class JGraphTTests {

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
	DirectedGraph graph = new DefaultDirectedGraph();
	
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
		
		graph.addVertex(claim);
		graph.addAllVertices(sources);
		for(String source : sources)
			graph.addEdge(claim, source);
		
		/**
		 * BulkClaimTest
		 */
		result.claim = "c2";
		result.sources.add("s1");
		result.sources.add("s2");
		result.sources.add("s3");
		result.sources.add("s4");
		graph.addVertex(result.claim);
		graph.addAllVertices(result.sources);
		for(String source : result.sources)
			graph.addEdge(result.claim, source);
	}
	
	@Test
	public void graphTest() {
		assertNotNull(graph.vertexSet().size());
		assertNotNull(graph.edgeSet().size());
		assertEquals(graph.vertexSet().size(), 7);
		assertEquals(graph.edgeSet().size(), 8);
	}

}
