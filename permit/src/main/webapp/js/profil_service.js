//profil du conductaire

/**
 * 
 * <p>Obtient le profil du conducteur </p>
 * <p>Lorsque le bouton #cond_profil est appelé appellez la méthode conductaireProfil qui est dans le fichier htmlModder.js</p>
 * 
 */
$(document).ready(function() {
	 $("#cond_profil").click(function(event){
//			jsonObj = getJson();
//			//console.log(jsonObj.sex);
//
//			$('#cond_profil').html('<p>Name: ' + jsonObj.name + '</p>');
//			$('#cond_profil').append('<p>Age: ' + jsonObj.age + '</p>');
//			$('#cond_profil').append('<p>Sex: ' + jsonObj.sex + '</p>');
		 conductaireProfil();
	 });
});

/**
 * <p>Connexion au profil </p>
 * <p>Lorsque le bouton #login est appelé récupère email est mot de passe à partir des champs du formulaire
 * 		et registre dans var log puis envoye dans le method login du fichier restAccess.js</p>
 * 
 */
$(document).ready(function(){
	$("#login").click(function(event){
		var email = document.getElementById("email").value;
		var password = document.getElementById("password").value;
		
		console.log("e " + email + " " +" p "+ password);
/*		var log = {
				"email" : document.getElementById("email").value,
				"password" : document.getElementById("password").value
		};*/
		login("", email, password);
	});
});

/**
 * 
 * <p>Creation du profil d'un conducteur </p>
 * <p>Lorsque le bouton #cree_conductaire est appelé appelez la méthode creeConductaire qui est dans le fichier htmlModder.js</p>
 * 
 */
$(document).ready(function() {
	 $("#cree_conductaire").click(function(event){
		 creeConductaire();
	 });
});
/**
 * 
 * <p>Télécharger une image  </p>
 * <p>Lorsque le bouton #uploadImg est appelé cree un objet image du type FormData est recupere l'image par son chemin est stocke dans var files.
 * si files.length > 0 ajouter l'image à var image est appel puis appelez la méthode uploadImage avec 
 * le restPoint("http://localhost:8080/permit/permit/v1/driver/upload?email=" + email de l'utilisateur) est l'image.
 * uploadImage est dans le fichier restAccess.js </p>
 */
$(document).ready(function(){
	$("#uploadImg").click(function(event){
		var image = new FormData();
		var files = $("#imgPath")[0].files;
		if(files.length > 0){
			image.append('file', files[0]);
			uploadImage("http://localhost:8080/permit/v1/driver/upload?email=" + sessionStorage.getItem("email"), image);			
		}
	});
});

/**
 * 
 * <p>Registre le resultat d'un examin du code de la route</p>
 * <p>Lorsque le bouton #code_ex_cree est appelé appelez la méthode creeCode_exam qui est dans le fichier htmlModder.js</p>
 */
$(document).ready(function() {
	 $("#code_ex_cree").click(function(event){
		 creeCode_exam();
	 });
});

/**
 * 
 * <p>Registre le resultat d'un examin pratique</p>
 * <p>Lorsque le bouton #prat_ex_cree est appelé appelez la méthode creePrat_exam qui est dans le fichier htmlModder.js</p>
 */
$(document).ready(function() {
	 $("#prat_ex_cree").click(function(event){
		// creePrat_exam();
	 });
});

///**
// * 
// * <p>Afficher le profil du conducteur </p>
// * <p>Lorsque le bouton #conductaireProfil est appelé appelez la méthode conductaireProfil qui est dans le fichier htmlModder.js</p>
// */
////creation de profil conductaire
//$(document).ready(function() {
//	 $("#cond_profil").click(function(event){
////			jsonObj = getJson();
////			//console.log(jsonObj.sex);
////
////			$('#cond_profil').html('<p>Name: ' + jsonObj.name + '</p>');
////			$('#cond_profil').append('<p>Age: ' + jsonObj.age + '</p>');
////			$('#cond_profil').append('<p>Sex: ' + jsonObj.sex + '</p>');
//		 conductaireProfil();
//	 });
//});







