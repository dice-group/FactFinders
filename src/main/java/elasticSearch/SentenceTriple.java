package elasticSearch;

import java.util.ArrayList;
import java.util.List;
import edu.stanford.nlp.trees.Tree;

public class SentenceTriple {

	private List<String> subject;
	private String predicate;
	private List<String> object;
	private Tree sentenceParseTree;
	
	private List<String> subjectModifier = new ArrayList<String>();
	private List<String> objectModifier = new ArrayList<String>();
	
	
	
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
	public List<String> getSubjectModifier() {
		return subjectModifier;
	}
	public void setSubjectModifier(List<String> subjectModifier) {
		this.subjectModifier = subjectModifier;
	}
	public List<String> getObjectModifier() {
		return objectModifier;
	}
	public void setObjectModifier(List<String> objectModifier) {
		this.objectModifier = objectModifier;
	}
	@Override
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append("Sub:").append(subject).append(" , Pred:").append(predicate).append(" , Obj:").append(object);
		return res.toString();
	}
	
}
