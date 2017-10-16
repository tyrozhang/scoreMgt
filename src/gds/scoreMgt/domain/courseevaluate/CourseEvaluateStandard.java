/**
 * 
 */
package gds.scoreMgt.domain.courseevaluate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import gds.scoreMgt.domain.share.MarkTypeEnum;
import infrastructure.entityID.CourseID;

/**
 * 课程考核标准
 * @author zhangyp
 *
 */
public class CourseEvaluateStandard {
	private CourseID courseID;
	private String termID;//暂时不实现，先留着
	private Set<MarkTypeEnum> requireMarkTypes=new HashSet();
	private HashMap<MarkTypeEnum,Float> everyMarkWeighting=new HashMap();
	
	public CourseEvaluateStandard(CourseID courseID){
		this.courseID=courseID;
	}
	
	public String getTermID() {
		return termID;
	}

	public void setTermID(String termID) {
		this.termID = termID;
	}

	public Set<MarkTypeEnum> getRequireMarkTypes() {
		return requireMarkTypes;
	}

	/**
	 * 添加必考项
	 * @param requireMarkType
	 */
	public void addRequireMarkTypes(MarkTypeEnum requireMarkType) {
		this.requireMarkTypes.add(requireMarkType);
	}

	public CourseID getCourseID() {
		return courseID;
	}

	public HashMap<MarkTypeEnum, Float> getEveryMarkWeighting() {
		return everyMarkWeighting;
	}

	/**
	 * 设置分项权重
	 * @param markType
	 * @param weighting
	 * @throws Exception
	 */
	public void setCalculateFinalScoreUsingSubmarkWeighting(MarkTypeEnum markType,Float weighting) throws Exception{
		if(!this.requireMarkTypes.contains(markType)){
			throw new Exception("当前课程的考核不包含真要制定权重的考核！");
		}
		
		if((weighting<0) || (weighting>100)){
			throw new Exception("权重数字需介于0..100之间");
		}
		
		this.everyMarkWeighting.put(markType, weighting);
	}
	
	/**
	 * 标准是否完整，权重之和是为100即完成
	 */
	public boolean sumWeightingIsHundred(){
		if(this.everyMarkWeighting.isEmpty()){
			return false;
		}
		
		Optional<Float> totalWeighting=this.everyMarkWeighting.values().stream().reduce((sum, element) -> sum + element);
		
		if(totalWeighting.isPresent()){
			return totalWeighting.get().equals(100f);
		}
		
		return false;
	}
}
