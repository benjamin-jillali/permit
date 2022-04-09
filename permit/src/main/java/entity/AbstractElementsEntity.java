package entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import enums.TypePermitEnum;

/**
 * 
 * <h5>AbstractElementsEntity </h5>
 * <p>c'est une superclasse mappée </p>
 * 
 */

@MappedSuperclass
public abstract class AbstractElementsEntity implements Serializable{
	/**
	 * <p> c'est l'id d'element avec un autogeneration de numero.</p>
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
/**
 * <p>la date de creation</p>
 */
	protected LocalDateTime createdOn;
//	@NotNull
//	@Enumerated(EnumType.STRING)
//	TypePermitEnum typePermit;	
	/**
	 * <p>obtient la date de création d'un élément avec PrePersist</p>
	 */
	@PrePersist
	private void init() {
		setCreatedOn(LocalDateTime.now());
	}

	/**
	 * @return le createdOn
	 */
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn le date de creation a mettre
	 */
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return le id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id l'id a mettre
	 */
	public void setId(long id) {
		this.id = id;
	}

	
	
	

}
