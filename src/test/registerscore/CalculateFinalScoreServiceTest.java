package test.registerscore;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandard;
import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandardFactory;
import gds.scoreMgt.domain.courseevaluate.CourseEvaluateStandardRepository;
import gds.scoreMgt.domain.registerscore.CalculateFinalScoreService;
import gds.scoreMgt.domain.registerscore.RegisterTeachingClassScoreService;
import gds.scoreMgt.domain.share.Mark;
import gds.scoreMgt.domain.share.MarkTypeEnum;
import gds.scoreMgt.domain.teachingclass.TeachingClass;
import gds.scoreMgt.domain.teachingclass.TeachingClassFactory;
import gds.scoreMgt.domain.teachingclass.TeachingClassRepository;
import infrastructure.entityID.CourseID;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeachingClassID;

public class CalculateFinalScoreServiceTest {

	@Test
	public void calculateFinalScoreWithDailyAndPaper() throws Exception {
		//生成教学班
		CourseID courseID=new CourseID();
		String courseName="高等数学";
		TeachingClassID teachingClassID=Tool.createTeachingClass(courseID, courseName);
		
		//添加学生
		StudentID firstStudentID=Tool.AddStudentToTeachingClass(teachingClassID);
		StudentID secondStudentID=Tool.AddStudentToTeachingClass(teachingClassID);

		//添加课程标准
		CourseEvaluateStandard courseEvaluateStandard=CourseEvaluateStandardFactory.getInstance().createCourseEvaluateStandard(courseID);
		courseEvaluateStandard.addRequireMarkTypes(MarkTypeEnum.DAILYPORFORMANCE);
		courseEvaluateStandard.addRequireMarkTypes(MarkTypeEnum.TESTPAPERMARK);
		courseEvaluateStandard.setCalculateFinalScoreUsingSubmarkWeighting(MarkTypeEnum.DAILYPORFORMANCE, 30f);
		courseEvaluateStandard.setCalculateFinalScoreUsingSubmarkWeighting(MarkTypeEnum.TESTPAPERMARK, 70f);
		CourseEvaluateStandardRepository.getInstance().save(courseEvaluateStandard);
		/*
		 * 登记成绩
		 */
		RegisterTeachingClassScoreService<Float> rtss=new RegisterTeachingClassScoreService<Float>();
		//登记平时成绩
		rtss.registerScore(teachingClassID,firstStudentID,MarkTypeEnum.DAILYPORFORMANCE, new Mark<Float>(80f));
		rtss.registerScore(teachingClassID,secondStudentID,MarkTypeEnum.DAILYPORFORMANCE, new Mark<Float>(90f));
		
		//登记考试成绩
		rtss.registerScore(teachingClassID,firstStudentID,MarkTypeEnum.TESTPAPERMARK, new Mark<Float>(75f));
		rtss.registerScore(teachingClassID,secondStudentID,MarkTypeEnum.TESTPAPERMARK, new Mark<Float>(87f));

		//计算最终成绩
		CalculateFinalScoreService alculateFinalScoreService=new CalculateFinalScoreService();
		Mark finalMark=alculateFinalScoreService.calculateFinalScore(teachingClassID,firstStudentID);

		//System.out.println(aTeachingClass.getStudentScore(firstStudentID).getClass());
		assertTrue(finalMark.getMark().equals((80f*30f+75f*70F)/100));
	}

}
