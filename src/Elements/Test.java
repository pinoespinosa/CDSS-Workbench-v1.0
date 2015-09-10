package Elements;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import userint.UI;
import Auxiliares.SimpleFile;

public class Test {

	String request="";
	String response="";
	String fileName;

	public Test(String fileName){
		this.fileName=fileName;
		List<String> fileText=SimpleFile.readFile(UI.getEnvirometValue(UI.CARPETA_FILES), fileName);

		int i=1;
		if (!fileText.isEmpty() && fileText.get(0).equals("##_REQUEST_##"))
		{
			while(i<fileText.size() && !fileText.get(i).equals("##_RESPONSE_##"))
			{
				request+=fileText.get(i)+" ";
				i++;
			}


			if (fileText.get(i).equals("##_RESPONSE_##"))
			{
				i++;
				while(i<fileText.size())
				{
					response+=fileText.get(i)+" ";
					i++;
				}
			}
		}
	}

	public boolean runTest() throws SOAPException, IOException{
		return runTest(request, response);
}

	public static boolean runTest(String input, String output) throws SOAPException, IOException{

		String webResponse="";
		if ( !output.isEmpty() && !input.isEmpty() )
		{


			URL url = new URL("http://"+ UI.getEnvirometValue(UI.IP_WEB_SERVICE)+":8080/TestWSDL/services/Testing");
			java.net.URLConnection conn = url.openConnection();
			conn.setReadTimeout(50*1000);
			// Set the necessary header fields
			conn.setRequestProperty("SOAPAction", "http://"+ UI.getEnvirometValue(UI.IP_WEB_SERVICE)+":8080/TestWSDL/services/Testing");
			conn.setDoOutput(true);
			// Send the request
			java.io.OutputStreamWriter wr = new java.io.OutputStreamWriter(conn.getOutputStream());
			wr.write(input);
			wr.flush();
			// Read the response
			java.io.BufferedReader rd = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
			String line;
			
			while ((line = rd.readLine()) != null){ 
				webResponse+=line+"\n";
			}
			
			MessageFactory factory = MessageFactory.newInstance();
		    SOAPMessage message = factory.createMessage(
		            new MimeHeaders(),
		            new ByteArrayInputStream(webResponse.getBytes(Charset.forName("UTF-8"))));

		    @SuppressWarnings("unused")
			SOAPBody body = message.getSOAPBody();
		    /*
		    List
		    
		    
		    
		    if (body.getFirstChild().getLocalName().equals("soapenv:Body"))
		    {
		    	Node elem= body.getFirstChild();

		    	if (elem.getChildNodes()getFirstChild().getLocalName().equals("soapenv:Body"))

		    	
		    	
		    }*/	
		//	System.out.println();																																															
			
	//	} catch (Exception e) {
	//		System.err.println("Error occurred while sending SOAP Request to Server");
	//		e.printStackTrace();
	//		return false;
		}
		return webResponse.replaceAll(" ","").replaceAll("\n", "").equals("<?xmlversion=\"1.0\"encoding=\"UTF-8\"?>"+output.replaceAll(" ",""));	
	}
	
	@Override
	public String toString() {
		if ( response.isEmpty() || request.isEmpty())
			return fileName + "- Archivo con errores";
		else
			return fileName;
	}

}
