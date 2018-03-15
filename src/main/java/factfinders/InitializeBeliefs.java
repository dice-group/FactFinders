package factfinders;

import java.util.Set;

import graphConstruct.Graph;

/**
 * Beliefs are to be initialized before the execution of algorithms
 * Training claims are initialized with "1.0"; the certainty of being facts
 * Test claims are initialized with "0.5"; the ambiguity between facts/false claims
 * @author Hussain
 *
 */
public class InitializeBeliefs {
	
	private double trainBelief;
	private double testBelief;
	
	public InitializeBeliefs(double trainBelief, double testBelief) {
		this.trainBelief = trainBelief;
		this.testBelief = testBelief;
	}
	
	public InitializeBeliefs() {
		this.trainBelief = 1.0;
		this.testBelief = 0.5;
	}

	public Graph initialize(Graph graph, Set<String> claims) {	
		for(String c : claims) {
			graph.getVertex(c).setScore(trainBelief);  
		}
		return graph;
	}

	public Graph initialize(Graph graph, String claim) {
		
		graph.getVertex(claim).setScore(testBelief); 
		return graph;
	}

}
