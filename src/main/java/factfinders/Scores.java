package factfinders;

import java.util.Set;

import graphConstruct.Graph;

/**
 * The basic functions for all the algorithms are presented in this interface
 * They have to be executed iteratively as the prior sets the scores for the former.
 * @author Hussain
 *
 */
public interface Scores {
	
	/**
	 * Belief score sets the notion of belief in the claim by a source.
	 * It is calculated iteratively by a fact finder.
	 * The prior or initial belief has to be set in order to instantiate the process.
	 * @param graph
	 * @param claims; a set of all claims in the graph to iterate through the claim vertices in the graph  
	 */
	public void beliefScore(Graph graph, Set<String> claims);
	
	/**
	 * Trust score describes the trustworthiness of a source.
	 * It is calculated iteratively by a fact finder.
	 * The initial values are set by initial beliefs.
	 * @param graph
	 * @param sources; a set of all sources in the graph to iterate through source vertices in the graph  
	 */
	public void trustScore(Graph graph, Set<String> sources);

}
