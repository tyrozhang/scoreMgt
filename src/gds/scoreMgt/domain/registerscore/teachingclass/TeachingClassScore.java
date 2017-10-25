package gds.scoreMgt.domain.registerscore.teachingclass;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import gds.scoreMgt.domain.registerscore.common.BreakRuleBehaviorEnum;
import gds.scoreMgt.domain.share.ScoreTypeEnum;
import gds.scoreMgt.domain.share.mark.Mark;
import infrastructure.entityID.CourseID;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeachingClassID;

public class TeachingClassScore {
	private TeachingClassID teachingClassID;
	private CourseID courseID;
	
	private HashMap<ScoreTypeEnum,ScoreReportCard> scoreReportCards=new HashMap<ScoreTypeEnum,ScoreReportCard>();
	
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
	 * @param scoreType
	 * @param mark
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void registerScore(StudentID studentID, ScoreTypeEnum scoreType,Mark mark) throws Exception{
		
		
		//如果登记表不存在，先创建登记表
		if(!this.scoreReportCards.containsKey(scoreType)){
			this.scoreReportCards.put(scoreType, new ScoreReportCard(scoreType));
		}
		
		//登记成绩
		this.getScoreReportCard(scoreType).registerScore(studentID, mark);
	}
	
	/**
	 * 得到指定成绩登记单
	 * @return 登记单
	 */
	private ScoreReportCard getScoreReportCard(ScoreTypeEnum scoreType){
		if(this.scoreReportCards==null) return null;
		return this.scoreReportCards.get(scoreType);
	}
	
	/**
	 * 得到学生登记的某项成绩
	 * @param studentID
	 * @param scoreType
	 * @return 分数
	 */
	@SuppressWarnings("rawtypes")
	public Mark getStudentScore(StudentID studentID,ScoreTypeEnum scoreType){
		ScoreReportCard reportCard=this.getScoreReportCard(scoreType);
		if(reportCard==null) return null;
		
		return reportCard.getScore(studentID);
	}
	
	/**
	 * 得到该教学班中某学生的所有分项成绩
	 * @param studentID
	 * @return 分项成绩集合
	 */
	public HashMap<ScoreTypeEnum,Mark> getStudentScore(StudentID studentID){
		if(this.scoreReportCards.isEmpty()) return null;
		
		HashMap<ScoreTypeEnum,Mark> studentSubMarks = null;
		
		Iterator iter=this.scoreReportCards.entrySet().iterator();
		while(iter.hasNext()){
			
			Map.Entry<ScoreTypeEnum,ScoreReportCard> entry=(Map.Entry<ScoreTypeEnum,ScoreReportCard>)iter.next();
			
			Mark oneMark =entry.getValue().getScore(studentID);
			if(oneMark!=null){
				if(studentSubMarks==null) {
					studentSubMarks=new HashMap<ScoreTypeEnum,Mark>();
				}
				studentSubMarks.put(entry.getKey(), oneMark);
			}
		}
		return studentSubMarks;
	}

	/**
	 * 登记违纪行为
	 * @param studentID
	 * @param scoreType
	 * @param breakRuleBehavior
	 */
	public void registerBreakRuleBehavior(StudentID studentID, ScoreTypeEnum scoreType,
			BreakRuleBehaviorEnum breakRuleBehavior) {

		//如果登记表不存在，先创建登记表
		if(!this.scoreReportCards.containsKey(scoreType)){
			this.scoreReportCards.put(scoreType, new ScoreReportCard(scoreType));
		}
		
		//登记成绩
		this.getScoreReportCard(scoreType).registerBreakRuleBehavior(studentID, breakRuleBehavior);
		
	}

	/**
	 * 取得学生的违纪行为
	 * @param studentID
	 * @param scoreType
	 * @return
	 */
	public BreakRuleBehaviorEnum getBreakRuleBehavior(StudentID studentID, ScoreTypeEnum scoreType) {
		ScoreReportCard reportCard=this.getScoreReportCard(scoreType);
		if(reportCard==null) return null;
		
		return reportCard.getBreakRuleBehavior(studentID);
	}
	
	/**
	 * 是否违纪
	 * @param studentID
	 * @return
	 */
	public boolean studentIsBreakRule(StudentID studentID){
		
		if(this.scoreReportCards.isEmpty()) return false;
		
		Iterator iter=this.scoreReportCards.entrySet().iterator();
		while(iter.hasNext()){
			
			Map.Entry<ScoreTypeEnum,ScoreReportCard> entry=(Map.Entry<ScoreTypeEnum,ScoreReportCard>)iter.next();
			
			
			BreakRuleBehaviorEnum breakRuleBehavior =entry.getValue().getBreakRuleBehavior(studentID);
			
			
			if(breakRuleBehavior!=null){
				return true;
			}
		}
		
		return false;
	}
}
