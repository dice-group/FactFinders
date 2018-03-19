package org.dice_research.factfinders.triplizers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.dice_research.factfinders.triplizers.SentenceTriple;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * Extract a triple of subject, predicate and object from a given sentence
 * @author Hussain
 */

public class SentenceTriplizer {
	
	public Tree currentPredicate = null;
	public Tree currentSubject = null;
	public Tree currentAdj = null;
	public List<Tree> siblings = new ArrayList<Tree>();
	List<String> objList = new ArrayList<String>();
	List<String> subList = new ArrayList<String>();
	boolean predFlag = false;
	boolean subFlag = false;
	boolean objFlag = false;

	private Set<String> nounTagSet;
	private Set<String> verbTagSet;
	private Set<String> adjTagSet;
	private Set<String> nounModifiersSet;

	private StanfordCoreNLP pipeline ; 
	private boolean PRINT_TREE = false;
	SentenceTriple triple = new SentenceTriple();


	public void init(){
		nounTagSet = new HashSet<String>();
		nounTagSet.add("NN");
		nounTagSet.add("NNP");
		nounTagSet.add("NNPS");
		nounTagSet.add("NNS");
		nounTagSet.add("PRP");
		nounTagSet.add("POS");
		nounTagSet.add("CD");
		nounTagSet.add("JJ");
		nounTagSet.add("PP");
		nounTagSet.add("SBAR");

		verbTagSet = new HashSet<String>();
		verbTagSet.add("VB");
		verbTagSet.add("VBD");
		verbTagSet.add("VBG");
		verbTagSet.add("VBN");
		verbTagSet.add("VBP");
		verbTagSet.add("VBZ");

		adjTagSet = new HashSet<String>();
		adjTagSet.add("JJ");
		adjTagSet.add("JJR");
		adjTagSet.add("JJS");

		nounModifiersSet = new HashSet<String>();
		nounModifiersSet.add("DT");
		nounModifiersSet.add("PRP$");
		nounModifiersSet.add("PRP");
		nounModifiersSet.add("POS");
		nounModifiersSet.add("JJ");
		nounModifiersSet.add("CD");
		nounModifiersSet.add("ADJP");
		nounModifiersSet.add("RB");
		nounModifiersSet.add("QP");
		nounModifiersSet.addAll(nounTagSet);
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, parse");
		pipeline= new StanfordCoreNLP(props, false);

	}

	public Map<Integer,SentenceTriple> extractTriples(String text) {

		int sentenceId = 0;

		Map<Integer,SentenceTriple> tripleList = new HashMap<Integer,SentenceTriple>();

		Annotation document = pipeline.process(text);


		for (CoreMap sentence : document.get(SentencesAnnotation.class)) {

			SentenceTriple triple = extractTriplesForOneSentence(sentence);
			System.out.println(triple.getSubject());
			System.out.println(triple.getPredicate());
			System.out.println(triple.getObject());
			tripleList.put(sentenceId, triple);
			sentenceId++;
		}


		return tripleList;
	}


