package com.koreait.matzip.vo;

public class RestaurantDomain extends RestaurantVO {
	private String userNm;
	private int cntHits;
	private int cntFavorite;
	private String cd_category_nm;
	
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public int getCntHits() {
		return cntHits;
	}
	public void setCntHits(int cntHits) {
		this.cntHits = cntHits;
	}
	public int getCntFavorite() {
		return cntFavorite;
	}
	public void setCntFavorite(int cntFavorite) {
		this.cntFavorite = cntFavorite;
	}
	public String getCd_category_nm() {
		return cd_category_nm;
	}
	public void setCd_category_nm(String cd_category_nm) {
		this.cd_category_nm = cd_category_nm;
	}
}
