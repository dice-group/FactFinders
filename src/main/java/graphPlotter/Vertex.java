package graphPlotter;

import java.util.ArrayList;

/**
 * This class models a vertex in a graph. 
 * The label of a vertex represents a claim or a source.
 * The edgelink represent the edges incident to a vertex
 * @author Hussain
 */
public class Vertex {

    private ArrayList<Edge> edgeLink;
    private String label;
    private double score;
    
    /**
     * 
     * @param label The unique label associated with this Vertex
     */
    public Vertex(String label){
        this.label = label;
        this.edgeLink = new ArrayList<Edge>();
        this.setScore(0);
    }
    
    
    /**
     * This method adds an Edge to the incidence neighborhood of this graph iff
     * the edge is not already present. 
     * 
     * @param edge The edge to add
     */
    public void addNeighbor(Edge edge){
        if(this.edgeLink.contains(edge)){
            return;
        }
//        System.out.println(this.edgeLink);
        this.edgeLink.add(edge);
//        System.out.println(this.edgeLink);
    }
    
    
    /**
     * 
     * @param other The edge for which to search
     * @return true iff other is contained in this.neighborhood
     */
    public boolean containsNeighbor(Edge other){
        return this.edgeLink.contains(other);
    }
    
    /**
     * 
     * @param index The index of the Edge to retrieve
     * @return Edge The Edge at the specified index in this.neighborhood
     */
    public Edge getNeighbor(int index){
        return this.edgeLink.get(index);
    }
    
    
    /**
     * 
     * @param index The index of the edge to remove from this.neighborhood
     * @return Edge The removed Edge
     */
    Edge removeNeighbor(int index){
        return this.edgeLink.remove(index);
    }
    
    /**
     * 
     * @param e The Edge to remove from this.neighborhood
     */
    public void removeNeighbor(Edge e){
        this.edgeLink.remove(e);
    }
    
    
    /**
     * 
     * @return int The number of neighbors of this Vertex
     */
    public int getNeighborCount(){
    	System.out.println(this.edgeLink);
        return this.edgeLink.size();
    }
    
    
    /**
     * 
     * @return String The label of this Vertex
     */
    public String getLabel(){
        return this.label;
    }
    
    
    /**
     * 
     * @return String A String representation of this Vertex
     */
    public String toString(){
        return label;
    }
    
    /**
     * 
     * @return The hash code of this Vertex's label
     */
    public int hashCode(){
        return this.label.hashCode();
    }
    
    /**
     * 
     * @param other The object to compare
     * @return true iff other instanceof Vertex and the two Vertex objects have the same label
     */
    public boolean equals(Object other){
        if(!(other instanceof Vertex)){
            return false;
        }
        
        Vertex v = (Vertex)other;
        return this.label.equals(v.label);
    }
    
    /**
     * 
     * @return ArrayList<Edge> A copy of this.neighborhood. Modifying the returned
     * ArrayList will not affect the neighborhood of this Vertex
     */
    public ArrayList<Edge> getNeighbors(){
        return new ArrayList<Edge>(this.edgeLink);
    }


	public double getScore() {
		return score;
	}


	public void setScore(double d) {
		this.score = d;
	}
    
}

