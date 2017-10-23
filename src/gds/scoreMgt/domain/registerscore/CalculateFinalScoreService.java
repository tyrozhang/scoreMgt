package gds.scoreMgt.domain.registerscore;

import java.util.HashMap;

import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandard;
import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandardRepository;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassScore;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassScoreRepository;
import gds.scoreMgt.domain.share.CheckTypeEnum;
import gds.scoreMgt.domain.share.ScoreTypeEnum;
import gds.scoreMgt.domain.share.mark.HundredMark;
import gds.scoreMgt.domain.share.mark.Mark;
import gds.scoreMgt.domain.teachingclass.TeachingClass;
import gds.scoreMgt.domain.teachingclass.TeachingClassRepository;
import infrastructure.entityID.CourseID;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeachingClassID;

public class CalculateFinalScoreService {
	public CalculateFinalScoreService(){

	}
	
	@SuppressWarnings("rawtypes")
	public HundredMark calculateFinalScore(TeachingClassID teachingClassID,StudentID studentID) throws Exception{
		//得到学生的课程分项成绩
		TeachingClassScore teachingClassScore=TeachingClassScoreRepository.getInstance().getTeachingClassScore(teachingClassID);
		
		HashMap<ScoreTypeEnum,Mark> subScore=teachingClassScore.getStudentAllSubMark(studentID);
		
		if(subScore==null) return null;
		if(subScore.isEmpty()) return null;
		
		//得到课程的考核标准
		CourseEvaluateStandard courseEvaluateStandard=CourseEvaluateStandardRepository.getInstance().getCourseEvaluateStandard(teachingClassScore.getCourseID());
		
		//未维护当前课程的考核标准，不能进行最终成绩计算
		if(courseEvaluateStandard==null){
			throw new Exception("未找到当前课程的考核标准，请检查！");
		}
		
		if(courseEvaluateStandard.getCheckType().equals(CheckTypeEnum.INSPECT)){
			throw new Exception("考查课不需要进行成绩计算！");
		}
		
		//检查课程标准中计算最终成绩权重是否正确
		if(!courseEvaluateStandard.sumWeightingIsHundred())
		{
			throw new Exception("课程标准未制定完成，其分项成绩权重之和小于100。");
		}
		
		//检查计算总成绩所需的分项成绩是否均有
		for(ScoreTypeEnum askForMarkType:courseEvaluateStandard.getEveryMarkWeighting().keySet())
		{
			if(!subScore.containsKey(askForMarkType)){
				throw new Exception("缺少计算总成绩所需的分项成绩！");
			}
		}
			
		//计算最终成绩
		Float finalMark=0f;
		for(ScoreTypeEnum askForMarkType:courseEvaluateStandard.getEveryMarkWeighting().keySet())
		{
			Float weighting=courseEvaluateStandard.getEveryMarkWeighting().get(askForMarkType);
			HundredMark subMark=(HundredMark) subScore.get(askForMarkType);
			finalMark+=weighting*subMark.getMark();
		}
		return new HundredMark(finalMark/100);
	}
}