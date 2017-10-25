package gds.scoreMgt.domain.registerscore;

import java.util.HashMap;

import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandard;
import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandardRepository;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassScore;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassScoreRepository;
import gds.scoreMgt.domain.share.CheckTypeEnum;
import gds.scoreMgt.domain.share.ScoreTypeEnum;
import gds.scoreMgt.domain.share.mark.HundredMark;
import gds.scoreMgt.domain.share.mark.LevelMark;
import gds.scoreMgt.domain.share.mark.Mark;
import gds.scoreMgt.domain.share.mark.TwoLevelMarkTypeEnum;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeachingClassID;

public class CalculateFinalScoreService {
	public CalculateFinalScoreService(){

	}
	
	public Mark calculateFinalScore(TeachingClassID teachingClassID,StudentID studentID) throws Exception{
		
		TeachingClassScore teachingClassScore=TeachingClassScoreRepository.getInstance().getTeachingClassScore(teachingClassID);
		
		//得到课程的考核标准
		CourseEvaluateStandard courseEvaluateStandard=CourseEvaluateStandardRepository.getInstance().getCourseEvaluateStandard(teachingClassScore.getCourseID());
		
		//未维护当前课程的考核标准，不能进行最终成绩计算
		if(courseEvaluateStandard==null){
			throw new Exception("当前课程的考核标准未制定，请检查！");
		}		

		
		//如果存在作弊行为，最终成绩为0或不通过
		if(teachingClassScore.studentIsBreakRule(studentID)){
			if(courseEvaluateStandard.getCheckType().equals(CheckTypeEnum.EXAM)){
				return new HundredMark(0f);
			}else{
				return new LevelMark(TwoLevelMarkTypeEnum.FAIL);
			}
		}
		
		//检查课程标准中计算最终成绩权重是否正确
		if(!courseEvaluateStandard.sumWeightingIsHundred())
		{
			throw new Exception("课程标准未制定完成，其分项成绩权重之和小于100。");
		}

		
		//得到学生的课程分项成绩
		HashMap<ScoreTypeEnum,Mark> subScore=teachingClassScore.getStudentScore(studentID);
				
		//如果没有分项成绩也无需进一步计算
		if(subScore==null) return null;
		if(subScore.isEmpty()) return null;
		
		//检查计算总成绩所需的分项成绩是否均有
		for(ScoreTypeEnum askForMarkType:courseEvaluateStandard.getEveryMarkWeighting().keySet())
		{
			if(!subScore.containsKey(askForMarkType)){
				throw new Exception("缺少计算总成绩所需的分项成绩！");
			}
		}
			
		//计算成绩
		if(courseEvaluateStandard.getCheckType().equals(CheckTypeEnum.EXAM)){
			return calculateHundredFinalMark(subScore, courseEvaluateStandard);
		}else{
			return calculateLevelFinalMark(subScore, courseEvaluateStandard);
		}

	}

	/**
	 * 计算百分制最终成绩
	 * @param subScore
	 * @param courseEvaluateStandard
	 * @return HundredMark
	 * @throws Exception
	 */
	private HundredMark calculateHundredFinalMark(HashMap<ScoreTypeEnum, Mark> subScore, CourseEvaluateStandard courseEvaluateStandard)
			throws Exception {
		Float finalMark=0f;
		for(ScoreTypeEnum askForMarkType:courseEvaluateStandard.getEveryMarkWeighting().keySet())
		{
			Float weighting=courseEvaluateStandard.getEveryMarkWeighting().get(askForMarkType);
			HundredMark subMark=(HundredMark) subScore.get(askForMarkType);
			finalMark+=weighting*subMark.getMark();
		}
		return new HundredMark(finalMark/100);
	}
	
	/**
	 * 计算等级制最终成绩
	 * @param subScore
	 * @param courseEvaluateStandard
	 * @return
	 * @throws Exception
	 */
	private LevelMark calculateLevelFinalMark(HashMap<ScoreTypeEnum, Mark> subScore, CourseEvaluateStandard courseEvaluateStandard)
			throws Exception {
		HundredMark dagitalFinalMark=calculateHundredFinalMark(subScore, courseEvaluateStandard);
		
		if(dagitalFinalMark.getMark()>60F){
			return new LevelMark(TwoLevelMarkTypeEnum.PASS);
		}
		
		return new LevelMark(TwoLevelMarkTypeEnum.FAIL);
	}
}