package org.dice_research.factfinders.factfinders;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.graph.SimpleGraph;
import org.dice_research.factfinders.graphPlotter.Vertex;

/**
 * (Originally by Pasternack) Computing trust score as an average of belief in its claims
 * It over-estimates the trustworthiness of a source with relatively few claims
 * @author Hussain
 */
public class AverageLog implements Scores {

	private Normalize normalize;

	public AverageLog() {
		this.normalize = new Normalize();
	}

	/**
	 * Belief scores are computed by adding the Trust Scores of the sources commiting the claim
	 */
	@Override
	public void beliefScore(SimpleGraph graph) {
		double beliefScore = 0;
		double maxScore = 0;
		Vertex singleVertex, sourceVertex;
		Edge singleEdge;
		for(Object vertex : graph.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getType() == 'c') {
				for(Object edge : graph.edgesOf(singleVertex)) {
					singleEdge = (Edge) edge;
					sourceVertex = (Vertex)singleEdge.getTarget();
					beliefScore +=  sourceVertex.getScore();
				}
				singleVertex.setScore(beliefScore);
				beliefScore = 0;
			}
		}
		maxScore = normalize.maxScoreFinder(graph, 'c');
		normalize.avoidOverflow(graph, maxScore, 'c');
	}

	/**
	 * Trust scores for each score is calculated by adding the neighboring claims
	 * This sum is averaged by the number of neighbors and enhanced by the Log-10 of neighbors count   
	 */
	@Override
	public void trustScore(SimpleGraph graph) {
		double maxScore = 0;
		double avgScore = 0;
		int claimSet= 0;
		Vertex singleVertex, sourceVertex;
		Edge singleEdge;
		for(Object vertex : graph.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getType() == 's') {
				for(Object edge : graph.edgesOf(singleVertex)) {
					singleEdge = (Edge) edge;
					sourceVertex = (Vertex)singleEdge.getSource();
					avgScore +=  sourceVertex.getScore();
				}
				claimSet = graph.degreeOf(singleVertex);
				avgScore = (avgScore / claimSet)*Math.log10(claimSet);
				singleVertex.setScore(avgScore);
				avgScore = 0;
			}
		}
		maxScore = normalize.maxScoreFinder(graph, 's');
		normalize.avoidOverflow(graph, maxScore, 'c');
	}
}
