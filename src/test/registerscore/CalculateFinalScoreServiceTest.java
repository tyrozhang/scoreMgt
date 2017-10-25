package test.registerscore;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandard;
import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandardFactory;
import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandardRepository;
import gds.scoreMgt.domain.registerscore.CalculateFinalScoreService;
import gds.scoreMgt.domain.registerscore.RegisterTeachingClassScoreService;
import gds.scoreMgt.domain.registerscore.common.BreakRuleBehaviorEnum;
import gds.scoreMgt.domain.share.CheckTypeEnum;
import gds.scoreMgt.domain.share.ScoreTypeEnum;
import gds.scoreMgt.domain.share.mark.HundredMark;
import gds.scoreMgt.domain.share.mark.LevelMark;
import gds.scoreMgt.domain.share.mark.Mark;
import gds.scoreMgt.domain.share.mark.TwoLevelMarkTypeEnum;
import gds.scoreMgt.domain.teachingclass.TeachingClass;
import gds.scoreMgt.domain.teachingclass.TeachingClassFactory;
import gds.scoreMgt.domain.teachingclass.TeachingClassRepository;
import infrastructure.entityID.CourseID;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeachingClassID;

public class CalculateFinalScoreServiceTest {

	@Test
	public void calculateHundredFinalScoreWithDailyAndPaper() throws Exception {
		//生成教学班
		CourseID courseID=new CourseID();
		String courseName="高等数学";
		TeachingClassID teachingClassID=Tool.createTeachingClass(courseID, courseName);
		
		//添加学生
		StudentID firstStudentID=Tool.AddStudentToTeachingClass(teachingClassID);
		StudentID secondStudentID=Tool.AddStudentToTeachingClass(teachingClassID);

		//添加课程标准
		CourseEvaluateStandard courseEvaluateStandard=CourseEvaluateStandardFactory.getInstance().createCourseEvaluateStandard(courseID);
		courseEvaluateStandard.addRequireMarkTypes(ScoreTypeEnum.DAILYPORFORMANCE);
		courseEvaluateStandard.addRequireMarkTypes(ScoreTypeEnum.TESTPAPERMARK);
		courseEvaluateStandard.setCalculateFinalScoreUsingSubmarkWeighting(ScoreTypeEnum.DAILYPORFORMANCE, 30f);
		courseEvaluateStandard.setCalculateFinalScoreUsingSubmarkWeighting(ScoreTypeEnum.TESTPAPERMARK, 70f);
		courseEvaluateStandard.setCheckType(CheckTypeEnum.EXAM);
		CourseEvaluateStandardRepository.getInstance().save(courseEvaluateStandard);
		/*
		 * 登记成绩
		 */
		RegisterTeachingClassScoreService rtss=new RegisterTeachingClassScoreService(teachingClassID);
		//登记平时成绩
		rtss.registerScore(firstStudentID,ScoreTypeEnum.DAILYPORFORMANCE,new HundredMark(80f));
		rtss.registerScore(secondStudentID,ScoreTypeEnum.DAILYPORFORMANCE,new HundredMark(90f));
		
		//登记考试成绩
		rtss.registerScore(firstStudentID,ScoreTypeEnum.TESTPAPERMARK,new HundredMark(75f));
		rtss.registerScore(secondStudentID,ScoreTypeEnum.TESTPAPERMARK,new HundredMark(87f));

		//计算最终成绩
		CalculateFinalScoreService alculateFinalScoreService=new CalculateFinalScoreService();
		HundredMark firstStudentFinalMark=(HundredMark)alculateFinalScoreService.calculateFinalScore(teachingClassID,firstStudentID);
		assertTrue(firstStudentFinalMark.getMark().equals((80f*30f+75f*70F)/100));
		
		HundredMark secondStudentFinalMark=(HundredMark)alculateFinalScoreService.calculateFinalScore(teachingClassID,secondStudentID);
		assertTrue(secondStudentFinalMark.getMark().equals((90f*30f+87f*70F)/100));
	}
	
