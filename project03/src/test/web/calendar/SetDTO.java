package test.web.calendar;

public class SetDTO {
	// 게시판 설정을 위한 DTO
	private String boardtype;
	private int pagesize;
	private int imgsize;
	
	public void setBoardtype(String boardtype) {
		this.boardtype = boardtype;
	}
	public String getBoardtype() {
		return boardtype;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getImgsize() {
		return imgsize;
	}
	public void setImgsize(int imgsize) {
		this.imgsize = imgsize;
	}
	
}
