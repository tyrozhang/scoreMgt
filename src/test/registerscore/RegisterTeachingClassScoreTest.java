package test.registerscore;

import static org.junit.Assert.*;

import org.junit.Test;

import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandard;
import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandardFactory;
import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandardRepository;
import gds.scoreMgt.domain.registerscore.RegisterTeachingClassScoreService;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassScore;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassScoreRepository;
import gds.scoreMgt.domain.share.CheckTypeEnum;
import gds.scoreMgt.domain.share.LevelMarkEnum;
import gds.scoreMgt.domain.share.Mark;
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
		RegisterTeachingClassScoreService<Float> rtss=new RegisterTeachingClassScoreService<Float>();
		//登记平时成绩
		rtss.registerScore(teachingClassID,firstStudentID,ScoreTypeEnum.DAILYPORFORMANCE, new Mark<Float>(80f));
		rtss.registerScore(teachingClassID,secondStudentID,ScoreTypeEnum.DAILYPORFORMANCE, new Mark<Float>(90f));
		
		//登记考试成绩
		rtss.registerScore(teachingClassID,firstStudentID,ScoreTypeEnum.TESTPAPERMARK, new Mark<Float>(75f));
		rtss.registerScore(teachingClassID,secondStudentID,ScoreTypeEnum.TESTPAPERMARK, new Mark<Float>(87f));
		
		
		TeachingClassScore aTeachingClassScore=TeachingClassScoreRepository.getInstance().getTeachingClassScore(teachingClassID);
		//System.out.println(aTeachingClass.getStudentScore(firstStudentID).getClass());
		assertTrue(aTeachingClassScore.getStudentScore(firstStudentID,ScoreTypeEnum.TESTPAPERMARK).getMark().equals(75f));
		assertTrue(aTeachingClassScore.getStudentScore(secondStudentID,ScoreTypeEnum.DAILYPORFORMANCE).getMark().equals(90f));
		assertFalse(aTeachingClassScore.getStudentScore(firstStudentID,ScoreTypeEnum.DAILYPORFORMANCE).getMark().equals(75f));
		
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
		RegisterTeachingClassScoreService<LevelMarkEnum> rtss=new RegisterTeachingClassScoreService<LevelMarkEnum>();
		//登记最终成绩
		rtss.registerScore(teachingClassID,firstStudentID,ScoreTypeEnum.FINAL, new Mark<LevelMarkEnum>(LevelMarkEnum.PASS));
		rtss.registerScore(teachingClassID,secondStudentID,ScoreTypeEnum.FINAL, new Mark<LevelMarkEnum>(LevelMarkEnum.FAIL));

		
		TeachingClassScore aTeachingClassScore=TeachingClassScoreRepository.getInstance().getTeachingClassScore(teachingClassID);
		//System.out.println(aTeachingClass.getStudentScore(firstStudentID).getClass());
		assertTrue(aTeachingClassScore.getStudentScore(firstStudentID,ScoreTypeEnum.FINAL).getMark().equals(LevelMarkEnum.PASS));
		assertTrue(aTeachingClassScore.getStudentScore(secondStudentID,ScoreTypeEnum.FINAL).getMark().equals(LevelMarkEnum.FAIL));
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
		RegisterTeachingClassScoreService<Float> rtss=new RegisterTeachingClassScoreService<Float>();
		//登记最终成绩
		rtss.registerScore(teachingClassID,firstStudentID,ScoreTypeEnum.FINAL, new Mark<Float>(88f));
		rtss.registerScore(teachingClassID,secondStudentID,ScoreTypeEnum.FINAL, new Mark<Float>(77f));

		
		
		TeachingClassScore aTeachingClassScore=TeachingClassScoreRepository.getInstance().getTeachingClassScore(teachingClassID);
		//System.out.println(aTeachingClass.getStudentScore(firstStudentID).getClass());
		assertTrue(aTeachingClassScore.getStudentScore(firstStudentID,ScoreTypeEnum.FINAL).getMark().equals(88f));
		assertTrue(aTeachingClassScore.getStudentScore(secondStudentID,ScoreTypeEnum.FINAL).getMark().equals(77f));

	}
}
