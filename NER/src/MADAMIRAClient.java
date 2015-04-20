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

/** A HTTP Client that reads an input XML file, sends a request to the
* server and writes the response to an output XML file.
**/
class MADAMIRAClient {
	private static String url = "http://localhost:";
	private org.apache.http.client.HttpClient httpclient;
	private String inFile ;
	private int PORT = 8223;
	private String outputXML = null ;
	
	public MADAMIRAClient(String iFile) {
		this.inFile = iFile;
		httpclient = new DefaultHttpClient();
		}
	/**
	* @return true if successful execution, else false.
	*/

	public String run() {
		try {
			HttpPost httppost = new HttpPost(url+Integer.toString(PORT));
			StringEntity reqEntity = new StringEntity(inFile,"UTF-8");
			reqEntity.setContentType("application/xml");
			reqEntity.setChunked(true);
			// It may be more appropriate to use FileEntity
			// class in this particular instance but we are using
			// a more generic InputStreamEntity to demonstrate
			// the capability to stream out data from any
			// arbitrary source
			// FileEntity entity =
			// new FileEntity(file, "binary/octet-stream");
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
			//System.out.println(response.getStatusLine());
			if (resEntity != null) {
				InputStream responseBody = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(responseBody, "utf8"));
//				BufferedWriter writer = new BufferedWriter(
//						new OutputStreamWriter(new FileOutputStream(oFile), "utf8"));
				String line = null;
				StringBuilder sbr = new StringBuilder();
				while ((line = reader.readLine()) != null) {
//					sbr.append(line+"\\n");
						sbr.append(line);
				}
				reader.close();
//				writer.write(sbr.toString());
//				writer.flush();
//				writer.close();
//				System.out.println(sbr.toString());;
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
