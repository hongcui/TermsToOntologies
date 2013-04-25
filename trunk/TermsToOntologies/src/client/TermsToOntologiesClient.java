package client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.ProvisionalTermDAO;

import beans.ProvisionalTerm;
import beans.response.Success;

public class TermsToOntologiesClient {

	private BioPortalClient bioPortalClient;
	private Logger logger;
	
	public TermsToOntologiesClient() throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties(); 
		properties.load(loader.getResourceAsStream("config.properties"));
		String url = properties.getProperty("bioportalUrl");
		String userId = properties.getProperty("bioportalUserId");
		String apiKey = properties.getProperty("bioportalApiKey");
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
		String temporaryId = getIdFromSuccessfulCreate(success, "id");
		provisionalTerm.setTemporaryid(temporaryId);
		ProvisionalTermDAO.getInstance().addAwaitingAdoption(provisionalTerm);
		return temporaryId;
	}
	
	/**
	 * @return Map<Temporary ID, Permanent ID> of newly discovered adoptions 
	 * @throws Exception 
	 */
	public Map<String, String> checkTermAdoptions() throws Exception  {
		Map<String, String> result = new HashMap<String, String>();
		List<ProvisionalTerm> allProvisionalTermsAwaitingAdoption = ProvisionalTermDAO.getInstance().getAllAwaitingAdoption();
		for(ProvisionalTerm provisionalTerm : allProvisionalTermsAwaitingAdoption) {
			Success success = bioPortalClient.getProvisionalTerm(provisionalTerm.getTemporaryid());
			String permanentId = getIdFromSuccessfulCreate(success, "id");
			String temporaryId = getIdFromSuccessfulCreate(success, "fullId");
			if(permanentId.equals(temporaryId))
				continue;
			else {
				provisionalTerm.setPermanentid(permanentId);
				ProvisionalTermDAO.getInstance().deleteAwaitingAdoption(provisionalTerm);
				ProvisionalTermDAO.getInstance().storeAdopted(provisionalTerm);
				result.put(temporaryId, permanentId);
			}
		}
		return result;
	}
	
	private String getIdFromSuccessfulCreate(Success createSuccess, String idName) {
		List<Object> fullIdOrIdOrLabel = createSuccess.getData().getClassBean().getFullIdOrIdOrLabel();
		for(Object object : fullIdOrIdOrLabel) {
			if(object instanceof JAXBElement) {
				JAXBElement<String> possibleIdElement = (JAXBElement<String>)object;
				if(possibleIdElement.getName().toString().equals(idName)) {
					return possibleIdElement.getValue();
				}
			}
		}
		return null;
	}
}
