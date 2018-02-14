package factFinders;

import java.util.ArrayList;

import graphPlotter.Edge;
import graphPlotter.Graph;
import graphPlotter.Vertex;

public class Investment implements Scores {

	private double invScore = 0;
	private int claimSet = 0;
	private double investment =0;
	private double maxScore = 0;
	/**
	 * (Originally by Pasternack) sources "invest" their trust uniformly in claims
	 * belief in each claim grows according to a non linear function G where G=x^g, g=1.2
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
					claimSet = graph.getVertex(e.getNeighbor(claims.get(i)).getLabel()).getNeighborCount();
//					invScore = graph.getVertex(claims.get(i).getLabel()).getScore() + (e.getNeighbor(claims.get(i)).getScore()/(double)claimSet);
					invScore += (e.getNeighbor(claims.get(i)).getScore()/(double)claimSet);
				}
				invScore = Math.pow(invScore, 1.2);
				graph.getVertex(claims.get(i).getLabel()).setScore(invScore);
				invScore = 0;
				//System.out.println(sources.get(i).getLabel() + graph.getVertex(sources.get(i).getLabel()).getScore());
			}
		}
		maxScore = maxScore(graph, claims); 
		normalize(graph, maxScore, claims);
	}

	@Override
	public void trustScore(Graph graph, ArrayList<Vertex> sources) {
		for(int i = 0; i < sources.size(); i++) {
			if(graph.containsVertex(sources.get(i))) {
				if(graph.getVertex(sources.get(i).getLabel()).getScore() == 0.0) {
					for(Edge e : graph.getVertex(sources.get(i).getLabel()).getNeighbors()) {
						if(!e.equals(null) && graph.getVertex(sources.get(i).getLabel()).getScore() == 0.0) {
							graph.getVertex(sources.get(i).getLabel()).setScore(1.0);
//							System.out.println(sources.get(i).getLabel() +" "+graph.getVertex(sources.get(i).getLabel()).getScore());
						}
					}
				}
				else {
					for(Edge e : graph.getVertex(sources.get(i).getLabel()).getNeighbors()) {
//						System.out.println(graph.getVertex(sources.get(i).getLabel()).getNeighbors());
						for(Edge ed :graph.getVertex(e.getNeighbor(sources.get(i)).getLabel()).getNeighbors()) {
//							System.out.println(graph.getVertex(e.getNeighbor(sources.get(i)).getLabel()).getNeighbors());
								claimSet = graph.getVertex(ed.getNeighbor(e.getNeighbor(sources.get(i))).getLabel()).getNeighborCount();
//								System.out.println(ed.getNeighbor(e.getNeighbor(sources.get(i))).getLabel() + ":" +claimSet);
//								System.out.println(investment);
								investment += graph.getVertex(ed.getNeighbor(e.getNeighbor(sources.get(i))).getLabel()).getScore()/claimSet;
//								System.out.println(ed.getNeighbor(e.getNeighbor(sources.get(i))).getLabel() + " : " +investment);
						}
						claimSet = graph.getVertex(sources.get(i).getLabel()).getNeighborCount();
//						System.out.println(sources.get(i).getLabel()+ ">" +claimSet);
						invScore = graph.getVertex(sources.get(i).getLabel()).getScore()/((double)claimSet*investment);
//						System.out.println(graph.getVertex(sources.get(i).getLabel()).getScore());
						invScore += e.getNeighbor(sources.get(i)).getScore()*invScore;
//						System.out.println(graph.getVertex(sources.get(i).getLabel()) + " : " + invScore);
					}
//					System.out.println("The new trust score for " +sources.get(i)+ ":"+ invScore);
					graph.getVertex(sources.get(i).getLabel()).setScore(invScore);
					investment =0;
				}
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
