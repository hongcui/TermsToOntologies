package client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.ProvisionalTermDAO;

import beans.ProvisionalTerm;
import beans.response.Success;

public class TermsToOntologiesClient {

	private BioPortalClient bioPortalClient;
	private Logger logger;
	
	public TermsToOntologiesClient() {
		String url = "http://rest.bioontology.org/bioportal/";
		String userId = "40522";
		String apiKey = "b5ca12b0-23f8-4627-be61-1e045cf73a7d";
		bioPortalClient = new BioPortalClient(url, userId, apiKey);	
		logger =  LoggerFactory.getLogger(this.getClass());
	}
	
	/**
	 * @param provisionalTerm
	 * @return temporary id given to the provided provisionalTerm
	 * @throws Exception
	 */
	public String sendTerm(ProvisionalTerm provisionalTerm) throws Exception {
		Success success = bioPortalClient.createProvisionalTerm(provisionalTerm);
		String temporaryId = getIdFromSuccessfulCreate(success);
		provisionalTerm.setTemporaryid(temporaryId);
		ProvisionalTermDAO.getInstance().add(provisionalTerm);
		return temporaryId;
	}
	
	/**
	 * @return Map<Temporary ID, Permanent ID> of newly discovered adoptions 
	 * @throws Exception 
	 */
	public Map<String, String> checkTermAdoptions() throws Exception  {
		Map<String, String> result = new HashMap<String, String>();
		List<ProvisionalTerm> allProvisionalTerms = ProvisionalTermDAO.getInstance().getAll();
		for(ProvisionalTerm provisionalTerm : allProvisionalTerms) {
			Success success = bioPortalClient.getProvisionalTerm(provisionalTerm.getTemporaryid());
			
		}
		return result;
	}
	
	private String getIdFromSuccessfulCreate(Success createSuccess) {
		List<Object> fullIdOrIdOrLabel = createSuccess.getData().getClassBean().getFullIdOrIdOrLabel();
		for(Object object : fullIdOrIdOrLabel) {
			if(object instanceof JAXBElement) {
				JAXBElement<String> possibleIdElement = (JAXBElement<String>)object;
				if(possibleIdElement.getName().toString().equals("id")) {
					return possibleIdElement.getValue();
				}
			}
		}
		return null;
	}

}
