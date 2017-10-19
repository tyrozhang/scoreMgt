package gds.scoreMgt.domain.registerscore.teachingclass;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	//private HashMap<MarkTypeEnum,ScoreReportCard> scoreReportCards=new HashMap<MarkTypeEnum,ScoreReportCard>();
	
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
	/**
	 * 登记成绩
	 * @param studentID
	 * @param markType
	 * @param mark
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void registerScore(StudentID studentID, MarkTypeEnum markType,Mark mark) throws Exception{
		//登记成绩
		this.registerScore(studentID, mark);
	}
		
	/**
	 * 得到学生登记的某项成绩
	 * @param studentID
	 * @param markType
	 * @return 分数
	 */
	@SuppressWarnings("rawtypes")
	public Mark getStudentScore(StudentID studentID){
		return this.getScore(studentID);
	}
	
	/**
	 * 得到该教学班中某学生的所有分项成绩
	 * @param studentID
	 * @return 分项成绩集合
	 */
/*	public HashMap<MarkTypeEnum,Mark> getStudentAllSubMark(StudentID studentID){
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
	}*/
}
