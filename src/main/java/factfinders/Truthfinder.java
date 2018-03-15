package factfinders;

import java.util.Set;

import graphConstruct.Edge;
import graphConstruct.Graph;


/**
 * Originally by Yin et al., 2008
 * Calculates the probability of a claim by assuming that each sourse's
 * trust score is the probability of being correct and then average the claim's score
 * @author Hussain
 *
 */
public class Truthfinder implements Scores {
	
	private double truthScore;
	private double probScore;
	private int claimSet;
	private double maxScore;
	private Normalize normalize;

	public Truthfinder(double truthScore, double probScore, int claimSet, double maxScore) {
		this.truthScore = truthScore;
		this.probScore = probScore;
		this.claimSet = claimSet;
		this.maxScore = maxScore;
		this.normalize = new Normalize();
	}
	
	public Truthfinder() {
		this.truthScore = 0;
		this.probScore = 1.0;
		this.claimSet = 0;
		this.maxScore = 0;
		this.normalize = new Normalize();
	}

	/**
	 * It takes the probability of a claim by assuming that each source's trust score is 
	 * the probability of it being correct
	 */
	@Override
	public void beliefScore(Graph graph, Set<String> claims) {
		for(String claim : claims) {
				for(Edge e : graph.getVertex(claim).getNeighbors()) {
					if(e.getClaim().getLabel() != claim)
						probScore *= (1-e.getClaim().getScore());
				}
				graph.getVertex(claim).setScore(1-probScore);
				probScore = 1.0;
		}
		maxScore = normalize.maxScoreFinder(graph, claims); 
		normalize.avoidOverflow(graph, maxScore, claims);
	}

	/**
	 * Average of sum of Belief scores of the all the claims being committed by this source 
	 */
	@Override
	public void trustScore(Graph graph, Set<String> sources) {
		for(String source : sources) {
			if(graph.containsVertex(source)) {
				for(Edge e : graph.getVertex(source).getNeighbors()) {
					truthScore += e.getSource().getScore();
				}
				claimSet = graph.getVertex(source).getNeighbors().size();
				truthScore = (truthScore / claimSet);
				graph.getVertex(source).setScore(truthScore);
				truthScore = 0;
			}
		}
		maxScore = normalize.maxScoreFinder(graph, sources); 
		normalize.avoidOverflow(graph, maxScore, sources);
	}
}
