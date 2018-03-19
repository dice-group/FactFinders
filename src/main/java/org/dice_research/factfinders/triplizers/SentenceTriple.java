package org.dice_research.factfinders.triplizers;

import java.util.List;
import edu.stanford.nlp.trees.Tree;

/**
 * Sentence triple is a construct containing the basic constituents
 * of a sentence. SentenceTriplizer generate these constituents by using 
 * the sentence tree annotations by Standford NLP.
 * @author Hussain
 *
 */
public class SentenceTriple {

	private List<String> subject;
	private String predicate;
	private List<String> object;
	private Tree sentenceParseTree;



	public List<String> getSubject() {
		return subject;
	}
	public void setSubject(List<String> subject) {
		this.subject = subject;
	}
	public String getPredicate() {
		return predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	public List<String> getObject() {
		return object;
	}
	public void setObject(List<String> object) {
		this.object = object;
	}



	public Tree getSentenceParseTree() {
		return sentenceParseTree;
	}
	public void setSentenceParseTree(Tree sentenceParseTree) {
		this.sentenceParseTree = sentenceParseTree;
	}

	@Override
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append("Sub:").append(subject).append(" , Pred:").append(predicate).append(" , Obj:").append(object);
		return res.toString();
	}

}
