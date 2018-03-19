package org.dice_research.factfinders.preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.dice_research.factfinders.elasticSearch.PatternedQuerySearcher;
import org.dice_research.factfinders.elasticSearch.Search;
import org.dice_research.factfinders.elasticSearch.SearchResult;

/**
 * As a part of pre-processing and to increase the efficiency, The graphs are populated as data files for 4 different scenarios:
 * 1. ClueWeb Index searched with BOA patterns
 * 2. ClueWeb Index searched with WordNet patterns
 * 3. Wiki Index searched with BOA patterns
 * 4. Wiki Index searched with WordNet patterns
 * Search is performed by using the training data-set on Elastic Search
 * @author Hussain
 *
 */
public class SearchTrainingGraphDefaults {

	public static void trainingGraph() throws IOException {
		BufferedReader boaPatterns = new BufferedReader(new FileReader("./src/main/resources/data/patterns/boapatterns.tsv"));
		BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/data/example.tsv"));
		BufferedReader trueClaims = new BufferedReader(new FileReader("./src/main/resources/data/trainData/true_claims.tsv"));
		SearchResult result = new SearchResult();
		Search search  = new PatternedQuerySearcher();
		Map<String,ArrayList<String>> patterns = new HashMap<String,ArrayList<String>>();
		String dataRow = boaPatterns.readLine();
		try {
			while (dataRow != null){
				String[] dataArray = dataRow.split("\\t");
				System.out.println(dataArray[0]); // Print the data line.
				dataArray[1] = dataArray[1].replace("[", "").replace("]", "");
				patterns.put(dataArray[0], new ArrayList<String>(Arrays.asList(dataArray[1].split(","))));
				dataRow = boaPatterns.readLine(); // Read next line of data.
			}
			boaPatterns.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		dataRow = trueClaims.readLine();
		try {
			while (dataRow != null){
				String[] parts = dataRow.split("\\t");
				result = search.query(dataRow, patterns.get(parts[1]));
				if(result != null) {
					writer.append(result.claim);
					writer.append("\t");
					writer.append(result.sources.toString());
					writer.append("\n");
					System.out.println(result.claim);
					System.out.println(result.sources);
				}
				dataRow = trueClaims.readLine(); 
			}
			trueClaims.close();
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		trainingGraph();
	}

}
