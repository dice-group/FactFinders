package graphPlotter;

/**
 * This class models an undirected Edge in the Graph implementation.
 * An Edge contains two vertices i.e a claim and a source.
 * A weight is optional for now.

 * @author Hussain
 */
public class Edge implements Comparable<Edge> {

    private Vertex claim, source;
    private int weight;
    
    /**
     * 
     * @param c is claim; The first vertex in the Edge
     * @param s is source; The second vertex in the Edge
     */
    public Edge(Vertex c, Vertex s){
        this(c, s, 1);
    }
    
    /**
     * 
     * @param c The first vertex in the Edge
     * @param s The second vertex of the Edge
     * @param weight The weight of this Edge
     */
    public Edge(Vertex c, Vertex s, int weight){
        this.claim = (c.getLabel().compareTo(s.getLabel()) <= 0) ? c : s;
        this.source = (this.claim == c) ? s : c;
        this.weight = weight;
    }
    
    
    
    /**
     * 
     * @param current
     * @return The neighbor of current along this Edge
     */
    public Vertex getNeighbor(Vertex current){
        if(!(current.equals(claim) || current.equals(source))){
            return null;
        }
        
        return (current.equals(claim)) ? source : claim;
    }
    
    /**
     * 
     * @return Vertex this.claim
     */
    public Vertex getClaim(){
        return this.claim;
    }
    
    /**
     * 
     * @return Vertex this.source
     */
    public Vertex getSource(){
        return this.source;
    }
    
    
    /**
     * 
     * @return int The weight of this Edge
     */
    public int getWeight(){
        return this.weight;
    }
    
    
    /**
     * 
     * @param weight The new weight of this Edge
     */
    public void setWeight(int weight){
        this.weight = weight;
    }
    
    
    /**
     * Note that the compareTo() method deviates from 
     * the specifications in the Comparable interface. A 
     * return value of 0 does not indicate that this.equals(other).
     * The equals() method checks the Vertex endpoints, while the 
     * compareTo() is used to compare Edge weights
     * 
     * @param other The Edge to compare against this
     * @return int this.weight - other.weight
     */
    public int compareTo(Edge other){
        return this.weight - other.weight;
    }
    
    /**
     * 
     * @return String A String representation of this Edge
     */
    public String toString(){
        return "({" + claim + ", " + source + "})";
    }
    
    /**
     * 
     * @return int The hash code for this Edge 
     */
    public int hashCode(){
        return (claim.getLabel() + source.getLabel()).hashCode(); 
    }
    
    /**
     * 
     * @param other The Object to compare against this
     * @return ture iff other is an Edge with the same Vertices as this
     */
    public boolean equals(Object other){
        if(!(other instanceof Edge)){
            return false;
        }
        
        Edge e = (Edge)other;
        
        return e.claim.equals(this.claim) && e.source.equals(this.source);
    }   
}

