package org.dice_research.factfinders.preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.dice_research.factfinders.patternGenerator.BOAPatternGenerator;
import org.dice_research.factfinders.patternGenerator.PatternFinder;

/**
 * UNique BOA patterns for all the Unique predicates are generated.
 * @author Hussain
 *
 */

public class GenerateBOAPatterns {
	
	public static void writeBOAPatterns() throws IOException {
		PatternFinder boa = new BOAPatternGenerator();
		BufferedReader TSVFile = new BufferedReader(new FileReader("./src/main/resources/data/testtriples.tsv"));
		BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/data/boapatterns.tsv"));
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
		    	writer.append(boa.querryPatterns("http://dbpedia.org/ontology/"+instance).toString());
		    	writer.append("\n");
		    }
		// Close the file once all data has been read.
		    writer.flush();
		    writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		writeBOAPatterns();
	}

}
