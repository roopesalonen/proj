<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="scripts/main.js"></script>
<link rel="stylesheet" type="text/css" href="css/main.css">
<title>Tuotteen muuttaminen</title>
</head>
<body onkeydown="tutkiKey(event)">
<form id="tiedot">
	<table class="table">
		<thead>
			<tr>
				<th colspan="2">Tuotteen muuttaminen</th>
				<th><a href="listaaTuote.jsp" id="takaisin">Takaisin ostoslistaan</a></th>
			</tr>
			<tr>
				<th>Tuote</th>
				<th>Kpl</th>	
				<th></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="keskelle"><input class="keskelle" name="tuoteNimi" id="tuoteNimi"></td>
				<td class="keskelle"><input class="keskelle" name="kpl" id="kpl"></td>		
				<td class="keskelle"><input type="button" name="nappi" value="Muuta" id="tallenna" onclick="vieTiedot()"></td>
			</tr>
		</tbody>
	</table>
	<input type="hidden" name="projectID" id="projectID">
</form>
<p class="keskelle" id="notif"></p>
<script>

function tutkiKey(event){
	if(event.keyCode==13){ // 13 = enter
		vieTiedot();
	}		
}

// tiedot GET-metodilla /tuotteet/haeyksi/id
var projectID = requestURLParam("projectID"); // funktio löytyy scripts/main.js 
fetch("tuotteet/haeyksi/" + projectID,{
      method: 'GET'	      
    })
.then( function (response) {
	return response.json()
})
.then( function (responseJson) {	
	document.getElementById("tuoteNimi").value = responseJson.tuoteNimi;		
	document.getElementById("kpl").value = responseJson.kpl;	
	document.getElementById("projectID").value = responseJson.projectID;	
});	

// tiedot POST-metodilla /tuotteet/
function vieTiedot(){
	var tuoteNimi = document.getElementById("tuoteNimi").value;
	var kpl = document.getElementById("kpl").value;
	if(String(tuoteNimi).length<2 || String(kpl).match("[^0-9]+")){ // tarkistetaan että nimi on yli 2 merkkiä ja kpl sisältää vain numeroita
		document.getElementById("notif").innerHTML = "Antamasi arvot eivät kelpaa!"
		return;
	}
	var formJsonStr=formDataToJSON(document.getElementById("tiedot")); // tiedot json-stringiksi
	// tiedot backendiin
	fetch("tuotteet",{
	      method: 'PUT',
	      body:formJsonStr
	    })
	.then( function (response) {
		return response.json();
	})
	.then( function (responseJson) {
		var vastaus = responseJson.response;		
		if(vastaus==0){
			document.getElementById("notif").innerHTML= "Tuotteen muuttaminen epäonnistui";
        }else if(vastaus==1){	        	
        	document.getElementById("notif").innerHTML= "Tuotteen muuttaminen onnistui";			      	
		}	
	});	
}
</script>
</body>
</html>