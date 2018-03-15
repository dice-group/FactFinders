package elasticSearch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import patternGenerator.PatternFinder;
import patternGenerator.WordnetPatternGenerator;

/**
 * This class searches for the sources for each claim. 
 * The Elastic search rest client is realized for the purpose of searching
 * For each claim several several queries are generated using patterns.
 * The result of all the queries is returned in the form of a singleton list containing the sources URLs 
 *
 * @author Hussain
 */

public class PatternedQuerySearcher extends Searcher{

	private Set<String> pageTitles = new LinkedHashSet<String>();
	private SearchResult searchResult = new SearchResult();
	private String[] parts;
	private String subject;
	private String property;
	private String object;
	private PatternFinder wordnetPatternGenerator = new WordnetPatternGenerator();

	/**
	 * This query function queries the index (either clue-web or Wiki-dump for these experiments)
	 * The query is elevated to multiple queries by using several predicate patterns.
	 * These patterns are generated at runtime by using Word-net or BOA patterns.
	 * @param claim is the fact to be queried/searched on index
	 */
	@Override
	public SearchResult query(String claim) {

		try {
			
			claimParser(claim);
			subject  = subject.replace("&", "and");
			object   = object.replace("&", "and");
			property = normalizePredicate(property.trim());
			Set<String> propertyWords = wordnetPatternGenerator.querryPatterns(property);
			if(propertyWords == null) {
				String q1 = String.format("\"%s %s %s\"", subject, property, object);
				if ( property.equals("??? NONE ???") )
					q1 = String.format("\"%s %s\"", subject, object);

								HttpEntity entity1 = new NStringEntity(
								 "{\n" +
										"	\"size\" : 10000,\n" +
										"    \"query\" : {\n" +
										"    \"match_phrase\" : {\n"+
										"	 \"Article\" : {\n" +
										"	\"query\" : "+q1+",\n"+
										"	\"slop\"  : 100 \n"+
										"} \n"+
										"} \n"+
										"} \n"+
								"}", ContentType.APPLICATION_JSON);
								
				response = restClientobj.performRequest("GET", "/clueweb/articles/_search",Collections.singletonMap("pretty", "true"),entity1, consumerFactory);
				String json = EntityUtils.toString(response.getEntity());			
				ObjectMapper mapper = new ObjectMapper();
				JsonNode rootNode = mapper.readValue(json, JsonNode.class);
				JsonNode hits = rootNode.get("hits");
				JsonNode hitCount = hits.get("total");
				int docCount = Integer.parseInt(hitCount.asText());
				for(int i=0; i<docCount; i++)
				{
					JsonNode articleURLNode = hits.get("hits").get(i).get("_source").get("URL");
					String articleURL = getDomainName(articleURLNode.asText());
					
					pageTitles.add(articleURL);
				}

				searchResult.claim = claim.replace("\t", "_");
				searchResult.sources = pageTitles;
				return searchResult;
			}
			else {
				for(String predicate : propertyWords) {
					String q1 = String.format("\"%s %s %s\"", subject, predicate, object);
					if ( predicate.equals("??? NONE ???") )
						q1 = String.format("\"%s %s\"", subject, object);

									HttpEntity entity1 = new NStringEntity(
									 "{\n" +
											"	\"size\" : 10000,\n" +
											"    \"query\" : {\n" +
											"    \"match_phrase\" : {\n"+
											"	 \"Article\" : {\n" +
											"	\"query\" : "+q1+",\n"+
											"	\"slop\"  : 50 \n"+
											"} \n"+
											"} \n"+
											"} \n"+
									"}", ContentType.APPLICATION_JSON);
									
					Response response = restClientobj.performRequest("GET", "/clueweb/articles/_search",Collections.singletonMap("pretty", "true"),entity1, consumerFactory);
					String json = EntityUtils.toString(response.getEntity());			
					ObjectMapper mapper = new ObjectMapper();
					JsonNode rootNode = mapper.readValue(json, JsonNode.class);
					JsonNode hits = rootNode.get("hits");
					JsonNode hitCount = hits.get("total");
					int docCount = Integer.parseInt(hitCount.asText());
					for(int i=0; i<docCount; i++)
					{
						JsonNode articleURLNode = hits.get("hits").get(i).get("_source").get("URL");
						String articleURL = getDomainName(articleURLNode.asText());
						
						pageTitles.add(articleURL);
					}
				}
				
				searchResult.claim = claim.replace("\t", "_");
				searchResult.sources = pageTitles;
				return searchResult;
			}
		}
		catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This query function queries the index (either clue-web or Wiki-dump for these experiments)
	 * The query is elevated to multiple queries by using several predicate patterns.
	 * These patterns are already saved during pre-processing to make the search faster
	 * In pre-processing Word-net or BOA pattern generators are used to store patterns as .tsv file.
	 * @param claim is the fact to be queried/searched on index
	 * @param propertyWords is a list of pattern words generated after pre-processing
	 */
	@Override
	public SearchResult query(String claim, ArrayList<String> propertyWords) {
		try {
			
			claimParser(claim);
			subject  = subject.replace("&", "and");
			object   = object.replace("&", "and");
			property = normalizePredicate(property.trim());

			for(String predicate : propertyWords) {
				String q1;
				if(predicate.equals("No_Pattern")) {
					q1 = String.format("\"%s %s %s\"", subject, property, object);
				}
				else
					q1 = String.format("\"%s %s %s\"", subject, predicate.trim(), object);

								HttpEntity entity1 = new NStringEntity(
								 "{\n" +
										"	\"size\" : 10000,\n" +
										"    \"query\" : {\n" +
										"    \"match_phrase\" : {\n"+
										"	 \"Article\" : {\n" +
										"	\"query\" : "+q1+",\n"+
										"	\"slop\"  : 100 \n"+
										"} \n"+
										"} \n"+
										"} \n"+
								"}", ContentType.APPLICATION_JSON);
				long start = System.nanoTime();					
				response = restClientobj.performRequest("GET", "/clueweb/articles/_search",Collections.singletonMap("pretty", "true"),entity1, consumerFactory);
				System.out.print("Search on server took " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start) + " seconds \n");
				BufferedReader in = new BufferedReader(
				        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));		
				ObjectMapper mapper = new ObjectMapper();
				JsonNode rootNode = mapper.readValue(in, JsonNode.class);
				in.close();
				JsonNode hits = rootNode.get("hits");
				JsonNode hitCount = hits.get("total");
				int docCount = Integer.parseInt(hitCount.asText());
				if (docCount != 0) {
					if(docCount > 10000) {
						docCount = 10000;
					}
					for (int i = 0; i < docCount; i++) {
						JsonNode articleURLNode = hits.get("hits").get(i).get("_source").get("URL");

						String articleURL = getDomainName(articleURLNode.asText());

						if (!pageTitles.contains(articleURL)) {
							pageTitles.add(articleURL);
						}
					} 
				}
			}
			
			searchResult.claim = claim.replace("\t", "_");
			searchResult.sources = pageTitles;
			return searchResult;
		}
		catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Vertex is read from the dataset which contains tab separated subject, predicate and object
	 * THey are saved in the local variables respectively.
	 * @param claim
	 */
	public void claimParser(String claim) {
		this.parts  = claim.split("\\t");
		this.subject   = parts[0];
		this.property  = parts[1];
		this.object    = parts[2];
		
	}
}

