/**
 * <h1>MADAMIRA Client</h1> 
 * This class is a HTTP Client that connects with a local MADAMIRA server
 * it receives an input string representing an XML file, sends a request to the
* server and writes the response to an output string representing another XML file.
**/

interface MADAMIRAClient {

	/**
	 * * connects with the server sends the xml string and receives the resulting xml String
	 * 
	 * @param iFile xml string of the following format :
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * 		<madamira_input xmlns="urn:edu.columbia.ccls.madamira.configuration:0.1">
	 * 			<in_doc id="id">
	 * 				<in_seg id="0">النص باللغة العربية</in_seg>
	 * 			</in_doc>
	 * 		</madamira_input>
	 * }
	 * 
	 * @return xml string of the following format :
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
	 * 		<madamira_output xmlns="urn:edu.columbia.ccls.madamira.configuration:0.1">
	 * 			<out_doc id="id">
	 * 				<out_seg id="0">
	 * 					 <word_info>
	 * 						<word id="0" word="الكلمة" length="0" offset="0">
	 * 							<svm_prediction>
	 * 								<morph_feature_set pos="noun" gen="f" num="s" stt="d" cas="n"/>
	 * 							</svm_prediction>
	 * 							<analysis rank="0" score="1">
	 * 								<morph_feature_set gloss="word" pos="noun" gen="f" num="s" stt="d" cas="n" stem="كلمة"/>
	 * 							</analysis>
	 * 							<tokenized scheme="tok">
	 * 								<tok id="0" form0="الكلمة"/>
	 * 							</tokenized>
	 * 						</word>
	 * 					</word_info>
	 * 				</out_seg>
	 * 			</out_doc>
	 * 		</madamira_output>
	 * }
	 */
	public abstract String run(String iFile);

}