package org.dice_research.factfinders.factfinders;

import org._3pq.jgrapht.graph.SimpleGraph;
import org.dice_research.factfinders.graphPlotter.Vertex;

/**
 * Beliefs are to be initialized before the execution of algorithms
 * Training claims are initialized with "1.0"; the certainty of being facts
 * Test claims are initialized with "0.5"; the ambiguity between facts/false claims
 * @author Hussain
 *
 */
public class InitializeBeliefs {

	public SimpleGraph initialize(SimpleGraph graph) {
		double trainBelief = 1.0;
		Vertex singleVertex;
		for(Object vertex : graph.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getType() == 'c') {
				singleVertex.setScore(trainBelief);;
			}
		}
		return graph;
	}
	
	public SimpleGraph initialize(SimpleGraph graph, String claim ) {	
		Vertex singleVertex;
		double testBelief = 0.5;
		for(Object vertex : graph.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getType() == 'c' && singleVertex.getLabel().equals(claim)) {
				singleVertex.setScore(testBelief);;
			}
		}
		return graph;
	}
}
