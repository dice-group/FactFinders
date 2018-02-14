package factFinders;

import java.util.ArrayList;

import graphPlotter.Edge;
import graphPlotter.Graph;
import graphPlotter.Vertex;

public class AverageLog implements Scores {

	private double maxScore = 0;
	private double avgScore = 0;
	private int claimSet = 0;
	private double beliefScore = 0;
	/**
	 * (Originally by Pasternack) Computing trust score as an average of belief in its claims
	 * It over-estimates the trustworthiness of a source with relatively few claims
	 * @author Hussain
	 */
	@Override
	public void beliefScore(Graph graph, ArrayList<Vertex> claims) {
		for(int i = 0; i < claims.size(); i++) {
			if(graph.containsVertex(claims.get(i))) {
				//System.out.println(graph.getVertex(sources.get(i).getLabel()).getNeighbors().toString());
				for(Edge e : graph.getVertex(claims.get(i).getLabel()).getNeighbors()) {
					//System.out.println(e.getNeighbor(sources.get(i)).toString());
					//System.out.println(sources.get(i).getLabel() + graph.getVertex(sources.get(i).getLabel()).getScore());
//					graph.getVertex(claims.get(i).getLabel()).setScore(graph.getVertex(claims.get(i).getLabel()).getScore() + e.getNeighbor(claims.get(i)).getScore());
					beliefScore +=  e.getNeighbor(claims.get(i)).getScore();
					//System.out.println(sources.get(i).getLabel() + graph.getVertex(sources.get(i).getLabel()).getScore());
				}
				graph.getVertex(claims.get(i).getLabel()).setScore(beliefScore);
				beliefScore = 0;
			}
		}
		maxScore = maxScore(graph, claims); 
		normalize(graph, maxScore, claims);
	}

	@Override
	public void trustScore(Graph graph, ArrayList<Vertex> sources) {
		for(int i = 0; i < sources.size(); i++) {
			if(graph.containsVertex(sources.get(i))) {
				//System.out.println(graph.getVertex(sources.get(i).getLabel()).getNeighbors().toString());
				for(Edge e : graph.getVertex(sources.get(i).getLabel()).getNeighbors()) {
					//System.out.println(e.getNeighbor(sources.get(i)).toString());
					//System.out.println(sources.get(i).getLabel() + graph.getVertex(sources.get(i).getLabel()).getScore());
//					avgScore = graph.getVertex(sources.get(i).getLabel()).getScore() + e.getNeighbor(sources.get(i)).getScore();
					avgScore += e.getNeighbor(sources.get(i)).getScore();
				}
				claimSet = graph.getVertex(sources.get(i).getLabel()).getNeighborCount();
				avgScore = (avgScore / claimSet)*Math.log10(claimSet);
				graph.getVertex(sources.get(i).getLabel()).setScore(avgScore);
				avgScore = 0;
				//System.out.println(sources.get(i).getLabel() + graph.getVertex(sources.get(i).getLabel()).getScore());
			}
		}
	}

	private double maxScore(Graph graph, ArrayList<Vertex> vertices) {
		double max = 0;
		for(int i = 0; i < vertices.size(); i++) {
			if(graph.containsVertex(vertices.get(i))) {
				if(graph.getVertex(vertices.get(i).getLabel()).getScore() > max)
					max = graph.getVertex(vertices.get(i).getLabel()).getScore();
			}
		}
		return max;	
	}
	
	private void normalize(Graph graph, double max, ArrayList<Vertex> vertices) {
		for(int i = 0; i < vertices.size(); i++) {
			if(graph.containsVertex(vertices.get(i))) {
				graph.getVertex(vertices.get(i).getLabel()).setScore(graph.getVertex(vertices.get(i).getLabel()).getScore()/max);
			}
		}
	}
}
