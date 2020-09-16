package com.koreait.matzip.restaurant;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.koreait.matzip.CommonDAO;
import com.koreait.matzip.CommonUtils;
import com.koreait.matzip.Const;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.ViewRef;
import com.koreait.matzip.vo.RestaurantRecommendMenuVO;
import com.koreait.matzip.vo.RestaurantVO;
// 컨테이너 - 맵핑 - 컨트롤러 - 서비스 - dao (다시 역순으로 쭉 return보내서 최종적으로 컨테이너 -> jsp)
public class RestaurantController {
	
	private RestaurantService service;
	
	public RestaurantController() {
		service = new RestaurantService();
	}
	
	public String restMap(HttpServletRequest request) {	
		request.setAttribute(Const.TITLE, "메뉴");
		request.setAttribute(Const.VIEW, "restaurant/restMap");
		return ViewRef.TEMP_MENU_TEMP;
	}
	
	public String restReg(HttpServletRequest request) {
		final int i_m = 1; // 카테고리 코드
		request.setAttribute("categoryList", CommonDAO.selCodeList(i_m));
		
		request.setAttribute(Const.TITLE, "가게등록");
		request.setAttribute(Const.VIEW, "restaurant/restReg");
		return ViewRef.TEMP_MENU_TEMP;
	}
	
	public String restRegProc(HttpServletRequest request) {
		RestaurantVO vo = new RestaurantVO();		
//		UserVO vo = SecurityUtils.getLoginUser(request);
//		vo.getI_user();
		
		vo.setI_user(SecurityUtils.getLoginUser(request).getI_user());
		vo.setNm(request.getParameter("nm"));
		vo.setAddr(request.getParameter("addr"));
		vo.setLat(Double.parseDouble(request.getParameter("lat")));
		vo.setLng(Double.parseDouble(request.getParameter("lag")));
		vo.setCd_category(Integer.parseInt(request.getParameter("cd_category")));
		
		int result = service.restRegProc(vo);
		
		return "redirect:/restaurant/restMap";
	}
	
	
	// 지도점찍기 기능(업체)
	public String ajaxGetList(HttpServletRequest request) {
		return "ajax:" + service.getRestList();
	}
	
	
	
	public String restDetail(HttpServletRequest request) {
		int i_rest = CommonUtils.getIntParameter("i_rest", request);
		
		RestaurantVO param = new RestaurantVO();
		param.setI_rest(i_rest);
		
		request.setAttribute("css", new String[]{"restaurant"});
		request.setAttribute("recommendMenuList", service.getRecommendMenuList(i_rest));
		request.setAttribute("menuList", service.selMenuList(i_rest));
		request.setAttribute("data",service.getRest(param));		
		request.setAttribute(Const.TITLE, "디테일");
		request.setAttribute(Const.VIEW, "restaurant/restDetail");
		return ViewRef.TEMP_MENU_TEMP;
	}
	
	public String addRecMenusProc(HttpServletRequest request) {
		int i_rest = service.addRecMenus(request);
		return "redirect:/restaurant/restDetail?i_rest=" + i_rest;
	}
	
	public String ajaxDelRecMenu(HttpServletRequest request) {
		int i_rest = CommonUtils.getIntParameter("i_rest", request);
		int seq = CommonUtils.getIntParameter("seq", request);
		int i_user = SecurityUtils.getLoginUserPk(request);
		
		RestaurantRecommendMenuVO param = new RestaurantRecommendMenuVO();
		param.setI_rest(i_rest);
		param.setSeq(seq);
		param.setI_user(i_user);
		
		int result = service.delRecMenu(param);
		
		return "ajax:" + result;
	}
	
	public String addRecMenus(HttpServletRequest request) {
		int i_rest = service.addMenus(request);
		return "redirect:/restaurant/restDetail?i_rest=" + i_rest;
	}
	
	public String addMenusProc(HttpServletRequest request) {
		int i_rest = service.addMenus(request);
		return "redirect:/restaurant/restDetail?i_rest=" + i_rest;
	}
	
	
	
}

