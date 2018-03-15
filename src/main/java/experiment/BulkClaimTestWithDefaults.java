package experiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashSet;
import elasticSearch.SearchResult;
import factfinders.AverageLog;
import factfinders.InitializeBeliefs;
import factfinders.Investment;
import factfinders.Sums;
import factfinders.Truthfinder;
import graphPlotter.CreateGraph;
import trainingGraph.Trainer;
import trainingGraph.TrainingResponse;
import trainingGraph.TrainingWithDefaults;

/**
 * BulkClaimTestWithDefaults for each algorithm follow the Pasternack's technique of Algorithm execution
 * The graph is trained once for all the train data which is stored in default files during pre-processing
 * All the test data is than introduced to the Graph, the test graph data is also stored as pre-processed file
 * Results are logged into the sample variant file of algorithms
 * 
 * @author Hussain
 */
public class BulkClaimTestWithDefaults {

	public static void main(String[] args) throws IOException {
		SearchResult result = new SearchResult();
		TrainingResponse response = new TrainingResponse();
		Trainer trainingData = new TrainingWithDefaults();
		CreateGraph newEdge = new CreateGraph();
		InitializeBeliefs beliefs = new InitializeBeliefs();
		Sums sums = new Sums();
		AverageLog avg = new AverageLog();
		Truthfinder tf = new Truthfinder();
		Investment inv = new Investment();
		String testTriples = "./src/main/resources/data/testinggraph_BOA_CW.tsv";
		String resultFile = "./src/main/resources/newExperiments/Investment.nt";
		BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/data/testtriples.tsv"));
		BufferedReader TSVFile = new BufferedReader(new FileReader(testTriples));
		Path path = Paths.get(resultFile);
		Charset charset = StandardCharsets.UTF_8;


		/**
		 * Training with default data creates a graph with true claims and initializing the belief score to be 1.0
		 */
		try {
			response = trainingData.train();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/**
		 * testing for a new claim from the default pre-searched data
		 * Bulk test follows Pasternack's technique of running the algorithms on complete graph,
		 * which in this experiment is training and testing data combined 
		 * the test claims are initialized with a belief score of 0.5.
		 */

		String dataRow = TSVFile.readLine();
		while (dataRow != null){
			String[] dataArray = dataRow.split("\\t");
			dataArray[1] = dataArray[1].replace("[", "").replace("]", "");
			result.claim = dataArray[0];
			result.sources = new LinkedHashSet<String>(Arrays.asList(dataArray[1].split(",")));
			response = newEdge.addEdge(response, result);
			response.graph = beliefs.initialize(response.graph, result.claim);	
			dataRow = TSVFile.readLine();
		}

		TSVFile.close();

		System.out.println("Total Sources: " + response.sources.size());
		System.out.println("Total Claims: " + response.claims.size());
		System.out.println("Total Edges: " + response.graph.getEdges().size());

		/**
		 * For Investmnet ALgorithm, Trust score of the sources is also needed to be initialized
		 */

		inv.trustScore(response.graph, response.sources);
		for(int i = 0; i < 20; i++) {
			tf.trustScore(response.graph, response.sources);
			tf.beliefScore(response.graph, response.claims);
		}

		dataRow = reader.readLine();
		while (dataRow != null){
			String part[] = dataRow.split("\\t");
			String claim = part[0].trim()+"_"+part[1].trim()+"_"+part[2].trim();
			String content = new String(Files.readAllBytes(path), charset);
			content = content.replace(claim, "\""+Double.toString(response.graph.getVertex(claim).getScore())+"\"");
			Files.write(path, content.getBytes(charset));
			dataRow = reader.readLine();
		}

		reader.close();
		System.out.println("--------------DONE----------------");

	}

}
