package restConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;
/**
 * 
 * @author osbot
 * <p>Le classe SecurityFilter.class donne le filtre pour securise des resource  du cette interface avec 
 * l'annotation @Authorisation grâce à l'annotation @NameBinding </p>
 * <p>l'annotation RetentionPolicy.RUNTIME conserve les annotations dans la classe pour les opérations d'exécution </p>
 * <p>@Target define les type des element définit les types de composants qui peuvent être 
 * annotés avec @authorisation ici ses... méthodes et types </p>
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Authorisation {

}
