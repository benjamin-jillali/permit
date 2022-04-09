package entity;

import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;


@Entity
@NamedQuery(name = DriverCodeReponse.GET_DRIVER_CODE_RESPONSE_BY_ID, query = "Select u.codeReponseDriver from DriverCodeReponse u where u.id = :id and u.conductaire.email = :email")
@NamedQuery(name = DriverCodeReponse.GET_CODE_RESPONSE_BY_EMAIL, query = "Select u.codeReponseDriver from DriverCodeReponse u where u.conductaire.email = :email")

public class DriverCodeReponse extends AbstractElementsEntity{
	
	public static final String GET_DRIVER_CODE_RESPONSE_BY_ID = "DriverCodeReponse.getCodeReponseDriverById";
	public static final String GET_CODE_RESPONSE_BY_EMAIL = "Driver.CodeReponse.getCodeReponseByEmail";
	
	@NotNull
	//Reponse au question Code structure ['id de question', 'reponse: a, c']
	private ArrayList<String> codeReponseDriver;
	
	@NotNull
	private HashMap<String, Integer> codeReponseCorrection;
	
	
	//TODO remove driver and refactor
	@NotNull
	@ManyToOne
	private Driver conductaire;
	
	@NotNull
	@OneToOne
	private CodeEntity questionDetails;
	
	
	/**
	 * @return the codeReponseDriver
	 */
	public ArrayList<String> getCodeReponseDriver() {
		return codeReponseDriver;
	}

	/**
	 * @param codeReponseDriver the codeReponseDriver to set
	 */
	public void setCodeReponseDriver(ArrayList<String> codeReponseDriver) {
		this.codeReponseDriver = codeReponseDriver;
	}
	
	

	/**
	 * @return the codeReponseCorrection
	 */
	public HashMap<String, Integer> getCodeReponseCorrection() {
		return codeReponseCorrection;
	}

	/**
	 * @param codeReponseCorrection the codeReponseCorrection to set
	 */
	public void setCodeReponseCorrection(HashMap<String, Integer> codeReponseCorrection) {
		this.codeReponseCorrection = codeReponseCorrection;
	}

	/**
	 * @return the conductaire
	 */
	public Driver getConductaire() {
		return conductaire;
	}

	/**
	 * @param conductaire the conductaire to set
	 */
	public void setConductaire(Driver conductaire) {
		this.conductaire = conductaire;
	}

	/**
	 * @return the questionAndReply
	 */
	public CodeEntity getQuestionDetails() {
		return questionDetails;
	}

	/**
	 * @param questionAndReply the questionAndReply to set
	 */
	public void setQuestionDetails(CodeEntity questionAndReply) {
		this.questionDetails = questionAndReply;
	}
	
	
}
