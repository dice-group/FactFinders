/**
 * 
 */
package factfinders;

import java.util.Set;

import graphConstruct.Graph;

/**
 * This class implements normalization over the Belief and Trust scores to avoid Overflow
 * @author Hussain
 *
 */
public class Normalize {
	
	/**
	 * finds max score out all sources or claims
	 * @param graph
	 * @param vertices
	 * @return
	 */
	public double maxScoreFinder(Graph graph, Set<String> vertices) {
		double max = 0;
		for(String vertex : vertices) {
			if(graph.getVertex(vertex).getScore() > max)
				max = graph.getVertex(vertex).getScore();
		}
		return max;	
	}
	
	/**
	 * The claims and sources are normalized by dividing each respective score by Maximum value
	 * @param graph
	 * @param max
	 * @param vertices
	 */
	public void avoidOverflow(Graph graph, double max, Set<String> vertices) {
		for(String vertex : vertices){
			graph.getVertex(vertex).setScore(graph.getVertex(vertex).getScore()/max);
		}
	}
}
