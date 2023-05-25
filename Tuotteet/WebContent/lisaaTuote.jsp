<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="scripts/main.js"></script>
<link rel="stylesheet" type="text/css" href="css/main.css">
<title>Tuotteen lis‰‰minen</title>
</head>
<body onkeydown="tutkiKey(event)">
<form id="tiedot">
<table>
	<thead>
		<tr>
			<th colspan="2">Tuotteen lis‰‰minen</th>
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
			<td class="keskelle"><input class="keskelle" type="text" name="tuoteNimi" id="tuoteNimi"></td>
			<td class="keskelle"><input class="keskelle" type="text" name="kpl" id="kpl"></td>
			<td class="keskelle"><input type="button" name="nappi" id="tallenna" value="Lis‰‰" onclick="lisaaTiedot()"></td>
		</tr>
	</tbody>
</table>
</form>
<p class="keskelle" id="notif"></p>
<script>

function tutkiKey(event){
	if(event.keyCode==13){ // 13 = enter
		lisaaTiedot();
	}		
}

// tiedot POST-metodilla /tuotteet/
function lisaaTiedot(){	
	var tuoteNimi = document.getElementById("tuoteNimi").value;
	var kpl = document.getElementById("kpl").value;
	if(String(tuoteNimi).length<2 || String(kpl).match("[^0-9]+")){ // tarkistetaan ett‰ nimi on yli 2 merkki‰ ja kpl sis‰lt‰‰ vain numeroita
		document.getElementById("notif").innerHTML = "Antamasi arvot eiv‰t kelpaa!"
		return;
	}
	var formJsonStr=formDataToJSON(document.getElementById("tiedot")); // tiedot json-stringiksi
	// tiedot backendiin
	fetch("tuotteet",{
	      method: 'POST',
	      body:formJsonStr
	    })
	.then( function (response) {
		return response.json()
	})
	.then( function (responseJson) {
		var vastaus = responseJson.response;		
		if(vastaus==0){
			document.getElementById("notif").innerHTML= "Tuotteen lis‰‰minen ep‰onnistui";
        }else if(vastaus==1){	        	
        	document.getElementById("notif").innerHTML= "Tuotteen lis‰‰minen onnistui";			      	
		}	
	});		
}
</script>
</body>
</html>