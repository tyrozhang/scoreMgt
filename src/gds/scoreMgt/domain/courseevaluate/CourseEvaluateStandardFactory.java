/**
 * 
 */
package gds.scoreMgt.domain.courseevaluate;

import infrastructure.entityID.CourseID;


/**
 * 教学班工厂
 * @author zhangyp
 *
 */
public  class CourseEvaluateStandardFactory {
	private static CourseEvaluateStandardFactory courseEvaluateStandardFactory=null;
	private CourseEvaluateStandardFactory(){
		
	}
	
	public static CourseEvaluateStandardFactory createCourseEvaluateStandardFactory()
	{
		if (courseEvaluateStandardFactory==null){
			courseEvaluateStandardFactory=new CourseEvaluateStandardFactory();
		}
		return courseEvaluateStandardFactory;
	}
	
	public  CourseEvaluateStandard createCourseEvaluateStandard(CourseID CourseID)
	{
		return new CourseEvaluateStandard(CourseID);
	}
}
