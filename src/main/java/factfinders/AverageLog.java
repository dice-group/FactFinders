package factfinders;

import java.util.Set;

import graphConstruct.Edge;
import graphConstruct.Graph;

/**
 * (Originally by Pasternack) Computing trust score as an average of belief in its claims
 * It over-estimates the trustworthiness of a source with relatively few claims
 * @author Hussain
 */
public class AverageLog implements Scores {

	private double maxScore;
	private double avgScore;
	private int claimSet;
	private double beliefScore;
	private Normalize normalize;

	public AverageLog(double maxScore, double avgScore, int claimSet, double beliefScore) {
		this.maxScore = maxScore;
		this.avgScore = avgScore;
		this.claimSet = claimSet;
		this.beliefScore = beliefScore;
		this.normalize = new Normalize();
	}

	public AverageLog() {
		this.maxScore = 0;
		this.avgScore = 0;
		this.claimSet = 0;
		this.beliefScore = 0;
		this.normalize = new Normalize();
	}

	/**
	 * Belief scores are computed by adding the Trust Scores of the sources commiting the claim
	 */
	@Override
	public void beliefScore(Graph graph, Set<String> claims) {
		for(String claim : claims) {
			for(Edge e : graph.getVertex(claim).getNeighbors()) {
				if(e.getClaim().getLabel() != claim)
					beliefScore +=  e.getClaim().getScore();
			}
			graph.getVertex(claim).setScore(beliefScore);
			beliefScore = 0;
		}
		maxScore = normalize.maxScoreFinder(graph, claims); 
		normalize.avoidOverflow(graph, maxScore, claims);
	}

	/**
	 * Trust scores for each score is calculated by adding the neighboring claims
	 * This sum is averaged by the number of neighbors and enhanced by the Log-10 of neighbors count   
	 */
	@Override
	public void trustScore(Graph graph, Set<String> sources) {
		for(String source : sources) {
			for(Edge e : graph.getVertex(source).getNeighbors()) {
				avgScore += e.getSource().getScore();
			}
			claimSet = graph.getVertex(source).getNeighborCount();
			avgScore = (avgScore / claimSet)*Math.log10(claimSet);
			graph.getVertex(source).setScore(avgScore);
			avgScore = 0;
		}
	}
}
