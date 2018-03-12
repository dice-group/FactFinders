package preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import patternGenerator.WordnetPatternGenerator;

public class GenerateWordNetPatterns {

	public static void main(String[] args) throws IOException {
		writeWordNetPatterns();

	}

	public static void writeWordNetPatterns() throws IOException {
		WordnetPatternGenerator wordnetPatternGenerator = new WordnetPatternGenerator();
		BufferedReader TSVFile = new BufferedReader(new FileReader("./src/main/resources/data/testtriples.tsv"));
		BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/data/wordnetpatterns.tsv"));
		String dataRow = TSVFile.readLine();
		Set<String> plotter = new HashSet<String>();
		try {
		    while (dataRow != null){
		        String[] dataArray = dataRow.split("\\t");
		        System.out.println(dataArray[1]); // Print the data line.
		        plotter.add(dataArray[1]);
		        dataRow = TSVFile.readLine(); // Read next line of data.
		    }
		    TSVFile.close();
		
		    for(String instance: plotter) {
		    	writer.append(instance);
		    	writer.append("\t");
		    	if(wordnetPatternGenerator.getSynonyms(instance) == null)
		    		writer.append("No_Pattern");
		    	else
		    		writer.append(wordnetPatternGenerator.getSynonyms(instance).toString());
		    	writer.append("\n");
		    }
		// Close the file once all data has been read.
		    writer.flush();
		    writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
