package demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import elasticSearch.RDFTriplizer;
import elasticSearch.SearchBOAPatterns;
import elasticSearch.SearchResult;
import elasticSearch.SearchWordnetPatterns;
import factFinders.AverageLog;
import factFinders.InitializeBeliefs;
import factFinders.Investment;
import factFinders.PooledInvestment;
import factFinders.Sums;
import factFinders.Truthfinder;
import graphPlotter.Graph;
import graphPlotter.Vertex;

/**
*
* @author Hussain
*/
public class DemoGraph {
   
   public static void main(String[] args) throws IOException{
       
////	   FileWriter writer = new FileWriter("./src/data/GraphPlotter.txt");
//       String factfinder = "Sums";
//       InitializeBeliefs beliefs = new InitializeBeliefs();
//       Sums sums = new Sums();
//       AverageLog avgLog = new AverageLog();
//       Investment inv = new Investment();
//       PooledInvestment pool = new PooledInvestment();
//       Truthfinder tf = new Truthfinder();
//       RDFTriplizer triplify = new RDFTriplizer();
////       SearchWordnetPatterns search = new SearchWordnetPatterns();
       SearchBOAPatterns search = new SearchBOAPatterns();
//       String queryClaim = new String();
       SearchResult result = new SearchResult();
//       
//       Graph graph = new Graph();
//       ArrayList<Vertex> claims = new ArrayList<Vertex>();
//       ArrayList<Vertex> sources = new ArrayList<Vertex>();
//       ArrayList<String> pageTitles = new ArrayList<String>();
//       HashMap<String, ArrayList<String>> graphBuilder = new HashMap<String, ArrayList<String>>();
//       //CreateGraph create = new CreateGraph();
       
//       String inputFile = "/home/datascienceadmin/eclipse-workspace/FactFinders/src/data/train.ttl";
//       String outputFile = triplify.triplize(inputFile);
       
       result = search.query("Katy Perry	spouse	Russell Brand");
//       result = search.query("The Right Stuff (film)	starring	Harry Shearer");
       
//       BufferedReader TSVFile;
//       TSVFile = new BufferedReader(new FileReader(outputFile));
//       String dataRow = TSVFile.readLine();
//       
//       try {
//	        while (dataRow != null){
//		       	result = search.query(dataRow);
////	        	result = search.query("Nokia	foundationPlace	Nokia");
//		       	if(result != null) {
//		       	 graphBuilder.put(result.claim, result.sources);
//		       		writer.append(result.claim);
//					writer.append('\n');
//					for(String source : result.sources) {
//						writer.append(source);
//					}
//					writer.append('\n');
//					writer.append("-------------------------------------------New-------------------------------");
//					writer.append('\n');
//		       	}
//		       	dataRow = TSVFile.readLine(); 
//		    }
//		        TSVFile.close();
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
//       
//       
////       for(int i = 0; i<5; i++) {
////    	   for(int j = 0; j<3; j++) {
////    		   pageTitles.add("s"+j);
////    	   }
////    	   graphBuilder.put("c"+i, pageTitles);
////    	   pageTitles = new ArrayList<String>();
////       }
//       
//       writer.flush();
//       writer.close();
//       CreateGraph create = new CreateGraph(graphBuilder);
////       
////       
//       graph = create.getGraph();
//       claims = create.getClaims();
//       sources = create.getSources();
//       
//       /**
//        * Take graph, initialize the claim vertices
//        * calculate score for every edge by selecting
//        * a factfinder algorithm
//        */
//        
//       /*
//       graph = beliefs.initialize(graph, claims, factfinder);
//       for(int i = 0; i < 19; i++) {
//    	   sums.trustScore(graph, sources);
//           sums.beliefScore(graph, claims);
//       }  
//       
//       System.out.println("Finalized SUM trust scores are");
//
////       for(Vertex v : graph.vertices()) {
////    	   if(sources.contains(v))
////    		  System.out.println(v.getLabel() +"="+ v.getScore());
////       }
//       System.out.println(maxScore(graph.vertices(), claims).getLabel());
//       
//       
//       factfinder = "Avg";
//       graph = beliefs.initialize(graph, claims, factfinder);
//       for(int i = 0; i < 19; i++) {
//    	   avgLog.trustScore(graph, sources);
//           avgLog.beliefScore(graph, claims);
//       }  
//       
//       System.out.println("Finalized AVG trust scores are");
//
////       for(Vertex v : graph.vertices()) {
////    	   if(sources.contains(v))
////    		   System.out.println(v.getLabel() +"="+ v.getScore());
////       }
//       System.out.println(maxScore(graph.vertices(), claims).getLabel());
//       */
////       factfinder = "Inv";
////       graph = beliefs.initialize(graph, claims, sources);
////       for(int i = 0; i < 19; i++) {
////    	   inv.trustScore(graph, sources);
////           inv.beliefScore(graph, claims);
////       }  
////       
////       System.out.println("Finalized Investment trust scores are");
//
////       for(Vertex v : graph.vertices()) {
////    	   if(sources.contains(v))
////    		  System.out.println(v.getLabel() +"="+ v.getScore());
////       }
////       
////       System.out.println(maxScore(graph.vertices(), claims).getLabel());
//       
//       /*
//       factfinder = "Pool";
//       graph = beliefs.initialize(graph, claims, factfinder);
//       for(int i = 0; i < 19; i++) {
//    	   pool.trustScore(graph, sources);
//           pool.beliefScore(graph, claims);
//       }  
//       
//       System.out.println("Finalized Pooled Investment trust scores are");
//
//       for(Vertex v : graph.vertices()) {
//    	   if(sources.contains(v))
//    		  System.out.println(v.getLabel() +"="+ v.getScore());
//       }
//       System.out.println(maxScore(graph.vertices(), sources).getLabel());
//       
//       
//       factfinder = "Tf";
//       graph = beliefs.initialize(graph, claims, factfinder);
//       for(int i = 0; i < 19; i++) {
//    	   tf.trustScore(graph, sources);
//           tf.beliefScore(graph, claims);
//       }  
//       
//       System.out.println("Finalized TruthFinder trust scores are");
//
//       for(Vertex v : graph.vertices()) {
//    	   if(sources.contains(v))
//    		  System.out.println(v.getLabel() +"="+ v.getScore());
//       }
//       System.out.println(maxScore(graph.vertices(), sources).getLabel());
//       */
   }
   
   private static Vertex maxScore(Iterable<Vertex> iterable, ArrayList<Vertex> sources) {
  		Vertex max = new Vertex("default");
  		max.setScore(0);
  		for(Vertex v : iterable) {
  			if(sources.contains(v)) {
  				if(v.getScore() > max.getScore() && v.getScore()!= Double.POSITIVE_INFINITY)
  					max = v;
  			 	}
  			}
  		return max;	
  	}
}


