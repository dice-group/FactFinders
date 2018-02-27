package elasticSearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.aksw.defacto.Defacto;
import org.aksw.defacto.boa.BoaPatternSearcher;
import org.aksw.defacto.boa.Pattern;


public class BOAPatternGenerator {
	
//	public static void main(String[] args) {
//
//		Defacto.init();
////        queryPatterns("http://dbpedia.org/ontology/award");
////        System.out.println("--------------");
////        queryPatterns("http://dbpedia.org/ontology/birthPlace");
////        System.out.println("--------------");
////        queryPatterns("http://dbpedia.org/ontology/deathPlace");
////        System.out.println("--------------");
//        queryPatterns("http://dbpedia.org/ontology/foundationPlace");
//        System.out.println("--------------");
////        queryPatterns("http://dbpedia.org/ontology/leaderName");
//        System.out.println("--------------");
////        queryPatterns("http://dbpedia.org/ontology/team");
//        System.out.println("--------------");
////        queryPatterns("http://dbpedia.org/ontology/author");
////        System.out.println("--------------");
////        queryPatterns("http://dbpedia.org/ontology/spouse");
////        System.out.println("--------------");
////        queryPatterns("http://dbpedia.org/ontology/starring");
////        System.out.println("--------------");
////        queryPatterns("http://dbpedia.org/ontology/subsidiary");
//    }
	
	/**
	 * @param bps
	 */
	public Set<String> queryPatterns(String uri) {
		Defacto.init();
		Set<String> foundWords = new HashSet<String>();
		int nr = 50;
		BoaPatternSearcher bps = new BoaPatternSearcher();
		List<Pattern> sub = new ArrayList<>();
        sub.addAll(bps.getNaturalLanguageRepresentations(uri, nr, "en"));
//        sub.addAll(bps.getNaturalLanguageRepresentations(uri, nr, "de"));
//        sub.addAll(bps.getNaturalLanguageRepresentations(uri, nr,  "fr"));
        
        System.out.println(uri);
        Iterator<Pattern> iterator = sub.iterator();
        while ( iterator.hasNext()) {
			Pattern pattern = iterator.next();
			foundWords.add(pattern.normalize());
//            System.out.println(pattern.naturalLanguageRepresentation + " --- " + pattern.normalize());
        }
//        System.out.println();
        return foundWords;
	}

}
