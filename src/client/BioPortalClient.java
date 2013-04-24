package client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBException;


import beans.ProvisionalTerm;
import beans.response.Success;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class BioPortalClient {

	private String apiKey;
	private String apiUrl;
	private Client client;

	public BioPortalClient(String apiUrl, String apiKey) {
		this.apiUrl = apiUrl;
		this.apiKey = apiKey;
		ClientConfig clientConfig = new DefaultClientConfig();
		client = Client.create(clientConfig);
		client.addFilter(new LoggingFilter(System.out));
	}
	
	/**
	 * Get a single provisional term for the given provisional term id.
	 * @throws JAXBException 
	 */
	public Success getProvisionalTerm(String termId) throws JAXBException {
		String url = this.apiUrl + "provisional";
	    WebResource webResource = client.resource(url);
	    MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
	    queryParams.add("termid", termId);
	    queryParams.add("apikey", this.apiKey);
	    return webResource.queryParams(queryParams).get(Success.class);
	}
	
	/**
	 * Get all available provisional terms using a paged interface.
	 */
	public Success getProvisionalTerms() {
		String url = this.apiUrl + "provisional";
	    WebResource webResource = client.resource(url);
	    MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
	    queryParams.add("apikey", this.apiKey);
	    return webResource.queryParams(queryParams).get(Success.class);
	}
	
	/**
	 * Create a provisional term.
	 */
	public String createProvisionalTerm(ProvisionalTerm provisionalTerm) {
		String url = this.apiUrl + "provisional";
	    WebResource webResource = client.resource(url);
	    MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();

	    MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
	    formData.add("apikey", this.apiKey);
	    if(provisionalTerm.hasPreferredName() && provisionalTerm.hasDefinition() && provisionalTerm.hasSubmittedBy()) {
	    	formData.add("preferredname", provisionalTerm.getPreferredname());
	    	formData.add("definition", provisionalTerm.getDefinition());
	    	formData.add("submittedby", provisionalTerm.getSubmittedby());
	    } else {
	    	//required parameters not met
	    	return "";
	    }
	    if(provisionalTerm.hasOntologyIds())
	    	formData.add("ontologyids", provisionalTerm.getOntologyids());
	    if(provisionalTerm.hasSynonyms())
	    	formData.add("synonyms", provisionalTerm.getSynonyms());
	    if(provisionalTerm.hasSuperClass())
	    	formData.add("superclass", provisionalTerm.getSuperclass());
	    
	    String result = webResource.queryParams(queryParams).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
	    		.post(String.class, formData);
	    return result;
	}
	
	/**
	 * Update fields for a provisional term.
	 */
	public String updateProvisionalTerm(String termId, ProvisionalTerm provisionalTerm) {	    
		String url = this.apiUrl + "provisional";
	    WebResource webResource = client.resource(url);
	    
	    MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
	    MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
	    formData.add("apikey", this.apiKey);
	    formData.add("termid", termId);
	    if(provisionalTerm.hasPreferredName())
	    	formData.add("preferredname", provisionalTerm.getPreferredname());
	    if(provisionalTerm.hasDefinition()) 
	    	formData.add("definition", provisionalTerm.getDefinition());
	    if(provisionalTerm.hasSubmittedBy()) 
	    	formData.add("submittedby", provisionalTerm.getSubmittedby());
	    if(provisionalTerm.hasOntologyIds())
	    	formData.add("ontologyids", provisionalTerm.getOntologyids());
	    if(provisionalTerm.hasPermanentId())
	    	formData.add("permanentid", provisionalTerm.getPermanentid());
	    if(provisionalTerm.hasSuperClass())
	    	formData.add("superclass", provisionalTerm.getSuperclass());
	    
	    String result = webResource.queryParams(queryParams).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
	    		.put(String.class, formData);
	    return result;
	}
	
	/**
	 * Delete a provisional term.
	 */
	public void deleteProvisionalTerm(String termId) {
		String url = this.apiUrl + "provisional";
	    WebResource webResource = client.resource(url);
	    MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
	    queryParams.add("termid", termId);
	    queryParams.add("apikey", this.apiKey);
	    webResource.queryParams(queryParams).delete();
	}
	
	public static void main(String[] args) throws JAXBException {
		BioPortalClient bioPortalClient = new BioPortalClient(
				"http://rest.bioontology.org/bioportal/", "apikey");
		ProvisionalTerm provisionalTerm = new ProvisionalTerm();
		provisionalTerm.setDefinition("this is a test. so i give a long test definition to see if the test can be completed successfully.");
		provisionalTerm.setPreferredname("test");
		provisionalTerm.setSubmittedby("thomas.rodenhausen");
		bioPortalClient.createProvisionalTerm(provisionalTerm);
		
		
		//Success success = bioPortalClient.getProvisionalTerms();
		//System.out.println(success.getAccessDate());
		//Success success = bioPortalClient.getProvisionalTerm(
		//		"http://purl.bioontology.org/ontology/provisional/9cc8b147-e193-4d23-a3a1-9da34eeeb5a9");
		//System.out.println(success.getAccessDate());
	}
}
