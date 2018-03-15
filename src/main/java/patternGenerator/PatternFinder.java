/**
 * 
 */
package patternGenerator;

import java.util.Set;

/**
 * It develops interaction with the search for querying patterns
 * @author Hussain
 *
 */
public interface PatternFinder {

	public Set<String> querryPatterns(String wordForm);

}
