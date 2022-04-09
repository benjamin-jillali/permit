package service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import entity.CodeRouteExam;
import entity.Driver;
import entity.PratiqueExam;

/**
 * 
 * @author osbot
 * <p> la class qui recoit et gere les raquette de permit service.</p> 
 *
 */
@Stateless
public class PermitService {
	
	@Inject
	Logger logger;
	
	@Inject
	private EntityManager entityManager;
	
	@Inject
	private  QueryService queryService;
	
	@Inject
	private SecurityUtil securityUtil;
	
	@Context
	SecurityContext securityContext;
	
	/**
	 * 
	 * @param driver un conductaire
	 * @return Driver
	 * <p>methode pour registre un conductaire dans le db
	 * 	en verifie si le conductaire n'existe pas deja. 
	 *  Verification par email est si le nombre avec c'est email = 0 est id = null en commence la registration</p>
	 */	
	public int registreDriver(Driver driver) {
		//errorCode represent le type de erreur cree par la registre de conducataire.
		//0 aucun erreur, 1 conductaire existe deja, 2 email non valable, 3 mot de pass non valable, 4 autre information est mal forme.
		int errorCode = 0;
		Integer count = (Integer) queryService.nombreDriverAvecMemeEmail(driver.getEmail()).get(0);
		logger.log(Level.INFO, "count: " + count);
		if(driver.getId() == null && count != 0) {
			//errorCode = 1;
			return 1;
		}
		if(!validEmail(driver.getEmail())) {
			//errorCode = 2;
			return 2;
		}
		
		/**
		 * <p>creation de mot de passe haché est la salt avec securityUtil.hashPassword.
		 * Est registre sur un Map puis registre dans l'objet securityUtil.hashPassword, driver.setSalt
		 * si il ya un image ou/est carte ID en mettre le chemin d'image.
		 * en fin registration de l'objet driver dans le db entityManager.persist, vide le Map est retourne l'objet driver.</p>
		 */
		Map<String, String> credMap = securityUtil.hashPassword(driver.getMotPasse());
		if(credMap == null) {
			//errorCode = 3;
			driver = null;
			return 3;
		} if(errorCode == 0) {
			driver.setMotPasse(credMap.get(SecurityUtil.HASHED_PASSWORD_KEY));
			driver.setSalt(credMap.get(SecurityUtil.SALT_KEY));
			try {
				entityManager.persist(driver);
			}catch (ConstraintViolationException e) {
				logger.log(Level.SEVERE, e.getMessage());
				return 4;				
			}
			
			logger.log(Level.INFO, " if driver exists: " + queryService.checkIfDriverExists(driver.getEmail()));
			if(!queryService.checkIfDriverExists(driver.getEmail())) {
				errorCode = 4;
			}
			credMap.clear();
		}
		
		return errorCode;
		
		
//		if(driver.getId() == null && count == 0 && validEmail(driver.getEmail())) {
			
/*
 * Map<String, String> credMap =
 * securityUtil.hashPassword(driver.getMotPasse()); if(credMap != null) {
 * driver.setMotPasse(credMap.get(SecurityUtil.HASHED_PASSWORD_KEY));
 * driver.setSalt(credMap.get(SecurityUtil.SALT_KEY)); // if(driver.getPhoto()
 * != null) driver.setPhoto(driver.getImgPath()); // if(driver.getCartIdPath()
 * != null) driver.setCarteId(driver.getCartIdPath());
 * 
 * entityManager.persist(driver); credMap.clear(); } else { driver = null; }
 
			
					
		} else { driver = null; }*/
	}
	/**
	 * 
	 * @param driver
	 * @return Driver
	 * <p>méthode pour mettre à jour un conductaire</P>
	 * en cherche le conductaire et vérifier l'autorisation liée à l'utilisateur dans la session en cours avec
	 * securityContext.getUserPrincipal().getName(). Si c'est bonne en mis a jour le conductaire entityManager.merge est retourne le conductaire.</p>
	 */
	
	public Driver updateDriver(Driver driver) {
		Driver dbDriver = queryService.findDriverByEmail(securityContext.getUserPrincipal().getName());
		if(driver.getId() != null && dbDriver != null) {
			entityManager.merge(driver);
		}
		return driver;
	}
	
	/** 
	 * @param codeE
	 * @return CodeRouteExam
	 * <p>methode pour le creation d'un examin de code de la route</p>
	 * <p>en cherche le conductaire et vérifier l'autorisation liée à l'utilisateur dans la session en cours avec
	 * securityContext.getUserPrincipal().getName().
	 * si le conductaire existe en mettre le conductaire dans l'object de code puis en verfie si le conductaire a passe le code ou non et registre les resultat.
	 * apres en registre le code dans le db est mis a jour le conductaire</p>
	 */
	public CodeRouteExam creeCodeExam(CodeRouteExam codeE) {
		Driver driverParEmail = queryService.findDriverByEmail(securityContext.getUserPrincipal().getName());
		if(driverParEmail != null) {
			codeE.setConductaire(driverParEmail);
			codeE.setCodePasse(codeE);
			driverParEmail.setCodePasse(codeE.isCodePasse());
			if(driverParEmail.isCodePasse() && driverParEmail.isTestPractiquepasse()) {
				driverParEmail.setPermitpasse(true);
			}
			entityManager.persist(codeE);
			entityManager.merge(driverParEmail);
		}
		return codeE;
	}
	
