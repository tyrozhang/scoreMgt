package test.registerscore;

import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClass;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassFactory;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassRepository;
import gds.scoreMgt.domain.share.Mark;
import gds.scoreMgt.domain.share.MarkTypeEnum;
import infrastructure.entityID.CourseID;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeachingClassID;

public class Tool {
	//生成教学班
	public static TeachingClassID createTeachingClass(CourseID courseID,String courseName){
		TeachingClassID teachingClassID=new TeachingClassID();
		TeachingClass aTeachingClass=TeachingClassFactory.createTeachingClassFactory().createTeachingClass(teachingClassID, courseID, courseName);
		TeachingClassRepository.getInstance().save(aTeachingClass);
		return teachingClassID;
	}
	
	public static StudentID AddStudentToTeachingClass(TeachingClassID teachingClassID){
		TeachingClass teachingClass=TeachingClassRepository.getInstance().getTeachingClass(teachingClassID);
		StudentID firstStudentID=new StudentID();
		teachingClass.assignStudent(firstStudentID);
		return firstStudentID;
	}
	
	public static void RegisterMark(TeachingClassID teachingClassID,StudentID studentID,MarkTypeEnum markType,Mark mark) throws Exception{
		TeachingClass teachingClass=TeachingClassRepository.getInstance().getTeachingClass(teachingClassID);
		teachingClass.registerScore(studentID,markType, mark);
	}
}
