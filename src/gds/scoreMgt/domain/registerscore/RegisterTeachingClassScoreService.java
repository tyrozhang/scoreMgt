package gds.scoreMgt.domain.registerscore;

import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandard;
import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandardFactory;
import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandardRepository;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassScore;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassScoreFactory;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassScoreRepository;
import gds.scoreMgt.domain.share.CheckTypeEnum;
import gds.scoreMgt.domain.share.Mark;
import gds.scoreMgt.domain.share.ScoreTypeEnum;
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
public  class RegisterTeachingClassScoreService<T> {
	public RegisterTeachingClassScoreService(){
		super();
	}
	
	public void registerScore(TeachingClassID teachingClassID,StudentID studentID, ScoreTypeEnum scoreType,Mark<T> mark) throws Exception{
	
		/*
		 * 查看学员是否是上课学员之一
		 */
		TeachingClass curTeachingClass=TeachingClassRepository.getInstance().getTeachingClass(teachingClassID);
		if(!curTeachingClass.isStudyStudent(studentID)) {
			throw new Exception("对不起，不是教学班上课学员，不能录入成绩！");
		}
		
		//获取教学班成绩登记对象
		TeachingClassScore curTeachingClassScore=getTeachingClassScore(curTeachingClass);
		
		/*
		 * 检查登记的成绩类型是否符合当前课程的考核标准要求
		 */
		CourseEvaluateStandard courseEvaluateStandard=CourseEvaluateStandardRepository.getInstance().getCourseEvaluateStandard(curTeachingClassScore.getCourseID());
		if(courseEvaluateStandard==null){
			throw new Exception("课程标准未制定,请核查!");
		}
		
		//检测考核标准中是否要求当前要录入的分项成绩
		if(!courseEvaluateStandard.requireScoreType(scoreType))
		{
			throw new Exception("当前课程没有要求考核该类成绩，请核查！");
		}
		
		//检测成绩格式是否符合课程标准中相关要求
		if(!courseEvaluateStandard.markStyleIsCorrectly(mark.getMark().getClass()))
		{
			throw new Exception("录入的成绩格式不符合课程考核标准中要求的考核方式，请核查！");
		}

		//如果要求考试方式，则需要检查课程标准中权重是否维护完善
		if(courseEvaluateStandard.getCheckType().equals(CheckTypeEnum.EXAM) &&
				!courseEvaluateStandard.sumWeightingIsHundred()){
			throw new Exception("课程标准未制定不完善，其分项成绩权重之和不是100%,请完善！");
		}
				
		//登记成绩
		curTeachingClassScore.registerScore(studentID,scoreType, mark);
	
	}

	/*
	 * 取教学班成绩登记对象
	 */
	private TeachingClassScore getTeachingClassScore(TeachingClass aTeachingClass) {
		TeachingClassScore curTeachingClassScore=TeachingClassScoreRepository.getInstance().getTeachingClassScore(aTeachingClass.getTeachingClassID());
		if(curTeachingClassScore==null){
			curTeachingClassScore=TeachingClassScoreFactory.getInstance().createTeachingClassScore(aTeachingClass.getTeachingClassID(), aTeachingClass.getCourseID(), aTeachingClass.getCourseName(), "", "");
			TeachingClassScoreRepository.getInstance().save(curTeachingClassScore);
		}
		return curTeachingClassScore;
	}
}
