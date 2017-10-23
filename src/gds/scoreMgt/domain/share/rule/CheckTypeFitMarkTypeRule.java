package gds.scoreMgt.domain.share.rule;

import java.util.HashMap;
import gds.scoreMgt.domain.share.CheckTypeEnum;
import gds.scoreMgt.domain.share.mark.HundredMark;
import gds.scoreMgt.domain.share.mark.LevelMark;
import gds.scoreMgt.domain.share.mark.Mark;
/*
 * 考试方式与分数类型规则
 */
public  class CheckTypeFitMarkTypeRule {
	private static HashMap<CheckTypeEnum,String> ruleRelation=new HashMap<CheckTypeEnum,String>();	
	
	private static CheckTypeFitMarkTypeRule rule=new CheckTypeFitMarkTypeRule();
	
	private CheckTypeFitMarkTypeRule()
	{
		ruleRelation.put(CheckTypeEnum.EXAM, HundredMark.class.getTypeName());
		ruleRelation.put(CheckTypeEnum.INSPECT,LevelMark.class.getTypeName());
	}
	
	public static  CheckTypeFitMarkTypeRule getInstance(){
		return rule;
	}
	/**
	 * 设置分项权重
	 * @param markType
	 * @param weighting
	 * @throws Exception
	 */
	public static boolean CheckTypeIsFitMarkType(CheckTypeEnum checkType,Mark mark){
		if(ruleRelation.get(checkType).equals(mark.getClass().getTypeName())){
			return true;
		}
		return false;
	}
	
}
