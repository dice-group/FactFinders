package org.dice_research.factfinders.algorithmsTests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org._3pq.jgrapht.graph.SimpleGraph;
import org.dice_research.factfinders.elasticSearch.SearchResult;
import org.dice_research.factfinders.graphPlotter.CreateGraph;
import org.junit.Before;
import org.junit.Test;

/**
 * Edge removal is tested via this test.
 * It verifies the removal of only the targeted edge, if either source or claim are shared between many edges.
 * @author Hussain
 *
 */
public class RemoveEdgeTest {

	public String claim = new String();
	public Set<String> sources = new LinkedHashSet<String>();
	public HashMap<String, Set<String>> training = new HashMap<String, Set<String>>();
	public SimpleGraph response = new SimpleGraph();
	public SearchResult result = new SearchResult();
	public CreateGraph create;
	
	@Before
	public void plotDummyGraph(){
		/**
		 * Training
		 */
		claim = "c1";
		sources.add("s1");
		sources.add("s3");
		sources.add("s2");
		sources.add("s6");
		training.put(claim, sources);
		
		create = new CreateGraph(training);
		response = create.getGraph();
		
		/**
		 * Single Claim test
		 */
		result.claim = "c2";
		result.sources.add("s1");
		result.sources.add("s2");
		result.sources.add("s3");
		result.sources.add("s4");
		response = create.addEdge(response, result);
	}
	
	@Test
	public void edgeRemovalTest() {
		response = create.removeEdge(response, result);
		assertNotNull(response.vertexSet().size());
		assertNotNull(response.edgeSet().size());
		assertEquals(response.vertexSet().size(), 5);
		assertEquals(response.edgeSet().size(), 4);
	}

}