	@Test
	public void calculateLevelFinalScoreWithDailyAndPaper() throws Exception {
		//生成教学班
		CourseID courseID=new CourseID();
		String courseName="高等数学";
		TeachingClassID teachingClassID=Tool.createTeachingClass(courseID, courseName);
		
		//添加学生
		StudentID firstStudentID=Tool.AddStudentToTeachingClass(teachingClassID);
		StudentID secondStudentID=Tool.AddStudentToTeachingClass(teachingClassID);

		//添加课程标准
		CourseEvaluateStandard courseEvaluateStandard=CourseEvaluateStandardFactory.getInstance().createCourseEvaluateStandard(courseID);
		courseEvaluateStandard.addRequireMarkTypes(ScoreTypeEnum.DAILYPORFORMANCE);
		courseEvaluateStandard.addRequireMarkTypes(ScoreTypeEnum.TESTPAPERMARK);
		courseEvaluateStandard.setCalculateFinalScoreUsingSubmarkWeighting(ScoreTypeEnum.DAILYPORFORMANCE, 30f);
		courseEvaluateStandard.setCalculateFinalScoreUsingSubmarkWeighting(ScoreTypeEnum.TESTPAPERMARK, 70f);
		courseEvaluateStandard.setCheckType(CheckTypeEnum.INSPECT);
		CourseEvaluateStandardRepository.getInstance().save(courseEvaluateStandard);
		/*
		 * 登记成绩
		 */
		RegisterTeachingClassScoreService rtss=new RegisterTeachingClassScoreService(teachingClassID);
		//登记平时成绩
		rtss.registerScore(firstStudentID,ScoreTypeEnum.DAILYPORFORMANCE,new HundredMark(20f));
		rtss.registerScore(secondStudentID,ScoreTypeEnum.DAILYPORFORMANCE,new HundredMark(45f));
		
		//登记考试成绩
		rtss.registerScore(firstStudentID,ScoreTypeEnum.TESTPAPERMARK,new HundredMark(40f));
		rtss.registerScore(secondStudentID,ScoreTypeEnum.TESTPAPERMARK,new HundredMark(87f));

		//计算最终成绩
		CalculateFinalScoreService alculateFinalScoreService=new CalculateFinalScoreService();
		LevelMark firstStudentFinalMark=(LevelMark)alculateFinalScoreService.calculateFinalScore(teachingClassID,firstStudentID);
		assertTrue(firstStudentFinalMark.getMark().equals(TwoLevelMarkTypeEnum.FAIL));
		
		LevelMark secondStudentFinalMark=(LevelMark)alculateFinalScoreService.calculateFinalScore(teachingClassID,secondStudentID);
		assertTrue(secondStudentFinalMark.getMark().equals(TwoLevelMarkTypeEnum.PASS));
	}
	
	@Test
	public void calculateFinalScoreWhenBreakRule() throws Exception {
		//生成教学班
		CourseID courseID=new CourseID();
		String courseName="高等数学";
		TeachingClassID teachingClassID=Tool.createTeachingClass(courseID, courseName);
		
		//添加学生
		StudentID firstStudentID=Tool.AddStudentToTeachingClass(teachingClassID);
		StudentID secondStudentID=Tool.AddStudentToTeachingClass(teachingClassID);

		//添加课程标准
		CourseEvaluateStandard courseEvaluateStandard=CourseEvaluateStandardFactory.getInstance().createCourseEvaluateStandard(courseID);
		courseEvaluateStandard.setCheckType(CheckTypeEnum.INSPECT); 
		courseEvaluateStandard.addRequireMarkTypes(ScoreTypeEnum.DAILYPORFORMANCE);
		courseEvaluateStandard.addRequireMarkTypes(ScoreTypeEnum.TESTPAPERMARK);
		courseEvaluateStandard.setCalculateFinalScoreUsingSubmarkWeighting(ScoreTypeEnum.DAILYPORFORMANCE, 30f);
		courseEvaluateStandard.setCalculateFinalScoreUsingSubmarkWeighting(ScoreTypeEnum.TESTPAPERMARK, 70f);

		CourseEvaluateStandardRepository.getInstance().save(courseEvaluateStandard);
		/*
		 * 登记违纪
		 */
		RegisterTeachingClassScoreService rtss=new RegisterTeachingClassScoreService(teachingClassID);
		rtss.registerBreakRuleBehavior(firstStudentID, ScoreTypeEnum.TESTPAPERMARK, BreakRuleBehaviorEnum.Cheated);
		//计算最终成绩
		CalculateFinalScoreService alculateFinalScoreService=new CalculateFinalScoreService();
		LevelMark firstStudentFinalMark=(LevelMark)alculateFinalScoreService.calculateFinalScore(teachingClassID,firstStudentID);

		assertTrue(firstStudentFinalMark.getMark().equals(TwoLevelMarkTypeEnum.FAIL));
	}

}
