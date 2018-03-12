package graphPlotter;

import java.util.ArrayList;
import java.util.HashMap;

import elasticSearch.SearchResult;
import graphConstruct.Edge;
import graphConstruct.Graph;
import graphConstruct.Vertex;
import trainingGraph.TrainingResponse;

/**
 * 
 * @author Hussain
 *
 */

public class CreateGraph {

	public Graph graph = new Graph();
	public ArrayList<Vertex> claims = new ArrayList<Vertex>();
	public ArrayList<Vertex> sources = new ArrayList<Vertex>();
	public int flag = 0;
    
    public CreateGraph(){
    	//initialize some vertices and add them to the graph
//        for(int i = 0; i < 15; i++) {
//     	   sources.add(new Vertex("s"+i));
//     	   graph.addVertex(sources.get(i), true);
//        }
//        
//        for(int i = 0; i < 5; i++) {
//     	   claims.add(new Vertex("c"+i));
//     	   graph.addVertex(claims.get(i), true);
//        }
        
      //display the initial setup- all claims and sources
//        System.out.println("Sources");
//        for(int i = 0; i < sources.size(); i++)
//            System.out.println(sources.get(i));
            
//        System.out.println("Claims");
//        for(int i = 0; i < claims.size(); i++)
//            System.out.println(claims.get(i));
        
      //initialize source-claim pairs and add them to the graph
//        for(int i = 0; i < claims.size(); i++) {
//     	   for(int j = 0; j < sources.size(); j++) {
//     		   if(j == i || j == i+2 || j == i+4 || j == i+6) {
// 				  edges.add(new Edge(claims.get(i), sources.get(j)));
//     			  graph.addEdge(claims.get(i), sources.get(j)); 
//     		   }
//     	   }
//        }

//        for(int i = 0; i < edges.size(); i++) {
//     	   System.out.println(edges.get(i).toString());
//        }
    }
    
    public CreateGraph(HashMap<String, ArrayList<String>> buildGraph){
		for(ArrayList<String> sourceList : buildGraph.values()) {
			for(String source: sourceList) {
				if(sources.size() != 0) {
					for(int i=0; i < sources.size() ;i++) {
						if(sources.get(i).getLabel().equals(source))
							flag = 1;
					}
					if(flag == 0) {
						sources.add(new Vertex(source));
						graph.addVertex(new Vertex(source), false);
					}
				}
				else
				{
					sources.add(new Vertex(source));
					graph.addVertex(new Vertex(source), false);
				}
				flag = 0;
			}
		}
		
//    	for(int i = 0; i < sources.size(); i++)
//    		graph.addVertex(sources.get(i), false);
    	
		for(String claim: buildGraph.keySet()) {
			claims.add(new Vertex(claim));
			graph.addVertex(new Vertex(claim), false);

//			System.out.println(buildGraph.get(claim));
			for(String source: buildGraph.get(claim)) {
				if(sources.contains(graph.getVertex(source))) {
  					graph.addEdge(graph.getVertex(claim), graph.getVertex(source));
  					graph.getVertex(graph.getVertex(claim).getLabel()).addNeighbor(new Edge(graph.getVertex(claim), graph.getVertex(source)));
  					graph.getVertex(graph.getVertex(source).getLabel()).addNeighbor(new Edge(graph.getVertex(claim), graph.getVertex(source)));
				}
			} 
		   
		}
    	
//		for(int i = 0; i < claims.size(); i++)
//    		graph.addVertex(claims.get(i), false);
    	
//	   for(String claim: buildGraph.keySet()){
//		 for(int i = 0; i < claims.size(); i++) {
//			 if(claim.equals(claims.get(i).getLabel())) {
//				for(String source: buildGraph.get(claim)) {
//					for(int j = 0; j < sources.size(); j++) {
//						if(source.equals(sources.get(j).getLabel())) {
//		  					graph.addEdge(claims.get(i), sources.get(j));
//		  					graph.getVertex(claims.get(i).getLabel()).addNeighbor(new Edge(claims.get(i), sources.get(j)));
//		  					graph.getVertex(sources.get(j).getLabel()).addNeighbor(new Edge(claims.get(i), sources.get(j)));
//						}
//					}
//				} 
//			 }
//		 }
//	   }
	   
//	   for(int i = 0; i < edges.size(); i++) {
//     	   System.out.println(edges.get(i).toString());
//        }
    }
    
