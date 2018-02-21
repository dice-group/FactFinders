# FactFinders
Fact finders is an implementation based on Jeff Pasternack, 2010, reserach. This evaluatory project intends to implement the trustworthiness model and graph based fact finding algorithms for Open Data.

Search is federated for querying resources in Clueweb index, currently hosted at 131.234.28.255:6060
Search uses Wordtnet Index for synonyms, in order to organize multiple possible queries. 

# Properties.xml
User is required to create an internal Properties.xml file with the content given below:

<?xml version="1.0" encoding="UTF-8"?>
<jwnl_properties language="en">
  <version publisher="Princeton" number="3.0" language="en"/>
  <dictionary class="net.didion.jwnl.dictionary.FileBackedDictionary">
    <param name="dictionary_element_factory" 
      value="net.didion.jwnl.princeton.data.PrincetonWN17FileDictionaryElementFactory"/>
    <param name="file_manager" value="net.didion.jwnl.dictionary.file_manager.FileManagerImpl">
      <param name="file_type" value="net.didion.jwnl.princeton.file.PrincetonRandomAccessDictionaryFile"/>
      <param name="dictionary_path" value="PATH TO LOCAL DIRECTORY WHERE THE "dict" FOLDER IS PLACED/>
    </param>
  </dictionary>
  <resource class="PrincetonResource"/>
</jwnl_properties>
 
# Wordnet dictionary
Wordnet's current version can be downloaded from <http://wordnet.princeton.edu/wordnet/download/current-version/>
Place the "dict" folder in your project forlder to enable the Wordnet functionality.
