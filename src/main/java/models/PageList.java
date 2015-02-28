package models;

import java.util.ArrayList;
import java.util.List;

public class PageList<TEntity> {
	private List list;
	private Pager pager;

	public PageList(List list) {
		this.list = list;
	}
	public PageList(List list, Pager pager) {
		this.list = list;
		this.pager = pager;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}
	
	
}