	public SentenceTriple extractTriplesForOneSentence(CoreMap sentence) {

		Tree tree = sentence.get(TreeAnnotation.class);
		System.out.println(tree);
		System.out.println(tree.depth());
		System.out.println(tree.firstChild());
		if(PRINT_TREE){
			tree.pennPrint();
		}

		Tree root = tree.firstChild();
		Tree[] root1 = tree.children();
		for(Tree t : root1) {
			System.out.println(t.label().value());
			for(Tree t1 : t.children())
				System.out.println(t1.label().value());
		}

		if(root.label().value().equals("NP")){ //e.g. female name, null(female name, null)
			//e.g. name of person null(person, name)

			triple = extractTripleFromNPVP(root.firstChild().children());

			if(triple.getPredicate()==null){

				triple =  extractTriplesFromNPSBAR(root.firstChild().children());


			}
			if(triple.getPredicate() == null){
				triple = extractSubObjNoPred(tree, root);
			}


		}
		boolean containsSNP = false;
		boolean containsSVP = false;

		if(root.label().value().equals("S")||root.label().value().equals("SINV")||root.label().value().equals("UCP")  ){
			for(Tree child : root.children()) {
				if(child.label().value().equals("FRAG")) {
					extractFrag(child, root);
					subFlag = true;
				}
				if(child.label().value().equals("NP")){
					containsSNP = true;
				}
				if(child.label().value().equals("VP")){
					containsSVP = true;
				}
			}
			if(containsSNP && containsSVP && subFlag == false){

				triple = extractTripleFromNPVP(root.children());
				containsSNP = false;
				containsSVP = false;

			}
			if(containsSNP && containsSVP && subFlag == true){
				for(Tree child : root.children())
					if(child.label().value().equals("VP")) {
						Tree pred = extractPredicateRest(child);

						if(pred!=null){

							String predStr = prettyPrint(pred);

							triple.setPredicate(predStr);
							predFlag = true;
						}
					}
				containsSVP = false;

			}
			else if(containsSNP && !containsSVP){
				//Get the NP child

				for(Tree child : root.children()) {

					if(child.label().value().equals("NP")){

						triple = extractTripleFromNPVP(child.children());

						if(triple.getPredicate()==null && predFlag == false){
							triple = extractSubObjNoPred(tree, child);
						}
						break;
					}
				}
			}
		}

		else if(root.label().value().equals("FRAG")){
			boolean containsNP= false;
			boolean containsSBAR= false;
			boolean containsPP= false;
			boolean containsFVP = false;

			for(Tree child : root.children()) {
				if(child.label().value().equals("NP")){
					containsNP = true;
				}
				if(child.label().value().equals("SBAR")){
					containsSBAR = true;
				}

				if(child.label().value().equals("PP")){
					containsPP = true;
				}
				if(child.label().value().equals("VP")){
					containsFVP = true;
				}
			}
			if(containsNP && containsFVP){  //e.g. Jack who killed Mary killed(Jack, Mary)
				triple =  extractTripleFromNPVP(root.children());
			}
			if(containsNP && containsSBAR){  //e.g. Jack who killed Mary killed(Jack, Mary)
				triple =  extractTriplesFromNPSBAR(root.children());
			}
			else if(containsNP && containsPP){//e.g. Company where Jack works  works(Jack,Company)
				triple =  extractTriplesFromFRAGNPPP(root.children());
			}
			else if(containsNP && !containsPP && !containsSBAR && !containsFVP){//e.g. Company where Jack works  works(Jack,Company)

				//Check if the NP in turn contains SBAR and NP
				for(Tree child : root.firstChild().children()) {

					if(child.label().value().equals("NP")){
						containsNP = true;
					}
					if(child.label().value().equals("SBAR")){
						containsSBAR = true;
					}

					if(child.label().value().equals("PP")){
						containsPP = true;

					}
				}
				if(containsNP && containsSBAR){  //e.g. Jack who killed Mary killed(Jack, Mary)
					triple =  extractTriplesFromNPSBAR(root.firstChild().children());
				}
				else if(containsNP && containsPP){//e.g. Company where Jack works  works(Jack,Company)
					triple =  extractTriplesFromFRAGNPPP(root.firstChild().children());
				}

				else if(containsNP && !containsPP && !containsSBAR){

					triple = extractTripleFromNPVP(root.firstChild().children());

					if(triple.getPredicate()==null){

						triple =  extractTriplesFromNPSBAR(root.firstChild().children());

					}
					if(triple.getPredicate() == null){
						triple = extractSubObjNoPred(tree, root);
					}


				}
			}
		}

		if(subFlag == true && predFlag == true && objFlag == false) {
			for(Tree child : root.children()) {
				if(child.label().value().equals("NP")){
					Tree object = extractSubjectRest(child);
					if(!siblings.isEmpty()) {
						for(Tree obj : siblings) {
							if(obj!=null){
								String objStr = prettyPrint(obj);

								String attr ="";
								if(obj.siblings(root) != null){

									attr = extractAttributesForNouns(obj.siblings(root));
								}

								System.out.println(objStr);
								objList.add(attr + objStr);
							}
							triple.setObject(objList);
							siblings = new ArrayList<Tree>();;
							objFlag = true;
						}
					}
				}
			}
		}

		return triple;


	}


	private SentenceTriple extractFrag(Tree child, Tree tree) {
		boolean containsNP= false;
		boolean containsSBAR= false;
		boolean containsPP= false;

		for(Tree fragChild : child.children()) {
			if(fragChild.label().value().equals("NP")){
				containsNP = true;
			}
			if(fragChild.label().value().equals("SBAR")){
				containsSBAR = true;
			}

			if(fragChild.label().value().equals("PP")){
				containsPP = true;

			}
		}
		if(containsNP && containsSBAR){  //e.g. Jack who killed Mary killed(Jack, Mary)
			triple =  extractTriplesFromNPSBAR(child.children());
		}
		else if(containsNP && containsPP){//e.g. Company where Jack works  works(Jack,Company)
			triple =  extractTriplesFromFRAGNPPP(child.children());
		}
		else if(containsNP && !containsPP && !containsSBAR){//e.g. Company where Jack works  works(Jack,Company)

			triple = extractTripleFromNPVP(child.children());
		}
		return triple;
	}

