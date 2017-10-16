package gds.scoreMgt.domain.registerscore;

import java.util.HashMap;

import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandard;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClass;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassRepository;
import gds.scoreMgt.domain.share.Mark;
import gds.scoreMgt.domain.share.MarkTypeEnum;
import infrastructure.entityID.CourseID;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeachingClassID;

public class CalculateFinalScoreService {
	public CalculateFinalScoreService(){

	}
	
	@SuppressWarnings("rawtypes")
	public Mark calculateFinalScore(TeachingClassID teachingClassID,StudentID studentID) throws Exception{
		//得到学生的课程分项成绩
		TeachingClass teachingClass=TeachingClassRepository.getInstance().getTeachingClass(teachingClassID);
		
		HashMap<MarkTypeEnum,Mark> subScore=teachingClass.getStudentAllSubMark(studentID);
		
		if(subScore==null) return null;
		if(subScore.isEmpty()) return null;
		
		//得到课程的考核标准
		CourseID courseID=teachingClass.getCourseID();
		//以下为模拟代码
		CourseEvaluateStandard courseEvaluateStandard=new CourseEvaluateStandard(courseID);
		courseEvaluateStandard.addRequireMarkTypes(MarkTypeEnum.DAILYPORFORMANCE);
		courseEvaluateStandard.addRequireMarkTypes(MarkTypeEnum.TESTPAPERMARK);
		
		courseEvaluateStandard.setCalculateFinalScoreUsingSubmarkWeighting(MarkTypeEnum.DAILYPORFORMANCE,30f);
		courseEvaluateStandard.setCalculateFinalScoreUsingSubmarkWeighting(MarkTypeEnum.TESTPAPERMARK,70f);
		
		//检查课程标准中计算最终成绩权重是否正确
		if(!courseEvaluateStandard.sumWeightingIsHundred())
		{
			throw new Exception("课程标准未制定完成，其分项成绩权重之和小于100。");
		}
		
		//检查计算总成绩所需的分项成绩是否均有
		for(MarkTypeEnum askForMarkType:courseEvaluateStandard.getEveryMarkWeighting().keySet())
		{
			if(!subScore.containsKey(askForMarkType)){
				throw new Exception("缺少计算总成绩所需的分项成绩！");
			}
		}
			
		//计算最终成绩
		Float finalMark=0f;
		for(MarkTypeEnum askForMarkType:courseEvaluateStandard.getEveryMarkWeighting().keySet())
		{
			Float weighting=courseEvaluateStandard.getEveryMarkWeighting().get(askForMarkType);
			Float subMark=(Float) subScore.get(askForMarkType).getMark();
			finalMark+=weighting*subMark;
		}
		return new Mark<Float>(finalMark/100);
	}
}