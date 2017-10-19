package gds.scoreMgt.domain.teachingclass;


import java.util.ArrayList;
import gds.scoreMgt.domain.share.TeacherPositionEnum;
import infrastructure.entityID.CourseID;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeacherID;
import infrastructure.entityID.TeachingClassID;

/**
 * 
 * @author zhangyp
 */
public class TeachingClass {
	
	
	private TeachingClassID teachingClassID;
	private CourseID courseID;
	private String courseName;
	private String studingInterval;
	private ArrayList<CourseTeacher> courseTeachers;
	private ArrayList<StudentID> students;
	
	
	public TeachingClass(TeachingClassID teachingClassID,CourseID courseID,String courseName){
		this.teachingClassID=teachingClassID;
		this.courseID=courseID;
		this.courseName=courseName;
	}
	
	public String getStudingInterval() {
		return studingInterval;
	}

	public void setStudingInterval(String studingInterval) {
		this.studingInterval = studingInterval;
	}

	public TeachingClassID getTeachingClassID() {
		return teachingClassID;
	}

	public CourseID getCourseID() {
		return courseID;
	}

	public String getCourseName() {
		return courseName;
	}
	
	public ArrayList<StudentID> getStudents() {
		return this.students;
	}
	
	/*获得主讲教员*/
	public  ArrayList<TeacherID> getMajorCourseTeachers() {
		return getCourseTeachersByPosition(TeacherPositionEnum.MAJOR);
	}
	
	/*获得辅讲教员*/
	public  ArrayList<TeacherID> getAssistCourseTeachers() {
		return getCourseTeachersByPosition(TeacherPositionEnum.ASSIST);
	}
	
	/*获得指定类型教员*/
	private ArrayList<TeacherID> getCourseTeachersByPosition(TeacherPositionEnum position) {
		if(this.courseTeachers==null) return null;
		if(this.courseTeachers.size()==0) return null;
		
		ArrayList matchCourseTeachers=new ArrayList();
		for(CourseTeacher teacher:this.courseTeachers){
			if(teacher.getTeacherPosition().equals(position)){
				matchCourseTeachers.add(teacher.getCourseTeacherID());
			}
		}
		
		if(matchCourseTeachers.size()==0) return null;
		return matchCourseTeachers;
		
	}

	
	public void assignCourseTeacher(TeacherID teacherID, TeacherPositionEnum teacherPosition){
		if(this.courseTeachers==null) {
			courseTeachers=new ArrayList<CourseTeacher>();
		}
		
		CourseTeacher courseTeacher=new CourseTeacher(teacherID,teacherPosition);
		
		this.courseTeachers.add(courseTeacher);
	}
	
	public void assignStudent(StudentID studentID){
		if(this.students==null) {
			students=new ArrayList<StudentID>();
		}
		
		this.students.add(studentID);
	} 
	
	/*
	 * 是否上课学员
	 */
	public boolean isStudyStudent(StudentID studentID){
		if(this.students==null) {
			return false;
		}
		
		if(this.students.contains(studentID)){
			return true;
		}
		
		return false;
	}
	
}
