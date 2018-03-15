package elasticSearch;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class offers are simple construct of the result to returned as a result of elastic search
 * The return object of the search contains searched claim and the list of sources asserting this claim
 * @author Hussain
 *
 */
public class SearchResult {
	
	public String claim = new String();
	public Set<String> sources = new LinkedHashSet<String>();
	
	public String getClaim() {
		return claim;
	}
	
	public void setClaim(String claim) {
		this.claim = claim;
	}
	
	public Set<String> getSources() {
		return sources;
	}
	
	public void setSources(Set<String> sources) {
		this.sources = sources;
	}
}
