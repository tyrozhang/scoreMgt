package gds.scoreMgt.domain.registerscore;

import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandard;
import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandardFactory;
import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandardRepository;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassScore;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassScoreFactory;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassScoreRepository;
import gds.scoreMgt.domain.share.Mark;
import gds.scoreMgt.domain.share.MarkTypeEnum;
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
	
	public void registerScore(TeachingClassID teachingClassID,StudentID studentID, MarkTypeEnum markType,Mark<T> mark) throws Exception{
	
		/*
		 * 查看学员是否是上课学员之一
		 */
		TeachingClass aTeachingClass=TeachingClassRepository.getInstance().getTeachingClass(teachingClassID);
		if(!aTeachingClass.isStudyStudent(studentID)) {
			throw new Exception("对不起，不是教学班上课学员，不能录入成绩！");
		}
		
		//获取教学班成绩登记对象
		TeachingClassScore curTeachingClassScore=getTeachingClassScore(aTeachingClass);
		
		/*
		 * 检查登记的成绩类型是否符合当前课程的考核标准要求
		 */
		CourseEvaluateStandard courseEvaluateStandard=CourseEvaluateStandardRepository.getInstance().getCourseEvaluateStandard(curTeachingClassScore.getCourseID());
		if(courseEvaluateStandard==null){
			throw new Exception("课程标准未制定,请核查!");
		}
		
		//检查课程标准中权重是否维护完善
		if(!courseEvaluateStandard.sumWeightingIsHundred()){
			throw new Exception("课程标准未制定不完善，其分项成绩权重之和不是100%,请完善！");
		}
		
		if(!courseEvaluateStandard.requireMarkType(markType))
		{
			throw new Exception("当前课程没有要求考核该类成绩，请核查！");
		}
				
		//登记成绩
		curTeachingClassScore.registerScore(studentID,markType, mark);
	
	}

	/*
	 * 取教学班成绩登记对象
	 */
	private TeachingClassScore getTeachingClassScore(TeachingClass aTeachingClass) {
		TeachingClassScore curTeachingClassScore=TeachingClassScoreRepository.getInstance().getTeachingClassScore(aTeachingClass.getTeachingClassID());
		if(curTeachingClassScore==null){
			curTeachingClassScore=TeachingClassScoreFactory.createTeachingClassScoreFactory().createTeachingClassScore(aTeachingClass.getTeachingClassID(), aTeachingClass.getCourseID(), aTeachingClass.getCourseName(), "", "");
			TeachingClassScoreRepository.getInstance().save(curTeachingClassScore);
		}
		return curTeachingClassScore;
	}
}
