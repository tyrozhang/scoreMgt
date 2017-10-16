package gds.scoreMgt.domain.registerscore;

import gds.scoreMgt.domain.registerscore.teachingclass.ScoreReportCard;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClass;
import gds.scoreMgt.domain.registerscore.teachingclass.TeachingClassFactory;
import gds.scoreMgt.domain.share.MarkTypeEnum;
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
	
	public void registerScore(TeachingClassID teachingClassID,StudentID studentID, MarkTypeEnum markType,T mark){
		//查看学员是否是上课学员之一
				//（以下代码为模拟）
				//生成教学班
				CourseID courseID=new CourseID();
				String courseName="高等数学";
				TeachingClass aTeachingClass=TeachingClassFactory.createTeachingClassFactory().createTeachingClass(teachingClassID, courseID, courseName);
				
				//添加学生
				StudentID firstStudentID=new StudentID();
				aTeachingClass.assignStudent(firstStudentID);
				
				StudentID secondStudentID=new StudentID();
				aTeachingClass.assignStudent(secondStudentID);
		//分数类型是否符合课程考核要求
		//成绩分制是否符合课程考核要求
		//登记成绩
				//以下代码为模拟
				//登记平时成绩
				//ScoreReportCard registerScore=new ScoreReportCard<Double>(markType);
				//registerScore.registerScore(studentID, mark);
	}
}
