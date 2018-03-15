package graphPlotter;

/**
 * Simple Vertex construct, if graph is plotted with JGraphT library
 * Vertices are needed to be defined
 * Each claim and source consist of a Label and a Score
 * @author Hussain
 *
 */
public class Vertex {
	
	public String claim;
	public double score;
	
	public Vertex(String claim, double score) {
		this.claim = claim;
		this.score = score;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	} 
	
	public String getClaim() {
		return claim;
	}

	public void setClaim(String claim) {
		this.claim = claim;
	}
}
