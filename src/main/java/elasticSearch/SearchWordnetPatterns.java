package elasticSearch;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import patternGenerator.WordnetPatternGenerator;



/**
 * This class searches for the sources for each claim. 
 * The Elastic search rest client is realized for the purpose of searching
 * For each claim several several queries are generated using Word net patterns.
 * The result of all the queries is returned in the form of a singleton list containing the sources URL  
 *
 * @author Hussain
 */

public class SearchWordnetPatterns{

	private static String ELASTIC_SERVER = "131.234.28.255";
	private static String ELASTIC_PORT = "6060";
	private static RestClient restClientobj;
	private static Response response;
	private static HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory consumerFactory;
	
	public SearchWordnetPatterns(){
		 restClientobj = RestClient.builder(new HttpHost(ELASTIC_SERVER , Integer.parseInt(ELASTIC_PORT), "http"))
				 .setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
           public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
               return requestConfigBuilder.setConnectTimeout(5000)
                       .setSocketTimeout(600000);
           }
       }).setMaxRetryTimeoutMillis(600000)
				.build();
		
		 consumerFactory =
		        new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(1024 * 1024 * 1024);
	}

	public SearchResult query(String claim) {

		try {
			ArrayList<String> pageTitles = new ArrayList<String>();
			SearchResult searchResult = new SearchResult();
			WordnetPatternGenerator wordnetPatternGenerator = new WordnetPatternGenerator();
			
			
//			System.out.println(claim);
//			SentenceTriplizer triplizer = new SentenceTriplizer();
//			triplizer.init();
//			Map<Integer, SentenceTriple> results = triplizer.extractTriples(claim);
//
//			
//			for(Entry<Integer, SentenceTriple> res: results.entrySet()){
//				SentenceTriple triple = res.getValue();
//				System.out.println(res.getKey());
//				System.out.println( triple.getPredicate() + "(" + triple.getSubject() + "," +  triple.getObject() + ")");
//				System.out.println("...");
//			}

			
		       String[] parts      = claim.split("\\t");
		       String subject   = parts[0];
		       String property  = parts[1];
		       String object    = parts[2];
			subject  = subject.replace("&", "and");
			object   = object.replace("&", "and");
			property = normalizePredicate(property.trim());
			Set<String> propertyWords = wordnetPatternGenerator.getSynonyms(property);
			if(propertyWords == null) {
				String q1 = String.format("\"%s %s %s\"", subject, property, object);
				if ( property.equals("??? NONE ???") )
					q1 = String.format("\"%s %s\"", subject, object);
//				System.out.println(q1);
				String q2 = String.format("\"%s\"", object);
				String q3 = String.format("\"%s %s\"",subject,object);

								HttpEntity entity1 = new NStringEntity(
								 "{\n" +
										"	\"size\" : 10000,\n" +
										"    \"query\" : {\n" +
//////										"	 \"bool\"  : {\n" +
//////										"	 \"must\"  : [\n" +	
//////										"	{\n"+
										"    \"match_phrase\" : {\n"+
										"	 \"Article\" : {\n" +
										"	\"query\" : "+q1+",\n"+
										"	\"slop\"  : 100 \n"+
										"} \n"+
										"} \n"+
										"} \n"+
//////								"	{\n"+
//////								"    \"match_phrase\" : {\n"+
//////								"	 \"Article\" : {\n" +
//////								"	\"query\" : "+q3+",\n"+
//////								"	\"slop\"  : 10 \n"+
//////								"} \n"+
//////								"} \n"+
//////								"} \n"+
//////								"] \n"+
//////								"} \n"+
//////								"} \n"+
								"}", ContentType.APPLICATION_JSON);
								
				response = restClientobj.performRequest("GET", "/clueweb/articles/_search",Collections.singletonMap("pretty", "true"),entity1, consumerFactory);
				String json = EntityUtils.toString(response.getEntity());			
				//System.out.println(json);
				ObjectMapper mapper = new ObjectMapper();
				@SuppressWarnings("unchecked")
				JsonNode rootNode = mapper.readValue(json, JsonNode.class);
				JsonNode hits = rootNode.get("hits");
				JsonNode hitCount = hits.get("total");
				int docCount = Integer.parseInt(hitCount.asText());
				for(int i=0; i<docCount; i++)
				{
					JsonNode articleNode = hits.get("hits").get(i).get("_source").get("Article");
					JsonNode articleURLNode = hits.get("hits").get(i).get("_source").get("URL");
					JsonNode articleTitleNode = hits.get("hits").get(i).get("_source").get("Title");
					JsonNode articleID = hits.get("hits").get(i).get("_id");
					String articleText = articleNode.asText();
					String articleId = articleID.asText();
					String articleURL = getDomainName(articleURLNode.asText());
					String articleTitle = articleTitleNode.asText();
					//System.out.println(articleURL);
					//System.out.println(articleURL);
					//System.out.println(articleId);
					
					pageTitles.add(articleURL);
				}

				searchResult.claim = claim.replace("\t", "_");
				searchResult.sources = pageTitles;
//				System.out.println(pageTitles.size() +" : " + searchResult.sources.size());
				return searchResult;
			}
			else {
				for(String predicate : propertyWords) {
					String q1 = String.format("\"%s %s %s\"", subject, predicate, object);
					if ( predicate.equals("??? NONE ???") )
						q1 = String.format("\"%s %s\"", subject, object);
//					System.out.println(q1);
					String q2 = String.format("\"%s\"", object);
					String q3 = String.format("\"%s %s\"",subject,object);

									HttpEntity entity1 = new NStringEntity(
									 "{\n" +
											"	\"size\" : 10000,\n" +
											"    \"query\" : {\n" +
//////											"	 \"bool\"  : {\n" +
//////											"	 \"must\"  : [\n" +	
//////											"	{\n"+
											"    \"match_phrase\" : {\n"+
											"	 \"Article\" : {\n" +
											"	\"query\" : "+q1+",\n"+
											"	\"slop\"  : 50 \n"+
											"} \n"+
											"} \n"+
											"} \n"+
//////									"	{\n"+
//////									"    \"match_phrase\" : {\n"+
//////									"	 \"Article\" : {\n" +
//////									"	\"query\" : "+q3+",\n"+
//////									"	\"slop\"  : 10 \n"+
//////									"} \n"+
//////									"} \n"+
//////									"} \n"+
//////									"] \n"+
//////									"} \n"+
//////									"} \n"+
									"}", ContentType.APPLICATION_JSON);
									
					Response response = restClientobj.performRequest("GET", "/clueweb/articles/_search",Collections.singletonMap("pretty", "true"),entity1, consumerFactory);
					String json = EntityUtils.toString(response.getEntity());			
					//System.out.println(json);
					ObjectMapper mapper = new ObjectMapper();
					@SuppressWarnings("unchecked")
					JsonNode rootNode = mapper.readValue(json, JsonNode.class);
					JsonNode hits = rootNode.get("hits");
					JsonNode hitCount = hits.get("total");
					int docCount = Integer.parseInt(hitCount.asText());
					for(int i=0; i<docCount; i++)
					{
						JsonNode articleNode = hits.get("hits").get(i).get("_source").get("Article");
						JsonNode articleURLNode = hits.get("hits").get(i).get("_source").get("URL");
						JsonNode articleTitleNode = hits.get("hits").get(i).get("_source").get("Title");
						JsonNode articleID = hits.get("hits").get(i).get("_id");
						String articleText = articleNode.asText();
						String articleId = articleID.asText();
						String articleURL = getDomainName(articleURLNode.asText());
						String articleTitle = articleTitleNode.asText();
						//System.out.println(articleURL);
						//System.out.println(articleURL);
						//System.out.println(articleId);
						
						pageTitles.add(articleURL);
					}
				}
				
				searchResult.claim = claim.replace("\t", "_");
				searchResult.sources = pageTitles;
//				System.out.println(pageTitles.size() +" : " + searchResult.sources.size());
				return searchResult;
			}
		}
		catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public String normalizePredicate(String propertyLabel) {
		//System.out.println(propertyLabel);
		return propertyLabel.replaceAll(",", "").replace("`", "").replace(" 's", "'s").replace("?R?", "").replace("?D?", "").replaceAll(" +", " ").replaceAll("'[^s]", "").replaceAll("&", "and").trim();
	}
	
	public static String getDomainName(String url) throws MalformedURLException{
	    if(!url.startsWith("http") && !url.startsWith("https")){
	         url = "http://" + url;
	    }        
	    URL netUrl = new URL(url);
	    String host = netUrl.getHost();
	    if(host.startsWith("www")){
	        host = host.substring("www".length()+1);
	    }
	    return host;
	}
}

