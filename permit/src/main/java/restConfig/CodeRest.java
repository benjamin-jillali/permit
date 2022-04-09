package restConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.CodeEntity;
import entity.CodeRouteExam;
import entity.Driver;
import entity.DriverCodeReponse;
import entity.PratiqueExam;
import permitReglePOJO.CodeRegle;
import service.ExamService;
import service.PermitService;
import service.QueryService;
/**
 * 
 * @author osbot
 *<p>RestPoints pour le code de la route examin</p>
 *<p>url: http://localhost:8080/permit/permit/v1/codeexam</p>
 *<p>par défaut consomme et produit le type json </p>
 */
@Path("codeexam")
/*
 * @Consumes(MediaType.APPLICATION_JSON)
 * 
 * @Produces(MediaType.APPLICATION_JSON)
 */
/**
 * @Authorisation sécuriser ces ressources 
 */
@Authorisation
public class CodeRest {
	@Inject
	PermitService permitService;
	@Inject
	QueryService queryService;	
	@Inject
	ExamService examService;
	@Inject
	CodeRegle codeRegle;
	
	/** 
	 * <p> url: http://localhost:8080/permit/permit/v1/codeexam/new<p>
	 * @Post service
	 * @param codeRouteExam 
	 * @return Response
	 * <p>reçoit un objet d'examen de code et tente de persister dans la base de données 
	 * avec la method permitService.creeCode puis construit et renvoie le Reponse </p>
	 */
	@Path("new")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response creeCodeExam(@NotNull JSONObject jsonRouteExam) {
		CodeRouteExam codeRouteExam = new CodeRouteExam();
		String email = "";
		//int note = 0;
		JSONObject testResultat = new JSONObject();
		List<DriverCodeReponse> examDriverReponse;
		try {	
			testResultat = jsonRouteExam.getJSONObject("codeResponses");
			if(queryService.checkIfDriverExists(jsonRouteExam.getString("conductaire"))) {
				examDriverReponse = codeRegle.testReplies(testResultat, email);
				if(examDriverReponse != null) {
					persistQuestions(examDriverReponse); //i should check that the boolean is correct but already 
					email = jsonRouteExam.getString("conductaire");
					//note = jsonCode.getInt("note");
					Driver conductaire = queryService.findDriverByEmail(email);
					codeRouteExam.setConductaire(conductaire);
					codeRouteExam.setExamReponse(examDriverReponse);	
					codeRouteExam.setCodePasse(codeRouteExam);
					permitService.creeCodeExam(codeRouteExam);
					return Response.ok(codeRouteExam).build();
				}else {
					return Response.status(Response.Status.BAD_REQUEST).entity("Registration n'est pas faire. Verifie les information").build();
				}
				
			}else {
				return Response.status(Response.Status.BAD_REQUEST).entity("Registration n'est pas faire. Verifie les information").build();
			}
		}catch(Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Registration n'est pas faire. Verifie les information").build();
		} 
	}
	/** 
	 * <p> url: http://localhost:8080/permit/permit/v1/codeexam/{id}<p>
	 * @GET service
	 * @param id recois un Long id d'un code
	 * @return CodeRouteExam objet
	 * <p>Retoune un examen de code par son "id" avec le method queryService.findCodeRouteExamById </p>
	 */
	@Path("{id}")
	@GET
	public CodeRouteExam getCode(@PathParam("id") long id) {
		return queryService.findCodeRouteExamById(id);
	}
	
	/** 
	 * <p> url: http://localhost:8080/permit/permit/v1/codeexam/list<p>
	 * @GET service
	 * @return un List des CodeRouteExam objets
	 * <p>Retoune un liste des code examens avec le method queryService.getAllCodeRouteExam</p>
	 */
	@Path("list")
	@GET
	public List<CodeRouteExam> getCodeExams(){
		return queryService.getAllCodeRouteExam();
	}
	
	/** 
	 * <p> url: http://localhost:8080/permit/permit/v1/codeexam/resultat/{id}<p>
	 * @POST service
	 * @PathParam id recois un Long id d'un code
	 * @return Reponse
	 * <p>trouve un code par identifiant avec permitService.findCodeExamById() 
	 * et définit le codeExam.setCodePasse(). Puis le définit avec permitService.updateCodeExam() 
	 * puis renvoie la réponse. 
	 * </p>
	 */	
	//TODO method doesnt really make sense needs to recieve a new code result to update it rather then id to be checked
	/*
	 * @Path("resultat/{id}")
	 * 
	 * @POST public Response setCodePasse(@PathParam("id") long id) { CodeRouteExam
	 * codeExam = permitService.findCodeExamById(id);
	 * codeExam.setCodePasse(codeExam); permitService.updateCodeExam(codeExam);
	 * return Response.ok(codeExam).build();
	 * 
	 * }
	 */
	/**
	 * ********Everything above is old methods will be phased out********
	 * @param codeQ
	 * @return
	 */
	@POST
	@Path("creeQuestion")	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response creeCodeQuestion(@NotNull CodeEntity codeQ) {
		if(codeQ.getQuestion() != null && codeQ.getLesReponsePossible() != null && codeQ.getCodeSolutions() != null) {
			examService.creeCodeEntity(codeQ);
			return Response.ok(codeQ).build();
		}
		return Response.status(Response.Status.BAD_REQUEST)
				.entity("Manque des information verifie les details").build();
	}
	
