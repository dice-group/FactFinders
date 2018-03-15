package factfinders;

import java.util.Set;

import graphConstruct.Edge;
import graphConstruct.Graph;

/**
 * (Originally by Pasternack) Sources "invest" their trust uniformly in claims.
 * Belief in each claim grows according to a non linear function G where G=x^g,
 * where g=1.2.
 *
 * @author Hussain
 */
public class Investment implements Scores {

	private double invScore;
	private int claimSet;
	private double furtherInvestment;
	private double maxScore;
	private Normalize normalize;

	public Investment(double invScore, int claimSet, double furtherInvestment, double maxScore) {
		this.invScore = invScore;
		this.claimSet = claimSet;
		this.furtherInvestment = furtherInvestment;
		this.maxScore = maxScore;
		this.normalize = new Normalize();
	}

	public Investment() {
		this.invScore = 0;
		this.claimSet = 0;
		this.furtherInvestment = 0;
		this.maxScore = 0;
		this.normalize = new Normalize();
	}

	/**
	 * Belief scores are calculated by taking the ratio of the score of the source investing in a particular claim
	 * with all the claims it invests in. The score of every claim is raised to a fixed power of "1.2" to 
	 * accomplish non-linearity of belief scores
	 */
	@Override
	public void beliefScore(Graph graph, Set<String> claims) {
		for (String claim : claims) {
			for (Edge e : graph.getVertex(claim).getNeighbors()) {
				if(e.getClaim().getLabel() != claim) {
					claimSet = graph.getVertex(e.getClaim().getLabel()).getNeighborCount();
					invScore += (e.getClaim().getScore() / claimSet);
				}
			}
			invScore = Math.pow(invScore, 1.2);
			graph.getVertex(claim).setScore(invScore);
			invScore = 0;
		}
		maxScore = normalize.maxScoreFinder(graph, claims);
		normalize.avoidOverflow(graph, maxScore, claims);
	}

	/**
	 * Trust scores are calculated with three folds:
	 * 1. Source takes it initial trust score and accumulates it with all the claims which got investment by that source
	 * 2. It distributes its score with the scores of OTHER sources which have invested in its claims
	 * 3. Further it gets the similar distribution for those claims being invested by the OTHER sources
	 */
	@Override
	public void trustScore(Graph graph, Set<String> sources) {
		for (String source : sources) {
			for (Edge e : graph.getVertex(source).getNeighbors()) {
				for (Edge ed : graph.getVertex(e.getSource().getLabel()).getNeighbors()) {
					claimSet = graph.getVertex(ed.getClaim().getLabel()).getNeighborCount();
					furtherInvestment += graph.getVertex(ed.getClaim().getLabel())
							.getScore() / claimSet;
				}
				claimSet = graph.getVertex(source).getNeighborCount();
				invScore = graph.getVertex(source).getScore()
						/ (claimSet * furtherInvestment);
				invScore += e.getSource().getScore() * invScore;
			}
			graph.getVertex(source).setScore(invScore);
			furtherInvestment = 0;
		}
		maxScore = normalize.maxScoreFinder(graph, sources);
		normalize.avoidOverflow(graph, maxScore, sources);
	}
	
	/**
	 * Initialize the initial trust scores for sources, It is necessary for first iteration of Investment algorithm.
	 * @param graph
	 * @param sources
	 * @return
	 */
	public Graph initializeTrustScore(Graph graph, Set<String> sources) {
		for(String s : sources) {
			graph.getVertex(s).setScore(1.0);  
		}
		return graph;
	}
}
