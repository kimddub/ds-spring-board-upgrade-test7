package com.example.demo;

// 한 페이지의 규격을 갖는 객체
public class Page {
	private int cPage;
	private int perPageArticles;
	
	public Page() {
		cPage = 1;
		perPageArticles = 10;
	}
	
	public int getPrevPageArticles() {
		if ( cPage < 1 ) {
			cPage = 1;
		}
		return (cPage - 1) * perPageArticles;
	}
	
	public void setcPage(int cPage) {
		if ( cPage < 1 ) {
			cPage = 1;
		}
		
		this.cPage = cPage;
	}
	
	public int getcPage() {
		
		return cPage;
	}

	public void setPerPageArticles(int perPageArticles) {
		this.perPageArticles = perPageArticles;
	}

	public double getPerPageArticles() {
		return perPageArticles;
	}
}
