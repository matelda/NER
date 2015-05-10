
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class MADAFeatureExtracion implements MorphologicalAnalyser {

	String inputDocId ;
	String outputDocId ;
	MADAMIRAClient md = new MADAMIRAClientImp();
	
	/* (non-Javadoc)
	 * @see MorphologicalAnalyser#extract(java.lang.String)
	 */
	@Override
	public List<Token> extract(String inputText)
	{

		inputDocId = generateInputDocId().trim();
		List<Token> Tokens ;
		Tokens = new ArrayList<Token>();
		String inputXML = generateMADAInput(inputText);
		String outputXML = md.run(inputXML);
		extractFeatures(outputXML,Tokens);
		while(!inputDocId.equals(outputDocId))
		{
			Tokens.clear();
			outputXML = md.run(inputXML);
			extractFeatures(outputXML,Tokens);
		}
		return Tokens;
	}
	
	private String generateInputDocId()
	{
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(10000000);
		return Integer.toString(randomInt);
	}
	
	/**
	 * constructs an xml file in madamira's input format that contains the raw arabic text in addition to 
	 * the input document id
	 * 
	 * @param inputText -  raw arabic text String
	 * @return xml file in madamira's input format
	 */
	private String generateMADAInput(String inputText)
	{
		
		//buliding up xml Document
		DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            Element mainRootElement = doc.createElementNS("urn:edu.columbia.ccls.madamira.configuration:0.1", "madamira_input");
            doc.appendChild(mainRootElement);
            doc.setXmlStandalone(true);
            Element in_doc = doc.createElement("in_doc");
            in_doc.setAttribute("id", inputDocId);
            mainRootElement.appendChild(in_doc);
   
            Element in_seg;
            in_seg = doc.createElement("in_seg");
        	in_seg.setAttribute("id","0");
        	in_seg.appendChild(doc.createTextNode(inputText));
        	in_doc.appendChild(in_seg);

            //converting Document to String
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            
            return writer.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } 
	}
	
	/**
	 * parses madamira's output (xml) file and extracts morphological features and tokens
	 * and adds tokens to the empty list 
	 * 
	 * @param outputXML - the result of the madamira's analysis
	 * @param Tokens - an empty list of tokens
	 */
	private void extractFeatures(String outputXML, List<Token> Tokens)
	{
		try 
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource src = new InputSource();
			src.setCharacterStream(new StringReader(outputXML));

			Document doc = builder.parse(src);
			
			Token temp ;

			
			int wordOffset;
			
			String wordOffestString;
			int wordLength;
			String wordText;
			
			String morphCase;
			String morphState;
			String morphNum;
			String morphGen;
			String morphPos;
			String morphStem;
			String morphGloss;
			
			Element seg;
			Element outDocE;
			Element word;
			Element analysisTag;
			Element analysis;
			Element svm;
			Element morph;
			Element token;
			
			NodeList wordList;
			NodeList analysisList;
			NodeList tokenList;
			NodeList outDoc = doc.getElementsByTagName("out_doc");
			outDocE = (Element) outDoc.item(0);
			outputDocId = outDocE.getAttribute("id").trim();
			
			NodeList outSegList = doc.getElementsByTagName("out_seg");
			seg = (Element) outSegList.item(0);
			
			wordList = seg.getElementsByTagName("word");
			for (int j = 0 ; j < wordList.getLength(); j++)
			{
				List<String> innerTokens = new ArrayList<String>();
				word = (Element) wordList.item(j);
				wordOffestString = word.getAttribute("offset");
				wordOffset = Integer.parseInt(wordOffestString);
				wordLength = Integer.parseInt(word.getAttribute("length"));
				wordText = word.getAttribute("word");
				
				analysisList = word.getElementsByTagName("analysis");
				if (analysisList.getLength() > 0)
				{
					analysisTag = (Element) analysisList.item(0);
					analysis = (Element) analysisTag.getElementsByTagName("morph_feature_set").item(0);
					morphCase = analysis.getAttribute("cas");
					morphState = analysis.getAttribute("stt");
					morphNum = analysis.getAttribute("num");
					morphGen = analysis.getAttribute("gen");
					morphPos = analysis.getAttribute("pos");
					morphStem = analysis.getAttribute("stem");
					morphGloss = analysis.getAttribute("gloss");
				}
				else 
				{
					svm = (Element) word.getElementsByTagName("svm_prediction").item(0);
					morph = (Element) svm.getElementsByTagName("morph_feature_set").item(0);
					morphCase = morph.getAttribute("cas");
					morphState = morph.getAttribute("stt");
					morphNum = morph.getAttribute("num");
					morphGen = morph.getAttribute("gen");
					morphPos = morph.getAttribute("pos");
					morphStem = null;
					morphGloss = null;
				}
				innerTokens.clear();
				tokenList = word.getElementsByTagName("tok");
				for(int k = 0 ; k <tokenList.getLength();k++)
				{
					token = (Element) tokenList.item(k);
					innerTokens.add(token.getAttribute("form0"));
				}
					
				temp = new TokenImpl(wordText, wordOffset, wordLength, morphCase, 
						morphState, morphNum, morphGen, morphPos, morphStem, morphGloss);
				temp.setTokenized(innerTokens);
				Tokens.add(temp);
					
				}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
