package gds.scoreMgt.domain.registerscore.teachingclass;

import gds.scoreMgt.domain.share.TeacherPositionEnum;
import infrastructure.entityID.TeacherID;

/**
 *上课教员
 * @author zhangyp
 *
 */
public class CourseTeacher {
	
	private TeacherID courseTeacherID;
	private TeacherPositionEnum teacherPosition;
	
	public TeacherPositionEnum getTeacherPosition() {
		return teacherPosition;
	}

	public CourseTeacher(TeacherID courseTeacherID,TeacherPositionEnum teacherPosition)
	{
		this.courseTeacherID=courseTeacherID;
		this.teacherPosition=teacherPosition;
	}
	
	public TeacherID getCourseTeacherID() {
		return courseTeacherID;
	}

}
