/**
 * 
 */
package gds.scoreMgt.domain.registerscore.teachingclass;

import infrastructure.entityID.CourseID;
import infrastructure.entityID.TeachingClassID;

/**
 * 教学班工厂
 * @author zhangyp
 *
 */
public  class TeachingClassFactory {
	private static TeachingClassFactory teachingClassFactory=null;
	private TeachingClassFactory(){
		
	}
	
	public static TeachingClassFactory createTeachingClassFactory()
	{
		if (teachingClassFactory==null){
			teachingClassFactory=new TeachingClassFactory();
		}
		return teachingClassFactory;
	}
	
	public  TeachingClass createTeachingClass(TeachingClassID teachingClassID,CourseID courseID,String courseName)
	{
		return new TeachingClass(teachingClassID,courseID,courseName);
	}
}
