package service;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/**
 * 
 * @author osbot
 * <p>le contexte de persistance pour l'EntityManager. </p>
 *
 */
public class CDIProducer {
	@Produces
	@PersistenceContext
	EntityManager entityManager;

}
