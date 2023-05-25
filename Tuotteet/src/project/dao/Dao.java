package project.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import project.Tuote;

public class Dao {
	private Connection con=null;
	private ResultSet rs = null;
	private PreparedStatement stmtPrep=null; 
	private String sql;
	private String db ="Lista.sqlite"; // database tiedosto
	
	private Connection yhdista(){
    	Connection con = null;    	
    	String path = System.getProperty("catalina.base");    	
    	path = path.substring(0, path.indexOf(".metadata")).replace("\\", "/");
    	String url = "jdbc:sqlite:"+path+db;    	
    	try {	       
    		Class.forName("org.sqlite.JDBC");
	        con = DriverManager.getConnection(url);	
	        System.out.println("Yhteys avattu.");
	     }catch (Exception e){	
	    	 System.out.println("Yhteyden avaus ep�onnistui.");
	        e.printStackTrace();	         
	     }
	     return con;
	}
	
	public ArrayList<Tuote> listaaKaikki(){
		ArrayList<Tuote> tuotteet = new ArrayList<Tuote>();
		sql = "SELECT * FROM tuotteet";       
		try {
			con=yhdista();
			if(con!=null){ // jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql);        		
        		rs = stmtPrep.executeQuery();   
				if(rs!=null){ // jos kysely onnistui									
					while(rs.next()){
						Tuote tuote = new Tuote();
						tuote.setProjectID(rs.getInt(1));
						tuote.setTuoteNimi(rs.getString(2));
						tuote.setKpl(rs.getInt(3));
						tuotteet.add(tuote);
						}					
				}				
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return tuotteet;
	}
	
	public ArrayList<Tuote> listaaKaikki(String hakusana){
		ArrayList<Tuote> tuotteet = new ArrayList<Tuote>();
		sql = "SELECT * FROM tuotteet WHERE tuoteNimi LIKE ? or kpl LIKE ?";       
		try {
			con=yhdista();
			if(con!=null){ // jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql);  
				stmtPrep.setString(1, "%" + hakusana + "%");
				stmtPrep.setString(2, "%" + hakusana + "%");   
        		rs = stmtPrep.executeQuery();   
				if(rs!=null){ // jos kysely onnistui							
					while(rs.next()){
						Tuote tuote = new Tuote();
						tuote.setProjectID(rs.getInt(1));
						tuote.setTuoteNimi(rs.getString(2));
						tuote.setKpl(rs.getInt(3));
						tuotteet.add(tuote);
					}						
				}
				con.close();
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return tuotteet;
	}
		
	public boolean lisaaTuote(Tuote tuote){
		boolean paluuArvo=true;
		sql="INSERT INTO tuotteet(tuoteNimi, kpl) VALUES(?,?)";						  
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql); 
			stmtPrep.setString(1, tuote.getTuoteNimi());
			stmtPrep.setInt(2, tuote.getKpl());
			stmtPrep.executeUpdate();
	        con.close();
		} catch (SQLException e) {				
			e.printStackTrace();
			paluuArvo=false;
		}				
		return paluuArvo;
	}
	
	public boolean poistaTuote(int projectID){
		boolean paluuArvo=true;
		sql="DELETE FROM tuotteet WHERE projectID=?";						  
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql); 
			stmtPrep.setInt(1, projectID);			
			stmtPrep.executeUpdate();
	        con.close();
		} catch (SQLException e) {				
			e.printStackTrace();
			paluuArvo=false;
		}				
		return paluuArvo;
	}	
	
	public Tuote etsiTuote(int projectID){
		Tuote tuote = null;
		sql = "SELECT * FROM tuotteet WHERE projectID=?";       
		try {
			con=yhdista();
			if(con!=null){ 
				stmtPrep = con.prepareStatement(sql); 
				stmtPrep.setInt(1, projectID);
        		rs = stmtPrep.executeQuery();  
        		if(rs.isBeforeFirst()){ // jos kysely tuotti dataa
        			tuote = new Tuote(rs.getInt("projectID"), rs.getString("tuoteNimi"), rs.getInt("kpl"));       			
				}	
        		con.close(); 
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return tuote;		
	}
	
	public boolean muutaTuote(Tuote tuote){
		boolean paluuArvo=true;
		sql="UPDATE tuotteet SET tuoteNimi=?, kpl=? WHERE projectID=?";						  
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql); 
			stmtPrep.setString(1, tuote.getTuoteNimi());
			stmtPrep.setInt(2, tuote.getKpl());
			stmtPrep.setInt(3, tuote.getProjectID());
			stmtPrep.executeUpdate();
	        con.close();
		} catch (SQLException e) {				
			e.printStackTrace();
			paluuArvo=false;
		}				
		return paluuArvo;
	}
}