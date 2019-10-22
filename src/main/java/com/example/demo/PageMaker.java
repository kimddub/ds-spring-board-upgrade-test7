package com.example.demo;

import org.apache.tomcat.util.digester.SetPropertiesRule;

public class PageMaker {
	private Page page;
	private int startPage;
	private int endPage;
	private int limitPage;
	private int pageBlock;
	private int totalArticles;
	private boolean prev;
	private boolean next;
	
	public PageMaker(Page page, int totalArticles) {
		this.page = page;
		this.totalArticles = totalArticles;
		
		pageBlock = 5;
		
		limitPage = (int)Math.ceil(totalArticles / (double)page.getPerPageArticles());
		
		setPageBlock();
	}
	
	private void setPageBlock() {
		// ceil(1p~5p/5) = 1block => s:1 ,e:5
		// ceil(6p~10p/5)  = 2block => s:6 ,e:10 ...
		
		endPage = (int)Math.ceil(page.getcPage() / (double)pageBlock) * pageBlock;
		startPage = endPage - pageBlock + 1;
			
		if(endPage > limitPage) {
			endPage = limitPage;
		}
		
		prev = startPage == 1 ? false : true;
		next = endPage == limitPage ? false : true;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	
	public int getLimitPage() {
		return limitPage;
	}

	public void setLimitPage(int limitPage) {
		this.limitPage = limitPage;
	}

	public int getPageBlock() {
		return pageBlock;
	}

	public void setPageBlock(int pageBlock) {
		this.pageBlock = pageBlock;
	}

	public int getTotalArticles() {
		return totalArticles;
	}

	public void setTotalArticles(int totalArticles) {
		this.totalArticles = totalArticles;
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}
}
