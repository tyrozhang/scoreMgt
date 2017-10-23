package gds.scoreMgt.domain.share.mark;

/*
 * 登记分制
 */
public class LevelMark extends Mark {
	private TwoLevelMarkTypeEnum levelMark;
	public TwoLevelMarkTypeEnum getMark() {
		return levelMark;
	}
	public LevelMark(TwoLevelMarkTypeEnum levelMark){
		this.levelMark=levelMark;
	}
	
}
