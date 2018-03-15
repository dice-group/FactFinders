package graphPlotter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import elasticSearch.SearchResult;
import graphConstruct.Edge;
import graphConstruct.Graph;
import graphConstruct.Vertex;
import trainingGraph.TrainingResponse;

/**
 * It allows the creation of graph, addition and removal of edges from existing graph
 * @author Hussain
 */

public class CreateGraph {

	public Graph graph;
	public Set<String> claims;
	public Set<String> sources;

	public CreateGraph() {
		this.graph = new Graph();
		this.claims = new HashSet<String>();
		this.sources = new HashSet<String>();
	}

	/**
	 * This constructor creates the graph and populates teh sources and claims lists 
	 * It is called during training of the graph
	 * @param buildGraph
	 */
	public CreateGraph(HashMap<String, Set<String>> buildGraph){
		for(Set<String> sourceList : buildGraph.values()) {
			for(String source: sourceList) {
				if(!source.equals("")) {
					sources.add(source);
					graph.addVertex(new Vertex(source), false);
				}
			}
		}

		for(String claim: buildGraph.keySet()) {
			claims.add(claim);
			graph.addVertex(new Vertex(claim), false);

			for(String source: buildGraph.get(claim)) {
				if(sources.contains(source)) {
					graph.addEdge(graph.getVertex(claim), graph.getVertex(source));
				}
			} 

		}
	}

	/**
	 * The New edge is added during testing, Vertices are added in the graph and source, claims sets respectively
	 * @param response
	 * @param result
	 * @return
	 */
	public TrainingResponse addEdge(TrainingResponse response, SearchResult result) {
		if(!response.claims.contains(result.claim))
			response.claims.add(result.claim);
		if(!response.graph.containsVertex(new Vertex(result.claim)))
			response.graph.addVertex(new Vertex(result.claim), false);

		for(String source: result.sources) {
			response.sources.add(source);
			response.graph.addVertex(new Vertex(source), false);
		}

		for(String source : result.sources) {
			if(response.sources.contains(source)) {
				response.graph.addEdge(response.graph.getVertex(result.claim), response.graph.getVertex(source));
			}
		}

		return response;
	}

	/**
	 * In case of single claim experiments the edge has to be added and removed sequentially
	 * executing the algorithms between this envelop
	 * @param response
	 * @param result
	 * @return
	 */
	public TrainingResponse removeEdge(TrainingResponse response, SearchResult result) {
		for(String src : result.sources) {
			if(response.graph.containsEdge(new Edge(response.graph.getVertex(result.claim), response.graph.getVertex(src)))
					&& response.graph.containsVertex(new Vertex(src))){
				response.graph.removeEdge(new Edge(response.graph.getVertex(result.claim), response.graph.getVertex(src)));
				if(response.graph.getVertex(src).getNeighborCount() == 0) {
					response.sources.remove(src);
					response.graph.removeVertex(src);
				}
			}
		}

		if(response.graph.getVertex(result.claim).getNeighborCount() == 0) {
			response.claims.remove(result.claim);
			response.graph.removeVertex(result.claim);
		}

		return response;
	}

	public Set<String> getClaims() {
		return this.claims;
	}

	public Set<String> getSources() {
		return this.sources;
	}

	public Graph getGraph() {
		return this.graph;
	}
}
