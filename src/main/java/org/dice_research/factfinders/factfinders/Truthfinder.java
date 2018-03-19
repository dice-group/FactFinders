package org.dice_research.factfinders.factfinders;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.graph.SimpleGraph;
import org.dice_research.factfinders.graphPlotter.Vertex;


/**
 * Originally by Yin et al., 2008
 * Calculates the probability of a claim by assuming that each sourse's
 * trust score is the probability of being correct and then average the claim's score
 * @author Hussain
 *
 */
public class Truthfinder implements Scores {
	
	private Normalize normalize;
	
	public Truthfinder() {
		this.normalize = new Normalize();
	}

	/**
	 * It takes the probability of a claim by assuming that each source's trust score is 
	 * the probability of it being correct
	 */
	@Override
	public void beliefScore(SimpleGraph graph) {
		double probScore = 1.0;
		double maxScore = 0.0;
		Vertex singleVertex, sourceVertex;
		Edge singleEdge;
		for(Object vertex : graph.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getType() == 'c') {
				for(Object edge : graph.edgesOf(singleVertex)) {
					singleEdge = (Edge) edge;
					sourceVertex = (Vertex)singleEdge.getTarget();
					probScore *=  1-sourceVertex.getScore();
				}
				singleVertex.setScore(probScore);
				probScore = 1.0;
			}
		}
		maxScore = normalize.maxScoreFinder(graph, 'c'); 
		normalize.avoidOverflow(graph, maxScore, 'c');
	}

	/**
	 * Average of sum of Belief scores of the all the claims being committed by this source 
	 */
	@Override
	public void trustScore(SimpleGraph graph) {
		double truthScore = 0;
		int claimSet = 0;
		double maxScore = 0.0;
		Vertex singleVertex, sourceVertex;
		Edge singleEdge;
		for(Object vertex : graph.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getType() == 's') {
				for(Object edge : graph.edgesOf(singleVertex)) {
					singleEdge = (Edge) edge;
					sourceVertex = (Vertex)singleEdge.getSource();
					truthScore +=  sourceVertex.getScore();
				}
				claimSet = graph.degreeOf(singleVertex);
				truthScore = (truthScore / claimSet);
				singleVertex.setScore(truthScore);
				truthScore = 0;
			}
		}
		maxScore = normalize.maxScoreFinder(graph, 's'); 
		normalize.avoidOverflow(graph, maxScore, 's');
	}
}
