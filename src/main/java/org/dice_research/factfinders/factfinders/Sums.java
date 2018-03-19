package org.dice_research.factfinders.factfinders;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.graph.SimpleGraph;
import org.dice_research.factfinders.graphPlotter.Vertex;

/**
 * Hubs and Authorities (Kleinberg, 1999) adapted for claims and sources, 
 * where sources refers as hubs and claims refers to authorities.
 * Normalization: beliefScore(i)/max beleifScore(All Claims)
 * 				  trustScore(i)/max trustScore(All Sources)
 * @author Hussain
 */
public class Sums implements Scores {

	private Normalize normalize;
	
	public Sums() {
		this.normalize = new Normalize();
	}
	
	/**
	 * Belief scores is the accumulation of the trust scores of its neighbors
	 */
	@Override
	public void beliefScore(SimpleGraph graph) {
		double maxScore = 0;
		double score = 0;
		Vertex singleVertex, sourceVertex;
		Edge singleEdge;
		for(Object vertex : graph.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getType() == 'c') {
				System.out.println(singleVertex.getLabel());
				for(Object edge : graph.edgesOf(singleVertex)) {
					singleEdge = (Edge) edge;
					sourceVertex = (Vertex)singleEdge.getTarget();
					System.out.println(sourceVertex.getLabel() +" : " + sourceVertex.getScore());
					score +=  sourceVertex.getScore();
				}
				System.out.println(singleVertex.getLabel() +" : " + score);
				singleVertex.setScore(score);
				score = 0;
			}
		}
		maxScore = normalize.maxScoreFinder(graph, 'c');
		normalize.avoidOverflow(graph, maxScore, 'c');
		System.out.println("-----------------Belief------------");
	}

	/**
	 * Trust scores is the accumulation of the belief scores of its neighbors
	 */
	@Override
	public void trustScore(SimpleGraph graph) {
		double maxScore = 0;
		double score = 0;
		Vertex singleVertex, sourceVertex;
		Edge singleEdge;
		for(Object vertex : graph.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getType() == 's') {
				System.out.println(singleVertex.getLabel());
				for(Object edge : graph.edgesOf(singleVertex)) {
					singleEdge = (Edge) edge;
					sourceVertex = (Vertex)singleEdge.getSource();
					System.out.println(sourceVertex.getLabel() +" : " + sourceVertex.getScore());
					score +=  sourceVertex.getScore();
				}
				System.out.println(singleVertex.getLabel() +" : " + score);
				singleVertex.setScore(score);
				score = 0;
			}
		}
		maxScore = normalize.maxScoreFinder(graph, 's');
		normalize.avoidOverflow(graph, maxScore, 's');
		System.out.println("-----------------Trust------------");
	}

}
