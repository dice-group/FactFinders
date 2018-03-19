/**
 * 
 */
package org.dice_research.factfinders.factfinders;

import org._3pq.jgrapht.graph.SimpleGraph;
import org.dice_research.factfinders.graphPlotter.Vertex;


/**
 * This class implements normalization over the Belief and Trust scores to avoid Overflow
 * @author Hussain
 *
 */
public class Normalize {
	
	/**
	 * finds max score out all sources or claims
	 * @param graph
	 * @return
	 */
	public double maxScoreFinder(SimpleGraph graph, char type) {
		double max = 0;
		Vertex singleVertex;
		for(Object vertex : graph.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getScore() > max && singleVertex.getType() == type)
				max = singleVertex.getScore();
		}
		return max;	
	}
	
	/**
	 * The claims and sources are normalized by dividing each respective score by Maximum value
	 * @param graph
	 * @param max
	 */
	public void avoidOverflow(SimpleGraph graph, double max, char type) {
		Vertex singleVertex;
		for(Object vertex : graph.vertexSet()){
			singleVertex = (Vertex) vertex;
			if(singleVertex.getType() == type)
				singleVertex.setScore(singleVertex.getScore()/max);
		}
	}
}
