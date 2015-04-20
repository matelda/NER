import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.Node;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**
 * @author matelda
 *
 */
public class MADAFeatureExtracion {

	public List<Token> extract(String inputText)
	{
		List<Token> Tokens ;
		Tokens = new ArrayList<Token>();
		String inputXML = generateMADAInput(inputText);
//		System.out.println(inputXML);
		MADAMIRAClient md = new MADAMIRAClient(inputXML);
		String outputXML = md.run();
//		System.out.println(outputXML);
		extractFeatures(outputXML,Tokens);
		//checkTokens(Tokens);
		return Tokens;
	}
	public String generateMADAInput(String inputText)
	{
		String docId = "NERSystem";
//		int noOfSentences ;
//		String[] sentences ;
//		String[] sentPosition = null ;
//		//split sting into sentences
//		sentences = inputText.split("\\.");
//		noOfSentences = sentences.length;
//		sentPosition = new String[noOfSentences];
//		int position = 0 ;
//		for (int i = 0 ; i < noOfSentences ; i++)
//		{
//			sentPosition[i] = Integer.toString(position) ;
//			position += sentences[i].length() + 1 ;
//		}
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
            in_doc.setAttribute("id", docId);
            mainRootElement.appendChild(in_doc);
   
            Element in_seg;
            in_seg = doc.createElement("in_seg");
        	in_seg.setAttribute("id","0");
        	in_seg.appendChild(doc.createTextNode(inputText));
        	in_doc.appendChild(in_seg);
//            for (int i = 0 ; i < noOfSentences ; i++)
//            {
//            	in_seg = doc.createElement("in_seg");
//            	in_seg.setAttribute("id", sentPosition[i]);
//            	in_seg.appendChild(doc.createTextNode(sentences[i]));
//            	in_doc.appendChild(in_seg);
//            }
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
	
	public void extractFeatures(String outputXML, List<Token> Tokens)
	{
		try 
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource src = new InputSource();
			src.setCharacterStream(new StringReader(outputXML));

			Document doc = builder.parse(src);
			
			Token temp ;
			
			int segOffset = 0 ;
			int wordOffset;
			String segId ;
			
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
			Element word;
			Element analysisTag;
			Element analysis;
			Element svm;
			Element morph;
			Element token;
			
			NodeList wordList;
			NodeList analysisList;
			NodeList tokenList;
			NodeList outSegList = doc.getElementsByTagName("out_seg");
			
			for (int i = 0 ; i < outSegList.getLength() ; i++)
			{
				seg = (Element) outSegList.item(i);
				segId = seg.getAttribute("id");
				System.out.println(segId);
				segOffset = Integer.parseInt(segId);

				wordList = seg.getElementsByTagName("word");
				for (int j = 0 ; j < wordList.getLength(); j++)
				{
					List<String> innerTokens = new ArrayList<String>();
					word = (Element) wordList.item(j);
					wordOffestString = word.getAttribute("offset");
					wordOffset = Integer.parseInt(wordOffestString);
					wordOffset += segOffset ;
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
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void checkTokens(List<Token> Tokens)
	{
		for (int i = 0 ; i < Tokens.size() ;i++)
		{
			Tokens.get(i).printToken();
		}
	}
//	public static void main(String[] args) {
//		MADAFeatureExtracion instance =  new MADAFeatureExtracion();
//		String text = "السلطة الفلسطينية. حدثت";
//		instance.extract(text);
//	}
	
}
