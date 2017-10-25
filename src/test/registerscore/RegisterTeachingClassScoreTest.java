package test.registerscore;

import static org.junit.Assert.*;

import org.junit.Test;

import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandard;
import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandardFactory;
import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandardRepository;
import gds.scoreMgt.domain.registerscore.RegisterTeachingClassScoreService;
import gds.scoreMgt.domain.registerscore.common.BreakRuleBehaviorEnum;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassScore;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassScoreRepository;
import gds.scoreMgt.domain.share.CheckTypeEnum;

import gds.scoreMgt.domain.share.mark.HundredMark;
import gds.scoreMgt.domain.share.mark.LevelMark;
import gds.scoreMgt.domain.share.mark.Mark;
import gds.scoreMgt.domain.share.mark.TwoLevelMarkTypeEnum;
import gds.scoreMgt.domain.share.ScoreTypeEnum;
import gds.scoreMgt.domain.share.TeacherPositionEnum;
import gds.scoreMgt.domain.teachingclass.TeachingClass;
import gds.scoreMgt.domain.teachingclass.TeachingClassFactory;
import gds.scoreMgt.domain.teachingclass.TeachingClassRepository;
import infrastructure.entityID.CourseID;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeacherID;
import infrastructure.entityID.TeachingClassID;
import junit.framework.Assert;

public class RegisterTeachingClassScoreTest {

	
	
	
	/**
	 *教学班路成绩测试
	 * @throws Exception 
	 */
	@Test
	public void registerSubScores() throws Exception {
		
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
		
		
		TeachingClassScore aTeachingClassScore=TeachingClassScoreRepository.getInstance().getTeachingClassScore(teachingClassID);
		//System.out.println(aTeachingClass.getStudentScore(firstStudentID).getClass());
		assertTrue(((HundredMark)aTeachingClassScore.getStudentScore(firstStudentID,ScoreTypeEnum.TESTPAPERMARK)).getMark().equals(75f));
		assertTrue(((HundredMark)aTeachingClassScore.getStudentScore(secondStudentID,ScoreTypeEnum.DAILYPORFORMANCE)).getMark().equals(90f));
		assertFalse(((HundredMark)aTeachingClassScore.getStudentScore(firstStudentID,ScoreTypeEnum.DAILYPORFORMANCE)).getMark().equals(75f));
		
	}
	
	@Test
	public void registerInspectScores() throws Exception {
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
		CourseEvaluateStandardRepository.getInstance().save(courseEvaluateStandard);
		/*
		 * 登记成绩
		 */
		RegisterTeachingClassScoreService rtss=new RegisterTeachingClassScoreService(teachingClassID);
		//登记最终成绩
		rtss.registerScore(firstStudentID,ScoreTypeEnum.FINAL,new LevelMark(TwoLevelMarkTypeEnum.PASS));
		rtss.registerScore(secondStudentID,ScoreTypeEnum.FINAL,new LevelMark(TwoLevelMarkTypeEnum.FAIL));

		
		TeachingClassScore aTeachingClassScore=TeachingClassScoreRepository.getInstance().getTeachingClassScore(teachingClassID);
		//System.out.println(aTeachingClass.getStudentScore(firstStudentID).getClass());
		assertTrue(((LevelMark)aTeachingClassScore.getStudentScore(firstStudentID,ScoreTypeEnum.FINAL)).getMark().equals(TwoLevelMarkTypeEnum.PASS));
		assertTrue(((LevelMark)aTeachingClassScore.getStudentScore(secondStudentID,ScoreTypeEnum.FINAL)).getMark().equals(TwoLevelMarkTypeEnum.FAIL));
	}
	
	
	@Test
	public void directRegisterFinalScores() throws Exception {
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
		//登记最终成绩
		rtss.registerScore(firstStudentID,ScoreTypeEnum.FINAL,new HundredMark(88f));
		rtss.registerScore(secondStudentID,ScoreTypeEnum.FINAL,new HundredMark(77f));


		TeachingClassScore aTeachingClassScore=TeachingClassScoreRepository.getInstance().getTeachingClassScore(teachingClassID);
		//System.out.println(aTeachingClass.getStudentScore(firstStudentID).getClass());
		assertTrue(((HundredMark)aTeachingClassScore.getStudentScore(firstStudentID,ScoreTypeEnum.FINAL)).getMark().equals(88f));
		assertTrue(((HundredMark)aTeachingClassScore.getStudentScore(secondStudentID,ScoreTypeEnum.FINAL)).getMark().equals(77f));

	}
	
	/**
	 * 登记作弊测试
	 * @throws Exception
	 */
	@Test
	public void registerBreakRule() throws Exception {
		//生成教学班
		CourseID courseID=new CourseID();
		String courseName="高等数学";
		TeachingClassID teachingClassID=Tool.createTeachingClass(courseID, courseName);
		
		//添加学生
		StudentID firstStudentID=Tool.AddStudentToTeachingClass(teachingClassID);		

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
		//登记作弊
		rtss.registerBreakRuleBehavior(firstStudentID,ScoreTypeEnum.TESTPAPERMARK,BreakRuleBehaviorEnum.Cheated);
		
		TeachingClassScore aTeachingClassScore=TeachingClassScoreRepository.getInstance().getTeachingClassScore(teachingClassID);
	
		assertTrue(aTeachingClassScore.getBreakRuleBehavior(firstStudentID,ScoreTypeEnum.TESTPAPERMARK).equals(BreakRuleBehaviorEnum.Cheated));
	}
}
