package graphPlotter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.graph.DefaultDirectedGraph;

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
		HashMap<String, Set<String>> graphBuilder = new HashMap<String, Set<String>>();

		DirectedGraph graph = new DefaultDirectedGraph();
		Set<Vertex> claims = new HashSet<Vertex>();
		Set<Vertex> sources = new HashSet<Vertex>();
		
		long start;
		start = System.nanoTime();
		try {
			while (dataRow != null){
				String[] dataArray = dataRow.split("\\t");
				dataArray[1] = dataArray[1].replace("[", "").replace("]", "");
				newClaim = new Vertex(dataArray[0], 1.0);
				claims.add(newClaim);
				graph.addVertex(newClaim);
				for(String s : Arrays.asList(dataArray[1].split(","))){
					newSource = new Vertex(s, 0.0);
					sources.add(newSource);
					graph.addVertex(newSource);
					graph.addEdge(newClaim, newSource);
				}
				graphBuilder.put(dataArray[0], new LinkedHashSet<String>(Arrays.asList(dataArray[1].split(","))));
				dataRow = TSVFile.readLine(); 
			}
			TSVFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println(sources.size());
		System.out.println(claims.size());
		System.out.print("Graph creation time for JgraphT is " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start) + " seconds \n");
		System.out.print("Vertices : " + graph.vertexSet().size() + "\n");
		System.out.print("Edges : " + graph.edgeSet().size() + "\n");

		start = System.nanoTime();
		CreateGraph create = new CreateGraph(graphBuilder);
		System.out.print("Graph creation time for custom procedure is " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start) + " seconds \n");
		System.out.println(create.claims.size());
		System.out.println(create.sources.size());
		System.out.print("Vertices : " + create.getGraph().vertexKeys().size() + "\n");
		System.out.print("Edges : " + create.getGraph().getEdges().size() + "\n");

		System.out.println("--------------DONE----------------");
	}

}


