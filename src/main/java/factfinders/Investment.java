package factfinders;

import java.util.ArrayList;

import graphConstruct.Edge;
import graphConstruct.Graph;
import graphConstruct.Vertex;

/**
 * (Originally by Pasternack) Sources "invest" their trust uniformly in claims.
 * Belief in each claim grows according to a non linear function G where G=x^g,
 * where g=1.2.
 *
 * @author Hussain
 */
public class Investment implements Scores {

	// the investment of each source in its neighbors
	private double invScore;
	// set of sources as neighbors
	private int claimSet;
	// the investment of the other sources in every neighbor of the source
	private double furtherInvestment;
	// maximum score to normalize the scores
	private double maxScore;

	public Investment() {
		invScore = 0;
		claimSet = 0;
		furtherInvestment = 0;
		maxScore = 0;
	}

	@Override
	public void beliefScore(Graph graph, ArrayList<Vertex> claims) {
		for (final Vertex claim : claims) {
			if (graph.containsVertex(claim)) {
				// System.out.println(graph.getVertex(sources.get(i).getLabel()).getNeighbors().toString());
				for (final Edge e : graph.getVertex(claim.getLabel()).getNeighbors()) {
					// System.out.println(e.getNeighbor(sources.get(i)).toString());
					// System.out.println(sources.get(i).getLabel() +
					// graph.getVertex(sources.get(i).getLabel()).getScore());
					claimSet = graph.getVertex(e.getNeighbor(claim).getLabel()).getNeighborCount();
					// invScore = graph.getVertex(claims.get(i).getLabel()).getScore() +
					// (e.getNeighbor(claims.get(i)).getScore()/(double)claimSet);
					invScore += (e.getNeighbor(claim).getScore() / claimSet);
				}
				invScore = Math.pow(invScore, 1.2);
				graph.getVertex(claim.getLabel()).setScore(invScore);
				invScore = 0;
				// System.out.println(sources.get(i).getLabel() +
				// graph.getVertex(sources.get(i).getLabel()).getScore());
			}
		}
		maxScore = maxScoreFinder(graph, claims);
		normalize(graph, maxScore, claims);
	}

	@Override
	public void trustScore(Graph graph, ArrayList<Vertex> sources) {
		for (final Vertex source : sources) {
			if (graph.containsVertex(source)) {
				if (graph.getVertex(source.getLabel()).getScore() == 0.0) {
					for (final Edge e : graph.getVertex(source.getLabel()).getNeighbors()) {
						if (!(e == null) && graph.getVertex(source.getLabel()).getScore() == 0.0) {
							graph.getVertex(source.getLabel()).setScore(1.0);
							// System.out.println(sources.get(i).getLabel() +"
							// "+graph.getVertex(sources.get(i).getLabel()).getScore());
						}
					}
				} else {
					for (final Edge e : graph.getVertex(source.getLabel()).getNeighbors()) {
						// System.out.println(graph.getVertex(sources.get(i).getLabel()).getNeighbors());
						for (final Edge ed : graph.getVertex(e.getNeighbor(source).getLabel()).getNeighbors()) {
							// System.out.println(graph.getVertex(e.getNeighbor(sources.get(i)).getLabel()).getNeighbors());
							claimSet = graph.getVertex(ed.getNeighbor(e.getNeighbor(source)).getLabel())
									.getNeighborCount();
							// System.out.println(ed.getNeighbor(e.getNeighbor(sources.get(i))).getLabel() +
							// ":" +claimSet);
							// System.out.println(investment);
							furtherInvestment += graph.getVertex(ed.getNeighbor(e.getNeighbor(source)).getLabel())
									.getScore() / claimSet;
							// System.out.println(ed.getNeighbor(e.getNeighbor(sources.get(i))).getLabel() +
							// " : " +investment);
						}
						claimSet = graph.getVertex(source.getLabel()).getNeighborCount();
						// System.out.println(sources.get(i).getLabel()+ ">" +claimSet);
						invScore = graph.getVertex(source.getLabel()).getScore()
								/ (claimSet * furtherInvestment);
						// System.out.println(graph.getVertex(sources.get(i).getLabel()).getScore());
						invScore += e.getNeighbor(source).getScore() * invScore;
						// System.out.println(graph.getVertex(sources.get(i).getLabel()) + " : " +
						// invScore);
					}
					// System.out.println("The new trust score for " +sources.get(i)+ ":"+
					// invScore);
					graph.getVertex(source.getLabel()).setScore(invScore);
					furtherInvestment = 0;
				}
			}
		}
		maxScore = maxScoreFinder(graph, sources);
		normalize(graph, maxScore, sources);
	}

	private double maxScoreFinder(Graph graph, ArrayList<Vertex> vertices) {
		double max = 0;
		for (final Vertex v : vertices) {
			if (graph.containsVertex(v)) {
				if (graph.getVertex(v.getLabel()).getScore() > max) {
					max = graph.getVertex(v.getLabel()).getScore();
				}
			}
		}
		return max;
	}

	private void normalize(Graph graph, double max, ArrayList<Vertex> vertices) {
		for (final Vertex v : vertices) {
			if (graph.containsVertex(v)) {
				graph.getVertex(v.getLabel()).setScore(graph.getVertex(v.getLabel()).getScore() / max);
			}
		}
	}
}
