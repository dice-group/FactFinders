package elasticSearch;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class offers are simple struct of the result to returned as a result of elastic search
 * The return object of the search contains searched claim and the list of sources asserting this claim
 * @author Hussain
 *
 */
public class SearchResult {
	public String claim = new String();
	public Set<String> sources = new LinkedHashSet<String>();
}
