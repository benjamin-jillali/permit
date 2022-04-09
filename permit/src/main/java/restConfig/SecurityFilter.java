package restConfig;

import java.io.IOException;
import java.security.Key;
import java.security.Principal;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import service.SecurityUtil;

/**

 * @author osbot
 *<p>classe qui implémente la couche de sécurité </p>
 *<p>c'est applique avec l'annotation @Authorisation
 *@Provider il fournit l'autorisation 
 *@Priority donne le prioritaire, ici c'est AUTHENTIFICATION
 */
@Authorisation
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter{
	
	@Inject
	private SecurityUtil securityUtil;

	/**
	 * @param requestContext 	 * 
	 * <p>obtient l'en-tête d'autorisation sous forme de chaîne</p>
	 * <p>si c'est null ou vide ou ca commence pas avec "Bearer" en ajoute UNAUTHORIZED statut sinon en enleve le parti "BEARER" du header</p>
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String authString = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if(authString == null || authString.isEmpty() || !authString.startsWith(securityUtil.BEARER)) {
			throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).build());
		}
		String token = authString.substring(securityUtil.BEARER.length()).trim();
		try {
			/**
			 * <p>en obtient le cle de securite est utlise Jws
			 * pour analyser et définir la clé de signature, puis obtenir le ClaimsJws(token)</p>
			 * <p>apres en prendre le contexte d'origine de securite est cree un nouveau requestContext est faire l'implementation dans l'interface
			 * ContainerRequestFilter</p>
			 */
			Key key = securityUtil.getSecurityKey();
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
			SecurityContext originalContext = requestContext.getSecurityContext();
			requestContext.setSecurityContext(new SecurityContext() {
				/**
				 * @return new Principal() nouveau Principal
				 */
				@Override
				public Principal getUserPrincipal() {
					// TODO Auto-generated method stub
					return new Principal() {
						/**
						 * @return claimsJws.getBody().getSubject(); le nom de claimsJws
						 */
						@Override
						public String getName() {
							// TODO Auto-generated method stub
							return claimsJws.getBody().getSubject();
						}
					};
				}
				/**
				 * @return boolean originalContext.isUserInRole(role) si l'utilisateur est dans le rôle 
				 */
				@Override
				public boolean isUserInRole(String role) {
					// TODO Auto-generated method stub
					return originalContext.isUserInRole(role);
				}
				/**
				 * @return boolean originalContext.isSecure() si il est securise
				 */
				
				@Override
				public boolean isSecure() {
					// TODO Auto-generated method stub
					return originalContext.isSecure();
				}
				
				/**
				 * @return String originalContext.getAuthenticationScheme() le Schema d'authentification
				 */
				@Override
				public String getAuthenticationScheme() {
					// TODO Auto-generated method stub
					return originalContext.getAuthenticationScheme();
				}
			});
			
		}
		/**
		 * @return Exception e  en arret l'execution et envoye le reponse avec le statut UNAUTHORIZED
		 */
		catch(Exception e) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
		
	}
	

}
