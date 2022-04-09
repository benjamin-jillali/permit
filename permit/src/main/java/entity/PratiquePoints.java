package entity;

import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;

import enums.TypePermitEnum;



@Entity
@NamedQuery(name = PratiquePoints.FIND_PRAT_POINT_BY_ID, query = "Select u from PratiquePoints u where u.id = :id and u.conductaire.email = :email")
@NamedQuery(name = PratiquePoints.FIND_ALL_PRAT_POINT_BY_EMAIL, query = "Select u from PratiquePoints u where u.conductaire.email = :email")

public class PratiquePoints extends AbstractElementsEntity{
	
	public static final String FIND_PRAT_POINT_BY_ID = "PratiquePoints.FindPratPointById";
	public static final String FIND_ALL_PRAT_POINT_BY_EMAIL = "PratiquePoints.FindPratPointByEmail";
	
	//Integer[] score = {0, 0, 0};
	//map currently not in use leave for moment if solution is found.
	//or transfer storage functionality to bean layer.
	//private HashMap<String, Integer[]> notes = new HashMap<String, Integer[]>();
	
	@ManyToOne
	Driver conductaire;
	
	//note -1 ou -1 = eliminatoire est 0 - 3
	private float[] bilanCompetencesPrimiere = {0, 0, 0, 0, 0, 0, 0, 0, 0};
	//ou les note son de 0 - 1
	private float[] autonomie = {0, 0, 0};
	//ou les note son de 0, 0.5, 1
	private float[] sousTot = {0, 0};
	
	private float pratTestNote;
	
	private boolean pratPasse = false;
	
		
	
	/**
	 * @return the pratTestNote
	 */
	public float getPratTestNote() {
		return pratTestNote;
	}


	/**
	 * @param pratTestNote the pratTestNote to set
	 */
	public void setPratTestNote(float pratTestNote) {
		this.pratTestNote = pratTestNote;
	}


	/**
	 * @return the bilanCompetencesPrimiere
	 */
	public float[] getBilanCompetencesPrimiere() {
		return bilanCompetencesPrimiere;
	}


	/**
	 * @param bilanCompetencesPrimiere the bilanCompetencesPrimiere to set
	 */
	public void setBilanCompetencesPrimiere(float[] bilanCompetencesPrimiere) {
		this.bilanCompetencesPrimiere = bilanCompetencesPrimiere;
	}


	/**
	 * @return the autonomie
	 */
	public float[] getAutonomie() {
		return autonomie;
	}


	/**
	 * @param autonomie the autonomie to set
	 */
	public void setAutonomie(float[] autonomie) {
		this.autonomie = autonomie;
	}


	/**
	 * @return the sousTot
	 */
	public float[] getSousTot() {
		return sousTot;
	}


	/**
	 * @param sousTot the sousTot to set
	 */
	public void setSousTot(float[] sousTot) {
		this.sousTot = sousTot;
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
	
	public boolean isPratPasse() {
		return pratPasse;
	}
	
	public void setPratPasse(PratiquePoints pratExam, Enum<TypePermitEnum> typePermit) {
		if(typePermit == TypePermitEnum.B || typePermit == TypePermitEnum.B1) {
			pratExam.pratPasse = pratExam.getPratTestNote() >= 20;
		}else if(typePermit == TypePermitEnum.A1 || typePermit == TypePermitEnum.A2) {
			pratExam.pratPasse = pratExam.getPratTestNote() >= 17;
		}else {
			pratExam.pratPasse = pratExam.getPratTestNote() >= 21;
		}
	}

}