    public TrainingResponse addEdge(TrainingResponse response, SearchResult result) {
//    	System.out.println(result.claim);
    	if(!response.claims.contains(new Vertex(result.claim)))
    		response.claims.add(new Vertex(result.claim));
    	if(!response.graph.containsVertex(new Vertex(result.claim)))
    		response.graph.addVertex(new Vertex(result.claim), false);
		
//    	System.out.println(result.sources);
    	for(String source: result.sources) {
			if(response.sources.size() != 0) {
				for(int i=0; i < response.sources.size() ;i++) {
					if(response.sources.get(i).getLabel().equals(source))
						flag = 1;
				}
				if(flag == 0) {
					response.sources.add(new Vertex(source));
					response.graph.addVertex(new Vertex(source), false);
				}
			}
			else
			{
				response.sources.add(new Vertex(source));
				response.graph.addVertex(new Vertex(source), false);
			}
			flag = 0;
		}
		
//    	System.out.println(response.sources);
//    	for(String source: result.sources)
//			response.graph.addVertex(new Vertex(source), false);
    	
		for(String source : result.sources) {
			if(response.sources.contains(response.graph.getVertex(source))) {
				response.graph.addEdge(response.graph.getVertex(result.claim), response.graph.getVertex(source));
				response.graph.getVertex(result.claim).addNeighbor(new Edge(response.graph.getVertex(result.claim), response.graph.getVertex(source)));
				response.graph.getVertex(source).addNeighbor(new Edge(response.graph.getVertex(result.claim), response.graph.getVertex(source)));
//				System.out.println(response.graph.getVertex(source).getNeighborCount());
			}
		}
    	
//    	for(String claim : response.graph.vertexKeys()) {
//    		if(result.claim.equals(claim)) {
//    			for(String src : result.sources) {
//    				for(String source : response.graph.vertexKeys()) {
//    					if(source.equals(src)) {
//    						response.graph.addEdge(response.graph.getVertex(claim), response.graph.getVertex(source));
//    						response.graph.getVertex(claim).addNeighbor(new Edge(response.graph.getVertex(claim), response.graph.getVertex(source)));
//    						response.graph.getVertex(source).addNeighbor(new Edge(response.graph.getVertex(claim), response.graph.getVertex(source)));
////    						System.out.println(response.graph.getVertex(source).getNeighborCount());
//    					}
//    				}
//    			}
//    		}
//    	}
		
    	return response;
    }
    
    public TrainingResponse removeEdge(TrainingResponse response, SearchResult result) {
//    	for(String claim : response.graph.vertexKeys()) {
//    		if(result.claim.equals(claim)) {
//    			for(String src : result.sources) {
//    				for(String source : response.graph.vertexKeys()) {
//    					if(source.equals(src)) {
//    						response.graph.removeEdge(new Edge(response.graph.getVertex(claim), response.graph.getVertex(source)));
//    					}
//    				}
//    			}
//    		}
//    	}
//    	System.out.println(response.graph.getEdges());
    	for(String src : result.sources) {
    		if(response.graph.containsEdge(new Edge(response.graph.getVertex(result.claim), response.graph.getVertex(src)))
    				&& response.graph.containsVertex(new Vertex(src))){
//    			System.out.println(response.graph.getVertex(src).getNeighborCount());
    			response.graph.removeEdge(new Edge(response.graph.getVertex(result.claim), response.graph.getVertex(src)));
    			if(response.graph.getVertex(src).getNeighborCount() == 0) {
    				response.sources.remove(new Vertex(src));
    				response.graph.removeVertex(src);
    			}
    		}
    	}
		
//    	for(String source: result.sources) {
//    		if(response.graph.getVertex(source).getNeighborCount() == 0) {
//    			if(response.sources.size() != 0) {
//    				for(int i=0; i < response.sources.size() ;i++) {
//    					if(response.sources.get(i).getLabel().equals(source))
//    						response.sources.remove(new Vertex(source));
//    				}
//    			}
//    		}
//		}
    	
//    	System.out.println(result.sources);
//    	for(String source: result.sources) {
//    		System.out.println(source);
//    		if(response.graph.containsVertex(new Vertex(source))) {
//    			if(response.graph.getVertex(source).getNeighborCount() == 0)
//    				response.graph.removeVertex(source);
//    		}
//    			
//    	}
    		
    	if(response.graph.getVertex(result.claim).getNeighborCount() == 0) {
    		response.claims.remove(new Vertex(result.claim));
        	response.graph.removeVertex(new Vertex(result.claim).getLabel());
    	}
    	
    	return response;
    }
    
    public ArrayList<Vertex> getClaims() {
    	return this.claims;
    }
    
    public ArrayList<Vertex> getSources() {
    	return this.sources;
    }
    
    public Graph getGraph() {
    	return this.graph;
    }
}
