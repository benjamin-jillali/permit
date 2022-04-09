package service;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * 
 * @author osbot
 * <p>la classe pour configurer la connexion à la base de données et la persistance.</p>
 * <p>il utilise derby</p>
 * <p>nom du db permit</p>
 */
@DataSourceDefinition(
		name = "java:app/Permit/PermitDs",
		className = "org.apache.derby.jdbc.ClientDriver",
		url = "jdbc:derby://localhost:1527/permit;create=true",
		user = "appuser",
		password = "password")

@Stateless
public class PersistenceService {
//	@Inject
//	EntityManager entityManager;

}
 