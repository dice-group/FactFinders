package factFinders;

import java.util.ArrayList;

import graphPlotter.Edge;
import graphPlotter.Graph;
import graphPlotter.Vertex;


/**
 * Originally by Yin et al., 2008
 * Calculates the probability of a claim by assuming that each sourse's
 * trust score is the probability of being correct and then average the claim's score
 * @author Hussain
 *
 */
public class Truthfinder implements Scores {
	
	private double truthScore = 0;
	private double probScore = 1.0;
	private int claimSet = 0;
	private double maxScore = 0;

	@Override
	public void beliefScore(Graph graph,ArrayList<Vertex> claims) {
		for(int i = 0; i < claims.size(); i++) {
			if(graph.containsVertex(claims.get(i))) {
				//System.out.println(graph.getVertex(sources.get(i).getLabel()).getNeighbors().toString());
				for(Edge e : graph.getVertex(claims.get(i).getLabel()).getNeighbors()) {
//					System.out.println(e.getNeighbor(sources.get(i)).toString());
//					System.out.println(e.getNeighbor(claims.get(i)).getLabel() + e.getNeighbor(claims.get(i)).getScore());
//					System.out.println(1-e.getNeighbor(claims.get(i)).getScore());
					probScore *= 1-e.getNeighbor(claims.get(i)).getScore();
//					System.out.println(probScore);
				}
//				System.out.println(probScore);
				graph.getVertex(claims.get(i).getLabel()).setScore(1-probScore);
				probScore = 1.0;
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
//					truthScore = graph.getVertex(sources.get(i).getLabel()).getScore() + e.getNeighbor(sources.get(i)).getScore();
					truthScore += e.getNeighbor(sources.get(i)).getScore();
				}
				claimSet = graph.getVertex(sources.get(i).getLabel()).getNeighbors().size();
//				System.out.println(claimSet);
				truthScore = (truthScore / claimSet);
//				System.out.println(truthScore);
				graph.getVertex(sources.get(i).getLabel()).setScore(truthScore);
				//System.out.println(sources.get(i).getLabel() + graph.getVertex(sources.get(i).getLabel()).getScore());
				truthScore = 0;
			}
		}
		maxScore = maxScore(graph, sources); 
		normalize(graph, maxScore, sources);
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
