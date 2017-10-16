package test.registerscore;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import gds.scoreMgt.domain.registerscore.CalculateFinalScoreService;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClass;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassFactory;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassRepository;
import gds.scoreMgt.domain.share.Mark;
import gds.scoreMgt.domain.share.MarkTypeEnum;
import infrastructure.entityID.CourseID;
import infrastructure.entityID.StudentID;
import infrastructure.entityID.TeachingClassID;

public class CalculateFinalScoreServiceTest {

	@Test
	public void calculateFinalScoreWithDailyAndPaper() throws Exception {
		//生成教学班
		CourseID courseID=new CourseID();
		String courseName="高等数学";
		TeachingClassID teachingClassID=Tool.createTeachingClass(courseID, courseName);;
		
		//添加学生
		StudentID firstStudentID=Tool.AddStudentToTeachingClass(teachingClassID);
		StudentID secondStudentID=Tool.AddStudentToTeachingClass(teachingClassID);

		
		//登记平时成绩
		Tool.RegisterMark(teachingClassID,firstStudentID,MarkTypeEnum.DAILYPORFORMANCE, new Mark<Float>(80f));
		Tool.RegisterMark(teachingClassID,secondStudentID,MarkTypeEnum.DAILYPORFORMANCE, new Mark<Float>(90f));
		
		//登记考试成绩
		Tool.RegisterMark(teachingClassID,firstStudentID,MarkTypeEnum.TESTPAPERMARK, new Mark<Float>(75f));
		Tool.RegisterMark(teachingClassID,secondStudentID,MarkTypeEnum.TESTPAPERMARK, new Mark<Float>(87f));

		
		CalculateFinalScoreService alculateFinalScoreService=new CalculateFinalScoreService();
		Mark finalMark=alculateFinalScoreService.calculateFinalScore(teachingClassID,firstStudentID);
		
		//System.out.println(aTeachingClass.getStudentScore(firstStudentID).getClass());
		assertTrue(finalMark.getMark().equals((80f*30f+75f*70F)/100));
	}

}
