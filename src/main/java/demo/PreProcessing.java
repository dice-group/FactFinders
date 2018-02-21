package demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import elasticSearch.SentenceTriple;
import elasticSearch.SentenceTriplizer;

/**
 * In case of sentence triplization, some pre-processing is required.
 * It involves getting the sentence from the input tsv file and generating triples using the sentence triplizer.
 * @author Hussain
 */

public class PreProcessing {
	  public static void main(String[] arg) throws Exception {
		  ArrayList<String> facts = new ArrayList<String>();
		  facts = extraction();
		  triplization(facts);
       
	    }
	  
	  public static ArrayList<String> extraction() throws IOException {
		BufferedReader TSVFile;
		TSVFile = new BufferedReader(new FileReader("./src/main/resources/true_claims.tsv"));
		String dataRow = TSVFile.readLine(); // Read first line.
		ArrayList<String> facts = new ArrayList<String>();
			try {
		        while (dataRow != null){
			        String[] dataArray = dataRow.split("\\t");
			        System.out.println(dataArray[1]); // Print the data line.
			        facts.add(dataArray[1]);
			        dataRow = TSVFile.readLine(); // Read next line of data.
		        }
		        // Close the file once all data has been read.
		        TSVFile.close();
		        System.out.println(facts.size());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return facts;
	  }
	  
	  public static void triplization(ArrayList<String> facts) throws IOException {
		  FileWriter writer = new FileWriter("./src/main/resources/triples.tsv");
		  facts.remove(0);

		  for(String fact : facts) {
			  fact = fact.replace(".", "").replace(",", "");
				System.out.println(fact);
				SentenceTriplizer triplizer = new SentenceTriplizer();
				triplizer.init();
				Map<Integer, SentenceTriple> results = triplizer.extractTriples(fact);
				
				for(Entry<Integer, SentenceTriple> res: results.entrySet()){
					SentenceTriple triple = res.getValue();
					for(String frag : triple.getSubject())
						writer.append(frag);
					writer.append('\t');
					writer.append(triple.getPredicate());
					writer.append('\t');
					for(String frag : triple.getObject())
						 writer.append(frag);
					writer.append('\n');
					//System.out.println(res.getKey());
					//System.out.println( triple.getPredicate() + "(" + triple.getSubject() + "," +  triple.getObject() + ")");
					//System.out.println("...");
				}
			}
		  writer.flush();
		  writer.close();
	  }
}
