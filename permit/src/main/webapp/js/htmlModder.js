//TODO create defualt image and also get create method and display profile image from the server
/**
 * 
 * <p>Methode pour la creation du profil conducteur sur la page conductaire_profil.html</p>
 * <p>Url = le restPoint de page avec l'email pris à partir de sessionStorage
 * jsonObj appel le methode getJson qui retourne un objet de type json ici c'est le profil du conductaire avec cette url.
 * profImage appel le memthode downloadImage avec le restPoint url pour telecharge l'image du conductaire.
 * mainDive creation du div avec classe profil.
 * apres en cree le profil avec les element json envoye par le db
 * il ya id l'id du conductaire, createdOn date de creation, nom nom est prenom du conductaire, email l'email du conductaire,
 * phone le numero telephone du conductaire, typePermit le type de permit du conductaire.
 * en metre les element dans l'element mainDiv de page html est metre mainDiv dans l'element body.</p>
 */





function conductaireProfil(){
	var url = "http://localhost:8080/permit/v1/driver/conductaire/" + sessionStorage.getItem("email");
	var jsonObj = getJson(url);
	if(jsonObj != null){
		var  profImage = downloadImage("http://localhost:8080/permit/v1/driver/download?email=" + jsonObj.email);
		const mainDiv = document.createElement('div');
		$(mainDiv).addClass("profil")
		mainDiv.style.textAlign = "center";
		const id = document.createElement("h4");
		id.classList.add("id");
		id.innerHTML = jsonObj.id;
		const createdOn = document.createElement("h4");
		createdOn.classList.add("createdDate");
		createdOn.innerHTML = jsonObj.createdOn;
		const nom = document.createElement("h4");
		nom.classList.add("nom");
		nom.innerHTML = "Prenom et Nom: " + jsonObj.prenom + " " + jsonObj.nom;
		const email = document.createElement("h4");
		email.classList.add("email");
		email.innerHTML = "Email: " + jsonObj.email;
		const phone = document.createElement("h4");
		phone.classList.add("phone");
		phone.innerHTML = "N Tel: " + jsonObj.numeroTelepohone;
		const typePermit = document.createElement("h4");
		typePermit.classList.add("permit");
		typePermit.innerHTML = "Type de Permit: " + jsonObj.typePermit;
		const codePasse = document.createElement("h4");
		codePasse.classList.add("code_passe");
		if(jsonObj.codePasse === true){
			codePasse.innerHTML = "Code Passe: Qui";
		}else{
			codePasse.innerHTML = "Code Passe: Non";
		}
		//codePasse.innerHTML = "Code Passe: " + jsonObj.codePasse;
		const pratPasse = document.createElement("h4");
		pratPasse.classList.add("prat_passe");
		if(jsonObj.testPractiquepasse === true){
			pratPasse.innerHTML = "Pratique Passe: Qui";
		}else{
			pratPasse.innerHTML = "Pratique Passe: Non";
		}
		//pratPasse.innerHTML = "Pratique Passe: " + jsonObj.testPractiquepasse;
		const permitPasse = document.createElement("h4");
		permitPasse.classList.add("permit_passe");
		if(jsonObj.permitpasse === true){
			permitPasse.innerHTML = "Permit Passe: Qui";
		}else{
			permitPasse.innerHTML = "Permit Passe: Non";
		}
		//permitPasse.innerHTML = "Permit Passe: " + jsonObj.permitpasse;

		mainDiv.appendChild(id);
	  mainDiv.appendChild(createdOn);
	  mainDiv.appendChild(nom);
	  mainDiv.appendChild(email);
	  mainDiv.appendChild(phone);
	  mainDiv.appendChild(typePermit);
	  mainDiv.appendChild(codePasse);
	  mainDiv.appendChild(pratPasse);
	  mainDiv.appendChild(permitPasse);
	  document.querySelector('body').appendChild(mainDiv);
	}
	
}

/**
 * 
 * <p>methode pour le creation du profil conductaire avec la page nouveau_conductaire.html puis enovye pour est registre dans le db.</p>
 * <p>var conducteur est l'objet json qui porte l'information inscrite dans le formulaire du page html pour envoye a le db.</p>
 * <p>url contient le RestPoint pour enregistrer le profil.
 * enregistrer les données du formulaire html dans l'objet json avec document.getElementById.
 * il ya prenom, nom, email, numeroTelephone, motPasse mot de passe, imgPath chemin d'image, carteIdPath chemin d'image de la carte d'identite.</p>
 * <p>Enfin en envoye a le db avec postJson()
 * 
 */
//TODO set buttons to call these methods in the html page as well as setup login
//header data
function creeConductaire(){
var conductaire = new Object();
var url = "http://localhost:8080/permit/v1/driver/create";
var dropdown = document.getElementById("typePermit");
conductaire = {
		"typePermit": dropdown.options[dropdown.selectedIndex].text,
		"prenom": document.getElementById("prenom").value,
		"nom": document.getElementById("nom").value,
		"email" : document.getElementById("email").value,
		"numeroTelepohone" : document.getElementById("numeroTelepohone").value,
		"motPasse" : document.getElementById("motPasse").value
	//	"imgPath" : document.getElementById("imgPath").value,
	//	"cartIdPath" : document.getElementById("cartIdPath").value
	};
	postJson(url, conductaire);
}

/**
 * 
 * <p>Methode pour la creation d'un examin de code de la route.</p>
 * <p>
 * 	Creation d'objet json code_exam, url le restPoint pour un nouveau code.
 *  Ajouter le note d'examin est l'email de conductaire a l'objet prat_exam avec l'information 
 *  registre dans le formulaire du page code_exam.html
 *  envoye a le db avec postJson avec le url est code_exam.
 * </p>
 */
function creeCode_exam(){
	var code_exam = new Object();
	var url = "http://localhost:8080/permit/v1/codeexam/new";
	code_exam = {
			"note": document.getElementById("code_note").value,
			"conductaire": document.getElementById("emailCond").value
	};
	postJsonSecure(url, code_exam);
}

/**
 * 
 * <p>Methode pour la creation d'un examin pratique.</p>
 * <p>
 * 	Creation d'objet json prat_exam, url le restPoint pour un nouveau examin.
 *  Ajouter le note d'examin "note" est l'email de conductaire "conductaire" a l'objet prat_exam avec l'information 
 *  registre dans le formulaire du page pratique_exam.html
 *  envoye a le db avec postJson avec le url est prat_exam.
 * </p>
 */

/*function creePrat_exam(){
	var prat_exam = new Object();
	var url = "http://localhost:8080/permit/v1/exampratique/new";
	prat_exam = {
			"note": parseInt(document.getElementById("prat_note").value),
			"conductaire": document.getElementById("emailCond").value
	};
	postJsonSecure(url, prat_exam);
}*/