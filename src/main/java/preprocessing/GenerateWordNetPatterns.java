package preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import patternGenerator.PatternFinder;
import patternGenerator.WordnetPatternGenerator;

/**
 * WordNet patterns are extracted for each predicate word.
 * WordNet does not provide patterns for composite words like "Foundation Place"
 * @author Hussain
 *
 */
public class GenerateWordNetPatterns {

	public static void writeWordNetPatterns() throws IOException {
		PatternFinder wordnetPatternGenerator = new WordnetPatternGenerator();
		BufferedReader TSVFile = new BufferedReader(new FileReader("./src/main/resources/data/testtriples.tsv"));
		BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/data/wordnetpatterns.tsv"));
		String dataRow = TSVFile.readLine();
		Set<String> plotter = new HashSet<String>();
		try {
			while (dataRow != null){
				String[] dataArray = dataRow.split("\\t");
				plotter.add(dataArray[1]);
				dataRow = TSVFile.readLine(); // Read next line of data.
			}
			TSVFile.close();

			for(String instance: plotter) {
				writer.append(instance);
				writer.append("\t");
				if(wordnetPatternGenerator.querryPatterns(instance) == null)
					writer.append("No_Pattern");
				else
					writer.append(wordnetPatternGenerator.querryPatterns(instance).toString());
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
		writeWordNetPatterns();
	}
}
