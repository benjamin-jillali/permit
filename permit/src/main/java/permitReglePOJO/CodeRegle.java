package permitReglePOJO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.CodeEntity;
import entity.Driver;
import entity.DriverCodeReponse;
import service.ExamService;
import service.PermitService;
import service.QueryService;

@Stateless
public class CodeRegle {
	@Inject
	PermitService permitService;
	@Inject
	ExamService examService;
	@Inject 
	QueryService queryService;
	
	public List<DriverCodeReponse> testReplies(JSONObject testResultat, String email) {
		JSONObject jquestion = new JSONObject();
		JSONArray jresponses = new JSONArray();
		ArrayList<String> codeResponses = new ArrayList<String>();		
		DriverCodeReponse questionResponses = new DriverCodeReponse();
		List<DriverCodeReponse> examReponseList = null;
		CodeEntity testQuestion;
		int score;
		if(testResultat != null) {
			Driver driver = queryService.findDriverByEmail(email);
			if(driver != null) {
				for(String q : testResultat.keySet()) {
					jquestion = new JSONObject(q);
					testQuestion = examService.getCodeEntity(jquestion.getLong("id"));
					jresponses = jquestion.getJSONArray("respones");
					for(int i = 0; i < jresponses.length(); i++) {
						codeResponses.add(jresponses.getString(i));
					}					
					questionResponses.setConductaire(queryService.findDriverByEmail(email));
					questionResponses.setQuestionDetails(testQuestion);
					questionResponses.setCodeReponseDriver(codeResponses);
					questionResponses.setCodeReponseCorrection(codeCheckCorrectReplies(testQuestion, codeResponses));
					//save to database then send response
					//examService.creeDriverCodeReponse(questionResponses);
					examReponseList.add(questionResponses);					
				}
				//return (List<DriverCodeReponse>) Response.ok(examReponseList).build();
				return examReponseList;
			}else {
				//error no email response
				return null;
			}
		}
		//error info missing response
		return null;
	}
	
	
	private HashMap<String, Integer> codeCheckCorrectReplies(CodeEntity question, ArrayList<String> replies){
		//rules. -1 answer wrong 1 answer right 0 no answer ig possible replies a, b, c, d correct answers a, b, c
		//replies a, c, d correct digits = 1, 1, 1, 0 answer digits are 1, -1(because b is missing), 1, -1(shouldnt have responded)
		CodeEntity answersEntity = new CodeEntity();
		HashMap<String, Integer> responseCorrection = new HashMap<String, Integer>();
		int ansSize = 0;
		if(question != null) {
			answersEntity = examService.getCodeById(question.getId());
			//Sets default point values to 0 for all possible replies
			for(int i = 0; i < answersEntity.getLesReponsePossible().size(); i++) {
				responseCorrection.put(answersEntity.getLesReponsePossible().get(i), 0);
			}
			//checks the size of replies and correction arrays to find the biggest then set it to the answer size to loop through		
			if(answersEntity.getCodeSolutions().size() <= replies.size())
				ansSize = replies.size();
			else if(answersEntity.getCodeSolutions().size() > replies.size())
				ansSize = answersEntity.getCodeSolutions().size();
			//checks the response and matches it to correction then sets the values accordingly		
			for(int i = 0; i < ansSize; i++) {
				if(answersEntity.getCodeSolutions().contains(replies.get(i))) 
					responseCorrection.put(question.getLesReponsePossible().get(i), 1);
				else
					responseCorrection.put(question.getLesReponsePossible().get(i), -1);
			}
			return responseCorrection;
		}
		return null;
	}	
	

}















