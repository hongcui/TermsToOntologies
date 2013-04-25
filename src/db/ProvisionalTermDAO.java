package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.ProvisionalTerm;

public class ProvisionalTermDAO extends AbstractDAO {

	private static ProvisionalTermDAO instance;

	private ProvisionalTermDAO() throws Exception { }
	
	public static ProvisionalTermDAO getInstance() throws Exception {
		if(instance == null)
			instance = new ProvisionalTermDAO();
		return instance;
	}

	public void addAwaitingAdoption(ProvisionalTerm provisionalTerm) throws SQLException {
		this.openConnection();
		createTableIfNecessary("awaitingadoption");
		this.storeProvisionalTerm("awaitingadoption", provisionalTerm);		
		this.closeConnection();
	}

	public List<ProvisionalTerm> getAllAwaitingAdoption() throws SQLException {
		this.openConnection();
		List<ProvisionalTerm> result =  this.getAll("awaitingAdoption");
		this.closeConnection();
		return result;
	}

	public void storeAdopted(ProvisionalTerm provisionalTerm) throws SQLException {
		this.openConnection();
		createTableIfNecessary("adopted");
		storeProvisionalTerm("adopted", provisionalTerm);
		this.closeConnection();
	}
	
	public void deleteAwaitingAdoption(ProvisionalTerm provisionalTerm) throws SQLException {
		this.openConnection();
		this.executeSQL("DELETE FROM awaitingadoption WHERE temporaryId='" + provisionalTerm.getTemporaryid() + "'");
		this.closeConnection();
	}
	
	private void storeProvisionalTerm(String tableName, ProvisionalTerm provisionalTerm) throws SQLException {
		PreparedStatement preparedStatement = this.prepareStatement("INSERT INTO " + tableName + " (`temporaryId`, `permanentId`, `superClass`, " +
				"`submittedby`, `definition`, `ontologyids`, `preferredname`, `synonyms`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		preparedStatement.setString(1, provisionalTerm.getTemporaryid());
		preparedStatement.setString(2, provisionalTerm.getPermanentid());
		preparedStatement.setString(3, provisionalTerm.getSuperclass());
		preparedStatement.setString(4, provisionalTerm.getSubmittedby());
		preparedStatement.setString(5, provisionalTerm.getDefinition());
		preparedStatement.setString(6, provisionalTerm.getOntologyids());
		preparedStatement.setString(7, provisionalTerm.getPreferredname());
		preparedStatement.setString(8, provisionalTerm.getSynonyms());
		preparedStatement.executeUpdate();
	}
	
	private void createTableIfNecessary(String tableName) throws SQLException {
		this.executeSQL("CREATE TABLE IF NOT EXISTS " + tableName + "(" +
				" `temporaryId` varchar(100) NOT NULL, " +
				"  `permanentId` varchar(100) DEFAULT NULL, " +
				" `superClass` varchar(100) DEFAULT NULL, " +
				"  `submittedBy` varchar(100) DEFAULT NULL, " +
				"  `definition` text, " +
				"  `ontologyIds` varchar(100) DEFAULT NULL, " +
				"  `preferredName` varchar(100) DEFAULT NULL, " +
				"  `synonyms` varchar(100) DEFAULT NULL, " +
				"  PRIMARY KEY (`temporaryId`))");
	}
	
	public List<ProvisionalTerm> getAll(String tableName) throws SQLException {
		List<ProvisionalTerm> result = new ArrayList<ProvisionalTerm>();
		PreparedStatement preparedStatement = this.executeSQL("SELECT * FROM " + tableName);
		ResultSet resultSet = preparedStatement.getResultSet();
		while(resultSet.next()) {
			result.add(new ProvisionalTerm(resultSet.getString("temporaryId"), 
					resultSet.getString("permanentId"), 
					resultSet.getString("superClass"),
					resultSet.getString("submittedBy"), 
					resultSet.getString("definition"), 
					resultSet.getString("ontologyIds"), 
					resultSet.getString("preferredName"), 
					resultSet.getString("synonyms")
					));
		}
		return result;
	}

	
	public static void main(String[] args) {
		ProvisionalTerm provisionalTerm = new ProvisionalTerm();
		provisionalTerm.setTemporaryid("tempId");
		provisionalTerm.setDefinition("def");
		provisionalTerm.setPreferredname("name");
		provisionalTerm.setSubmittedby("submittedby");
		try {
			ProvisionalTermDAO.getInstance().addAwaitingAdoption(provisionalTerm);
			List<ProvisionalTerm> all = ProvisionalTermDAO.getInstance().getAllAwaitingAdoption();
			ProvisionalTermDAO.getInstance().storeAdopted(provisionalTerm);
			ProvisionalTermDAO.getInstance().deleteAwaitingAdoption(provisionalTerm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
