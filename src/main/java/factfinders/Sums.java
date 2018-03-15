package factfinders;

import java.util.Set;

import graphConstruct.Edge;
import graphConstruct.Graph;

/**
 * Hubs and Authorities (Kleinberg, 1999) adapted for claims and sources, 
 * where sources refers as hubs and claims refers to authorities.
 * Normalization: beliefScore(i)/max beleifScore(All Claims)
 * 				  trustScore(i)/max trustScore(All Sources)
 * @author Hussain
 */
public class Sums implements Scores {

	private double maxScore;
	private double score;
	private Normalize normalize;
	
	public Sums(double maxScore, double score) {
		this.maxScore = maxScore;
		this.score = score;
		this.normalize = new Normalize();
	}
	
	public Sums() {
		this.maxScore = 0;
		this.score = 0;
		this.normalize = new Normalize();
	}
	
	/**
	 * Belief scores is the accumulation of the trust scores of its neighbors
	 */
	@Override
	public void beliefScore(Graph graph, Set<String> claims) {
		for(String claim : claims) {
			for(Edge e : graph.getVertex(claim).getNeighbors()) {
				if(e.getClaim().getLabel() != claim)
					score +=  e.getClaim().getScore();
			}
			graph.getVertex(claim).setScore(score);
			score = 0;
		}
		maxScore = normalize.maxScoreFinder(graph, claims); 
		normalize.avoidOverflow(graph, maxScore, claims);
	}

	/**
	 * Trust scores is the accumulation of the belief scores of its neighbors
	 */
	@Override
	public void trustScore(Graph graph, Set<String> sources) {
		for(String source : sources) {
			for(Edge e : graph.getVertex(source).getNeighbors()) {
				score += e.getSource().getScore();
			}
			graph.getVertex(source).setScore(score);
			score = 0;
		}
		maxScore = normalize.maxScoreFinder(graph, sources); 
		normalize.avoidOverflow(graph, maxScore, sources);
	}
}
