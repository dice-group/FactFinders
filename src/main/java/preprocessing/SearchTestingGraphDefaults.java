package preprocessing;

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

import elasticSearch.SearchDefaults;
import elasticSearch.SearchResult;

public class SearchTestingGraphDefaults {

	public static void main(String[] args) throws IOException {
		testingGraph();

	}
	
	public static void testingGraph() throws IOException {
		BufferedReader boaPatterns = new BufferedReader(new FileReader("./src/main/resources/data/wordnetpatterns.tsv"));
		BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/data/CW_WN_testinggraph.tsv"));
		BufferedReader trueClaims = new BufferedReader(new FileReader("./src/main/resources/data/testtriples.tsv"));
		SearchResult result = new SearchResult();
		SearchDefaults search = new SearchDefaults();
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
//				start = System.nanoTime();
				String[] parts = dataRow.split("\\t");
				result = search.query(dataRow, patterns.get(parts[1]));
//				System.out.print("Search took " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start) + " seconds \n");
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

}
