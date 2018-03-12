package factfinders;

import java.util.ArrayList;

import graphConstruct.Edge;
import graphConstruct.Graph;
import graphConstruct.Vertex;

public class PooledInvestment implements Scores {
	private double invScore = 0;
	private int claimSet = 0;
	private double investment = 0;
	private double divisor = 0;
	/**
	 * (Originally by Pasternack) sources "invest" their trust uniformly in claims
	 * belief in each claim grows according to a linear function G where G=x^g, g=1.4 w.r.t mutual exclusion set
	 * training and testing being the two mutual exclusion sets.
	 * After a series of experiments, It is found that this algorithm does-not fit well for open-data scenario
	 * The reason behind this exclusion is the lack of mutual exclusion sets for claims, As the claims for which the veracity have to be checked are kept discrete. 
	 * @author Hussain
	 */
	@Override
	public void beliefScore(Graph graph, ArrayList<Vertex> claims) {
		for(int i = 0; i < claims.size(); i++) {
			if(graph.containsVertex(claims.get(i))) {
				for(Edge e : graph.getVertex(claims.get(i).getLabel()).getNeighbors()) {
					claimSet = graph.getVertex(e.getNeighbor(claims.get(i)).getLabel()).getNeighborCount();
					invScore = graph.getVertex(claims.get(i).getLabel()).getScore() + (e.getNeighbor(claims.get(i)).getScore()/(double)claimSet);
				}
				investment = Math.pow(invScore, 1.4);
				divisor = calculateDivisor(graph, claims);
				invScore = investment * invScore/divisor;
				graph.getVertex(claims.get(i).getLabel()).setScore(invScore);
			}
		}
	}

	@Override
	public void trustScore(Graph graph, ArrayList<Vertex> sources) {
		for(int i = 0; i < sources.size(); i++) {
			if(graph.containsVertex(sources.get(i))) {
				if(graph.getVertex(sources.get(i).getLabel()).getScore() == 0.0) {
					for(Edge e : graph.getVertex(sources.get(i).getLabel()).getNeighbors()) {
						if(!e.equals(null) && graph.getVertex(sources.get(i).getLabel()).getScore() == 0.0) {
							graph.getVertex(sources.get(i).getLabel()).setScore(graph.getVertex(sources.get(i).getLabel()).getScore() + e.getNeighbor(sources.get(i)).getScore());
							//System.out.println(sources.get(i).getLabel() +" "+graph.getVertex(sources.get(i).getLabel()).getScore());
					
						}
					}
				}
				else {
					for(Edge e : graph.getVertex(sources.get(i).getLabel()).getNeighbors()) {
						for(Edge ed :graph.getVertex(e.getNeighbor(sources.get(i)).getLabel()).getNeighbors()) {
							claimSet = graph.getVertex(ed.getNeighbor(e.getNeighbor(sources.get(i))).getLabel()).getNeighborCount();
							investment += graph.getVertex(ed.getNeighbor(e.getNeighbor(sources.get(i))).getLabel()).getScore()/claimSet;
						}
						claimSet = graph.getVertex(sources.get(i).getLabel()).getNeighborCount();
						invScore = graph.getVertex(sources.get(i).getLabel()).getScore()/((double)claimSet*investment);
						invScore += e.getNeighbor(sources.get(i)).getScore()*invScore;
					}
					graph.getVertex(sources.get(i).getLabel()).setScore(invScore);
					investment =0;
					invScore =0;
				}
			}
		}
	}
	
	private double calculateDivisor(Graph graph, ArrayList<Vertex> claims) {
		for(int i = 0; i < claims.size(); i++) {
			if(graph.containsVertex(claims.get(i))) {
				for(Edge e : graph.getVertex(claims.get(i).getLabel()).getNeighbors()) {
					claimSet = graph.getVertex(e.getNeighbor(claims.get(i)).getLabel()).getNeighborCount();
					invScore = graph.getVertex(claims.get(i).getLabel()).getScore() + (e.getNeighbor(claims.get(i)).getScore()/(double)claimSet);
				}
				invScore = Math.pow(invScore, 1.4);
			}
			investment += invScore;
		}
		return investment;
	}
	

}
