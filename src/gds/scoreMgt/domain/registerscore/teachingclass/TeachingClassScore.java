package gds.scoreMgt.domain.registerscore.teachingclass;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import gds.scoreMgt.domain.share.Mark;
import gds.scoreMgt.domain.share.MarkTypeEnum;
import infrastructure.entityID.CourseID;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeachingClassID;

public class TeachingClassScore {
	private TeachingClassID teachingClassID;
	private CourseID courseID;
	
	private HashMap<MarkTypeEnum,ScoreReportCard> scoreReportCards=new HashMap<MarkTypeEnum,ScoreReportCard>();
	
	public TeachingClassScore(TeachingClassID teachingClassID,CourseID courseID,String courseName,String courseTeachersDescript,
			String studyStudentsDescript){
		this.teachingClassID=teachingClassID;
		this.courseID=courseID;
		
	}
	
	public CourseID getCourseID() {
		return courseID;
	}

	public TeachingClassID getTeachingClassID() {
		return teachingClassID;
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
		
		HashMap<MarkTypeEnum,Mark> studentSubMarks = null;
		
		Iterator iter=this.scoreReportCards.entrySet().iterator();
		while(iter.hasNext()){
			
			Map.Entry<MarkTypeEnum,ScoreReportCard> entry=(Map.Entry<MarkTypeEnum,ScoreReportCard>)iter.next();
			
			Mark oneMark =entry.getValue().getScore(studentID);
			if(oneMark!=null){
				if(studentSubMarks==null) {
					studentSubMarks=new HashMap<MarkTypeEnum,Mark>();
				}
				studentSubMarks.put(entry.getKey(), oneMark);
			}
		}
		return studentSubMarks;
	}
}
