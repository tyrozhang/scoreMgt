package gds.scoreMgt.domain.registerscore.teachingclass;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gds.scoreMgt.domain.registerscore.common.BreakRuleBehaviorEnum;
import gds.scoreMgt.domain.share.ScoreTypeEnum;
import gds.scoreMgt.domain.share.mark.Mark;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeachingClassID;

/**
 * 教学班成绩
 * @author zhangyp
 *
 */
public class ScoreReportCard {
	
	private ScoreTypeEnum examType;
	//学生分数
	private HashMap<StudentID,Mark> scores;
	//违纪行为
	private HashMap<StudentID,BreakRuleBehaviorEnum> breakRules;
	
	public ScoreReportCard(ScoreTypeEnum examType)
	{
		this.examType=examType;
	}

	public ScoreTypeEnum getExamType() {
		return examType;
	}
	
	/**
	 * 登记成绩
	 */
	public void registerScore(StudentID studentID, Mark mark){
		
		if(scores==null){
			scores=new HashMap<StudentID,Mark>();
		}
		
		this.scores.put(studentID, mark);
	}

	/**
	 * 登记考试违纪行为
	 * @param studentID
	 * @param breakRuleBehavior 考试违纪行为
	 */
	public void registerBreakRuleBehavior(StudentID studentID, BreakRuleBehaviorEnum breakRuleBehavior) {
		if(breakRules==null){
			breakRules=new HashMap<StudentID,BreakRuleBehaviorEnum>();
		}
		
		this.breakRules.put(studentID, breakRuleBehavior);
	}
	

	/**
	 * 得到学员该分项成绩
	 */
	public Mark getScore(StudentID studentID){
		if(scores==null) return null;
		return this.scores.getOrDefault(studentID, null);
	}

	/**
	 * 取得学生在本次考试中违纪行为
	 * @param studentID
	 * @return
	 */
	public BreakRuleBehaviorEnum getBreakRuleBehavior(StudentID studentID) {
		if(breakRules==null) return null;
		return this.breakRules.getOrDefault(studentID, null);
	}

}
