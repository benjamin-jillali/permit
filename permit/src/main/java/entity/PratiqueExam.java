package entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

import enums.TypePermitEnum;

/**
 * <h5>Entity PratiqueExam </h5>
 * <p>c'est l'entité pour enregistrer les donnees pour un nouveau examin pratique de conductaire</p>
 * <p>Les requêtes SQL sont les suivantes: </p>
 * <p>FIND_ALL_PRACTQ_EXAMS_BY_DRIVER cherche un liste des tous les resultat d'examin pratique par l'email d'un conductaire</p>
 * <p>FIND_PRATQ_BY_ID cherche une pratique examin par son identifiant et l'email de conductiare</p>
 * <p>RETURN_PRACTQ_RESULT cherche le resultat d'un examin pratique par son identifiant et l'email de conductiare</p>
 */

@NamedQuery(name = PratiqueExam.FIND_PRATQ_BY_ID, query = "Select u from PratiqueExam u where u.id = :id and u.conductaire.email = :email")
@NamedQuery(name = PratiqueExam.FIND_ALL_PRACTQ_EXAMS_BY_DRIVER, query = "Select u from PratiqueExam u where u.conductaire.email = :email")
@NamedQuery(name = PratiqueExam.RETURN_PRACTQ_RESULT, query = "Select u.pratPasse from PratiqueExam u where u.id = :id and u.conductaire.email = :email")

@Entity
public class PratiqueExam extends AbstractElementsEntity{
	
	/**
	 * <p>les constantes des requêtes <p>
	 */	
	
	public static final String FIND_PRATQ_BY_ID = "PratiqueExam.FindPratById";
	public static final String FIND_ALL_PRACTQ_EXAMS_BY_DRIVER = "PratRouteExam.FindAllPratExamsByDriver";
	public static final String RETURN_PRACTQ_RESULT = "PratRouteExam.FindPratResult";
	
	@NotNull
	private int note;
	private boolean pratPasse = false;
	@ManyToOne
	Driver conductaire;
	/**
	 * @return le note
	 */
	public int getNote() {
		return note;
	}
	/**
	 * @param note le note d'examin a le mettre
	 */
	public void setNote(int note) {
		this.note = note;
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
	 * @return le boolean codePasse
	 */
	public boolean isPratPasse() {
		return pratPasse;
	}
	/**
	 * @param codeExam l'examin pratique a le mettre
	 * @param typePermit le type de permit pour verfifie
	 * <p>cette méthode donne des resultat pour si le permis est passe 
	 * 	  a la base de note obtenu et le type de permit</p>
	 * <p>les type des permis est leur note minimum pour passes:</p>
	 * <p>B ou B1 >= 20</p>
	 * <p>A1 ou A2 >= 17</p>
	 * <p>le reste >= 21</p>
	 */
	public void setPratPasse(PratiqueExam pratExam, Enum<TypePermitEnum> typePermit) {
		if(typePermit == TypePermitEnum.B || typePermit == TypePermitEnum.B1) {
			pratExam.pratPasse = pratExam.getNote() >= 20;
		}else if(typePermit == TypePermitEnum.A1 || typePermit == TypePermitEnum.A2) {
			pratExam.pratPasse = pratExam.getNote() >= 17;
		}else {
			pratExam.pratPasse = pratExam.getNote() >= 21;
		}
	}

}