	/**
	 * 
	 * @param codeE
	 * @return CodeRouteExam
	 * <p>Méthode pour mettre à jour un code de la route examin.</p>
	 * <p>verfication si le code est passe ou pas est en cherche le 
	 * conductaire et vérifier l'autorisation liée à l'utilisateur dans la session en cours avec
	 * securityContext.getUserPrincipal().getName().</p>
	 * si l'autorisation est bien, registre le resultat de isCodePasse() si true en verfie si le test pratique est passe si true, permitPasse = true,
	 * est en registre dans l'object driverParEmail est registre dans le db.
	 * registre le examin du code dans le db est retourne le code.</p>
	 */
	public CodeRouteExam updateCodeExam(CodeRouteExam codeE) {
		codeE.setCodePasse(codeE);
		Driver driverParEmail = queryService.findDriverByEmail(codeE.getConductaire().getEmail());
		if(driverParEmail != null) {
			driverParEmail.setCodePasse(codeE.isCodePasse());
			if(driverParEmail.isCodePasse() && driverParEmail.isTestPractiquepasse()) {
				driverParEmail.setPermitpasse(true);
			}
			entityManager.merge(driverParEmail);
		}
		entityManager.merge(codeE);
		return codeE;
	}
	
	/**
	 * 
	 * @param id
	 * @return CodeRouteExam Objet
	 * <p>cherche un Code de la Route examin dans le db avec un id donnee</p>
	 */
	public CodeRouteExam findCodeExamById(Long id) {
		return entityManager.find(CodeRouteExam.class, id);
	}
	
	/**
	 * 
	 * @return List<CodeRouteExam>
	 * <p>Retourne un liste des code de la route examin</p>
	 */
	public List<CodeRouteExam> getCodeExam(){
		return entityManager.createQuery("SELECT t from CodeRouteExam t", CodeRouteExam.class).getResultList();
	}
	
	/** 
	 * @param pratE
	 * @return PratiqueExam pratE
	 * <p>methode pour le creation de resultat d'examin pratique</p>
	 * <p>en cherche le conductaire et vérifier l'autorisation liée à l'utilisateur dans la session en cours avec
	 * securityContext.getUserPrincipal().getName().
	 * si le conductaire existe en mettre le conductaire dans l'object de pratE. Puis
	 * registre le resultat de isPratPasse() si true en verfie si le test de code est passe si true, permitPasse = true,
	 * est en registre dans l'object driverParEmail est registre dans le db.
	 * apres en registre le code dans le db est mis a jour le conductaire</p>
	 */	
	/*
	 * public PratiqueExam creeRouteExam(PratiqueExam pratE) { Driver driverParEmail
	 * = queryService.findDriverByEmail("email"); if(driverParEmail != null) {
	 * pratE.setConductaire(driverParEmail); pratE.setPratPasse(pratE,
	 * driverParEmail.getTypePermit());
	 * driverParEmail.setTestPractiquepasse(pratE.isPratPasse());
	 * if(driverParEmail.isCodePasse() && driverParEmail.isTestPractiquepasse()) {
	 * driverParEmail.setPermitpasse(true); } entityManager.persist(pratE);
	 * entityManager.merge(driverParEmail); } return pratE; }
	 */
	
	/**
	 * 
	 * @param pratE
	 * @return PratiqueExam pratE
	 * <p>Méthode pour mettre à jour un examin pratique.</p>
	 * <p>en cherche le conductaire et vérifier l'autorisation liée à l'utilisateur dans la session en cours avec
	 * securityContext.getUserPrincipal().getName().</p>
	 * <p>si l'autorisation est bien, registre le resultat de isPratPasse() avec le type de permit.
	 * Si true en verfie si l'examin de code est passe si true, permitPasse = true,
	 * est en registre dans l'object driverParEmail est registre dans le db.
	 * registre le examin pratique dans le db est retourne le pratE.</p>
	 */	
	/*
	 * public PratiqueExam updatePratiqueExam(PratiqueExam pratE) { Driver
	 * driverParEmail =
	 * queryService.findDriverByEmail(pratE.getConductaire().getEmail());
	 * pratE.setPratPasse(pratE, driverParEmail.getTypePermit()); if(driverParEmail
	 * != null) { driverParEmail.setTestPractiquepasse(pratE.isPratPasse());
	 * if(driverParEmail.isCodePasse() && driverParEmail.isTestPractiquepasse()) {
	 * driverParEmail.setPermitpasse(true); } entityManager.merge(driverParEmail); }
	 * entityManager.merge(pratE); return pratE; }
	 */
	
	/**
	 * 
	 * @param id
	 * @return ratiqueExam objet
	 * <p>cherche un examin pratique dans le db avec un id donnee</p>
	 */
	/*
	 * public PratiqueExam findPratiqueExamById(Long id) { return
	 * entityManager.find(PratiqueExam.class, id); }
	 */
	
	/**
	 * 
	 * @return List<PratiqueExam>
	 * <p>Retourne un liste des examin pratique</p>
	 */
	/*
	 * public List<PratiqueExam> getPratiqueExam(){ return
	 * entityManager.createQuery("SELECT t from PratiqueExam t",
	 * PratiqueExam.class).getResultList(); }
	 */
	
	private boolean validEmail(String email) {
		String regex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	

}
