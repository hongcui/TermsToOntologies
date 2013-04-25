package db;

import java.util.List;

import beans.ProvisionalTerm;

public class ProvisionalTermDAO {

	private static ProvisionalTermDAO instance;

	private ProvisionalTermDAO() { }
	
	public static ProvisionalTermDAO getInstance() {
		if(instance == null)
			instance = new ProvisionalTermDAO();
		return instance;
	}

	public void add(ProvisionalTerm provisionalTerm) {
		// TODO Auto-generated method stub
		
	}

	public List<ProvisionalTerm> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