	private SentenceTriple extractTriplesFromFRAGNPPP(Tree[] children) {

		for (Tree child: children) {

			if(child.label().value().equals("NP")){

				Tree subject = extractSubjectRest(child);
				if(subject!=null){

					String attr = extractAttributesForNouns(subject.siblings(child));
					objList.add(attr + prettyPrint(subject));
					triple.setObject(objList);

				}
			}
			if(child.label().value().equals("PP")){

				for(Tree childOfChild : child.children()){

					if(childOfChild.label().value().equals("SBAR") ){

						Tree[] sbarChildren = childOfChild.children();

						for(Tree sbarChild : sbarChildren){

							if(sbarChild.label().value().equals("S")){

								SentenceTriple triple2 = extractTripleFromNPVP(sbarChild.children());
								triple.setSubject(triple2.getSubject());
								triple.setPredicate(triple2.getPredicate());
							}

						}
					}
				}
			}
		}

		return triple;
	}

	/**
	 * Extract the components of PP 
	 * @param tree
	 * @param child
	 * @return
	 */
	private SentenceTriple extractSubObjNoPred(Tree tree, Tree child) {

		Tree sub = extractSubjectRest(child);
		if(sub!=null){

			String attr = extractAttributesForNouns(sub.siblings(child));
			subList.add(attr + prettyPrint(sub));
			triple.setSubject(subList);
		}
		//if the sibling is an PP you can extract the noun in that PP as object 
		//			List<Tree> siblings = sub.siblings(tree);
		for(Tree sibling : child.children()){

			if(sibling.label().value().equals("PP")){
				Tree obj = extractSubjectRest(sibling);
				if(obj != null){
					String attr = extractAttributesForNouns(obj.siblings(sibling));
					objList.add(attr + prettyPrint(obj));
					triple.setObject(objList);

				}
			}
		}
		return triple;
	}

	private SentenceTriple extractTriplesFromNPSBAR(Tree[] children) {

		for (Tree child: children) {

			if(child.label().value().equals("NP")){

				Tree subject = extractSubjectRest(child);
				if(subject!=null){

					String attr = extractAttributesForNouns(subject.siblings(child));
					subList.add(attr + prettyPrint(subject));
					triple.setSubject(subList);

				}
			}

			if(child.label().value().equals("SBAR")){

				Tree[] sbarChildren = child.children();

				for(Tree sbarChild : sbarChildren){

					if(sbarChild.label().value().equals("S")){

						SentenceTriple triple2 = extractTripleFromNPVP(sbarChild.children());
						if(triple2.getSubject() !=null){
							triple.setObject(triple.getSubject());
							triple.setSubject(triple2.getSubject());

						}
						else{
							triple.setObject(triple2.getObject());

						}

						triple.setPredicate(triple2.getPredicate());
					}

				}
			}

		}
		return triple;
	}

	private SentenceTriple extractTripleFromNPVP(Tree[] children) {

		Tree npSubtree;
		Tree vpSubtree;
		for (Tree child: children) {

			//The subject is extracted from NP
			if(child.label().value().equals("NP")){
				npSubtree = child;
				System.out.println(child.label().value());

				currentSubject=null; //reset

				Tree subject = extractSubjectRest(npSubtree);
				if(!siblings.isEmpty()) {
					for(Tree sub : siblings) {
						if(sub!=null){
							String subStr = prettyPrint(sub);

							String attr = "" ;
							if(sub.siblings(npSubtree)!=null){
								attr = extractAttributesForNouns(sub.siblings(npSubtree));
							}
							System.out.println(subStr);
							subList.add(subStr + attr);
						}
						triple.setSubject(subList);
						siblings = new ArrayList<Tree>();
					}
				}
				else {
					if(subject!=null){
						String subStr = prettyPrint(subject);

						String attr = "" ;
						if(subject.siblings(npSubtree)!=null){
							attr = extractAttributesForNouns(subject.siblings(npSubtree));
						}
						System.out.println(subStr);
						subList.add(subStr + attr);
					}
					triple.setSubject(subList);
					siblings = new ArrayList<Tree>();
				}
			}
			//The predicate and object are extracted from VP
			else if(child.label().value().equals("VP")){

				vpSubtree = child;
				Tree pred = extractPredicateRest(vpSubtree);

				if(pred!=null){

					String predStr = prettyPrint(pred);

					triple.setPredicate(predStr);

					Tree object = extractObject(pred.siblings(vpSubtree));
					if(!siblings.isEmpty()) {
						for(Tree obj : siblings) {
							if(obj!=null){
								String objStr = prettyPrint(obj);

								String attr ="";
								if(obj.siblings(vpSubtree) != null){

									attr = extractAttributesForNouns(obj.siblings(vpSubtree));
								}

								System.out.println(objStr);
								objList.add(objStr + attr);
							}
							triple.setObject(objList);
							siblings = new ArrayList<Tree>();
						}
					}
					else {
						if(object!=null){
							String objStr = prettyPrint(object);

							String attr ="";
							if(object.siblings(vpSubtree) != null){	
								attr = extractAttributesForNouns(object.siblings(vpSubtree));
							}

							System.out.println(objStr);
							objList.add(objStr + attr);
						}
						triple.setObject(objList);
						siblings  = new ArrayList<Tree>();
					}
				}
			}
		}
		return triple;
	}

