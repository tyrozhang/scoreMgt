package test.registerscore;

import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassScore;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassScoreFactory;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassScoreRepository;
import gds.scoreMgt.domain.share.Mark;
import gds.scoreMgt.domain.share.MarkTypeEnum;
import gds.scoreMgt.domain.teachingclass.TeachingClass;
import gds.scoreMgt.domain.teachingclass.TeachingClassFactory;
import gds.scoreMgt.domain.teachingclass.TeachingClassRepository;
import infrastructure.entityID.CourseID;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeachingClassID;

public class Tool {
	//生成教学班
	public static TeachingClassID createTeachingClass(CourseID courseID,String courseName){
		TeachingClassID teachingClassID=new TeachingClassID();
		TeachingClass aTeachingClass=TeachingClassFactory.getInstance().createTeachingClass(teachingClassID, courseID, courseName);
		TeachingClassRepository.getInstance().save(aTeachingClass);
		return teachingClassID;
	}
	
	
	public static StudentID AddStudentToTeachingClass(TeachingClassID teachingClassID){
		TeachingClass teachingClass=TeachingClassRepository.getInstance().getTeachingClass(teachingClassID);
		StudentID firstStudentID=new StudentID();
		teachingClass.assignStudent(firstStudentID);
		return firstStudentID;
	}
	

	//生成教学班成绩
	public static void createTeachingClassScore(TeachingClassID teachingClassID,CourseID courseID,String courseName,String courseTeachersDescript,String studyStdentsDescript){

		TeachingClassScore aTeachingClassScore=TeachingClassScoreFactory.getInstance().createTeachingClassScore(teachingClassID, courseID, courseName, courseTeachersDescript, studyStdentsDescript);
		TeachingClassScoreRepository.getInstance().save(aTeachingClassScore);
	}
	
	/*
	 * 登记成绩
	 */
	public static void RegisterMark(TeachingClassID teachingClassID,StudentID studentID,MarkTypeEnum markType,Mark mark) throws Exception{
		TeachingClassScore aTeachingClassScore=TeachingClassScoreRepository.getInstance().getTeachingClassScore(teachingClassID);
		aTeachingClassScore.registerScore(studentID,markType, mark);
	}
}
