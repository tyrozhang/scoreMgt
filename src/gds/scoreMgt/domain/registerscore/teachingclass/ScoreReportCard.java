package gds.scoreMgt.domain.registerscore.teachingclass;

import java.util.HashMap;
import java.util.List;

import gds.scoreMgt.domain.share.Mark;
import gds.scoreMgt.domain.share.MarkTypeEnum;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeachingClassID;

/**
 * 教学班成绩
 * @author zhangyp
 *
 */
public class ScoreReportCard {
	
	private MarkTypeEnum examType;
	private HashMap<StudentID,Mark> scores;
	
	public ScoreReportCard(MarkTypeEnum examType)
	{
		this.examType=examType;
	}

	public MarkTypeEnum getExamType() {
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

	public Mark getScore(StudentID studentID){
		if(scores==null) return null;
		return this.scores.getOrDefault(studentID, null);
	}	 
}
