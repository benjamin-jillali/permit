package restConfig;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Driver;
import entity.PratiqueExam;
import entity.PratiquePoints;
import permitReglePOJO.PermitPratiquePOJO;
import service.ExamService;
import service.PermitService;
import service.QueryService;

/**
 * 
 * @author osbot
 *<p>RestPoints pour l'examin routiere</p>
 *<p>url: http://localhost:8080/permit/permit/v1/exampratique</p>
 *<p>par défaut consomme et produit le type json </p>
 */
@Path("exampratique")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
/**
 * @Authorisation sécuriser ces ressources 
 */
@Authorisation
public class ExamPratRest {
	@Inject
	PermitService permitService;
	
	@Inject
	QueryService queryService;
	
	@Inject
	ExamService examService;
	
	@Inject
	PermitPratiquePOJO permitPratiquePOJO;
	
	@Inject
	Logger logger;
	/** 
	 * <p> url: http://localhost:8080/permit/permit/v1/exampratique/new<p>
	 * @Post service
	 * @param pratExam 
	 * @return Response
	 * <p>reçoit un objet d'examen pratique et tente de persister dans la base de données 
	 * avec la method permitService.creeRouteExam puis construit et renvoie le Reponse </p>
	 */
	/*
	 * @Consumes(MediaType.APPLICATION_JSON)
	 * 
	 * @Path("new")
	 * 
	 * @POST
	 */
	//public Response creeExam(PratiqueExam pratExam) {
	/*
	 * public Response creeExam(String jsonPratExam) { PratiqueExam pratExam = new
	 * PratiqueExam(); String email = ""; int note = 0; try { JSONObject pratExamObj
	 * = new JSONObject(jsonPratExam);
	 * if(queryService.checkIfDriverExists(pratExamObj.getString("conductaire")) &&
	 * pratExamObj.getInt("note") >= 0) { email =
	 * pratExamObj.getString("conductaire"); note = pratExamObj.getInt("note");
	 * Driver conductaire = queryService.findDriverByEmail(email);
	 * pratExam.setNote(note); pratExam.setConductaire(conductaire);
	 * permitService.creeRouteExam(pratExam); return Response.ok(pratExam).build();
	 * }else { return Response.status(Response.Status.BAD_REQUEST).
	 * entity("Registration n'est pas faire. Verifie les information").build(); }
	 * }catch (Exception e) { return Response.status(Response.Status.BAD_REQUEST).
	 * entity("Registration n'est pas faire. Verifie les information").build(); }
	 * 
	 * }
	 */
	/** 
	 * <p> url: http://localhost:8080/permit/permit/v1/exampratique/{id}<p>
	 * @GET service
	 * @param id recois un Long id d'un examin pratique
	 * @return PratiqueExam objet
	 * <p>Retoune un examen routiere par son "id" avec le method queryService.findPratExamById </p>
	 */
	/*
	 * @Path("{id}")
	 * 
	 * @GET public PratiqueExam getPratiqueExam(@PathParam("id") long id) { return
	 * queryService.findPratExamById(id); }
	 */
	/** 
	 * 
	 * <p> url: http://localhost:8080/permit/permit/v1/codeexam/exampratique<p>
	 * @GET service
	 * @return un List des PratiqueExam objets
	 * <p>Retoune un liste des examens pratique avec le method queryService.getAllPratExam</p>
	 */
	/*
	 * @Path("list")
	 * 
	 * @GET public List<PratiqueExam> getPratExam(){ return
	 * queryService.getAllPratExam(); }
	 */
	/** 
	 * <p> url: http://localhost:8080/permit/permit/v1/exampratique/resultat/{id}<p>
	 * @POST service
	 * @PathParam id recois un Long id d'un examen pratique
	 * @return Reponse
	 * <p>trouve un examen pratique par identifiant avec permitService.findPratiqueExamById
	 * est le type de permit pour cette examin avec l'email du conductaire 
	 * cherche par l'ID de conductaiare de l'examin pratique
	 * queryService.findDriverByEmail(pratiqueExam.getConductaire().getEmail())
	 * et définit le codeExam.setCodePasse(). 
	 * Puis le définit avec ratiqueExam.setPratPasse().
	 * Mise a jour le db.
	 * puis renvoie la réponse. 
	 * </p>
	 */	
	/*
	 * @Path("resultat{id}")
	 * 
	 * @POST public Response setResultat(@PathParam("id") long id) { PratiqueExam
	 * pratiqueExam = permitService.findPratiqueExamById(id); Driver driverParEmail
	 * = queryService.findDriverByEmail(pratiqueExam.getConductaire().getEmail());
	 * pratiqueExam.setPratPasse(pratiqueExam, driverParEmail.getTypePermit());
	 * permitService.updatePratiqueExam(pratiqueExam); return
	 * Response.ok(pratiqueExam).build(); }
	 */
	
	@Path("pratconductaire{email}")
	@GET
	public Collection<PratiquePoints> getPratResultParCond(@PathParam("email") String email){
		return examService.findPratPointByEmail(email);
	}
	
	@Path("pratExam")
	@GET
	public PratiquePoints getPratResultById(Long id) {
		return examService.findPratById(id);
	}
	
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("newTest")
	@POST
	public Response createTestPrat(String testResultat) {
		PratiquePoints pratExam = new PratiquePoints();
		String email = "";
		float note = 0;
		try {
			JSONObject jsonPrat = new JSONObject(testResultat);
			if(queryService.checkIfDriverExists(jsonPrat.getString("conductaire"))) {
				email = jsonPrat.getString("conductaire");
				//will work on this method need to check the data type structure how its returned/values
				//do calculations and populate the info correctly.				

				float[] bilanCompetencesPrimiere = permitPratiquePOJO.jsonArrToFloatArr(jsonPrat.getJSONArray("bilanCompetencesPrimiere"));
				float[] autonomie = permitPratiquePOJO.jsonArrToFloatArr(jsonPrat.getJSONArray("autonomie"));
				float[] sousTot = permitPratiquePOJO.jsonArrToFloatArr(jsonPrat.getJSONArray("sousTot"));
				if(bilanCompetencesPrimiere.length == 9 && autonomie.length == 3 && sousTot.length == 2) {
					pratExam.setConductaire(queryService.findDriverByEmail(email));
					pratExam.setBilanCompetencesPrimiere(bilanCompetencesPrimiere);
					pratExam.setAutonomie(autonomie);
					pratExam.setSousTot(sousTot);
					
					if(permitPratiquePOJO.calculateNote(bilanCompetencesPrimiere, "B") != -1) {
						note += permitPratiquePOJO.calculateNote(bilanCompetencesPrimiere, "B");
						note += permitPratiquePOJO.calculateNote(autonomie, "A");
						note += permitPratiquePOJO.calculateNote(sousTot, "S");
					}else
						note = -1;
					pratExam.setPratTestNote(note);
					
				}
				examService.creePratPoint(pratExam);
				return Response.ok(pratExam).build();
				
			}else {
				return Response.status(Response.Status.BAD_REQUEST).entity("Registration n'est pas faire. Verifie les information").build();
			}
		}catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Registration n'est pas faire. Verifie les information").build();
		}
	}
	
}







