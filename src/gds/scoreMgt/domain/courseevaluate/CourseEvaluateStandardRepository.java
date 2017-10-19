/**
 * 
 */
package gds.scoreMgt.domain.courseevaluate;

import java.util.HashMap;

import infrastructure.entityID.CourseID;

/**
 * @author zhangyp
 *
 */
public class CourseEvaluateStandardRepository {
	private  HashMap<CourseID,CourseEvaluateStandard> respository;
	private static CourseEvaluateStandardRepository courseEvaluateStandardRepository;
	private CourseEvaluateStandardRepository (){
		
	}
	
	public static CourseEvaluateStandardRepository getInstance()
	{
		if(courseEvaluateStandardRepository==null ){
			courseEvaluateStandardRepository=new CourseEvaluateStandardRepository();
		}
		return courseEvaluateStandardRepository;
	}
	
	public  CourseEvaluateStandard getCourseEvaluateStandard(CourseID CourseID){
		if(respository==null){
			return null;
		}
		return this.respository.getOrDefault(CourseID, null);
	}
	
	public void save(CourseEvaluateStandard CourseEvaluateStandard){
		if(respository==null){
			respository=new HashMap<CourseID,CourseEvaluateStandard>();
		}
		this.respository.put(CourseEvaluateStandard.getCourseID(), CourseEvaluateStandard);
	}
}
