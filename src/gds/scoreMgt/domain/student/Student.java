package gds.scoreMgt.domain.student;

import infrastructure.entityID.EntityID;
import infrastructure.entityID.StudentID;

public class Student extends EntityID {
	private StudentID studentID;
	private String studentName;
	
	public Student(StudentID studentID,String studentName)
	{
		this.studentID=studentID;
		this.studentName="studentName";
	}
	
	public StudentID getStudentID() {
		return studentID;
	}
	public String getStudentName() {
		return studentName;
	}
	
}
