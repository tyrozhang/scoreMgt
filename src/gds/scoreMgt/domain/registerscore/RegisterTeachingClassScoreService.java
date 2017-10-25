package gds.scoreMgt.domain.registerscore;

import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandard;
import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandardFactory;
import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandardRepository;
import gds.scoreMgt.domain.registerscore.common.BreakRuleBehaviorEnum;
import gds.scoreMgt.domain.registerscore.registerteachingclassscore.TeachingClassScore;
import gds.scoreMgt.domain.registerscore.registerteachingclassscore.TeachingClassScoreFactory;
import gds.scoreMgt.domain.registerscore.registerteachingclassscore.TeachingClassScoreRepository;
import gds.scoreMgt.domain.share.CheckTypeEnum;
import gds.scoreMgt.domain.share.ScoreTypeEnum;
import gds.scoreMgt.domain.share.mark.Mark;
import gds.scoreMgt.domain.teachingclass.TeachingClass;
import gds.scoreMgt.domain.teachingclass.TeachingClassFactory;
import gds.scoreMgt.domain.teachingclass.TeachingClassRepository;
import infrastructure.entityID.CourseID;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeachingClassID;

/**
 * 登记教学班成绩服务
 * @author zhangyp
 *
 */
public  class RegisterTeachingClassScoreService {
	private TeachingClassID teachingClassID;
	public RegisterTeachingClassScoreService(TeachingClassID teachingClassID){
		this.teachingClassID=teachingClassID;
	}
	
	public void registerScore(StudentID studentID,ScoreTypeEnum scoreType, Mark mark) throws Exception{
	
		/*
		 * 查看学员是否是上课学员之一，后期需要去掉对教学班聚合根的依赖
		 */
		TeachingClass curTeachingClass=TeachingClassRepository.getInstance().getTeachingClass(this.teachingClassID);
		if(!curTeachingClass.isStudyStudent(studentID)) {
			throw new Exception("对不起，不是教学班上课学员，不能录入成绩！");
		}
		
		//获取教学班成绩登记对象
		TeachingClassScore curTeachingClassScore=getTeachingClassScore(this.teachingClassID);
		
		CourseEvaluateStandard courseEvaluateStandard=CourseEvaluateStandardRepository.getInstance().getCourseEvaluateStandard(curTeachingClassScore.getCourseID());
		//课程标准必须完成
		courseEvaluateStandardMustCompleted(courseEvaluateStandard);
		
		//检测考核标准中是否要求当前要录入的分项成绩
		if(!courseEvaluateStandard.requireScoreType(scoreType))
		{
			throw new Exception("当前课程没有要求考核该类成绩，请核查！");
		}
		
		//如果录入的是最终成绩，则成绩格式需符合课程标准中考试方式相应的分数格式
		if(scoreType.equals(ScoreTypeEnum.FINAL) && ! courseEvaluateStandard.markStyleIsCorrectly(mark))
		{
			throw new Exception("录入的成绩格式不符合课程考核标准中要求的考核方式，请核查！");
		}
	
		//登记成绩
		curTeachingClassScore.registerScore(studentID,scoreType, mark);
	
	}

	/**
	 * 课程标准是否完成检查
	 * @param courseEvaluateStandard
	 * @throws Exception
	 */
	private void courseEvaluateStandardMustCompleted(CourseEvaluateStandard courseEvaluateStandard) throws Exception {
		if(courseEvaluateStandard==null){
			throw new Exception("课程标准未制定,请核查!");
		}
		
		//如果要求考试方式，则需要检查课程标准中权重是否维护完善
		if(courseEvaluateStandard.getCheckType().equals(CheckTypeEnum.EXAM) &&
				!courseEvaluateStandard.sumWeightingIsHundred()){
			throw new Exception("课程标准未制定不完善，其分项成绩权重之和不是100%,请完善！");
		}
	}

	/**
	 * 登记违纪
	 * @param firstStudentID
	 * @param testpapermark
	 * @param cheated
	 * @throws Exception 
	 */
	public void registerBreakRuleBehavior(StudentID studentID, ScoreTypeEnum scoreType,
			BreakRuleBehaviorEnum breakRuleBehavior) throws Exception {
		/*
		 * 查看学员是否是上课学员之一，后期需要去掉对教学班聚合根的依赖
		 */
		TeachingClass curTeachingClass=TeachingClassRepository.getInstance().getTeachingClass(this.teachingClassID);
		if(!curTeachingClass.isStudyStudent(studentID)) {
			throw new Exception("对不起，不是教学班上课学员，不能录入违纪行为！");
		}
		
		//获取教学班成绩登记对象
		TeachingClassScore curTeachingClassScore=getTeachingClassScore(this.teachingClassID);
		curTeachingClassScore.registerBreakRuleBehavior(studentID, scoreType,breakRuleBehavior);
		
	}
	
	/*
	 * 取教学班成绩登记对象
	 */
	private TeachingClassScore getTeachingClassScore(TeachingClassID teachingClass) {
		TeachingClassScore curTeachingClassScore=TeachingClassScoreRepository.getInstance().getTeachingClassScore(teachingClass);
		if(curTeachingClassScore==null){
			TeachingClass curTeachingClass=TeachingClassRepository.getInstance().getTeachingClass(teachingClass);
			curTeachingClassScore=TeachingClassScoreFactory.getInstance().createTeachingClassScore(curTeachingClass.getTeachingClassID(), curTeachingClass.getCourseID(), curTeachingClass.getCourseName(), "", "");
			TeachingClassScoreRepository.getInstance().save(curTeachingClassScore);
		}
		return curTeachingClassScore;
	}

}
