/**
 * 
 */
package gds.scoreMgt.domain.registerscore.teachingclass;

import java.util.HashMap;

import infrastructure.entityID.TeachingClassID;

/**
 * @author zhangyp
 *
 */
public class TeachingClassRepository {
	private  HashMap<TeachingClassID,TeachingClass> respository;
	private static TeachingClassRepository teachingClassRepository;
	private TeachingClassRepository (){
		
	}
	
	public static TeachingClassRepository getInstance()
	{
		if(teachingClassRepository==null ){
			teachingClassRepository=new TeachingClassRepository();
		}
		return teachingClassRepository;
	}
	
	public  TeachingClass getTeachingClass(TeachingClassID teachingClassID){
		if(respository==null){
			return null;
		}
		return this.respository.getOrDefault(teachingClassID, null);
	}
	
	public void save(TeachingClass teachingClass){
		if(respository==null){
			respository=new HashMap<TeachingClassID,TeachingClass>();
		}
		this.respository.put(teachingClass.getTeachingClassID(), teachingClass);
	}
}
