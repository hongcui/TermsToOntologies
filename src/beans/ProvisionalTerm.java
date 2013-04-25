package beans;

public class ProvisionalTerm {

	private String temporaryid;
	private String permanentid;
	private String superclass;
	private String submittedby;
	private String definition;
	private String ontologyids;
	private String preferredname;
	private String synonyms;
	
	public ProvisionalTerm() { }
	
	public ProvisionalTerm(String temporaryid, String permanentid, String superclass,
			String submittedby, String definition, String ontologyids,
			String preferredname, String synonyms) {
		this.temporaryid = temporaryid;
		this.permanentid = permanentid;
		this.superclass = superclass;
		this.submittedby = submittedby;
		this.definition = definition;
		this.ontologyids = ontologyids;
		this.preferredname = preferredname;
		this.synonyms = synonyms;
	}

	public String getPermanentid() {
		return permanentid;
	}

	public void setPermanentid(String permanentid) {
		this.permanentid = permanentid;
	}

	public String getSuperclass() {
		return superclass;
	}

	public void setSuperclass(String superclass) {
		this.superclass = superclass;
	}

	public String getSubmittedby() {
		return submittedby;
	}

	public void setSubmittedby(String submittedby) {
		this.submittedby = submittedby;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getOntologyids() {
		return ontologyids;
	}

	public void setOntologyids(String ontologyids) {
		this.ontologyids = ontologyids;
	}

	public String getPreferredname() {
		return preferredname;
	}

	public void setPreferredname(String preferredname) {
		this.preferredname = preferredname;
	}
	
	public boolean hasPermanentId() {
		return this.permanentid != null;
	}
	
	public boolean hasSuperClass() {
		return this.superclass != null;
	}
	
	public boolean hasSubmittedBy() {
		return this.submittedby != null;
	}
	
	public boolean hasDefinition() {
		return this.definition != null;
	}
	
	public boolean hasOntologyIds() {
		return this.ontologyids != null;
	}
	
	public boolean hasPreferredName() {
		return this.preferredname != null;
	}

	public String getSynonyms() {
		return this.synonyms;
	}
	
	public boolean hasSynonyms() {
		return this.synonyms != null;
	}
	
	public boolean hasTemporaryId() {
		return this.temporaryid != null;
	}

	public String getTemporaryid() {
		return temporaryid;
	}

	public void setTemporaryid(String temporaryid) {
		this.temporaryid = temporaryid;
	}

	public void setSynonyms(String synonyms) {
		this.synonyms = synonyms;
	}
	
	
}
