<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/main.css">

<title>Ostoslista</title>
</head>
<body onkeydown="tutkiKey(event)">
	<span class="keskelle" id="notif"></span>
	<table id="listaus">
		<thead>	
			<tr>
			<th colspan="3" class="keskelle">Ostoslista</th>
			</tr>
		
			<tr>
				<th class="keskelle"><a id="uusiTuote" href="lisaaTuote.jsp">Lis‰‰ tuote</a></th>
				<th colspan="3" class="keskelle">Hae tuotetta:
				<input type="text" id="hakusana">
				<input type="button" id="hae" value="Hae" onclick="haeTiedot()"></th>
			</tr>		
			<tr>
				<th class="keskelle">Asetukset</th>
				<th class="keskelle">Tuote</th>
				<th>Kpl</th>			
			</tr>
		</thead>
		<tbody id="tbody">
		</tbody>
	</table>
	
<script>
haeTiedot();	

function tutkiKey(event){
	if(event.keyCode==13){ // 13 = enter
		haeTiedot();
	}		
}

// tiedot GET-metodilla /tuotteet/{hakusana}
function haeTiedot(){	
	document.getElementById("tbody").innerHTML = "";
	fetch("tuotteet/" + document.getElementById("hakusana").value,{
	      method: 'GET'	      
	    })
	.then( function (response) {
		return response.json()
	})
	.then( function (responseJson) {
		var tuotteet = responseJson.tuotteet;	
		console.log(tuotteet);
		var htmlStr="";
		for(var i=0;i<tuotteet.length;i++){			
        	htmlStr+="<tr class='keskelle'>";
        	htmlStr+="<td class='muuta'><a href='muutaTuote.jsp?projectID="+tuotteet[i].projectID+"'>Muuta</a>";
        	htmlStr+="<i class=''>&nbsp;</i>";
        	htmlStr+="<span class='poista' onclick=poista("+tuotteet[i].projectID+",'"+tuotteet[i].tuoteNimi+"','"+tuotteet[i].kpl+"')>Poista</span></td>";
        	htmlStr+="<td class='keskelle'>"+tuotteet[i].tuoteNimi+"</td>";
        	htmlStr+="<td>"+tuotteet[i].kpl+"</td>";
        	htmlStr+="</tr>";        	
		}
		document.getElementById("tbody").innerHTML = htmlStr;		
	})	
}

// poistetaan tiedot DELETE metodilla /tuotteet/id
function poista(projectID, tuoteNimi, kpl){
	if(confirm("Poista tuote " + tuoteNimi +"?")){	
		fetch("tuotteet/" + projectID,{
		      method: 'DELETE'	      
		    })
		.then( function (response) {
			return response.json()
		})
		.then( function (responseJson) {
			var vastaus = responseJson.response;		
			if(vastaus==0){
				document.getElementById("notif").innerHTML= "Tuotteen poisto ep‰onnistui.";
	        }else if(vastaus==1){	        	
	        	alert("Tuotteen " + tuoteNimi + " poisto onnistui.");
				haeTiedot();        	
			}	
		})		
	}	
}
</script>
</body>
</html>