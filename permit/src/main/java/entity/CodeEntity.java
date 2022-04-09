package entity;

import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@NamedQuery(name = CodeEntity.GET_ALL_CODE_ENTITIES, query = "Select u from CodeEntity u")
@NamedQuery(name = CodeEntity.GET_CODE_ENTITY_BY_ID, query = "Select u from CodeEntity u where u.id = :id")
@NamedQuery(name = CodeEntity.GET_ALL_QUESTIONS, query = "Select u.question, u.id from CodeEntity u")
@NamedQuery(name = CodeEntity.GET_QUESTION_BY_ID, query = "Select u.id, u.question, u.lesReponsePossible"
															+ " FROM CodeEntity u where u.id = :id")
public class CodeEntity extends AbstractElementsEntity{
	
	public static final String GET_CODE_ENTITY_BY_ID = "CodeEntity.findCodeEntityById";
	public static final String GET_ALL_CODE_ENTITIES = "CodeEntity.findAllCodeEntities";
	public static final String GET_ALL_QUESTIONS = "CodeEntity.findAllQuestions";
	public static final String GET_QUESTION_BY_ID = "CodeEntity.getQuestionById";
	//format simple reference de question avec le texte de question.
	@NotNull(message = "Question non renseigne!")
	private String question;
	//les question le format est: lesQuestion<reference de question, reponse possible eg: 1, 3, 2> 
	@Size(min = 2, max = 4, message = "Reponse doit etre pas vide")
	@NotNull(message = "Reponse possible non renseigne!")
	private ArrayList<String> lesReponsePossible;
	//exemple de format pour un solution de question q1: a, c
	//primiere parties c'est l'id de question deuxieme parti c'est les contenue la reponse au question
	@Size(min = 2, max = 4, message = "Bon reponse doit etre pas vide")
	@NotNull(message = "Bon reponse non renseigne!")
	private ArrayList<String> codeSolutions;
	//format questionImage(reference, url d'image)
	private HashMap<String, String> questionImage;
	/**
	 * @return the lesQuestion
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * @param lesQuestion the lesQuestion to set
	 */
	public void setLesQuestion(String lesQuestion) {
		this.question = question;
	}
	/**
	 * @return the lesReponsePossible
	 */
	public ArrayList<String> getLesReponsePossible() {
		return lesReponsePossible;
	}
	/**
	 * @param lesReponsePossible the lesReponsePossible to set
	 */
	public void setLesReponsePossible(ArrayList<String> lesReponsePossible) {
		this.lesReponsePossible = lesReponsePossible;
	}
	/**
	 * @return the codeSolutions
	 */
	public ArrayList<String> getCodeSolutions() {
		return codeSolutions;
	}
	/**
	 * @param codeSolutions the codeSolutions to set
	 */
	public void setCodeSolutions(ArrayList<String> codeSolutions) {
		this.codeSolutions = codeSolutions;
	}
	/**
	 * @return the questionImage
	 */
	public HashMap<String, String> getQuestionImage() {
		return questionImage;
	}
	/**
	 * @param questionImage the questionImage to set
	 */
	public void setQuestionImage(HashMap<String, String> questionImage) {
		this.questionImage = questionImage;
	}
	
	

}
