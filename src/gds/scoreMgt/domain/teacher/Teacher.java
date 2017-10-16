/**
 * 
 */
package gds.scoreMgt.domain.teacher;

import infrastructure.entityID.TeacherID;

/**
 * 教员
 * @author zhangyp
 *
 */
public class Teacher {
	private TeacherID teacherID;
	private String teacherName="";
	
	public Teacher( TeacherID teacherID,String teacherName){
		this.teacherID=teacherID;
		this.teacherName=teacherName;
	}
	
	public TeacherID getTeacherID() {
		return teacherID;
	}
	
	public String getTeacherName() {
		return teacherName;
	}
	
}
