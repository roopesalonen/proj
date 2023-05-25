package project;

public class Tuote {
	private int projectID;
	private String tuoteNimi;
	private int kpl;
	public Tuote() {
		super();
	}
	
	public Tuote(int projectID, String tuoteNimi, int kpl) {
		super();
		this.projectID = projectID;
		this.tuoteNimi = tuoteNimi;
		this.kpl = kpl;
	}
	
	public int getProjectID() {
		return projectID;
	}
	
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	
	public String getTuoteNimi() {
		return tuoteNimi;
	}
	
	public void setTuoteNimi(String tuoteNimi) {
		this.tuoteNimi = tuoteNimi;
	}
	
	public int getKpl() {
		return kpl;
	}
	
	public void setKpl(int kpl) {
		this.kpl = kpl;
	}

	@Override
	public String toString() {
		return "Tuote [projectID=" + projectID + ", tuoteNimi=" + tuoteNimi + ", kpl=" + kpl + "]";
	}
}