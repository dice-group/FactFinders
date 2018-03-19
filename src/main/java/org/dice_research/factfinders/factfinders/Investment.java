package org.dice_research.factfinders.factfinders;

import java.util.HashMap;
import java.util.Map;
import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.graph.SimpleGraph;
import org.dice_research.factfinders.graphPlotter.Vertex;

/**
 * (Originally by Pasternack) Sources "invest" their trust uniformly in claims.
 * Belief in each claim grows according to a non linear function G where G=x^g,
 * where g=1.2.
 *
 * @author Hussain
 */
public class Investment implements Scores {
	
	private Normalize normalize;

	public Investment() {
		this.normalize = new Normalize();
	}

	/**
	 * Belief scores are calculated by taking the ratio of the score of the source investing in a particular claim
	 * with all the claims it invests in. The score of every claim is raised to a fixed power of "1.2" to 
	 * accomplish non-linearity of belief scores
	 */
	@Override
	public void beliefScore(SimpleGraph graph) {
		double invScore = 0;
		int claimSet = 0;
		double maxScore = 0;
		Vertex singleVertex, sourceVertex;
		Edge singleEdge;
		for(Object vertex : graph.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getType() == 'c') {
				for(Object edge : graph.edgesOf(singleVertex)) {
					singleEdge = (Edge) edge;
					sourceVertex = (Vertex)singleEdge.getTarget();
					claimSet = graph.degreeOf(sourceVertex);
					invScore +=  (sourceVertex.getScore() / claimSet);
				}
				invScore = Math.pow(invScore, 1.2);
				singleVertex.setScore(invScore);
				invScore = 0;
			}
		}
		maxScore = normalize.maxScoreFinder(graph, 'c');
		normalize.avoidOverflow(graph, maxScore, 'c');
	}

	/**
	 * Trust scores are calculated with three folds:
	 * 1. Source takes it initial trust score and accumulates it with all the claims which got investment by that source
	 * 2. It distributes its score with the scores of OTHER sources which have invested in its claims
	 * 3. Further it gets the similar distribution for those claims being invested by the OTHER sources
	 */
	@Override
	public void trustScore(SimpleGraph graph) {
		Map<String, Double> tempScores = new HashMap<String, Double>();
		double invScore = 0;
		int claimSet = 0;
		double furtherInvestment = 0;
		double preScore = 0.0;
		double maxScore = 0;
		Vertex singleVertex, sourceVertex;
		Edge singleEdge;
		for(Object vertex : graph.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getType() == 's') {
				for(Object edge : graph.edgesOf(singleVertex)) {
					singleEdge = (Edge) edge;
					sourceVertex = (Vertex)singleEdge.getSource();
					tempScores = furtherInvestment(graph);
					furtherInvestment = tempScores.get(sourceVertex.getLabel());
					claimSet = graph.degreeOf(singleVertex);
					invScore = (singleVertex.getScore()
							/ (claimSet * furtherInvestment));
					furtherInvestment = 0;
					invScore += sourceVertex.getScore() * invScore + preScore;
					preScore = invScore;
				}
			singleVertex.setScore(preScore);
			preScore = 0;
			}
		}
		maxScore = normalize.maxScoreFinder(graph, 's');
		normalize.avoidOverflow(graph, maxScore, 's');
	}
	
	/**
	 * Initialize the initial trust scores for sources, It is necessary for first iteration of Investment algorithm.
	 * @param graph
	 * @param sources
	 * @return
	 */
	public SimpleGraph initializeTrustScore(SimpleGraph graph) {
		Vertex singleVertex;
		for(Object vertex : graph.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getType() == 's') {
				singleVertex.setScore(1.0);;
			}
		}
		return graph;
	}
	
	/**
	 * Pre-calculating the investment of sources on other claims 
	 * @param graph
	 * @return
	 */
	public Map<String, Double> furtherInvestment(SimpleGraph graph){
		Map<String, Double> tempScores = new HashMap<String, Double>();
		Vertex singleVertex, sourceVertex;
		Edge singleEdge;
		int claimSet = 0;
		double furtherInvestment = 0;
		for(Object vertex : graph.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getType() == 'c') {
				for (Object edge : graph.edgesOf(singleVertex)) {
					singleEdge = (Edge) edge;
					sourceVertex = (Vertex)singleEdge.getTarget();
					claimSet = graph.degreeOf(sourceVertex);
					furtherInvestment += singleVertex.getScore() / claimSet;
				}
				tempScores.put(singleVertex.getLabel(), furtherInvestment);
				furtherInvestment = 0;
			}
		}
		return tempScores;
	}
}
