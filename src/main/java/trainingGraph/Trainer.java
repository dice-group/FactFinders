package trainingGraph;

import java.io.IOException;

/**
 * Trainer interface defines the interaction of experiments in training phase 
 * @author Hussain
 *
 */
public interface Trainer {

	public TrainingResponse train() throws IOException;

}