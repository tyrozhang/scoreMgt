/**
 * 
 */
package gds.scoreMgt.domain.registerscore.registerteachingclassscore;

import infrastructure.entityID.CourseID;
import infrastructure.entityID.TeachingClassID;

/**
 * 教学班成绩工厂
 * @author zhangyp
 *
 */
public  class TeachingClassScoreFactory {
	private static TeachingClassScoreFactory teachingClassScoreFactory=null;
	private TeachingClassScoreFactory(){
		
	}
	
	public static TeachingClassScoreFactory getInstance()
	{
		if (teachingClassScoreFactory==null){
			teachingClassScoreFactory=new TeachingClassScoreFactory();
		}
		return teachingClassScoreFactory;
	}
	
	public  TeachingClassScore createTeachingClassScore(TeachingClassID teachingClassID,CourseID courseID,String courseName,String courseTeachersDescript,
			String studyStudentsDescript)
	{
		return new TeachingClassScore(teachingClassID,courseID,courseName,courseTeachersDescript,studyStudentsDescript);
	}
}
