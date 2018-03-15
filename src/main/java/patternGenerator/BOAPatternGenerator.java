package patternGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 * Getting BOA patterns of the predicate word
 * Extracted from Defacto, modified for simpler pattern extraction in English only
 * @author Hussain
 *
 */

public class BOAPatternGenerator implements PatternFinder{

	private static HttpSolrServer enIndex;
	private static Map<String,QueryResponse> queryCache = new HashMap<>();

	public BOAPatternGenerator() {
		enIndex = new HttpSolrServer("http://139.18.2.164:8080/solr/boa_en/");
		enIndex.setRequestWriter(new BinaryRequestWriter());
	}


	/**
	 * Queries the configured BOA index with the configured number of returned 
	 * BOA Patterns (see. Constants.NUMBER_OF_BOA_PATTERNS).
	 * 
	 * @param propertyUri 
	 * @param numberOfBoaPatterns
	 * @return
	 */
	private List<String> getNaturalLanguageRepresentations(String propertyUri, int numberOfBoaPatterns, String language){
		return querySolrIndex(propertyUri, numberOfBoaPatterns, 0.5D, language);
	}

	/**
	 * Returns all patterns from the index and their factFeatures for reverb and the
	 * wordnet distance and the overall boa-boaScore.
	 * 
	 * @param propertyUri
	 * @param language 
	 * @return a list of patterns
	 */
	private List<String> querySolrIndex(String propertyUri, int numberOfBoaPatterns, double scoreThreshold, String language) {
		Map<String,String> patterns = new HashMap<String,String>();

		try {

			if ( propertyUri.equals("http://dbpedia.org/ontology/office") ) propertyUri = "http://dbpedia.org/ontology/leaderName";

			SolrQuery query = new SolrQuery("uri:\"" + propertyUri + "\"");
			query.addField("boa-score");
			query.addField("nlr-var");
			query.addField("nlr-gen");
			query.addField("nlr-no-var");
			query.addField("SUPPORT_NUMBER_OF_PAIRS_LEARNED_FROM");
			query.addSortField("SUPPORT_NUMBER_OF_PAIRS_LEARNED_FROM", ORDER.desc);
			//query.addSortField("boa-score", ORDER.desc);
			if ( numberOfBoaPatterns > 0 ) query.setRows(numberOfBoaPatterns);

			String key = propertyUri + numberOfBoaPatterns + language;

			if ( !queryCache.containsKey(key) ) {
				if ( language.equals("en") ) queryCache.put(key, enIndex.query(query));
			}

			SolrDocumentList docList = queryCache.get(key).getResults();

			for (SolrDocument d : docList) {

				String pattern = new String();
				pattern = (String) d.get("nlr-var");
				//                String generalized = (String) d.get("nlr-gen");
				//                String naturalLanguageRepresentationWithoutVariables = (String) d.get("nlr-no-var");
				//                String posTags = (String) d.get("pos");


				// only add the first pattern, we don't want to override the better scored pattern
				if ( !pattern.trim().isEmpty() && !patterns.containsKey(pattern)  
						&& patterns.size() < 10 ) 
					patterns.put(pattern, pattern);
			}

		}
		catch (SolrServerException e) {

			System.out.println("Could not execute query: " + e);
			e.printStackTrace();
		}

		List<String> patternList = new ArrayList<String>(patterns.values());

		return patternList;
	}


	@Override
	public Set<String> querryPatterns(String wordForm) {
		Set<String> foundWords = new HashSet<String>();
		int nr = 50;
		List<String> sub = new ArrayList<>();
		BOAPatternGenerator bpg = new BOAPatternGenerator();
		sub.addAll(bpg.getNaturalLanguageRepresentations(wordForm, nr, "en"));

		Iterator<String> iterator = sub.iterator();
		while ( iterator.hasNext()) {
			String pattern = iterator.next();
			foundWords.add(pattern);
		}
		return foundWords;
	}

	
//	public static void main(String[] args) {
//
//		queryPatterns("http://dbpedia.org/ontology/married");
//		System.out.println("--------------");
//	}

}