package service;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.xml.registry.infomodel.User;

import entity.CodeRouteExam;
import entity.Driver;
import entity.PratiqueExam;

/**
 * 
 * @author osbot
 * <p>classe de service de requête </p>
 * <p>Stateless</p>
 *
 */
@Stateless
public class QueryService {
	@Inject
	EntityManager entityManager;
	
	@Inject
	SecurityUtil securityUtil;
	
	@Inject
	Logger logger;
	
	@Context
	private SecurityContext securityContext;
	
	public List<Driver> getAllDrivers(){
		return entityManager.createNamedQuery(Driver.FIND_ALL_DRIVERS, Driver.class).getResultList();
	}
	
	/**
	 * 
	 * @param String email
	 * @return Driver
	 * <p>requête pour trouver un conductaire par email. 
	 * en récupérez l'email de la session et vérifiez son autorisation de sécurité puis définissez-le en paramètre.
	 * renvoyer les résultats à une liste. Est retourne le conductaire sinon retourne null. </p>
	 */
	public Driver findDriverByEmail(String email) {
		logger.log(Level.INFO, "email: " + email);
		List<Driver> driverList =  entityManager.createNamedQuery(Driver.FIND_DRIVER_BY_EMAIL, Driver.class)
								   .setParameter("email", securityContext.getUserPrincipal().getName()).getResultList();
		if(!driverList.isEmpty()) {
			return driverList.get(0);
		}
		return null;
	}
	
	public boolean checkIfDriverExists(String email) {
		List<Driver> driverList = entityManager.createNamedQuery(Driver.FIND_DRIVER_BY_EMAIL, Driver.class)
								  .setParameter("email", email).getResultList();
		
		if(driverList.isEmpty()){
			return false;
		}else return true;
	}
	
	/**
	 * 
	 * @param Long id
	 * @return Driver
	 * <p>requête pour trouver un conductaire par Id. 
	 * définissez le ID en paramètre renvoyer les résultats à une liste. Est retourne le conductaire sinon retourne null. </p>
	 */	
	public Driver findDriverById(Long id) {
		List<Driver> driverList = entityManager.createNamedQuery(Driver.FIND_DRIVER_BY_ID, Driver.class)
				.setParameter("id", id).getResultList();
		if(!driverList.isEmpty()) {
			return driverList.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param String Email
	 * @return List
	 * <p>requête pour trouver les nombre des conductaire avec le meme email(normalement pas des doublons).</p>
	 */	
	public List nombreDriverAvecMemeEmail(String email) {
		return entityManager.createNativeQuery("select count (id) from Driver where exists (select id from Driver where email = ?)")
			   .setParameter(1, email).getResultList();
	}
//	public boolean authenticateUser(String email, String password) {
//		return false;
//	}
	
	/**
	 * 
	 * @param Long id
	 * @return CodeRouteExam
	 * <p>requête pour trouver un code de la route examin par Id.
	 * En récupérez l'email de la session et vérifiez son autorisation de sécurité puis définissez-le en paramètre. 
	 * Définissez le ID en paramètre renvoyer les résultats à une liste. Est retourne le code examin sinon retourne null. </p>
	 */
	public CodeRouteExam findCodeRouteExamById(Long id) {
		List<CodeRouteExam> resultList = entityManager.createNamedQuery(CodeRouteExam.FIND_CODE_BY_ID, CodeRouteExam.class)
										 .setParameter("id", id)
										 .setParameter("email", securityContext.getUserPrincipal().getName()).getResultList();
		if(!resultList.isEmpty()) {
			return resultList.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @return List<CodeRouteExam>
	 * <p>requête pour retournes tous les code de la route examin pour un conductaire.
	 * En récupérez l'email de la session et vérifiez son autorisation de sécurité puis définissez-le en paramètre est retourne tous les 
	 * examin de code pour cette conductaire.</p>
	 */	
	public List<CodeRouteExam> getAllCodeRouteExam(){
		return entityManager.createNamedQuery(CodeRouteExam.FIND_ALL_CODE_EXAMS_BY_DRIVER, CodeRouteExam.class)
				.setParameter("email", securityContext.getUserPrincipal().getName()).getResultList();
	}
	
	/**
	 * 
	 * @param Long id
	 * @return PratiqueExam
	 * <p>requête pour trouver un examin pratique par Id.
	 * En récupérez l'email de la session et vérifiez son autorisation de sécurité puis définissez-le en paramètre. 
	 * Définissez le ID en paramètre renvoyer les résultats à une liste. Est retourne l'examin routier, sinon retourne null. </p>
	 */
	/*
	 * public PratiqueExam findPratExamById(Long id) { List<PratiqueExam> resultList
	 * = entityManager.createNamedQuery(PratiqueExam.FIND_PRATQ_BY_ID,
	 * PratiqueExam.class) .setParameter("id", id) .setParameter("email",
	 * securityContext.getUserPrincipal().getName()) .getResultList();
	 * if(!resultList.isEmpty()) { return resultList.get(0); } return null; }
	 */
	
	/**
	 * 
	 * @return List<PratiqueExam>
	 * <p>requête pour retournes tous les examin routiere pour un conductaire.
	 * En récupérez l'email de la session et vérifiez son autorisation de sécurité puis définissez-le en paramètre est retourne tous les 
	 * examin routiere pour cette conductaire.</p>
	 */	
	/*
	 * public List<PratiqueExam> getAllPratExam(){ return
	 * entityManager.createNamedQuery(PratiqueExam.FIND_ALL_PRACTQ_EXAMS_BY_DRIVER,
	 * PratiqueExam.class) .setParameter("email",
	 * securityContext.getUserPrincipal().getName()).getResultList(); }
	 */

}
