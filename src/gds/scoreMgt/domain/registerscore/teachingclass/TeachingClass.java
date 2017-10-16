package gds.scoreMgt.domain.registerscore.teachingclass;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import gds.scoreMgt.domain.share.Mark;
import gds.scoreMgt.domain.share.MarkTypeEnum;
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
	private HashMap<MarkTypeEnum,ScoreReportCard> scoreReportCards=new HashMap<MarkTypeEnum,ScoreReportCard>();
	
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
	
	/**
	 * 登记成绩
	 * @param studentID
	 * @param markType
	 * @param mark
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void registerScore(StudentID studentID, MarkTypeEnum markType,Mark mark) throws Exception{
		//校验学生是否是本教学班学生
		if(this.students==null) {
			throw new Exception("对不起，不是教学班上课学员，不能录入成绩！");
		}
		
		if(!this.students.contains(studentID)){
			throw new Exception("对不起，不是教学班上课学员，不能录入成绩！");
		}
		
		//如果登记表不存在，先创建登记表
		if(!this.scoreReportCards.containsKey(markType)){
			this.scoreReportCards.put(markType, new ScoreReportCard(markType));
		}
		
		//登记成绩
		this.getScoreReportCard(markType).registerScore(studentID, mark);
	}
	
	/**
	 * 得到指定成绩登记单
	 * @return 登记单
	 */
	private ScoreReportCard getScoreReportCard(MarkTypeEnum markType){
		if(this.scoreReportCards==null) return null;
		return this.scoreReportCards.get(markType);
	}
	
	/**
	 * 得到学生登记的某项成绩
	 * @param studentID
	 * @param markType
	 * @return 分数
	 */
	@SuppressWarnings("rawtypes")
	public Mark getStudentScore(StudentID studentID,MarkTypeEnum markType){
		ScoreReportCard reportCard=this.getScoreReportCard(markType);
		if(reportCard==null) return null;
		
		return reportCard.getScore(studentID);
	}
	
	/**
	 * 得到该教学班中某学生的所有分项成绩
	 * @param studentID
	 * @return 分项成绩集合
	 */
	public HashMap<MarkTypeEnum,Mark> getStudentAllSubMark(StudentID studentID){
		if(this.scoreReportCards.isEmpty()) return null;
		
		HashMap<MarkTypeEnum,Mark> studentSubMark = null;
		
		Iterator iter=this.scoreReportCards.entrySet().iterator();
		while(iter.hasNext()){
			
			Map.Entry<MarkTypeEnum,ScoreReportCard> entry=(Map.Entry<MarkTypeEnum,ScoreReportCard>)iter.next();
			
			Mark oneMark =entry.getValue().getScore(studentID);
			if(oneMark!=null){
				if(studentSubMark==null) {
					studentSubMark=new HashMap<MarkTypeEnum,Mark>();
				}
				studentSubMark.put(entry.getKey(), oneMark);
			}
		}
		return studentSubMark;
	}
}
