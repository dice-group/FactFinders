package factFinders;

import java.util.ArrayList;

import graphPlotter.Graph;
import graphPlotter.Vertex;

/**
 * 
 * @author Hussain
 *
 */
public class InitializeBeliefs {
	
	private double fixedBelief = 0.5;
	private double uniformBelief = 0.5;
	private double votedBelief = 0;
	private double trainBelief = 1.0;
	private double testBelief = 0.5;
	
	public Graph initialize(Graph graph, ArrayList<Vertex> claims, String factfinder) {	
		if(factfinder == "Sums") {
			for(int i = 0; i < claims.size(); i++) {
			 	   graph.getVertex(claims.get(i).getLabel()).setScore(fixedBelief); 
			 	   System.out.println( graph.getVertex(claims.get(i).getLabel()) +","+ graph.getVertex(claims.get(i).getLabel()).getScore());  
			}
		}
		else if(factfinder == "Avg") {
			for(int i = 0; i < claims.size(); i++) {
			 	   graph.getVertex(claims.get(i).getLabel()).setScore(fixedBelief); 
			 	   System.out.println( graph.getVertex(claims.get(i).getLabel()) +","+ graph.getVertex(claims.get(i).getLabel()).getScore());  
			}
		}
		else if(factfinder == "training") {
			for(int i = 0; i < claims.size(); i++) {
			 	   graph.getVertex(claims.get(i).getLabel()).setScore(trainBelief); 
//			 	   System.out.println( graph.getVertex(claims.get(i).getLabel()) +","+ graph.getVertex(claims.get(i).getLabel()).getScore());  
			}
		}
		else if(factfinder == "Pool") {
			uniformBelief = 1/2;
			for(int i = 0; i < claims.size(); i++) {
			 	   graph.getVertex(claims.get(i).getLabel()).setScore(uniformBelief); 
			 	   System.out.println( graph.getVertex(claims.get(i).getLabel()) +","+ graph.getVertex(claims.get(i).getLabel()).getScore());  
			}
		}
		else if(factfinder == "Tf") {
			uniformBelief = 1/2;
			for(int i = 0; i < claims.size(); i++) {
			 	   graph.getVertex(claims.get(i).getLabel()).setScore(uniformBelief); 
			 	   System.out.println( graph.getVertex(claims.get(i).getLabel()) +","+ graph.getVertex(claims.get(i).getLabel()).getScore());  
			}
		}
		else {
			for(int i = 0; i < claims.size(); i++) {
			 	   graph.getVertex(claims.get(i).getLabel()).setScore(1); 
			 	   System.out.println( graph.getVertex(claims.get(i).getLabel()) +","+ graph.getVertex(claims.get(i).getLabel()).getScore());  
			}
		}
		return graph;
	}

	public Graph initialize(Graph graph, ArrayList<Vertex> claims, ArrayList<Vertex> sources) {
		for(int i = 0; i < claims.size(); i++) {
			votedBelief = (double)(graph.getVertex(claims.get(i).getLabel()).getNeighborCount())/(double)sources.size();
			graph.getVertex(claims.get(i).getLabel()).setScore(votedBelief); 
			System.out.println( graph.getVertex(claims.get(i).getLabel()) +","+ graph.getVertex(claims.get(i).getLabel()).getScore());  
		}
		return graph;
	}

	public Graph initialize(Graph graph, String claim, String factfinder) {
		for(Vertex c : graph.vertices()) {
			if(c.getLabel() == claim) {
				 graph.getVertex(c.getLabel()).setScore(testBelief); 
//			 	   System.out.println( graph.getVertex(c.getLabel()) +","+ graph.getVertex(c.getLabel()).getScore());
			}  
		}
		return graph;
	}
	
}
