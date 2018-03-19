package org.dice_research.factfinders.experiment;

import static org.junit.Assert.assertEquals;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org._3pq.jgrapht.graph.SimpleGraph;
import org.dice_research.factfinders.elasticSearch.SearchResult;
import org.dice_research.factfinders.factfinders.AverageLog;
import org.dice_research.factfinders.factfinders.InitializeBeliefs;
import org.dice_research.factfinders.factfinders.Investment;
import org.dice_research.factfinders.factfinders.Sums;
import org.dice_research.factfinders.factfinders.Truthfinder;
import org.dice_research.factfinders.graphPlotter.CreateGraph;
import org.dice_research.factfinders.graphPlotter.Vertex;

/**
 * Minimal graph org.dice_research.factfinders.experiment just as Proof of concept for algorithms
 * Variants of this dummy test are also presented as JUnit tests
 * @author Hussain
 *
 */
public class DummyGraphTest {
	public static void main(String[] args) throws IOException {
		String claim = new String();
		Set<String> sources = new LinkedHashSet<String>();
		HashMap<String, Set<String>> training = new HashMap<String, Set<String>>();
		InitializeBeliefs beliefs = new InitializeBeliefs();
		SimpleGraph response = new SimpleGraph();
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
		response = create.getGraph();

		/**
		 * Populating test data in the graph
		 */

		result.claim = "c4";
		result.sources.add("s1");
		result.sources.add("s7");
		result.sources.add("s8");
		response = create.addEdge(response, result);

		result = new SearchResult();

		result.claim = "c5";
		result.sources.add("s9");
		result.sources.add("s10");
		result.sources.add("s11");
		response = create.addEdge(response, result);

		result = new SearchResult();

		result.claim = "c6";
		result.sources.add("s2");
		result.sources.add("s5");
		result.sources.add("s8");
		response = create.addEdge(response, result);

		System.out.println("Total Vertices: " + response.vertexSet().size());
		System.out.println("Total Edges: " + response.edgeSet().size());

		/**
		 * Run Algorithm
		 */
		for(int i = 0; i < 3; i++) {
			avg.trustScore(response);
			avg.beliefScore(response);
		}

		System.out.println("Finalized trust scores are");

		/**
		 * Print the finalized Belief Scores of test claims after the execution of algorithms
		 */
		Vertex singleVertex;
		for(Object vertex : response.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getType() == 'c')
				System.out.println(singleVertex.getLabel() +" = "+ singleVertex.getScore());
		}

	}
}
