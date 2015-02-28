package models;

public class Pager {
	/**
	 * 当前页
	 */
	private int pageIndex;
	
	/**
	 * 一页显示的条目
	 */
	private int pageSize;
	
	/**
	 * 总页数
	 */
	private long totalPage;
	
	/**
	 * 上一页
	 */
	private int prev;
	
	/**
	 * 下一页
	 */
	private int next;
	
	/**
	 * 链接
	 */
	private String actionLink;
	
	public Pager(int pageIndex, int pageSize) {
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() { 
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	} 

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	public String getActionLink() {
		return actionLink;
	}

	public void setActionLink(String actionLink) {
		this.actionLink = actionLink;
	}

	public int getPrev() {
		return prev;
	}

	public void setPrev(int prev) {
		this.prev = prev;
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}
}
