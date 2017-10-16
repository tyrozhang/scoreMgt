package gds.scoreMgt.domain.share;

/**
 * 分数
 * @author zhangyp
 *
 */
public class Mark<T> {
	private T mark;
	
	public Mark(T mark){
		this.mark=mark;
	}

	public T getMark() {
		return mark;
	}

}
