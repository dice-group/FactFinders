package factFinders;

import java.util.ArrayList;

import graphConstruct.Graph;
import graphConstruct.Vertex;

/**
 * 
 * @author Hussain
 *
 */
public interface Scores {
	
	/**
	 * Belief score sets the notion of belief in the claim by a source.
	 * It is calculated iteratively by a fact finder.
	 * The prior or initial belief has to be set in order to instantiate the process.
	 * @return
	 */
	public void beliefScore(Graph graph, ArrayList<Vertex> claims);
	
	/**
	 * Trust score describes the trustworthiness of a source.
	 * It is calculated iteratively by a fact finder.
	 * The initial values are set by initial beliefs.
	 * @return
	 */
	public void trustScore(Graph graph, ArrayList<Vertex> sources);

}
