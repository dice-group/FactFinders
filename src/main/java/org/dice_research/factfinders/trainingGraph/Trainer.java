package org.dice_research.factfinders.trainingGraph;

import java.io.IOException;

import org._3pq.jgrapht.graph.SimpleGraph;

/**
 * Trainer interface defines the interaction of experiments in training phase 
 * @author Hussain
 *
 */
public interface Trainer {

	public SimpleGraph train() throws IOException;

}