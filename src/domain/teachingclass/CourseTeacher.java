package domain.teachingclass;

import domain.share.TeacherPositionEnum;
import infrastructure.entityID.TeacherID;

/**
<<<<<<< HEAD
 * 授课教员
=======
 *上课教员
>>>>>>> origin/master
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
