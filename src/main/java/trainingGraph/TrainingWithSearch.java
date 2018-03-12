package trainingGraph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import elasticSearch.SearchBOAPatterns;
import elasticSearch.SearchResult;
import elasticSearch.SearchWordnetPatterns;
import factFinders.InitializeBeliefs;
import graphPlotter.CreateGraph;

/**
 * train the graph with true claims with belief score as 1.0
 * @author Hussain
 *
 */
public class TrainingWithSearch {
	public TrainingResponse train() throws IOException{
		SearchResult result = new SearchResult();
		HashMap<String, ArrayList<String>> graphBuilder = new HashMap<String, ArrayList<String>>();
		TrainingResponse response = new TrainingResponse();
		InitializeBeliefs beliefs = new InitializeBeliefs();
		
//		SearchWordnetPatterns search = new SearchWordnetPatterns();
		SearchBOAPatterns search = new SearchBOAPatterns();
		
		String inputFile = "./src/main/resources/data/true_claims.tsv";
		BufferedReader TSVFile = new BufferedReader(new FileReader(inputFile));
		String dataRow = TSVFile.readLine();
		long start;
   
		try {
			while (dataRow != null){
//				start = System.nanoTime();
				result = search.query(dataRow);
//				System.out.print("Search took " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start) + " seconds \n");
				if(result != null) {
					graphBuilder.put(result.claim, result.sources);
					System.out.println(result.claim);
				}
				dataRow = TSVFile.readLine(); 
			}
	        	TSVFile.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		
		start = System.nanoTime();
		CreateGraph create = new CreateGraph(graphBuilder);
		System.out.print("Graph creation time is " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start) + " seconds \n");
		response.graph = create.getGraph();
		response.claims = create.getClaims();
		response.sources = create.getSources();
		
		response.graph = beliefs.initialize(response.graph, response.claims, "training");
		
		return response;
	}

}
