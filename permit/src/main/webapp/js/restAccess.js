//return json array from restpoint
/**
 * 
 * @param url le url de restPoint
 * @returns json retourne un objet json
 * <p>Méthode qui récupère les données d'une restPoint Get requête et les renvoie dans un objet json</p>
 * <p>type GET, headers en-tête d'autorisation qui obtient le mot de passe haché de  sessionStorage.getItem avec le  BEARER,
 * async false permet de retourner une variable, dataType JSON, success retourner un json objet,</p> 
 */
function getJson(url){
	var data = {};
	$.ajax({
		url : url,
		type: 'GET',
		headers : {"Authorization" : sessionStorage.getItem('token')},
		async : false,
		contentType: 'application/json',
		dataType: 'json',
		success: function(json){
			data = json;
		},
		error : function(jQXrss, errorThrown){
			console.log("request failed: " + errorThrown);
		}
	})
	return data;
}

//Get bearer token getJson array
//TODO return headers or/and result of request header.
//function bearerToken(url){
//	$.ajax({
//		url : url,
//		type : 'GET',
//		headers : {"Authorization" : 'Bearer ' + sessionStorage.getItem('token')},
//		//or headers: { Authorization: 'Bearer '+token }
////		headers: {
////      'Authorization': 'Bearer <token>'
////   },
//		success : function(result){
//			//do stuff
//		},
//		error : function(error){
//			//do other stuff
//		}
//	});	
//}
/**
 * 
 * @param url le url de restPoint
 * @param data les donnees a envoyer
 * @returns json envoye un json objet
 * <p>Méthode qui envoye un objet json au serveur</p>
 * <p>type POST, async false permet de retourner une variable, data data les données à envoyer,
 *  dataType JSON, success message au console</p> 
 */
//TODO check if need to put authorisation header but then create different method for profile creation 
function postJson(restUrl, data){
	//var jsonObj = null;
	$.ajax({
		type: "POST",
		url: restUrl,
		async: false,
		dataType: 'json',
		contentType : 'application/json',
		data : JSON.stringify(data),
		headers: {
       "Access-Control-Allow-Origin":"*"
   },
		success: function(data, status, jQxhr){
			console.log(data);
		},
		error : function(jQXrss, data, errorThrown){
			/*		//console.log("post failed: " + errorThrown +  " " + data + " " + jQXrss);
			alert("Erreur. Le conductaire n'est pas enregistré. SVP vérifier les entrées ");*/
			alert(JSON.stringify(jQXrss.responseText));// + " Erreur. Le conductaire n'est pas enregistré. SVP vérifier les entrées ");
		}
	});
	//return jsonObj;
}

/**
 * 
 * @param url le url de restPoint
 * @param image l'image a telecharger
 * <p>Méthode qui telecharger un image au serveur</p>
 * <p>var image obtient l'image du lien, headers en-tête d'autorisation qui obtient le mot de passe haché de 
 * sessionStorage.getItem avec le  BEARER et l'email de client, type POST, data image le type d'information a envoye.
 * async false permet de retourner une variable, data data les données à envoyer,
 *  dataType</p> 
 */

function uploadImage(endpointUrl, image){
	//var image = $(inputId).get(0).item(0);
	var image = $("#uploadImg").get(0).item(0);
	$.ajax({
		//upload/{id} endpoint url
		headers : {"Authorization" : 'Bearer ' + sessionStorage.getItem('token'), "email" : sessionStorage.getItem('email')},
		type: "POST",
		url: endpointUrl, //endpoint ofc.
		dataType: 'text', ///what to expect from server response
		data : image,
		contentType: false,
		processData: false,
		success: function(serverResponse){
//        if(response != 0){
//           $("#img").attr("src",response); 
//           $(".preview img").show(); // Display image element
//        }else{
//           alert('file not uploaded');
//        }
			if(serverResponse != 0){
				console.log(serverResponse);
			}else{
				alert("file not found");
			}
		}	
	});
}

/**
 * 
 * @param url le url de restPoint
 * @returns json envoye un json objet avec l'image
 * <p>Méthode qui telecharger un image au client</p>
 * <p>type GET, xhrFields responseType blob type de réponse attendue blob,
 * async false permet de retourner une variable, data data les données à envoyer,
 * success si reponse est bon cree le url est retourne l'image sinon envoye un erreur au console</p> 
 */
