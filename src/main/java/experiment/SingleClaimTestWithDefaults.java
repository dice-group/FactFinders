package experiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import elasticSearch.SearchBOAPatterns;
import elasticSearch.SearchResult;
import factfinders.AverageLog;
import factfinders.InitializeBeliefs;
import factfinders.Investment;
import factfinders.PooledInvestment;
import factfinders.Sums;
import factfinders.Truthfinder;
import graphPlotter.CreateGraph;
import trainingGraph.TrainingResponse;
import trainingGraph.TrainingWithDefaults;

public class SingleClaimTestWithDefaults {

	public static void main(String[] args) throws IOException {
		SearchResult result = new SearchResult();
		TrainingResponse response = new TrainingResponse();
		TrainingWithDefaults trainingData = new TrainingWithDefaults();
//		SearchWordnetPatterns search = new SearchWordnetPatterns();
		SearchBOAPatterns search = new SearchBOAPatterns();
		CreateGraph newEdge = new CreateGraph();
		InitializeBeliefs beliefs = new InitializeBeliefs();
		Sums sums = new Sums();
		AverageLog avg = new AverageLog();
		Truthfinder tf = new Truthfinder();
		Investment inv = new Investment();
		PooledInvestment pool = new PooledInvestment();
		String testTriples = "./src/main/resources/data/testinggraph_BOA_CW.tsv";
		String resultFile = "./src/main/resources/newExperiments/SingleInv.nt";
		BufferedReader TSVFile = new BufferedReader(new FileReader(testTriples));
		Path path = Paths.get(resultFile);
		Charset charset = StandardCharsets.UTF_8;
		Map<String, String>  singleResults = new HashMap<String, String>();
		
		System.out.println("Running Single Avg Claim Test");
		/**
		 * TrainingWithSearch the graph with true claims and initializing the belief score to be 1.0
		 */
		response = trainingData.train();
		
		/**
		 * testing for a new claim
		 */
		
		String dataRow = TSVFile.readLine();
		while (dataRow != null){
//			System.out.println(dataRow);
			String[] dataArray = dataRow.split("\\t");
			dataArray[1] = dataArray[1].replace("[", "").replace("]", "");
//			if(result == null) {
//				System.out.println(" result null : " + dataRow);
//				dataRow = TSVFile.readLine();
//				continue;
//			}
			result.claim = dataArray[0];
			result.sources = new LinkedHashSet<String>(Arrays.asList(dataArray[1].split(",")));
			response = newEdge.addEdge(response, result);
			response.graph = beliefs.initialize(response.graph, result.claim, "testing");
			
//			System.out.println("Total Sources: " + response.sources.size());
//			System.out.println("Total Claims: " + response.claims.size());
//			System.out.println("Total Edges: " + response.graph.getEdges().size());
			
			
//			For Investmnet open the comment below
			
			inv.trustScore(response.graph, response.sources);
			for(int i = 0; i < 20; i++) {
				inv.trustScore(response.graph, response.sources);
		        inv.beliefScore(response.graph, response.claims);
		       }
			
			singleResults.put(result.claim, Double.toString(response.graph.getVertex(result.claim).getScore()));
			
			System.out.println(result.claim);
			response = newEdge.removeEdge(response, result);
			response.graph = beliefs.initialize(response.graph, response.claims, "training");
			
			dataRow = TSVFile.readLine();
		}
		TSVFile.close();
		
		for(String claim : singleResults.keySet()){
			String content = new String(Files.readAllBytes(path), charset);
			content = content.replace(claim, "\""+singleResults.get(claim)+"\"");
			Files.write(path, content.getBytes(charset));
//			System.out.println(claim +" : "+ singleResults.get(claim));
		}
		
		System.out.println("--------------DONE----------------");
	}

}
