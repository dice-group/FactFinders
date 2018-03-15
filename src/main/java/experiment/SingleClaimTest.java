package experiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import elasticSearch.Search;
import elasticSearch.SearchResult;
import elasticSearch.PatternedQuerySearcher;
import factfinders.AverageLog;
import factfinders.InitializeBeliefs;
import factfinders.Investment;
import factfinders.Sums;
import factfinders.Truthfinder;
import graphPlotter.CreateGraph;
import trainingGraph.TrainingWithSearch;
import trainingGraph.Trainer;
import trainingGraph.TrainingResponse;

/**
 * Algorithms are tested for each test claim with training claims 
 * The graph for training data is created and initialized after the claims are being searched for sources.
 * Each test claim is added and removed sequentially in the graph, while the Belief Score for each test claim is calculated separately
 * These experiments follow DeFacto's approach, Results are logged into the sample variant of algorithm
 * @author Hussain
 *
 */
public class SingleClaimTest {

	public static void main(String[] args) throws IOException {
		SearchResult result = new SearchResult();
		TrainingResponse response = new TrainingResponse();
		Trainer trainingData = new TrainingWithSearch();
		CreateGraph newEdge = new CreateGraph();
		InitializeBeliefs beliefs = new InitializeBeliefs();
		Search search = new PatternedQuerySearcher();
		
		Sums sums = new Sums();
		AverageLog avg = new AverageLog();
		Truthfinder tf = new Truthfinder();
		Investment inv = new Investment();
		
		String testTriples = "./src/main/resources/data/testtriples.tsv";
		String resultFile = "./src/main/resources/newExperiments/SingleAvg.nt";
		BufferedReader TSVFile = new BufferedReader(new FileReader(testTriples));
		Path path = Paths.get(resultFile);
		Charset charset = StandardCharsets.UTF_8;
		Map<String, String>  singleResults = new HashMap<String, String>();

		System.out.println("Running Single Avg Vertex Test");
		/**
		 * Training with data after search makes a graph with true claims and sources making those claims
		 * Belief score of each true claim is initialized to be 1.0
		 */
		response = trainingData.train();

		/**
		 * Testing for a new claim, the claim is searched and the SearchResult
		 * comprising of claim and list of sources are added into the training graph
		 * Test claim is initialized and Algorithm is executed
		 * This is performed sequentially for each single claim
		 */

		String dataRow = TSVFile.readLine();
		while (dataRow != null){
			result = search.query(dataRow);
			response = newEdge.addEdge(response, result);
			response.graph = beliefs.initialize(response.graph, result.claim);


			// inv.trustScore(response.graph, response.sources);
			for(int i = 0; i < 20; i++) {
				avg.trustScore(response.graph, response.sources);
				avg.beliefScore(response.graph, response.claims);
			}

			singleResults.put(result.claim, Double.toString(response.graph.getVertex(result.claim).getScore()));

			/**
			 * The graph is re-initialized after the removal of test claim to its naive state
			 */
			response = newEdge.removeEdge(response, result);
			response.graph = beliefs.initialize(response.graph, response.claims);

			dataRow = TSVFile.readLine();
		}
		TSVFile.close();

		/**
		 * Final Belief Scores of the test claims are logged into the sample result file
		 */
		for(String claim : singleResults.keySet()){
			String content = new String(Files.readAllBytes(path), charset);
			content = content.replace(claim, "\""+singleResults.get(claim)+"\"");
			Files.write(path, content.getBytes(charset));
		}

		System.out.println("--------------DONE----------------");
	}
}