function downloadImage(url){
	$.ajax({
		type : "GET",
		url : url,
		async : false,
		xhrFields : {
			responseType : "blob"
		},
		success : function(data){
			const url = window.URL || window.webkitURL;
			return url.createObjectURL(data);
		},
		error : function(jQXrss, data, errorThrown){
			console.log("fetch failed : " + errorThrown);
		}
	});
}

/**
 * 
 * @param login type json formulaire
 * <p>Méthode pour le verification de utilisateur</p>
 * <p>type POST, url le restPoint pour le login,
 * async false permet de retourner une variable,
 * data text le type de data
 * contentType envoye un formulaire urlencoded,
 * data login les données json à envoyer,
 * success si reponse est bon stocke l'en-tête et le mot de passe dans le stockage de session du client,
 * ouvrir le page du profil de conductaire. sinon ecrir un message avec erreur</p> 
 */
/*function login(login){
	$.ajax({
		type: "POST",
		url : "http://localhost:8080/permit/v1/driver/login",
		dataType: 'json',
		data : 'json',
		contentType : 'application/json',
		data: JSON.stringify(login),
		success : function(data, textStatus, jQxhr){
			//this should save the bearer token in the session storage to be able to send it back to the server for authenification requests
			const authHeader = jQxhr.getResponseHeader("Bearer");
			if(authHeader){
				sessionStorage.setItem("email", login.email);
				sessionStorage.setItem("token", authHeader);
				window.load("http://localhost:8080/permit/conductaire_profil.html", "_self");
			}						
		},
		error : function(jqXhr, textStatus, errorThrown){
			$('#cond_login').html("<p>Email ou mot de passe est pas bon.</p>");
		}	
	});
}*/
function login(form_id, email, password){
	$.ajax({
		type: "POST",
		url : "http://localhost:8080/permit/v1/driver/login",
		//dataType : 'application/x-www-form-urlencoded',
		//dataType : 'application/json',
		data : "&email=" + email + "&password=" + password,
		/*beforeSend: function(xhr){
			xhr.setRequestHeader("Authorization", "Basic " + btoa(email + ":" + password));
		},*/
		headers : {"email" : email, "password" : password},
		success : function(data, textStatus, jQxhr){
		//	$('#cond_login').html("<p>Bubble butt.</p>");
			authHeader = jQxhr.getResponseHeader("Authorization");
			//this should save the bearer token in the session storage to be able to send it back to the server for authenification requests
			const authorise = jQxhr.getResponseHeader("Allow");
			if(authorise){
				//authHeader = jQxhr.getResponseHeader("Authorization");
				sessionStorage.setItem("email", email);
				sessionStorage.setItem("token", jQxhr.getResponseHeader("Authorization"));
				window.location.replace("http://localhost:8080/permit/conductaire_profil.html", "_self");
			}						
		},
		//error : function(jqXhr, textStatus, errorThrown){
			error : function(error){
				console.log(error);
				$('#cond_login').html("<p>Email ou mot de passe n'est pas bon.</p>");
		}
	})
}

function postJsonSecure(restUrl, data){
	console.log("data is: " + JSON.stringify(data));
	//var jsonObj = null;	
	$.ajax({
		type: "POST",
		url: restUrl,
		async: false,
	//	headers : {"Authorization" : sessionStorage.getItem('token')},
		
		dataType: 'json',
		contentType : 'application/json',
		processData : false,
		data : JSON.stringify(data),
		headers: {
       "Access-Control-Allow-Origin":"*"
   },
   
		success: function(data, status, jQxhr){
			console.log(JSON.stringify(data));
		},
		beforeSend: function(xhr, settings){xhr.setRequestHeader("Authorization", sessionStorage.getItem('token'));},
		error : function(jQXrss, data, errorThrown){
			alert(JSON.stringify(jQXrss.responseText));// + " Erreur. Le conductaire n'est pas enregistré. SVP vérifier les entrées ");
			console.log("email: " + sessionStorage.getItem('email'));
			console.log("token: " + sessionStorage.getItem('token'));
			console.log(JSON.stringify(data));
			console.log("post failed: " + errorThrown +  " " + data + " " + jQXrss);
		}
	});
	//return jsonObj;
}



