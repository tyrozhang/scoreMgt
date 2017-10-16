package test.registerscore;

import static org.junit.Assert.*;

import org.junit.Test;

import gds.scoreMgt.domain.registerscore.teachingclass.ScoreReportCard;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClass;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassFactory;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassRepository;
import gds.scoreMgt.domain.share.Mark;
import gds.scoreMgt.domain.share.MarkTypeEnum;
import gds.scoreMgt.domain.share.TeacherPositionEnum;
import infrastructure.entityID.CourseID;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeacherID;
import infrastructure.entityID.TeachingClassID;
import junit.framework.Assert;

public class RegisterTeachingClassScoreTest {

	
	@Test
	public void testAssignCourseTeachers() {
		
		//生成教学班
		CourseID courseID=new CourseID();
		String courseName="高等数学";
		TeachingClassID teachingClassID=Tool.createTeachingClass(courseID, courseName);;
	
		
		TeachingClass aTeachingClass=TeachingClassRepository.getInstance().getTeachingClass(teachingClassID);
		
		/*test*/
		TeacherID assignMajorTeacherID=new TeacherID();
		aTeachingClass.assignCourseTeacher(assignMajorTeacherID,TeacherPositionEnum.MAJOR);
		
		TeacherID assignAssistTeacherID=new TeacherID();
		aTeachingClass.assignCourseTeacher(assignAssistTeacherID,TeacherPositionEnum.ASSIST);
		
		
		assertEquals(teachingClassID.getID(), aTeachingClass.getTeachingClassID().getID());
		assertTrue(aTeachingClass.getMajorCourseTeachers().stream().anyMatch(t->t.getID().equals(assignMajorTeacherID.getID())));
	}

	@Test
	public void testAssignStudent() {
		
		//生成教学班
		CourseID courseID=new CourseID();
		String courseName="高等数学";
		TeachingClassID teachingClassID=Tool.createTeachingClass(courseID, courseName);;
		
		//添加学生
		StudentID firstStudentID=Tool.AddStudentToTeachingClass(teachingClassID);
		StudentID secondStudentID=Tool.AddStudentToTeachingClass(teachingClassID);

		TeachingClass aTeachingClass=TeachingClassRepository.getInstance().getTeachingClass(teachingClassID);
		
		assertTrue(aTeachingClass.getStudents().stream().anyMatch(t->t.getID().equals(firstStudentID.getID())));
		assertTrue(aTeachingClass.getStudents().stream().anyMatch(t->t.getID().equals(secondStudentID.getID())));
		assertFalse(aTeachingClass.getStudents().stream().anyMatch(t->t.getID().equals("notExistStudentID")));
	}
	
	/**
	 *教学班路成绩测试
	 * @throws Exception 
	 */
	@Test
	public void registerScores() throws Exception {
		
		//生成教学班
		CourseID courseID=new CourseID();
		String courseName="高等数学";
		TeachingClassID teachingClassID=Tool.createTeachingClass(courseID, courseName);;
		
		//添加学生
		StudentID firstStudentID=Tool.AddStudentToTeachingClass(teachingClassID);
		StudentID secondStudentID=Tool.AddStudentToTeachingClass(teachingClassID);

		
		//登记平时成绩
		Tool.RegisterMark(teachingClassID,firstStudentID,MarkTypeEnum.DAILYPORFORMANCE, new Mark<Float>(80f));
		Tool.RegisterMark(teachingClassID,secondStudentID,MarkTypeEnum.DAILYPORFORMANCE, new Mark<Float>(90f));
		
		//登记考试成绩
		Tool.RegisterMark(teachingClassID,firstStudentID,MarkTypeEnum.TESTPAPERMARK, new Mark<Float>(75f));
		Tool.RegisterMark(teachingClassID,secondStudentID,MarkTypeEnum.TESTPAPERMARK, new Mark<Float>(87f));
		
		TeachingClass aTeachingClass=TeachingClassRepository.getInstance().getTeachingClass(teachingClassID);
		
		//System.out.println(aTeachingClass.getStudentScore(firstStudentID).getClass());
		assertTrue(aTeachingClass.getStudentScore(firstStudentID,MarkTypeEnum.TESTPAPERMARK).getMark().equals(75f));
		assertTrue(aTeachingClass.getStudentScore(secondStudentID,MarkTypeEnum.DAILYPORFORMANCE).getMark().equals(90f));
		assertFalse(aTeachingClass.getStudentScore(firstStudentID,MarkTypeEnum.DAILYPORFORMANCE).getMark().equals(75f));
		
	}
}
