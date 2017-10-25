/**
 * 
 */
package gds.scoreMgt.domain.registerscore.registerteachingclassscore;

import java.util.HashMap;

import infrastructure.entityID.TeachingClassID;

/**
 * @author zhangyp
 *
 */
public class TeachingClassScoreRepository {
	private  HashMap<TeachingClassID,TeachingClassScore> respository;
	private static TeachingClassScoreRepository teachingClassScoreRepository;
	private TeachingClassScoreRepository (){
		
	}
	
	public static TeachingClassScoreRepository getInstance()
	{
		if(teachingClassScoreRepository==null ){
			teachingClassScoreRepository=new TeachingClassScoreRepository();
		}
		return teachingClassScoreRepository;
	}
	
	public  TeachingClassScore getTeachingClassScore(TeachingClassID teachingClassID){
		if(respository==null){
			return null;
		}
		return this.respository.getOrDefault(teachingClassID, null);
	}
	
	public void save(TeachingClassScore teachingClassScore){
		if(respository==null){
			respository=new HashMap<TeachingClassID,TeachingClassScore>();
		}
		this.respository.put(teachingClassScore.getTeachingClassID(), teachingClassScore);
	}
}
