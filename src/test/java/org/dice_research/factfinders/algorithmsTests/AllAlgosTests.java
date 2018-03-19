package org.dice_research.factfinders.algorithmsTests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org._3pq.jgrapht.graph.SimpleGraph;
import org.dice_research.factfinders.elasticSearch.SearchResult;
import org.dice_research.factfinders.factfinders.AverageLog;
import org.dice_research.factfinders.factfinders.Investment;
import org.dice_research.factfinders.factfinders.Sums;
import org.dice_research.factfinders.factfinders.Truthfinder;
import org.dice_research.factfinders.graphPlotter.CreateGraph;
import org.dice_research.factfinders.graphPlotter.Vertex;
import org.junit.Before;
import org.junit.Test;

/**
 * It tests for all the algorithms for a very small graph.
 * Algorithms were solved manually to verify the assertions
 * @author Hussain
 *
 */
public class AllAlgosTests {

	public String claim = new String();
	public Set<String> sources = new LinkedHashSet<String>();
	public HashMap<String, Set<String>> training = new HashMap<String, Set<String>>();
	public SimpleGraph response = new SimpleGraph();
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
		sources.add("s1");
		training.put(claim, sources);
		
		CreateGraph create = new CreateGraph(training);
		response = create.getGraph();
		
		/**
		 * BulkClaimTest
		 */
		result.claim = "c2";
		result.sources.add("s1");
		result.sources.add("s2");
		result.sources.add("s3");
		response = create.addEdge(response, result);
	}
	
	@Test
	public void sumsTest() {
		for(int i = 0; i < 2; i++) {
	 	   	sums.trustScore(response);
	        sums.beliefScore(response);
			}
		
		Vertex singleVertex;
		for(Object vertex : response.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getLabel().equals("c1"))
				assertEquals(singleVertex.getScore(), 1, 0.01);
			else if (singleVertex.getLabel().equals("c2"))
				assertEquals(singleVertex.getScore(), 1, 0.01);
		}
	}
	
	@Test
	public void AvgLogTest() {
		for(int i = 0; i < 2; i++) {
	 	   	avg.trustScore(response);
	        avg.beliefScore(response);
			}
		
		Vertex singleVertex;
		for(Object vertex : response.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getLabel().equals("c1"))
				assertEquals(singleVertex.getScore(), 1, 0.01);
			else if (singleVertex.getLabel().equals("c2"))
				assertEquals(singleVertex.getScore(), 1, 0.01);
		}
	}
	
	@Test
	public void investmnetTest() {
		inv.trustScore(response);
		for(int i = 0; i < 2; i++) {
	 	   	inv.trustScore(response);
	        inv.beliefScore(response);
			}
		
		Vertex singleVertex;
		for(Object vertex : response.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getLabel().equals("c1"))
				assertEquals(singleVertex.getScore(), 1, 0.01);
			else if (singleVertex.getLabel().equals("c2"))
				assertEquals(singleVertex.getScore(), 0.22, 0.01);
		}
	}
	
	@Test
	public void tfTest() {
		for(int i = 0; i < 2; i++) {
	 	   	tf.trustScore(response);
	        tf.beliefScore(response);
			}
		
		Vertex singleVertex;
		for(Object vertex : response.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getLabel().equals("c1"))
				assertEquals(singleVertex.getScore(), 1, 0.01);
			else if (singleVertex.getLabel().equals("c2"))
				assertEquals(singleVertex.getScore(), 0.99, 0.01);
		}
	}
	
}
