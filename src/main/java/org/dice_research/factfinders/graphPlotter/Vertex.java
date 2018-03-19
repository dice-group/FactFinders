package org.dice_research.factfinders.graphPlotter;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Simple Vertex construct, if graph is plotted with JGraphT library
 * Vertices are needed to be defined
 * Each claim and source consist of a Label and a Score
 * @author Hussain
 *
 */
public class Vertex {
	
	public String label;
	public double score;
	public char type;
	
	public Vertex(String claim, double score, char type) {
		this.label = claim;
		this.score = score;
		this.type = type;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	} 
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}
	
	@Override
	public int hashCode() {
		
		return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
	            append(label).
	            append(type).
	            toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		
		boolean result = false;
		if (this == other) 
			return true;
		if (other == null || other.getClass() != getClass())
			result = false; 
		if (other instanceof Vertex) {
			Vertex otherPoint = (Vertex) other;
			if (this.label.equals(otherPoint.getLabel()) && this.type == otherPoint.type) {
				result = true;
			}
		}
		return result;
	}

}
