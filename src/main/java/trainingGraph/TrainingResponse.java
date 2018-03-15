package trainingGraph;

import java.util.HashSet;
import java.util.Set;

import graphConstruct.Graph;

/**
 * Training response is the simple construct providing the structure to the result
 * of training phase to be used by other objects
 * @author Hussain
 *
 */
public class TrainingResponse {
	
	public Graph graph;
	public Set<String> claims;
	public Set<String> sources;
	
	public TrainingResponse() {
		this.graph = new Graph();
		this.claims = new HashSet<String>();
		this.sources = new HashSet<String>();
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	
	public Set<String> getClaims() {
		return claims;
	}
	
	public void setClaims(Set<String> claims) {
		this.claims = claims;
	}
	
	public Set<String> getSources() {
		return sources;
	}

	public void setSources(Set<String> sources) {
		this.sources = sources;
	}
}
