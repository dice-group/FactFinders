package trainingGraph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import factfinders.InitializeBeliefs;
import graphPlotter.CreateGraph;


/**
 * The data for graph is already searched via pre-processing. TrainingWithDefaults use that data to generate the graph.
 * It elevates the performance by eradicating the time to search.
 * @author Hussain
 *
 */
public class TrainingWithDefaults implements Trainer {
	@Override
	public TrainingResponse train() throws IOException{
		HashMap<String, Set<String>> graphBuilder = new HashMap<String, Set<String>>();
		TrainingResponse response = new TrainingResponse();
		InitializeBeliefs beliefs = new InitializeBeliefs();


		String inputFile = "./src/main/resources/data/CW_BOA_Graph/traininggraph_BOA_CW.tsv";
		BufferedReader TSVFile = new BufferedReader(new FileReader(inputFile));
		String dataRow = TSVFile.readLine();
		long start;
		try {
			while (dataRow != null){
				String[] dataArray = dataRow.split("\\t");
				dataArray[1] = dataArray[1].replace("[", "").replace("]", "");
				graphBuilder.put(dataArray[0], new LinkedHashSet<String>(Arrays.asList(dataArray[1].split(","))));
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
