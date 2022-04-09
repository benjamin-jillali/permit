package service;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import entity.CodeEntity;
import entity.CodeRouteExam;
import entity.Driver;
import entity.DriverCodeReponse;
import entity.PratiquePoints;

@Stateless
public class ExamService {
	@Inject
	Logger logger;
	
	@Inject
	EntityManager entityManager;
	
	@Inject 
	QueryService queryService;
	
	@Inject
	private SecurityUtil securityUtil;
	
	@Context
	private SecurityContext securityContext;
	
	
	public Collection<PratiquePoints> findPratPointByEmail(String email) {
		return entityManager.createNamedQuery(PratiquePoints.FIND_ALL_PRAT_POINT_BY_EMAIL, PratiquePoints.class)
				.setParameter("email", email).getResultList();
	}
	
	public PratiquePoints findPratById(Long id) {
		List<PratiquePoints> pratiqueList =  entityManager.createNamedQuery(PratiquePoints.FIND_PRAT_POINT_BY_ID, PratiquePoints.class)
				.setParameter("id", id)
				.setParameter("email", securityContext.getUserPrincipal().getName()).getResultList();
		if(!pratiqueList.isEmpty()) {
			return pratiqueList.get(0);
		}
		return null;
	}
	
	public PratiquePoints creePratPoint(PratiquePoints pratPoint) {
		Driver emailDriver = queryService.findDriverByEmail(securityContext.getUserPrincipal().getName());
		if(emailDriver != null) {			
			pratPoint.setConductaire(emailDriver);
			pratPoint.setPratPasse(pratPoint, emailDriver.getTypePermit());
			emailDriver.setTestPractiquepasse(pratPoint.isPratPasse());
			if(emailDriver.isCodePasse() && emailDriver.isTestPractiquepasse()) {
				emailDriver.setPermitpasse(true);				
			}
			entityManager.persist(pratPoint);
			entityManager.merge(emailDriver);
		}
		return pratPoint;
	}
	
	//need to call method in PermitService to update and check if exam has been passed
	public PratiquePoints updatePratPoints(PratiquePoints pratPoint) {
		Driver emailDriver = queryService.findDriverByEmail(securityContext.getUserPrincipal().getName());
		if(emailDriver != null) {
			pratPoint.setPratPasse(pratPoint, emailDriver.getTypePermit());
			emailDriver.setTestPractiquepasse(pratPoint.isPratPasse());
			if(emailDriver.isCodePasse() && emailDriver.isTestPractiquepasse()) {
				emailDriver.setPermitpasse(true);
			}
			entityManager.merge(emailDriver);
		}
		entityManager.merge(pratPoint);
		return pratPoint;
	}
	
	public Collection<CodeEntity> getAllQuestion(){
		return entityManager.createNamedQuery(CodeEntity.GET_ALL_CODE_ENTITIES).getResultList();
	}
	public CodeEntity getCodeById(Long id) {
		List<CodeEntity> cEntity =  entityManager.createNamedQuery(CodeEntity.GET_CODE_ENTITY_BY_ID).setParameter("id", id).getResultList();
		if(!cEntity.isEmpty()) {
			return cEntity.get(0);
		}
		return null;
	}
	
	public CodeEntity creeCodeEntity(CodeEntity cEntity) {
		entityManager.persist(cEntity);
		return cEntity;		
	}
	
	public CodeEntity updateCodeEntity(CodeEntity codeEntity) {
		entityManager.merge(codeEntity);
		return codeEntity;
	}
	public List<CodeEntity> getAllCodeEntities(){
		return entityManager.createNamedQuery(CodeEntity.GET_ALL_CODE_ENTITIES, CodeEntity.class).getResultList();
	}
	
	public CodeEntity getCodeEntity(Long id) {
		List<CodeEntity> codeResponseList = entityManager.createNamedQuery(CodeEntity.GET_CODE_ENTITY_BY_ID)
				.setParameter("id", id).getResultList();
		if(!codeResponseList.isEmpty()) {
			return codeResponseList.get(0);
		}
		return null;
	}
	
	public Collection<Object> getCodeQuestion(long id){
		return entityManager.createNamedQuery(CodeEntity.GET_QUESTION_BY_ID, Object.class)
				.setParameter("id", id).getResultList();
	}
	
	public long[] getQuestionIds(){		
		List<CodeEntity> questions = entityManager.createNamedQuery(CodeEntity.GET_ALL_QUESTIONS, CodeEntity.class).getResultList();
		if(questions.size() > 0) {
			long[] codeQuestions = new long[questions.size()];
			for (int i = 0; i < questions.size(); i++) {
				if(questions.get(i).getId() != null) {				
					codeQuestions[i] = questions.get(i).getId();
				}
			}
			return codeQuestions;
		}
		return null;
	}
	
	
	public Collection<DriverCodeReponse> getReponseByEmail(String email){
		return entityManager.createNamedQuery(DriverCodeReponse.GET_CODE_RESPONSE_BY_EMAIL)
				.setParameter("email", email).getResultList();
	}
	
	public DriverCodeReponse getDriverReponseById(Long id) {
		List<DriverCodeReponse> reponseList = entityManager.createNamedQuery(DriverCodeReponse.GET_DRIVER_CODE_RESPONSE_BY_ID)
				.setParameter("id", id)
				.setParameter("email", securityContext.getUserPrincipal().getName()).getResultList();
		if(!reponseList.isEmpty()) {
			return reponseList.get(0);
		}
		return null;
	}
	
	public DriverCodeReponse creeDriverCodeReponse(DriverCodeReponse codeReponse) {
		Driver emailDriver = queryService.findDriverByEmail(securityContext.getUserPrincipal().getName());
		if(emailDriver != null) {
			entityManager.merge(codeReponse);
			return codeReponse;
		}else
			return null;		
	}
	
	public DriverCodeReponse updateDriverCodeReponse(DriverCodeReponse codeReponse) {
		Driver driverEmail = queryService.findDriverByEmail(securityContext.getUserPrincipal().getName());
		if(driverEmail != null) {
			entityManager.merge(codeReponse);
		}
		return codeReponse;
	}
	
	
	
}






