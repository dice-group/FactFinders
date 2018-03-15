package experiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import elasticSearch.PatternedQuerySearcher;
import elasticSearch.Search;
import elasticSearch.SearchResult;
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
 * BulkClaimTest for each algorithm follow the Pasternack's technique of Algorithm execution
 * The graph is trained once for all the train data
 * All the test data is than introduced to the Graph
 * The test and train triples are created using RDF triplizer which are used for search
 * Results are logged into the sample variant file of algorithms
 * @author Hussain
 *
 */
public class BulkClaimTest {

	public static void main(String[] args) throws IOException {
		SearchResult result = new SearchResult();
		TrainingResponse response = new TrainingResponse();
		Trainer trainingData = new TrainingWithSearch();
		CreateGraph newEdge = new CreateGraph();
		Search search = new PatternedQuerySearcher();
		InitializeBeliefs beliefs = new InitializeBeliefs();
		Sums sums = new Sums();
		AverageLog avg = new AverageLog();
		Truthfinder tf = new Truthfinder();
		Investment inv = new Investment();
		String testTriples = "./src/main/resources/data/testtriples.tsv";
		String resultFile = "./src/main/resources/newExperiments/Sum.nt";
		BufferedReader reader = new BufferedReader(new FileReader(testTriples));
		BufferedReader TSVFile = new BufferedReader(new FileReader(testTriples));
		Path path = Paths.get(resultFile);
		Charset charset = StandardCharsets.UTF_8;


		/**
		 * TrainingWithSearch the graph with true claims and initializing the belief score to be 1.0
		 */
		response = trainingData.train();

		/**
		 * Testing for all the claims by adding them 1 by 1
		 * Once all the test data becomes part of the Graph.
		 * The test claims are initialized by a Belief Score of 0.5
		 */

		String dataRow = TSVFile.readLine();
		while (dataRow != null){
			result = search.query(dataRow);
			response = newEdge.addEdge(response, result);
			response.graph = beliefs.initialize(response.graph, result.claim);	
			dataRow = TSVFile.readLine();
		}

		TSVFile.close();

		System.out.println("Total Sources: " + response.sources.size());
		System.out.println("Total Claims: " + response.claims.size());
		System.out.println("Total Edges: " + response.graph.getEdges().size());

		/**
		 * Algorithms are executed in sequential iterations for generating
		 * sources's Trust Score and claims's Belief Score
		 * Number of iterations are set to 20 as per Pasternacks Technique
		 */

		//		For Investmnet open the comment below

		//		inv.trustScore(response.graph, response.sources);
		for(int i = 0; i < 20; i++) {
			sums.trustScore(response.graph, response.sources);
			sums.beliefScore(response.graph, response.claims);
		}

		/**
		 * The results after 20 iterations are logged against each claim.
		 * This result file can be uploaded to GERBIL for experiment's results.
		 */
		dataRow = reader.readLine();
		while (dataRow != null){
			String part[] = dataRow.split("\\t");
			String claim = part[0].trim()+"_"+part[1].trim()+"_"+part[2].trim();
			String content = new String(Files.readAllBytes(path), charset);
			content = content.replace(claim, "\""+Double.toString(response.graph.getVertex(claim).getScore())+"\"");
			Files.write(path, content.getBytes(charset));
			dataRow = reader.readLine();
		}

		System.out.println("--------------DONE----------------");
		reader.close();
	}

}
