package permitReglePOJO;

import javax.ejb.Stateless;

import org.json.JSONArray;

@Stateless
public class PermitPratiquePOJO {
	
	public float[] jsonArrToFloatArr(JSONArray jsArr) {
		if(jsArr != null) {
			float[] points = new float[jsArr.length()];
			for (int i = 0; i < jsArr.length(); i++){
				points[i] = jsArr.optFloat(i);
			}
			return points;
		}
		return null;
	}
	
	public float calculateNote(float[] noteArr, String type) {
		float totalNote = 0;
		try {
			switch (type) {
			case "B":
				if(noteArr.length == 9) {
					for(int i = 0; i < noteArr.length; i++) {
						if(noteArr[i] == -1) {
							return -1;
						}
						if(noteArr[i] <= 3 && noteArr[i] >= 0) {
							totalNote += Math.round(noteArr[i]);
						}
					}
				}break;
			case "A":
				for(int i = 0; i < noteArr[i]; i++) {
					if(noteArr[i] <= 1 && noteArr[i] >= 0) {
						totalNote += Math.round(noteArr[i]);
					}
					
				}break;
			case "S":
				float note = 0;
				float n = 0;
				for(int i = 0; i < noteArr[i]; i++) {
					n = noteArr[i];
					if(n <= 0 && n <= 0.2) {
						note = (float) 0.0;
					}
					else if(n > 0.2 && n < 0.5)
						note = (float) 0.5;
					else if(n >= 0.5 && n <= 1.0)
						note = (float) 1.0;
					totalNote += note;
								
				}

			default:
				break;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
