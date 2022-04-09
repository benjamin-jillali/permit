package service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.util.ByteSource;

import entity.Driver;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
/**
 * 
 * @author osbot
 * <p>classe pour fournir des outils de sécurité </p>
 * <p>ApplicationScoped</p>
 */
@ApplicationScoped
public class SecurityUtil {
	@Inject
	QueryService queryService;
	
	@Inject 
	EntityManager entityManager;
	
	public static final String HASHED_PASSWORD_KEY = "hashedPassword";
	public static final String SALT_KEY = "salt";
	public static final String BEARER = "Bearer";
	
	
	
	private SecretKey securityKey;
	
	// initialisation 
	@PostConstruct
	private void init() {
		securityKey = generateKey();
	}
	/**
	 * 
	 * @param localDateTime
	 * @return Date
	 * <p>crée la date actuelle </p>
	 */
	public Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	/**
	 * 
	 * @param dbStoredHashedPassword //le mot de passe haché stocke dans le db
	 * @param saltText le Salt de hache
	 * @param plainTextPassword le mot de passe saisi par l'utilisateur 
	 * @return boolean passHache = dbPass
	 * <p>méthode pour vérifier si le mot de passe envoyé par l'utilisateur correspond au mot de passe dans la base de données</p>
	 * <p>créer une source d'octets à partir du saltText.
	 * 	 hacher le mot de passe de l'utilisateur avec le Salt.
	 * vérifier si le mot de passe haché correspond à celui dans la db et renvoyer les résultats.
	 * </p>
	 */
	public boolean passwordMatch(String dbStoredHashedPassword, String saltText, String plainTextPassword) {
		ByteSource salt = ByteSource.Util.bytes(Hex.decode(saltText));
		String hashedPassword = hashAndSaltPassword(plainTextPassword, salt);
		return hashedPassword.equals(dbStoredHashedPassword);
	}
	
	/**
	 * 
	 * @param plainTextPassword //le mot de passe saisi par l'utilisateur 
	 * @return credMap Map<String, String>
	 * <p>méthode pour hacher un mot de passe en texte brut </p>
	 * <p>obtenir le Salt puis créez une nouvelle HashMap. 
	 * Mettre la clé de mot de passe et le mot de passe haché dans le HashedMap 
	 * mettre le cle est le salt.toHex dans le HashedMap est retourne le HashedMap</p>
	 */
	public Map<String, String> hashPassword(String plainTextPassword){
		ByteSource salt = getSalt();
		Map<String, String> credMap = new HashedMap();
		if(regExpPassword(plainTextPassword)) {
			credMap.put(HASHED_PASSWORD_KEY, hashAndSaltPassword(plainTextPassword, salt));
			credMap.put(SALT_KEY, salt.toHex());
			return credMap;
		}		
		return null;
	}
	
	/**
	 * 	
	 * @param plainTextPassword
	 * @param salt
	 * @return String
	 * <p>méthode pour créer un mot de passe haché et ajouter le Salt.</p>
	 * <p> Utilisons l'algorithme Sha512Hash avec 2000000 itérations et convertissons en hexadécimal </p>
	 */
	private String hashAndSaltPassword(String plainTextPassword, ByteSource salt) {
		return new Sha512Hash(plainTextPassword, salt, 2000000).toHex();
	}
	
	/**
	 * 
	 * @return ByteSource
	 * <p>Retourne un Salt généré aléatoirement avec SecureRandomNumberGenerator</p>
	 */
	private ByteSource getSalt() {
		return new SecureRandomNumberGenerator().nextBytes();
	}
	
	/**
	 * 
	 * @param email
	 * @param password
	 * @return boolean
	 * <p>Méthode pour authentifier l'utilisateur. </p>
	 * <p>obtient l'utilisateur par e-mail. si l'utilisateur 
	 * existe vérifier si le mot de passe correspond à celui stocké dans la base de données est retourne vrai ou faux.</p>
	 */
	public boolean authenticateUser(String email, String password) {
		Driver driver = null;
		List<Driver> driverList = entityManager.createNamedQuery(Driver.FIND_DRIVER_BY_EMAIL, Driver.class)
																.setParameter("email", email).getResultList();
		
		//driver = queryService.findDriverByEmail(email);
		if(!driverList.isEmpty()) {
			driver = driverList.get(0);
		}
		if(driver == null) {
			return false;
		}
		return passwordMatch(driver.getMotPasse(), driver.getSalt(), password);
	}
	/**
	 * 
	 * @return SecretKey
	 * <p>méthode pour générer une clé sécurisée aléatoire avec l'algorithme HS512.</p>
	 */
	private SecretKey generateKey() {
		return MacProvider.generateKey(SignatureAlgorithm.HS512);
	}
	
	/**
	 * @return the securityKey
	 * <p>méthode pour retourner la clé sécurisée.<</p>
	 */
	public SecretKey getSecurityKey() {
		return securityKey;
	}
	
	public boolean regExpPassword(String password) {
		String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^.&-+=(){}])(?=\\S+$).{8,20}$";
		Pattern pattern = Pattern.compile(regex);
		if(password == null) {
			return false;
		}
		Matcher match = pattern.matcher(password);
		return match.matches();
	}
		

}












