package com.tk.bzintegration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

import com.j2bugzilla.base.Bug;
import com.j2bugzilla.base.BugzillaConnector;
import com.j2bugzilla.base.BugzillaException;
import com.j2bugzilla.base.ConnectionException;
import com.j2bugzilla.rpc.BugComments;
import com.j2bugzilla.rpc.BugSearch;
import com.j2bugzilla.rpc.CommentBug;
import com.j2bugzilla.rpc.BugSearch.SearchLimiter;
import com.j2bugzilla.rpc.LogIn;
import com.tk.bzintegration.properties.BZIntegrationProperties;
import com.tk.bzintegration.properties.PropertyKey;


public class App implements Authenticator{

	private static final Logger logger = Logger
			.getLogger(App.class);

	public static final String FAIL = "Authentication failed";

	HttpClient  httpClient = new HttpClient();
	XmlRpcClient rpcClient = new XmlRpcClient();
	XmlRpcCommonsTransportFactory factory = new XmlRpcCommonsTransportFactory(
			rpcClient);
	XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

	public App() {
		factory.setHttpClient(httpClient);
		rpcClient.setTransportFactory(factory);
		try {
			config.setServerURL(new URL(BZIntegrationProperties.getInstance().getProperty(PropertyKey.BZ_RPC_ADDRESS)));
		} catch (MalformedURLException e) {
			logger.error("Error during BugzillaAuthenticator init", e);
			throw new RuntimeException(e);
		}
		rpcClient.setConfig(config);
	}

	public String authenticateUser(String username, char[] password)
			throws XmlRpcException {
		Map<String, String> loginMap = new HashMap<String, String>();
		loginMap.put("login", username);
		loginMap.put("password", new String(password));
		//loginMap.put("rememberlogin", "Bugzilla_remember");
		try {
			String result = rpcClient.execute("User.login", new Object[] { loginMap })
					.toString();
			if (result.contains("id=")){
				return Authenticator.SUCCESS;
			}else{
				return App.FAIL;
			}

			
		} catch (XmlRpcException e) {
			logger.error("Error during authenticating", e);
			return e.getLocalizedMessage();
		}
	}
	
	
	public static void main(String[] args) throws XmlRpcException,
			KeyManagementException,
			NoSuchAlgorithmException, IOException, ConnectionException, BugzillaException {
		
		//System.setProperty("https.protocols", "SSLv3");
		
		System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
		System.setProperty("javax.net.ssl.keyStore", "devel.p12");
		System.setProperty("javax.net.ssl.keyStorePassword", "devel");
        //System.setProperty("javax.net.debug", "ssl");
        
		System.setProperty("javax.net.ssl.trustStore", "cacerts");
        System.setProperty("javax.net.ssl.trustStorePassword", "haslo123");
        //disableCertificateValidation();
        
        //System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "false");
        System.out.println("DUPA");

		//App ba = new App();
		
		BugzillaConnector conn = new BugzillaConnector();
		conn.connectTo("https://shawnee.pentesters.pl/bugzilla");
		
		LogIn logIn = new LogIn("tomasz.krajewski@pentesters.pl", "soh2iNg6seisoi");
		conn.executeMethod(logIn);
		
		BugSearch findBugs = new BugSearch(SearchLimiter.OWNER, "tomasz.krajewski@pentesters.pl");
		conn.executeMethod(findBugs);
		List<Bug> results = findBugs.getSearchResults();
		
		int i = 0;
		for (Bug bug : results) {
			System.out.println((++i)+ " " +bug.getProduct()+ " " +bug.getSummary());
		}
		
		CommentBug cb = new CommentBug(1, "test");
		conn.executeMethod(cb);
	
		System.out.println("DUPA1");
	}
	
}
