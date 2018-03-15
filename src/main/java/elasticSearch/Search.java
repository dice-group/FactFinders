/**
 * 
 */
package elasticSearch;

import java.util.ArrayList;

/**
 * This interface provides the basic search queries implementation
 * @author Hussain
 *
 */
public interface Search {

	/**
	 * This is query with out default predicate patterns.
	 * @param claim Test or Train claim to be searched for sources
	 * @return SearchResult as an object containing the claim and the list of sources found by searching that claim
	 */
	public SearchResult query(String claim);
	
	/**
	 * The query with default query patterns generated during pre-processing
	 * @param claim Test or Train claim to be searched for sources
	 * @param propertyWords is a list of pattern words generated through either WOrd-Net or BOA pattern generator 
	 * @return SearchResult as an object containing the claim and the list of sources found by searching that claim
	 */
	public SearchResult query(String claim, ArrayList<String> propertyWords);
}
