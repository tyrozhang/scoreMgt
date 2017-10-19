package test.registerscore;

import static org.junit.Assert.*;

import org.junit.Test;

import gds.scoreMgt.domain.share.TeacherPositionEnum;
import gds.scoreMgt.domain.teachingclass.TeachingClass;
import gds.scoreMgt.domain.teachingclass.TeachingClassRepository;
import infrastructure.entityID.CourseID;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeacherID;
import infrastructure.entityID.TeachingClassID;

public class TeachingClassTest {

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

}
