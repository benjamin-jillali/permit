package entity;



import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import enums.TypePermitEnum;
/**
 * 
 * @author osbot
 * @version 0.0.1
 * <h5>Entity Driver </h5>
 * <p>c'est l'entité pour enregistrer les donnees pour un conducteur qui voulez passez son permit</p>
 * <p>Les requêtes SQL sont les suivantes: </p>
 * <p>FIND_ALL_DRIVERS cherche un liste des tous les conducteurs</p>
 * <p>FIND_DRIVER_BY_ID cherche une conducteurs par son identifiant</p>
 * <p>FIND_DRIVER_BY_EMAIL cherche une conducteurs par son email</p>
 * <p>FIND_DRIVER_PERMIT cherche cherche le type de permis du conducteur par son identifiant</p>
 * <p>GET_DRIVER_PASSWORD retourne le mot de passe haché de conductaire</p>
 */
@Entity
@NamedQuery(name = Driver.FIND_ALL_DRIVERS, query = "Select u from Driver u order by u.nom")
@NamedQuery(name = Driver.FIND_DRIVER_BY_ID, query = "Select u from Driver u where u.id = :id")
@NamedQuery(name = Driver.FIND_DRIVER_BY_EMAIL, query = "Select u from Driver u where u.email = :email")
@NamedQuery(name = Driver.FIND_DRIVER_PERMIT, query = "Select u.typePermit from Driver u where u.id = :id")
@NamedQuery(name = Driver.GET_DRIVER_PASSWORD, query = "Select u.motPasse from Driver u where u.motPasse = :motPasse")


public class Driver extends AbstractElementsEntity{
	//TODO add dates for tests and boolean passed
	
	private boolean permitpasse = false;
	private boolean codePasse = false;
	private boolean testPractiquepasse = false;
	/**
	 * <p>les constantes des requêtes <p>
	 */	
	public static final String FIND_ALL_DRIVERS = "Driver.findAllDrivers";
	public static final String FIND_DRIVER_BY_ID = "Driver.findDriverById";
	public static final String FIND_DRIVER_BY_EMAIL = "Driver.findDriverByEmail";
	public static final String FIND_DRIVER_PERMIT = "Driver.findDriverPermit";
	public static final String GET_DRIVER_PASSWORD = "Driver.GetDriverMotPasse";

	
	
	@Size(min = 2, max = 40, message = "Prenom doit etre pas vide.")
	@NotNull(message = "Prénom non renseigné!")
	private String prenom;
	@NotNull(message = "Nom non renseigné!")
	@Size(min = 2, max = 40, message = "Nom doit etre pas vide.")
	private String nom;
	@NotNull(message = "Email non renseigné!")
	private String email;
	private String numeroTelepohone;
	//TODO get regexp to work for password
	//private static final String regexpression = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=\\\\S+$).{8,200}$";
	//@Size(min = 8, message = "Mot de passe doit être d'au moins 8 caractères!")	
	//@Pattern(regexp = "^(?=.*[0-9]).{8,15}$", message = "le mot de passe doit contenir au moins au moins 1 numero, 1 symole 1 lettre majiscule 1 letre miniscule, pas d'espace")
	//@Pattern(regexp="^[a-zA-Z0-9]{3}",message="length must be 3")  
	@NotNull(message = "Mot de passe non renseigné!")
	@Size(min = 8)
	private String motPasse;
	@Lob
	private byte[] photo;
	//@NotNull(message = "Carte d'identite non renseigné!")
	@Lob
	private byte[] carteId;
	
	private String salt;
	
	@NotNull(message = "Type Permit non selectionnes")
	@Enumerated(EnumType.STRING)
	private TypePermitEnum typePermit;	
	
	//@Column(columnDefinition = "varchar(255) default /home/osbot/eclipse-workspace/permit/images/default.png")
	@Column(columnDefinition = "varchar(255)")
	
	private String imgPath;
	
	private String cartIdPath;
	


	/**
	 * @return le typePermit
	 */
	public TypePermitEnum getTypePermit() {
		return typePermit;
	}

