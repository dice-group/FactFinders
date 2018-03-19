package org.dice_research.factfinders.graphPlotter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org._3pq.jgrapht.graph.SimpleGraph;
import org.dice_research.factfinders.elasticSearch.SearchResult;

/**
 * A simple comparison between graph plotting time by JGraphT and custom approach
 * JGraphT is undoubtedly performance wise better as it takes 2 secs while the custom approach takes 5-8 secs 
 * JGraphT does not consider same vertices and insert duplicates which violates the decorum of Graph required f
 * or these experiments
 * The decision for modifying the simple graph is to ensure usability and re-usability.
 * The custom approach allows manipulation on scores for each vertex which adds two levels of 
 * complexity(apart from algorithms complexity).
 * @author Hussain
 */
public class DemoGraph {

	public static void main(String[] args) throws IOException{
		Vertex newSource;
		Vertex newClaim;
		String inputFile = "./src/main/resources/data/CW_BOA_Graph/traininggraph_BOA_CW.tsv";
		BufferedReader TSVFile = new BufferedReader(new FileReader(inputFile));
		String dataRow = TSVFile.readLine();
		SimpleGraph graph = new SimpleGraph();
		CreateGraph newEdge;
		SearchResult result = new SearchResult();
		
		long start;
		start = System.nanoTime();
		try {
			while (dataRow != null){
				String[] dataArray = dataRow.split("\\t");
				dataArray[1] = dataArray[1].replace("[", "").replace("]", "");
				newClaim = new Vertex(dataArray[0], 1.0, 'c');
				if(!graph.containsVertex(newClaim)) {
					graph.addVertex(newClaim);
				}
				for(String s : new HashSet<String>(Arrays.asList(dataArray[1].split(",")))){
					newSource = new Vertex(s.trim(), 0.5, 's');
					if(!graph.containsVertex(newSource)) {
						graph.addVertex(newSource);
					}
					graph.addEdge(newClaim, newSource);
				}
				dataRow = TSVFile.readLine(); 
			}
			TSVFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		Vertex singleVertex;
		for(Object vertex : graph.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getType() == 's') {
				singleVertex.setScore(1.0);;
			}
		}
		
		for(Object vertex : graph.vertexSet()) {
			singleVertex = (Vertex) vertex;
			if(singleVertex.getType() == 's') {
				System.out.print( singleVertex.getLabel() + ": "+ singleVertex.getScore()+ "\n");
			}
		}
		
		
			
		result.claim = "Nils Gustaf Dalén_award_Nobel Prize in Physics";
		result.sources.add("");
		newEdge = new CreateGraph(graph);
		graph = newEdge.removeEdge(graph, result);
		newClaim = new Vertex("Nils Gustaf Dalén_award_Nobel Prize in Physics", 1.0, 'c');
		System.out.print( graph.containsVertex(newClaim) + "\n");
	
		System.out.print("Graph creation time for JgraphT is " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start) + " seconds \n");
		System.out.print("Vertices : " + graph.vertexSet().size() + "\n");
		System.out.print("Edges : " + graph.edgeSet().size() + "\n");

		System.out.println("--------------DONE----------------");
	}
}


