package org.dice_research.factfinders.graphPlotter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org._3pq.jgrapht.graph.SimpleGraph;
import org.dice_research.factfinders.elasticSearch.SearchResult;

/**
 * It allows the creation of graph, addition and removal of edges from existing graph
 * @author Hussain
 */

public class CreateGraph {

	public SimpleGraph graph = new SimpleGraph();

	public CreateGraph(SimpleGraph graph) {
		this.graph = graph;
	}

	/**
	 * This constructor creates the graph and populates the sources and claims lists 
	 * It is called during training of the graph
	 * @param buildGraph
	 */
	public CreateGraph(HashMap<String, Set<String>> buildGraph){
		Vertex newClaim;
		Vertex newSource;
		for(Map.Entry<String, Set<String>> nodeSet : buildGraph.entrySet()) {
			newClaim = new Vertex(nodeSet.getKey(), 1.0, 'c');
			if(!graph.containsVertex(newClaim)) {
				graph.addVertex(newClaim);
			}
			if (!nodeSet.getValue().contains("")) {
				for (String s : nodeSet.getValue()) {
					newSource = new Vertex(s.trim(), 0.0, 's');
					if (!graph.containsVertex(newSource)) {
						graph.addVertex(newSource);
					}
					graph.addEdge(newClaim, newSource);
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
	public SimpleGraph addEdge(SimpleGraph response, SearchResult result) {
		Vertex newClaim;
		Vertex newSource;
		newClaim = new Vertex(result.claim, 0.5, 'c');
		if(!response.containsVertex(newClaim))
			response.addVertex(newClaim);

		if (!result.sources.contains("")) {
			for (String source : result.sources) {
				newSource = new Vertex(source.trim(), 0.0, 's');
				if (!response.containsVertex(newSource)) {
					response.addVertex(newSource);
				}
				response.addEdge(newClaim, newSource);
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
	public SimpleGraph removeEdge(SimpleGraph response, SearchResult result) {
		Vertex newClaim;
		Vertex newSource;
		newClaim = new Vertex(result.claim, 0.5, 'c');

		if (!result.sources.contains("")) {
			for (String source : result.sources) {
				newSource = new Vertex(source.trim(), 0.0, 's');
				if (response.containsVertex(newSource) && response.degreeOf(newSource) <= 1) {
					response.removeVertex(newSource);
				}
				response.removeEdge(newClaim, newSource);
			} 
		}
		
		if(response.containsVertex(newClaim) && response.degreeOf(newClaim) <= 1)
			response.removeVertex(newClaim);
		
		return response;
	}

	public SimpleGraph getGraph() {
		return this.graph;
	}
}