	/**
	 * @param typePermit le type de permit pour le metre
	 *<p>Le type de permis(B, B1, BE, A1, A2) est un énumérateur trouve dans la classe TypePermitEnum</p>
	 */
	public void setTypePermit(TypePermitEnum typePermit) {
		this.typePermit = typePermit;
	}

	/**
	 * @return le boolean permitpasse
	 */
	public boolean isPermitpasse() {
		return permitpasse;
	}

	/**
	 * @param permitpass boolean pour voir si le permis est passe ou pas
	 */
	public void setPermitpasse(boolean permitpasse) {
		this.permitpasse = permitpasse;
	}

	/**
	 * @return le prenom
	 */
	public String getPrenom() {
		return prenom;
	}
	/**
	 * @param prenom le prénom à mettre 
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	/**
	 * @return le nom
	 */
	public String getNom() {
		return nom;
	}
	/**
	 * @param nom le nom à mettre 
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	/**
	 * @return l'email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email l'email à mettre 
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return le numeroTelepohone
	 */
	public String getNumeroTelepohone() {
		return numeroTelepohone;
	}
	/**
	 * @param numeroTelepohone le numeroTelepohone à mettre
	 */
	public void setNumeroTelepohone(String numeroTelepohone) {
		this.numeroTelepohone = numeroTelepohone;
	}
	/**
	 * @return le dateRegistre
	 */
	/**
	 * @param dateRegistre le date de registration
	 */
	/**
	 * @return le motPasse
	 */
	public String getMotPasse() {
		return motPasse;
	}
	/**
	 * @param motPasse le motPasse haché a mettre
	 */
	public void setMotPasse(String motPasse) {
		this.motPasse = motPasse;
	}
	/**
	 * @return le imgPath
	 */
	public String getImgPath() {
		return imgPath;
	}

	/**
	 * @param imgPath le chemin d'image
	 */
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	/**
	 * @return le cartIdPath
	 * @deprecated
	 */
	public String getCartIdPath() {
		return cartIdPath;
	}

	/**
	 * @param cartIdPath le chemin de l'image pour la carte d'identité 
	 * @deprecated
	 */
	public void setCartIdPath(String cartIdPath) {
		this.cartIdPath = cartIdPath;
	}
	//méthode pour convertir un lien d'image a un LOB avec le parametre imgPath ca retourne un LOB
	private byte[] pathToLobImage(String imgPath) {
		//creation d'un nouveu fichier avec l'image
		File image = new File(imgPath);
		try {
			//creation d'un tableau d'octets la taille d'image
			byte[] photo = new byte[(int) image.length()];
			FileInputStream inputStream = new FileInputStream(image);
			//lissez le photo et fermes l'inputSyream
			inputStream.read(photo);
			inputStream.close();			
		}catch(Exception e) {
			e.printStackTrace();
		}
		//retourne le photo.
		return photo;
	}
	/**
	 * @return le photo
	 */
	public byte[] getPhoto() {
		return photo;
	}
	/**
	 * @param photo le photo a mettre
	 */
	public void setPhoto(String imgPath) {	
		this.photo =  pathToLobImage(imgPath);
	}
	/**
	 * @return le carteId
	 */
	public byte[] getCarteId() {
		return carteId;
	}
	/**
	 * @param carteId l'image pour la carte d'identité 
	 */
	public void setCarteId(String imgPath) {
		this.carteId =  pathToLobImage(imgPath);
	}

	/**
	 * @return le codePasse
	 */
	public boolean isCodePasse() {
		return codePasse;
	}

	/**
	 * @param codePasse un boolean pour savoir si le code est passe ou pas
	 */
	public void setCodePasse(boolean codePasse) {
		this.codePasse = codePasse;
	}

	/**
	 * @return le testPractiquepasse
	 */
	public boolean isTestPractiquepasse() {
		return testPractiquepasse;
	}

	/**
	 * @param testPractiquepasse un boolean pour savoir si le test pratique est passe ou pas
	 */
	public void setTestPractiquepasse(boolean testPractiquepasse) {
		this.testPractiquepasse = testPractiquepasse;
	}

	/**
	 * @return salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * @param salt le salt pour le token a mettre
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	
	

}
