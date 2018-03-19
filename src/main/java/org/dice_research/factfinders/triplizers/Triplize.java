package org.dice_research.factfinders.triplizers;

import java.io.IOException;

/**
 * Basic triple extraction definition 
 * It is implemented by RDF triplizer for now, can be used as a general interface for further org.dice_research.factfinders.triplizers.
 * @author Hussain
 *
 */
public interface Triplize {

	String triplize(String file) throws IOException;

}