package org.dice_research.factfinders.patternGenerator;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

/**
 * Getting synonyms of the predicate word from the word net index
 * @author Hussain
 *
 */
public class WordnetPatternGenerator implements PatternFinder{

	@Override
	public Set<String> querryPatterns(String wordForm){
		File f=new File("./dict");
		System.setProperty("wordnet.database.dir", f.toString());
		//setting path for the WordNet Directory

		WordNetDatabase database = WordNetDatabase.getFileInstance();
		Synset[] synsets = database.getSynsets(wordForm);
		// Display the word forms and definitions for synsets retrieved

		if (synsets.length > 0){
			Set<String> foundwords = new HashSet<String>();
			// add elements to al, including duplicates
			for (int i = 0; i < synsets.length; i++){
				String[] wordForms = synsets[i].getWordForms();
				for (int j = 0; j < wordForms.length; j++)
				{
					foundwords.add(wordForms[j]);
				}
			}
			return foundwords;
		}
		else
		{
			return null;
		}
	}
	
	public static void main(String[] args){
		String wordForm = "polygon";
		//  Get the synsets containing the word form=capicity

		File f=new File("./dict");
		System.setProperty("wordnet.database.dir", f.toString());
		//setting path for the WordNet Directory

		WordNetDatabase database = WordNetDatabase.getFileInstance();
		Synset[] synsets = database.getSynsets(wordForm);
		//  Display the word forms and definitions for synsets retrieved

		if (synsets.length > 0){
			Set<String> al = new HashSet<String>();
			// add elements to al, including duplicates
			for (int i = 0; i < synsets.length; i++){
				String[] wordForms = synsets[i].getWordForms();
				for (int j = 0; j < wordForms.length; j++)
				{
					al.add(wordForms[j]);
				}
				//showing all synsets
			}
			for (String s : al) {
				System.out.println(s);
				System.out.println("...................");
			}
		}
		else
		{
			System.err.println("No synsets exist that contain the word form '" + wordForm + "'");
		}
	}
}
