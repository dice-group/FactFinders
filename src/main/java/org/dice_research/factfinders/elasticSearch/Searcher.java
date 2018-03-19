package org.dice_research.factfinders.elasticSearch;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

/**
 * This abstract class provides the default functionality for the search query methods
 * such as the rest client obj and consumer factory.
 * @author Hussain
 *
 */
public abstract class Searcher implements Search{

	public static String ELASTIC_SERVER = "131.234.29.16";
	public static String ELASTIC_PORT = "6060";
	public static String W_W_W = "www";
	public static RestClient restClientobj;
	public static Response response;
	public static HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory consumerFactory;

	public Searcher(){
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

	/**
	 * It normalize the property or predicate label by removing punctuation and useless information
	 * @param propertyLabel
	 * @return
	 */
	public String normalizePredicate(String propertyLabel) {
		return propertyLabel.replaceAll(",", "").replace("`", "").replace(" 's", "'s").replace("?R?", "").replace("?D?", "").replaceAll(" +", " ").replaceAll("'[^s]", "").replaceAll("&", "and").trim();
	}

	/**
	 * This method performs the basic operation of URL extraction
	 * So that all the pages redirecting to a single source can be considered as one source.
	 * It helps to enrich the Graph minimizing the number of extra vertices and maximizing the edges  
	 * @param url
	 * @return base url
	 * @throws MalformedURLException
	 */
	public static String getDomainName(String url) throws MalformedURLException{
		if(!url.startsWith("http") && !url.startsWith("https")){
			url = "http://" + url;
		}        
		URL netUrl = new URL(url);
		String host = netUrl.getHost();
		if(host.startsWith(W_W_W)){
			host = host.substring(W_W_W.length()+1);
		}
		return host;
	}

}
