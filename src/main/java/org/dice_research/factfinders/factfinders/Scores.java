package org.dice_research.factfinders.factfinders;

import org._3pq.jgrapht.graph.SimpleGraph;

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
	 */
	public void beliefScore(SimpleGraph graph);
	
	/**
	 * Trust score describes the trustworthiness of a source.
	 * It is calculated iteratively by a fact finder.
	 * The initial values are set by initial beliefs.
	 * @param graph 
	 */
	public void trustScore(SimpleGraph graph);

}
