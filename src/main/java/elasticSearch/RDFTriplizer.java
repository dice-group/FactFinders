package elasticSearch;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;

/**
 * 
 * @author Hussain
 *
 */
public class RDFTriplizer {
	
	public static Model readModel(String fileNameOrUri)
	{
		Model model= ModelFactory.createDefaultModel();
		InputStream in = FileManager.get().open( fileNameOrUri );
		if (in == null) {
			throw new IllegalArgumentException(fileNameOrUri + " not found");
		}
		if(fileNameOrUri.contains(".ttl") || fileNameOrUri.contains(".n3")){
			model.read(in, null, "TTL");
		}else if(fileNameOrUri.contains(".rdf")){
			model.read(in, null);
		}else if(fileNameOrUri.contains(".nt")){
			model.read(in, null, "N-TRIPLE");
		}else{
			model.read(fileNameOrUri);
		}
		return model;
	}
	
	private static String triplify(Model input) throws IOException {
		Set<Resource> set = new HashSet<Resource>();
		String file = "./src/main/resources/testtriples.tsv";
		FileWriter writer = new FileWriter(file);
		FileWriter test = new FileWriter("./src/main/resources/Sum.nt");
		StmtIterator iter= input.listStatements();
		String sub = new String();
		String obj = new String();
		String pred = new String();

		while (iter.hasNext()) {

			Statement stmt = iter.nextStatement(); 
			
			Resource subject = stmt.getSubject(); 
			if(set.contains(subject)) {
				continue;
			}
			StmtIterator iter_obj = input.listStatements(subject, null, (RDFNode)null);
			
			while(iter_obj.hasNext()) {
				Statement statement = iter_obj.nextStatement();
				//System.out.println(statement.toString());
//				 && statement.getObject().toString().contains("1.0")
				if(statement.getPredicate().toString().contains("hasTruthValue")) {
					StmtIterator iter_obj1 = input.listStatements(subject, null, (RDFNode)null);
					while(iter_obj1.hasNext()) {
						Statement stsub = iter_obj1.nextStatement();
						//System.out.println("The inner: " + stsub.toString());
						if(stsub.getPredicate().toString().contains("hasTruthValue")) {
							test.append("<");
							test.append(stsub.getSubject().toString());
							test.append(">");
							test.append(" ");
							test.append("<");
							test.append(stsub.getPredicate().toString());
							test.append(">");
							test.append(" ");
							//System.out.println("Subject: " + sub);
						}
						if(stsub.getPredicate().toString().contains("subject")) {
							sub = stsub.getObject().toString();
							StmtIterator iterSub = input.listStatements();
							while(iterSub.hasNext()) {
								Statement stment = iterSub.nextStatement();
								if(stment.getSubject().toString().equals(sub)) {
									sub = stment.getObject().toString();
									//System.out.println("Subject: " + sub);
								}
							}
							//System.out.println("Subject: " + sub);
						}
						else if(stsub.getPredicate().toString().contains("object")) {
							obj = stsub.getObject().toString();
							StmtIterator iterobj = input.listStatements();
							while(iterobj.hasNext()) {
								Statement stment = iterobj.nextStatement();
								if(stment.getSubject().toString().equals(obj)) {
									obj = stment.getObject().toString();
									//System.out.println("object: " + obj);
								}
							}
							//System.out.println("object: " + obj);
						}
						else if(stsub.getPredicate().toString().contains("predicate")) {
							pred = stsub.getObject().toString().replaceAll("_", " ").replace("http://dbpedia.org/ontology/", " ").trim();
							//System.out.println("predicate: " + pred);
						}
					}
					writer.append(sub);
					writer.append('\t');
					writer.append(pred);
					writer.append('\t');
					writer.append(obj);
					writer.append('\n');
					test.append(sub+"_"+pred+"_"+obj + " .");
					test.append("\n");
				}
				set.add(subject);
			}
		}
		test.flush();
		test.close();
		writer.flush();
		writer.close();
		return file;
	}
	
	public String triplize(String file) throws IOException {
		Model model= readModel(file);

		return triplify(model);
	}

//	public static void main(String[] args) throws IOException {
//
//		Model model= readModel("./src/main/resources/test.nt");
//
//		triplify(model);
//
//	}
}
