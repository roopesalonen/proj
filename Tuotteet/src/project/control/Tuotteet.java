package project.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import project.Tuote;
import project.dao.Dao;

@WebServlet("/tuotteet/*")
public class Tuotteet extends HttpServlet {
	private static final long serialVersionUID = 1L;       
   
    public Tuotteet() {    	
        super();     
        System.out.println("Tuotteet.Tuotteet()");
    }
    
    // haetaan tuotteet GET-metodilla  /tuotteet
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Tuotteet.doGet()");
		String pathInfo = request.getPathInfo(); // haetaan polkutiedot		
		System.out.println("polku: "+pathInfo);		
		String strJSON="";
		ArrayList<Tuote> tuotteet;
		Dao dao = new Dao();
		if(pathInfo==null) { // haetaan kaikki tuotteet
			tuotteet = dao.listaaKaikki();			
			strJSON = new JSONObject().put("tuotteet", tuotteet).toString();	
		}else if(pathInfo.indexOf("haeyksi")!=-1) {		// haetaan yksi tuote
			int projectID = Integer.parseInt(pathInfo.replace("/haeyksi/", ""));
			Tuote tuote = dao.etsiTuote(projectID);
			if(tuote==null){ // jos tuotetta ei löydy, palautetaan tyhjä arvo
				strJSON = "{}";
			}else{	
				JSONObject JSON = new JSONObject();
				JSON.put("projectID", tuote.getProjectID());
				JSON.put("tuoteNimi", tuote.getTuoteNimi());
				JSON.put("kpl", tuote.getKpl());	
				strJSON = JSON.toString();
			}			
		}else{ // haetaan tuote hakusanan perusteella
			String hakusana = pathInfo.replace("/", "");	
			tuotteet = dao.listaaKaikki(hakusana);			
			strJSON = new JSONObject().put("tuotteet", tuotteet).toString();				
		}	
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println(strJSON);		
	}

	// lisätään tuote POST-metodilla  /tuotteet
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Tuotteet.doPost()");
		JSONObject jsonObj = new JsonStrToObj().convert(request); // json-string -> json-objekti
		Tuote tuote = new Tuote();		
		tuote.setTuoteNimi(jsonObj.getString("tuoteNimi"));
		tuote.setKpl(jsonObj.getInt("kpl"));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();			
		if(dao.lisaaTuote(tuote)){
			out.println("{\"response\":1}");  // response 1 = tuotteen lisäys onnistui
		}else{
			out.println("{\"response\":0}");  // response 0 = tuotteen lisäys epäonnistui
		}		
	}
	
	// tuotteen tietojen muuttaminen PUT-metodilla /tuotteet
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Tuotteet.doPut()");		
		JSONObject jsonObj = new JsonStrToObj().convert(request); // json-string -> json-objekti			
		Tuote tuote = new Tuote();	
		tuote.setProjectID(Integer.parseInt(jsonObj.getString("projectID"))); // projectID muutetaan String -> Int
		tuote.setTuoteNimi(jsonObj.getString("tuoteNimi"));
		tuote.setKpl(jsonObj.getInt("kpl"));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();			
		if(dao.muutaTuote(tuote)) {
			out.println("{\"response\":1}"); // response 1 = tuotteen muuttaminen onnistui
		}else {
			out.println("{\"response\":0}"); // response 0 = tuotteen muuttaminen epäonnistui
		} 		
	}

	// poistetaan tiedot DELETE-metodilla  /tuotteet/id 	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Tuotteet.doDelete()");
		String pathInfo = request.getPathInfo();	// haetaan polkutiedot
		int projectID = Integer.parseInt(pathInfo.replace("/", "")); // poistetaan "/", jäljellä id joka muutetaan int
		Dao dao = new Dao();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();		    
		if(dao.poistaTuote(projectID)){
			out.println("{\"response\":1}"); // response 1 = tuotteen poistaminen onnistui
		}else {
			out.println("{\"response\":0}"); // response 0 = tuotteen poistaminen epäonnistui
		}		
	}
}
