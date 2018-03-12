package factfinders;

import java.util.ArrayList;

import graphConstruct.Edge;
import graphConstruct.Graph;
import graphConstruct.Vertex;

public class Sums implements Scores {

	private double maxScore = 0;
	private double score = 0;
	
	/**
	 * Hubs and Authorities (Kleinberg, 1999) adapted for claims and sources, 
	 * where sources refers as hubs and claims refers to authorities.
	 * Normalization: beliefScore(i)/max beleifScore(All Claims)
	 * 				  trustScore(i)/max trustScore(All Sources)
	 * @author Hussain
	 */
	@Override
	public void beliefScore(Graph graph, ArrayList<Vertex> claims) {
		for(int i = 0; i < claims.size(); i++) {
			if(graph.containsVertex(claims.get(i))) {
				//System.out.println(graph.getVertex(sources.get(i).getLabel()).getNeighbors().toString());
				for(Edge e : graph.getVertex(claims.get(i).getLabel()).getNeighbors()) {
//					System.out.println(e.getNeighbor(claims.get(i)).toString());
//					System.out.println(claims.get(i).getLabel() + graph.getVertex(claims.get(i).getLabel()).getScore());
//					graph.getVertex(claims.get(i).getLabel()).setScore(graph.getVertex(claims.get(i).getLabel()).getScore() + e.getNeighbor(claims.get(i)).getScore());
					score +=  e.getNeighbor(claims.get(i)).getScore();
//					System.out.println(score);
				}
				graph.getVertex(claims.get(i).getLabel()).setScore(score);
				score = 0;
			}
		}
		maxScore = maxScoreFinder(graph, claims); 
		normalize(graph, maxScore, claims);
	}

	@Override
	public void trustScore(Graph graph, ArrayList<Vertex> sources) {
		for(int i = 0; i < sources.size(); i++) {
			if(graph.containsVertex(sources.get(i))) {
//				System.out.println(graph.getVertex(sources.get(i).getLabel()).getNeighbors().toString());
				for(Edge e : graph.getVertex(sources.get(i).getLabel()).getNeighbors()) {
//					System.out.println(e.getNeighbor(sources.get(i)).toString());
//					System.out.println(sources.get(i).getLabel() + graph.getVertex(sources.get(i).getLabel()).getScore());
//					graph.getVertex(sources.get(i).getLabel()).setScore(graph.getVertex(sources.get(i).getLabel()).getScore() + e.getNeighbor(sources.get(i)).getScore());
					score += e.getNeighbor(sources.get(i)).getScore();
//					System.out.println(score);
				}
				graph.getVertex(sources.get(i).getLabel()).setScore(score);
				score = 0;
			}
		}
		maxScore = maxScoreFinder(graph, sources); 
		normalize(graph, maxScore, sources);
	}

	private double maxScoreFinder(Graph graph, ArrayList<Vertex> vertices) {
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
