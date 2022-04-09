package entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

/**
 * 
 * <h5>Entity CodeRouteExam </h5>
 * <p>c'est l'entité pour enregistrer les donnees pour un nouveau code de la route examin</p>
 * <p>Les requêtes SQL sont les suivantes: </p>
 * <p>FIND_ALL_CODE_EXAMS_BY_DRIVER cherche un liste des tous les resultat de codeExam par l'email d'un conductaire</p>
 * <p>FIND_CODE_BY_ID cherche une code exam par son identifiant et l'email de conductiare</p>
 */


@NamedQuery(name = CodeRouteExam.FIND_CODE_BY_ID, query = "Select u from CodeRouteExam u where u.id = :id and u.conductaire.email = :email")
@NamedQuery(name = CodeRouteExam.FIND_ALL_CODE_EXAMS_BY_DRIVER, query = "Select u from CodeRouteExam u where u.conductaire.email = :email")
//TODO why is this commented need to test :-)
//@NamedQuery(name = CodeRouteExam.RETURN_CODE_RESULT, query = "Select u.codePasse from CodeRouteExam u where u.id = :id and u.conductaire.email = :email")

@Entity
public class CodeRouteExam extends AbstractElementsEntity{
	/**
	 * <p>les constantes des requêtes <p>
	 */		
	public static final String FIND_CODE_BY_ID = "CodeRouteExam.FindCodeById";
	public static final String FIND_ALL_CODE_EXAMS_BY_DRIVER = "CodeRouteExam.FindAllCodeExamsByDriver";
	//public static final String RETURN_CODE_RESULT = "CodeRouteExam.FindCodeResult";
	
	@NotNull
	private int note;
	private boolean codePasse = false;
	@ManyToOne
	private Driver conductaire;
	@ManyToMany
	private List<DriverCodeReponse> examReponse;
	/**
	 * @return le note
	 */
	public int getNote() {		
		return this.note;
	}
	/**
	 * @param note le note d'examin a le mettre
	 */
	public void setNote(List<DriverCodeReponse> examReponse) {
		Integer exNote = 0;
		for (DriverCodeReponse driverCodeReponse : examReponse) {
			if(!driverCodeReponse.getCodeReponseCorrection().containsValue(-1)) {
				exNote += 1;
			}
		}
		this.note = exNote;
	}
	/**
	 * @return le boolean codePasse
	 */
	public boolean isCodePasse() {
		return codePasse;
	}
	/**
	 * @param codePasse boolean pour voir si le code exam est passe ou pas si les note son >= 30
	 */
	public void setCodePasse(CodeRouteExam codeRoute) {
		codeRoute.codePasse = codeRoute.getNote() >= 30;
	}
	/**
	 * @return conductaire lien avec cette examin
	 */
	public Driver getConductaire() {
		return conductaire;
	}
	/**
	 * @param conductaire le conductaire a le mettre pour cette examin
	 */
	public void setConductaire(Driver conductaire) {
		this.conductaire = conductaire;
	}
	/**
	 * @return the examReponse
	 */
	public List<DriverCodeReponse> getExamReponse() {
		return examReponse;
	}
	/**
	 * @param examReponse the examReponse to set
	 */
	public void setExamReponse(List<DriverCodeReponse> examReponse) {
		this.examReponse = examReponse;
		setNote(examReponse);
	}
	
	
}
