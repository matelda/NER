import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * <h1>MADAMIRA Client</h1> 
 * This class is a HTTP Client that connects with a local MADAMIRA server
 * it receives an input string representing an XML file, sends a request to the
* server and writes the response to an output string representing another XML file.
**/
class MADAMIRAClient {
	private static String url = "http://localhost:";
	private org.apache.http.client.HttpClient httpclient;
	private String inFile ;
	private int PORT = 8223;
	private String outputXML = null ;
	
	@SuppressWarnings("deprecation")
	public MADAMIRAClient() {
		httpclient = new DefaultHttpClient();
		}

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
	 * @param iFile
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
	 * @return xml string of the following format :
	 */
	public String run(String iFile) {
		this.inFile = iFile;
		try {
			HttpPost httppost = new HttpPost(url+Integer.toString(PORT));
			StringEntity reqEntity = new StringEntity(inFile,"UTF-8");
			reqEntity.setContentType("application/xml");
			reqEntity.setChunked(true);
			httppost.setEntity(reqEntity);
			HttpResponse response=null;
			HttpEntity resEntity=null;
			try {
				response = httpclient.execute(httppost);
				resEntity = response.getEntity();
			} catch(HttpHostConnectException ex) {
				System.out.println(ex.getMessage());
			return outputXML;
			}

			if (resEntity != null) {
				InputStream responseBody = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(responseBody, "utf8"));

				String line = null;
				StringBuilder sbr = new StringBuilder();
				while ((line = reader.readLine()) != null) {
						sbr.append(line);
				}
				reader.close();
				outputXML = sbr.toString();
			}
			EntityUtils.consume(resEntity);
			} catch(IOException ioe) {
				ioe.printStackTrace();
			} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
				httpclient.getConnectionManager().shutdown();
			}
			return outputXML;
			}
}