	@POST
	@Path("updateQuestion")	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCodeQuestion(@NotNull CodeEntity codeQ) {
		if(codeQ.getQuestion() != null && codeQ.getLesReponsePossible() != null && codeQ.getCodeSolutions() != null) {
			examService.updateCodeEntity(codeQ);
			return Response.ok(codeQ).build();
		}
		return Response.status(Response.Status.BAD_REQUEST)
				.entity("Manque des information verifie les details").build();
	}
	
	@GET
	@Path("getQuestion")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getCodeQuestion() {
		List<CodeEntity> allCodeQuestions = examService.getAllCodeEntities();
		CodeEntity codeQuestion = new CodeEntity();
		JSONObject questionsToSend = new JSONObject();
		JSONObject singleQuestion = new JSONObject();
		//JSONArray questionPossbileReplies = new JSONArray();
		Random questionSearch = new Random();
		int examLength = 10;
		long[] questionIds = new long[examLength];
		int i = 0;
		while(i < examLength) {
			codeQuestion = allCodeQuestions.get(questionSearch.nextInt(allCodeQuestions.size() - 1));
			if(checkUniqueId(questionIds, codeQuestion.getId())) {
				questionIds[i] = codeQuestion.getId();
				singleQuestion.put("id", codeQuestion.getId());
			    singleQuestion.put("question", codeQuestion.getQuestion());
			    JSONArray questionPossbileReplies = new JSONArray(codeQuestion.getLesReponsePossible());
				singleQuestion.put("lesReponsePossible", questionPossbileReplies);
				questionsToSend.put(Long.toString(codeQuestion.getId()), singleQuestion);
				i++;
			}
		}
		if(questionsToSend.length() == examLength) {
			return questionsToSend;
		}else  return null;
	}
	
	private boolean persistQuestions(List<DriverCodeReponse> examReponses) {
		if(!examReponses.isEmpty()) {
			for (DriverCodeReponse driverCodeReponse : examReponses) {
				if(examService.creeDriverCodeReponse(driverCodeReponse) == null) {
					return false;
				}
			}
		}
		return true;
	}
	
	/*
	 * @POST
	 * 
	 * @Path("addQuestionReponse")
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) //public List<DriverCodeReponse>
	 * testReplies(JSONObject testResultat, String email) { public Response
	 * testReplies(JSONObject testResultat, String email) { JSONObject jquestion =
	 * new JSONObject(); JSONArray jresponses = new JSONArray(); ArrayList<String>
	 * codeResponses = new ArrayList<String>(); DriverCodeReponse questionResponses
	 * = new DriverCodeReponse(); List<DriverCodeReponse> examReponseList = null;
	 * CodeEntity testQuestion; int score; if(testResultat != null) { Driver driver
	 * = queryService.findDriverByEmail(email); if(driver != null) { for(String q :
	 * testResultat.keySet()) { jquestion = new JSONObject(q); testQuestion =
	 * examService.getCodeEntity(jquestion.getLong("id")); jresponses =
	 * jquestion.getJSONArray("respones"); for(int i = 0; i < jresponses.length();
	 * i++) { codeResponses.add(jresponses.getString(i)); }
	 * questionResponses.setConductaire(queryService.findDriverByEmail(email));
	 * questionResponses.setQuestionDetails(testQuestion);
	 * questionResponses.setCodeReponseDriver(codeResponses);
	 * questionResponses.setCodeReponseCorrection(codeCheckCorrectReplies(
	 * testQuestion, codeResponses)); //save to database then send response
	 * examService.creeDriverCodeReponse(questionResponses);
	 * examReponseList.add(questionResponses); } //return (List<DriverCodeReponse>)
	 * Response.ok(examReponseList).build(); return
	 * Response.ok(examReponseList).build(); }else { //error no email response
	 * return Response.status(Response.Status.BAD_REQUEST).
	 * entity("Manque l'email du conuctaire.").build(); } } //error info missing
	 * response return Response.status(Response.Status.PARTIAL_CONTENT).
	 * entity("Manque d'information.").build(); }
	 */
	
	@GET
	@Path("response{email}")
	//@Produces(MediaType.APPLICATION_JSON)
	public Collection<DriverCodeReponse> getDriverReponsesByEmail(@PathParam("email") String email){	
		return examService.getReponseByEmail(email);
	}
	
	@GET
	@Path("reponseId{id}")
	public DriverCodeReponse getDriverReponseById(@PathParam("id") Long id) {
		return examService.getDriverReponseById(id);
	}
	
	private boolean checkUniqueId(long[] arrayToCheck, long id) {
		for(int i = 0; i < arrayToCheck.length; i++) {
			if(arrayToCheck[i] == id) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * private HashMap<String, Boolean> codeCheckCorrectReplies(CodeEntity question,
	 * ArrayList<String> replies) { CodeEntity answersEntity = new CodeEntity();
	 * HashMap<String, Boolean> responseCorrection = new HashMap<String, Boolean>();
	 * if(question != null) { answersEntity =
	 * examService.getCodeById(question.getId()); for(int i = 0; i <
	 * question.getLesReponsePossible().size(); i++) { if(i <
	 * answersEntity.getCodeSolutions().size()) { if(replies.get(i) != null &&
	 * answersEntity.getCodeSolutions().contains(replies.get(i))) {
	 * responseCorrection.put(question.getLesReponsePossible().get(i), true);
	 * //responseCorrection[i] = true; }else {
	 * responseCorrection.put(question.getLesReponsePossible().get(i), false); }
	 * }else { responseCorrection.put(question.getLesReponsePossible().get(i),
	 * false); } } return responseCorrection; }else { return null; }
	 * 
	 * }
	 */
	
}