	private String prettyPrint(Tree pred) {
		return pred.firstChild().label().value() +"/" + pred.label().value();
	}

	/**
	 * Extract the noun from NP branch of the sentence
	 * The extracted noun is the first noun in the tree
	 * @param npSubtree
	 * @return
	 */
	public Tree extractSubject(Tree npSubtree) {
		Tree[] iter = npSubtree.children();
		for(Tree ch : iter){
			currentSubject = null;
			if(nounTagSet.contains(ch.label().value())){
				if(ch != null) {
					currentSubject = ch;
					if(!currentSubject.equals(null))
						siblings.add(currentSubject);
				}
				return currentSubject;
			}
			else if (currentSubject == null){
				extractSubject(ch);
			}
			else if(currentSubject != null) {
				siblings.add(currentSubject);
				currentSubject = null;
				extractSubject(ch);
			}
		}
		//			if(!currentSubject.equals(null))
		//				siblings.add(currentSubject);
		return currentSubject;
	}


	public Tree extractSubjectRest(Tree npSubtree) {
		currentSubject = null;
		return extractSubject(npSubtree);
	}


	/**
	 * Extract the verb from the VP branch of the sentence. 
	 * The extracted verb corresponds to the deepest verb descendant of the VP
	 * @param vpSubtree
	 * @return
	 */
	private Tree extractPredicate(Tree vpSubtree) {


		if(vpSubtree.isLeaf())
			return currentPredicate;
		List<Tree> subTrees = vpSubtree.getChildrenAsList();

		for(Tree ch : subTrees){

			if(verbTagSet.contains(ch.label().value())){
				currentPredicate = ch;
			}

			extractPredicate(ch);
		}
		return currentPredicate;

	}

	private Tree extractPredicateRest(Tree vpSubtree) {
		currentPredicate = null;
		return extractPredicate(vpSubtree);

	}

	/**
	 * Extract the object from sibling subtrees of the VP containing the predicate
	 * @param vpSiblings
	 * @return
	 */
	private Tree extractObject(List<Tree> vpSiblings) {

		if(vpSiblings == null)
			return null;

		Tree object = null;
		for(Tree ch : vpSiblings){

			if(ch.label().value().equals("PP") || ch.label().value().equals("NP") ){

				object = extractSubjectRest(ch);
				if(object !=null)
					return object;
			}
			else if(ch.label().value().equals("ADJP")){
				object = extractAdjective(ch);

				if(object !=null)

					return object;
			}
		}
		return object;
	}

	public Tree extractAdjective(Tree npSubtree) {

		Tree[] iter = npSubtree.children();

		for(Tree ch : iter){


			if(adjTagSet.contains(ch.label().value())){

				currentAdj = ch;

			}
			else{
				extractSubjectRest(ch);
			}

		}
		return currentAdj;
	}

	public Tree extractAdjectiveRest(Tree npSubtree) {

		currentAdj = null;
		return extractAdjective(npSubtree);

	}


	/**
	 * Extract attributes (modifier) for nouns. Those are normally adjectives.
	 * @param npTree
	 * @return
	 */
	public String extractAttributesForNouns(List<Tree> npTree){

		if(npTree == null)
			return "";

		StringBuffer res = new StringBuffer();
		for(Tree e : npTree){

			if(nounModifiersSet.contains(e.label().value())){

				res.append(prettyPrint(e)).append(" ");
			}

		}

		return res.toString();
	}

}