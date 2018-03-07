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

import demo.CreateGraph;
import elasticSearch.SearchBOAPatterns;
import elasticSearch.SearchResult;
import elasticSearch.SearchWordnetPatterns;
import factFinders.AverageLog;
import factFinders.InitializeBeliefs;
import factFinders.Investment;
import factFinders.PooledInvestment;
import factFinders.Sums;
import factFinders.Truthfinder;

/**
 * Testing for each algorithm
 * The test and train triples are created using RDF triplizer(Search)
 * Algorithms are tested for each test claim with training claims 
 * Results are logged into the sample variant of algorithm
 * @author Hussain
 *
 */
public class SingleClaimTest {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		SearchResult result = new SearchResult();
		TrainingResponse response = new TrainingResponse();
		Training trainingData = new Training();
//		SearchWordnetPatterns search = new SearchWordnetPatterns();
		SearchBOAPatterns search = new SearchBOAPatterns();
		CreateGraph newEdge = new CreateGraph();
		InitializeBeliefs beliefs = new InitializeBeliefs();
		Sums sums = new Sums();
		AverageLog avg = new AverageLog();
		Truthfinder tf = new Truthfinder();
		Investment inv = new Investment();
		PooledInvestment pool = new PooledInvestment();
		String testTriples = "./src/main/resources/data/testtriples.tsv";
		String resultFile = "./src/main/resources/newExperiments/SingleSum.nt";
		BufferedReader reader = new BufferedReader(new FileReader(testTriples));
		BufferedReader TSVFile = new BufferedReader(new FileReader(testTriples));
		Path path = Paths.get(resultFile);
		Charset charset = StandardCharsets.UTF_8;
		Map<String, String>  singleResults = new HashMap<String, String>();
		
		System.out.println("Running Single Claim Test");
		/**
		 * Training the graph with true claims and initializing the belief score to be 1.0
		 */
		response = trainingData.train();
		
		/**
		 * testing for a new claim
		 */
		
		String dataRow = TSVFile.readLine();
		while (dataRow != null){
			System.out.println(dataRow);
			result = search.query(dataRow);
//			if(result == null) {
//				System.out.println(" result null : " + dataRow);
//				dataRow = TSVFile.readLine();
//				continue;
//			}
			response = newEdge.addEdge(response, result);
			response.graph = beliefs.initialize(response.graph, result.claim, "testing");
			
			System.out.println("Total Sources: " + response.sources.size());
			System.out.println("Total Claims: " + response.claims.size());
			System.out.println("Total Edges: " + response.graph.getEdges().size());
			
			
//			For Investmnet open the comment below
			
//			inv.trustScore(response.graph, response.sources);
			for(int i = 0; i < 20; i++) {
				sums.trustScore(response.graph, response.sources);
		        sums.beliefScore(response.graph, response.claims);
		       }
			
			String part[] = dataRow.split("\\t");
			String claim = part[0].trim()+"_"+part[1].trim()+"_"+part[2].trim();
			singleResults.put(claim, Double.toString(response.graph.getVertex(claim).getScore()));
			
			response = newEdge.removeEdge(response, result);
			
			dataRow = TSVFile.readLine();
		}
		TSVFile.close();
		
		for(String claim : singleResults.keySet()){
			String content = new String(Files.readAllBytes(path), charset);
			content = content.replace(claim, "\""+singleResults.get(claim)+"\"");
			Files.write(path, content.getBytes(charset));
			System.out.println(claim +" : "+ singleResults.get(claim));
		}
		
		System.out.println("--------------DONE----------------");
		reader.close();
	}
}
