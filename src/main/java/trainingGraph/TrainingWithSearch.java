package trainingGraph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import elasticSearch.PatternedQuerySearcher;
import elasticSearch.SearchResult;
import factfinders.InitializeBeliefs;
import graphPlotter.CreateGraph;

/**
 * Train the graph with true claims with belief score as 1.0
 * claims are searched and the sources returned as the result of search are added into the graph  
 * @author Hussain
 *
 */
public class TrainingWithSearch implements Trainer {
	@Override
	public TrainingResponse train() throws IOException{
		SearchResult result = new SearchResult();
		HashMap<String, Set<String>> graphBuilder = new HashMap<String, Set<String>>();
		TrainingResponse response = new TrainingResponse();
		InitializeBeliefs beliefs = new InitializeBeliefs();

		PatternedQuerySearcher search = new PatternedQuerySearcher();

		String inputFile = "./src/main/resources/data/true_claims.tsv";
		BufferedReader TSVFile = new BufferedReader(new FileReader(inputFile));
		String dataRow = TSVFile.readLine();
		long start;

		try {
			while (dataRow != null){
				result = search.query(dataRow);
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

		response.graph = beliefs.initialize(response.graph, response.claims);

		return response;
	}

}
