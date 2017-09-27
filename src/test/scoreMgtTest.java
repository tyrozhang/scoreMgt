package test;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.share.TeacherPositionEnum;
import domain.teachingclass.TeachingClass;
import domain.teachingclass.TeachingClassFactory;
import infrastructure.entityID.CourseID;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeacherID;
import infrastructure.entityID.TeachingClassID;
import junit.framework.Assert;

public class scoreMgtTest {

	
	@Test
	public void testAssignCourseTeachers() {
		
		TeachingClassID teachingClassID=new TeachingClassID();
		CourseID courseID=new CourseID();

		String courseName="张三";
		TeachingClass aTeachingClass=TeachingClassFactory.createTeachingClassFactory().createTeachingClass(teachingClassID, courseID, courseName);
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
		
		TeachingClassID teachingClassID=new TeachingClassID();
		CourseID courseID=new CourseID();

		String courseName="李四";
		TeachingClass aTeachingClass=TeachingClassFactory.createTeachingClassFactory().createTeachingClass(teachingClassID, courseID, courseName);
		
		StudentID firstStudentID=new StudentID();
		aTeachingClass.assignStudent(firstStudentID);
		
		StudentID secondStudentID=new StudentID();
		aTeachingClass.assignStudent(secondStudentID);
		
		assertTrue(aTeachingClass.getStudents().stream().anyMatch(t->t.getID().equals(firstStudentID.getID())));
		assertTrue(aTeachingClass.getStudents().stream().anyMatch(t->t.getID().equals(secondStudentID.getID())));
		assertFalse(aTeachingClass.getStudents().stream().anyMatch(t->t.getID().equals("notExistStudentID")));
	}
}
