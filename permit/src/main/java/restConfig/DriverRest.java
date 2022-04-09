package restConfig;

import java.io.Console;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

//import com.sun.jersey.core.header.FormDataContentDisposition;
//import com.sun.jersey.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

import entity.Driver;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import service.PermitService;
import service.QueryService;
import service.SecurityUtil;

/**
 * 
 * @author osbot
 *         <p>
 *         RestPoints pour le profil conductaire
 *         </p>
 *         <p>
 *         url: http://localhost:8080/permit/permit/v1/driver
 *         </p>
 */
@Path("driver")
public class DriverRest {
	
	@Inject
	private Logger logger;

	@Inject
	SecurityUtil securityUtil;

	@Context
	private UriInfo uriInfo;

	@Inject
	private PermitService permitService;

	@Inject
	QueryService queryService;

	/**
	 * <p>
	 * url: http://localhost:8080/permit/permit/v1/driver/login
	 * <p>
	 * 
	 * @Post service
	 *       <p>
	 *       consomme type APPLICATION_FORM_URLENCODED
	 *       </p>
	 * @param FormParam("email"), FormParam("password")
	 * @return Response
	 *         <p>
	 *         reçoit un form url avec le mot de passe et email d'utilisateur et
	 *         verifie si le passe correspong a le passe de utilisateur registre
	 *         avec le meme email.
	 *         </p>
	 */
	@Path("login")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@NotNull @FormParam("email") String email, @NotNull @FormParam("password") String password) {
		// authenticate user
		// generate token if succeds
		// return token to the client
		/**
		 * <p>
		 * un boolean "authenticated" qui retourne le resultat d'authentification
		 * d'utilisateur registre dans le base de donnees par son email.
		 * </p>
		 * <p>
		 * si authenticated = false false, il lève une SecurityException.
		 * </p>
		 */
		boolean authenticated = securityUtil.authenticateUser(email, password);
		if (!authenticated) {
			throw new SecurityException("Email ou mot de passe n'est pas correcte");
		}
		/**
		 * <p>
		 * si authentifié = vrai, il crée un jeton de sécurité par la méthode
		 * generateToken qui reçoit l'e-mail.
		 * </p>
		 * <p>
		 * puis il retourne un Reponse avec l'header AUTHORIZATION, et le mot "Bearer" +
		 * le token de securite.
		 * </p>
		 */
		String token = generateToken(email);
		// System.out.println(Response.ok().header(HttpHeaders.AUTHORIZATION,
		// securityUtil.BEARER + " " + token).build());

		return Response.ok().header(HttpHeaders.AUTHORIZATION, securityUtil.BEARER + " " + token).header(HttpHeaders.ALLOW, true).build();
	}

	// Methode pour générer un token de securite.
	// @param email
	private String generateToken(String email) {
		// reçoit une clé de sécurité
		Key securityKey = securityUtil.getSecurityKey();
		// pour créer et renvoyer un token, il définit le subject(email) la date
		// d'émission.
		// issuer le chemin d'uri de base, définit l'audience de chemin absolu de uri.
		// date d'expiration maintenent + 15 minutes.
		// Puis signe avec la signature et la clé de sécurité.
		return Jwts.builder().setSubject(email).setIssuedAt(new Date()).setIssuer(uriInfo.getBaseUri().toString())
				.setAudience(uriInfo.getAbsolutePath().toString())
				.setExpiration(securityUtil.toDate(LocalDateTime.now().plusMinutes(15)))
				.signWith(SignatureAlgorithm.HS512, securityKey).compact();
	}

	/**
	 * <p>
	 * url: http://localhost:8080/permit/permit/v1/driver/create
	 * <p>
	 * 
	 * @Post service
	 *       <p>
	 *       consomme type APPLICATION_JSON
	 *       </p>
	 *       <p>
	 *       Produit type APPLICATION_JSON
	 *       </p>
	 * @param Driver
	 * @return Response
	 *         <p>
	 *         reçoit un objet json et registre le conductaire(driver) dans le db
	 *         avec le methode permitService.registreDriver puis retourne la Reponse
	 *         </p>
	 */
	@POST
	@Path("create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveDriver(@NotNull Driver driver) {
		logger.log(Level.INFO, driver.toString());
		//errorCode represent le type de erreur cree par la registre de conducataire.
		//0 aucun erreur, 1 conductaire existe deja, 2 email non valable, 3 mot de pass non valable, 4 autre information est mal forme.
		int errorCode = permitService.registreDriver(driver);
		/*
		 * if(permitService.registreDriver(driver) != null) { return
		 * Response.ok(driver).build(); }else { throw new Nu }
		 * errorCode represent le type de erreur cree par la registre de conducataire.
		 * 0 aucun erreur, 1 conductaire existe deja, 2 email non valable, 3 mot de pass non valable, 4 autre information est mal forme.
		 */
		switch(errorCode) {
		case 1:
			return Response.status(Response.Status.BAD_REQUEST).entity("Conductaire existe deja").build();
		case 2:
			return Response.status(Response.Status.BAD_REQUEST).entity("email non valable").build();
		case 3:
			return Response.status(Response.Status.BAD_REQUEST).entity("mot de pass non valable").build();
		case 4:
			return Response.status(Response.Status.BAD_REQUEST).entity("Registration n'est pas faire. Verifie les information").build();
		default:
			return Response.ok(driver).build();	
		}
		
		
	}

	/**
	 * <p>
	 * url: http://localhost:8080/permit/permit/v1/driver/upload/{email}
	 * <p>
	 * 
	 * @Post service
	 *       <p>
	 *       consomme type MediaType.MULTIPART_FORM_DATA
	 *       </p>
	 * @param Driver
	 * @return Response
	 *         <p>
	 *         reçoit un objet json et registre le conductaire(driver) dans le db
	 *         avec le methode permitService.registreDriver puis retourne la Reponse
	 *         </p>
	 *         <p>
	 *         prendre les parametre pour le email inputStream et le fichier. Apres
	 *         en cree un variable avec le location ou en va registre le fichier
	 *         avec le nom de fichier ajoutee apres en cherche le conductaire si il
	 *         existe en registre le fichier avec le location cree ajoutee le
	 *         location dans le chemin d'image pour le
	 *         conductaire(driver.setImgPath). mis a jour le db cree le fichier est
	 *         envoye la reponse.
	 *         </p>
	 */

	// issue test with using the @For
	/*
	 * @POST
	 * 
	 * @Path("upload/{email}")
	 * 
	 * @Consumes({MediaType.MULTIPART_FORM_DATA}) public Response
	 * uploadFile(@PathParam("email") @DefaultValue("") String email,
	 * 
	 * @FormDataParam("file") InputStream uploadedInputStream,
	 * 
	 * @DefaultValue("") @FormDataParam("file") FormDataContentDisposition
	 * detailFichier)throws Exception { //String uploadLocation =
	 * "/mirror/part1/dev_docs/pics/"; String uploadLocation =
	 * "/home/osbot/eclipse-workspace/permit/images/"; try { int read = 0; byte[]
	 * bytes = new byte[1024]; OutputStream out = new FileOutputStream(new
	 * File(uploadLocation + detailFichier.getFileName())); Driver driver =
	 * queryService.findDriverByEmail(email); if(driver != null) { while((read =
	 * uploadedInputStream.read(bytes)) != -1) { out.write(bytes, 0, read); }
	 * out.flush(); out.close(); driver.setImgPath(uploadLocation);
	 * permitService.updateDriver(driver);
	 * 
	 * }
	 * 
	 * if(detailFichier != null) { //uploadLocation = uploadLocation + " ";
	 * uploadLocation += detailFichier.getFileName(); // change location for war
	 * upload } Driver driver = queryService.findDriverByEmail(email); if(driver !=
	 * null) { if(registreSurFichier(uploadedInputStream, uploadLocation)) {
	 * driver.setImgPath(uploadLocation); permitService.updateDriver(driver); String
	 * output = "Ficher registre a: " + uploadLocation; //return
	 * Response.status(200).entity(output).build(); return
	 * Response.ok(detailFichier.getName() + "upload successful. " ).build(); } }
	 * 
	 * Response.ok(detailFichier.getName() + "upload successful. " ).build(); }catch
	 * (Exception e) { throw new WebApplicationException("error uploading image"); }
	 * 
	 * if(detailFichier != null) { //uploadLocation = uploadLocation + " ";
	 * uploadLocation += detailFichier.getFileName(); // change location for war
	 * upload } //String uploadLocation =
	 * "/home/osbot/eclipse-workspace/permit/images" + detailFichier.getFileName();
	 * // change location for war upload Driver driver =
	 * queryService.findDriverByEmail(email); if(driver != null) {
	 * if(registreSurFichier(uploadedInputStream, uploadLocation)) {
	 * driver.setImgPath(uploadLocation); permitService.updateDriver(driver); String
	 * output = "Ficher registre a: " + uploadLocation; return
	 * Response.status(200).entity(output).build(); } } return
	 * Response.noContent().build(); }
	 */
	// recu l'image est le location
	// cree un outputStream cree un tableau d'octets cree un FileOutputStream
	// boucle à travers l'imageStream et écris les octets puis vide et ferme le flux
	// de sortie
	// sinon en retourne false

	private boolean registreSurFichier(InputStream uploadedInputStream, String uploadedLocation) {
		try {
			OutputStream out = new FileOutputStream(new File(uploadedLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
			out = new FileOutputStream(new File(uploadedLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <p>
	 * url: http://localhost:8080/permit/permit/v1/driver/download/{email}
	 * <p>
	 * 
	 * @GET service
	 *      <p>
	 *      Produit type image
	 *      </p>
	 * @param String email
	 * @return Response
	 *         <p>
	 *         enovye un image
	 *         </p>
	 *         <p>
	 *         trouve l'utilisateur par e-mail si l'utilisateur existe obtient
	 *         l'image par le chemin de l'image. cree. Crée un nouveau fichier image
	 *         et construit la réponse avec l'image avec ResponseBuilder. ajoute les
	 *         response.header, le contenu et le nom de fichier et renvoie la
	 *         réponse
	 *         </p>
	 *         <p>
	 *         sinon envoye en reponse noContent
	 */
	@GET
	@Path("download{email}")
	@Produces("image/*")
	public Response ImageProfil(@PathParam("email") @DefaultValue("") String email) {
		Driver driver = queryService.findDriverByEmail(email);
		if (driver != null) {
			File file = new File(driver.getImgPath());
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=" + driver.getNom() + "_profil.png");
			return response.build();
		}
		return Response.noContent().build();
	}

	/**
	 * <p>
	 * url: http://localhost:8080/permit/permit/v1/driver/conductaire/{email}
	 * <p>
	 * 
	 * @GET service
	 *      <p>
	 *      MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML
	 *      </p>
	 * @param String email
	 * @return Response
	 *         <p>
	 *         enovye un conductaire par emial
	 *         </p>
	 *         <p>
	 *         trouve l'utilisateur par e-mail avec queryService.findDriverByEmail
	 *         est renvoye la reponse avec le conductaire.
	 *         </p>
	 */
	
	@GET
	@Path("alldrivers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDrivers() {
		return Response.ok(queryService.getAllDrivers()).status(Response.Status.OK).build();
	}
	
	@GET
	@Path("conductaire/{email}")
	@Authorisation
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getConductaireByEmail(@PathParam("email") String email) {
		logger.log(Level.INFO, "Email is: " + email);
		System.out.println("method reached");
		return Response.ok(queryService.findDriverByEmail(email)).status(Response.Status.OK).build();
	}

}
