package com.koreait.matzip.restaurant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.google.gson.Gson;
import com.koreait.matzip.CommonUtils;
import com.koreait.matzip.FileUtils;
import com.koreait.matzip.vo.RestaurantDomain;
import com.koreait.matzip.vo.RestaurantRecommendMenuVO;
import com.koreait.matzip.vo.RestaurantVO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class RestaurantService {

	private RestaurantDAO dao;

	public RestaurantService() {
		dao = new RestaurantDAO();
	}

	public int restRegProc(RestaurantVO vo) {
		return dao.insRest(vo);

	}

	public String getRestList() {
		List<RestaurantDomain> list = dao.selRestList();
		Gson gson = new Gson();
		return gson.toJson(list);
	}

	public RestaurantDomain getRest(RestaurantVO param) {
		return dao.selRest(param);
	}

	// 다수사진등록(최신버전) (앞으로 복붙해서 쓰자)
	public int addMenus(HttpServletRequest request) { // 메뉴등록

		int i_rest = CommonUtils.getIntParameter("i_rest", request);
		String savePath = request.getServletContext().getRealPath("/res/img/restaurant");
		String tempPath = savePath + "/temp";

		FileUtils.makeFolder(tempPath);

		RestaurantRecommendMenuVO param = new RestaurantRecommendMenuVO();
		param.setI_rest(i_rest);

		try {

			for (Part part : request.getParts()) {
				String fileNm = part.getSubmittedFileName(); // FileUtils.getFileName(Part part) 랑 똑같은 일을하는 내장객체
				if (fileNm != null) {
					String ext = FileUtils.getExt(fileNm);
					String saveFileNm = UUID.randomUUID() + ext;
					part.write(tempPath + "/" + fileNm);

					param.setMenu_pic(saveFileNm);
					System.out.println("주소 : " + tempPath);
					dao.insMenu(param);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i_rest;
	}

	// 다수 사진 등록하기 (이전버전)
	public int addRecMenus(HttpServletRequest request) {
		List<RestaurantRecommendMenuVO> list = new ArrayList();
		String savePath = "/res/img/restaurant";
		String tempPath = request.getServletContext().getRealPath(savePath + "/temp"); // getRealPath() 메소드 = 절대주소값(서버가
																					// 생각하면 됨
		FileUtils.makeFolder(tempPath); // 만약 위에 주소가 없다면 폴더를 만들어라 (FileUtils.makeFolder() 메소드 참고)

		int maxFileSize = 10_485_760; // 1024 * 1024 * 10 (10mb) //최대 파일 사이즈 크기
		MultipartRequest multi = null;
		int i_rest = 0;
		String[] menu_nmArr = null;
		String[] menu_priceArr = null;
		try {
			multi = new MultipartRequest(request, tempPath, maxFileSize, "UTF-8", new DefaultFileRenamePolicy()); // 객체화
																													// 작업
			// new Default...() 얘는 직접 우리가 사진파일 올렸을때 name1, name2, name3 이렇게 안겹치게 자동으로 업로드
			// 해주는 녀석

			i_rest = CommonUtils.getIntParameter("i_rest", multi); //

			System.out.println("i_rest : " + i_rest);
			menu_nmArr = multi.getParameterValues("menu_nm"); // getPara..values (이거내가 팀플할때 썻던거임 이건 이해할거임 )
			menu_priceArr = multi.getParameterValues("menu_price");

			if (menu_nmArr == null || menu_priceArr == null) { // 둘중에 하나라도 null이면 밑에 코드들 '강제 종료' 둘다 한몸이라고보면됨 둘중 한개라도
																// null 뜨면 결국 값 저장 실패란 것
				return i_rest;
			}

			if (menu_nmArr != null && menu_priceArr != null) { // null 이 아니라면 위에 만들어놓은 list에 값담을 작업을 함
				for (int i = 0; i < menu_nmArr.length; i++) {
					RestaurantRecommendMenuVO vo = new RestaurantRecommendMenuVO();
					vo.setI_rest(i_rest);
					vo.setMenu_nm(menu_nmArr[i]);
					vo.setMenu_price(CommonUtils.parseStringToInt(menu_priceArr[i]));
					list.add(vo);
				}
			}

			// - - - - ↑ 위엣 부분까지가 객체생성 부분 ↑ - - - - -

			// - - - - 밑에부터 실제 이미지 저장하고, 파일name 등 추가하는것

			String targetPath = request.getServletContext().getRealPath(savePath + "/" + i_rest);
			FileUtils.makeFolder(targetPath);

			String fileNm = "";
			String saveFileNm = "";
			Enumeration files = multi.getFileNames(); // Enumeration : 리스트 관련(?) 나중에 검색해보기
			while (files.hasMoreElements()) { // rs.next() 랑 비슷하게 보되, '아래주석 참고'
				String key = (String) files.nextElement(); // 실제로 가르키는행위는 nextElement() 메소드가 함(키값을 가르킨다(반환))
				System.out.println("key : " + key); // 위에 찍힌 키값 확인하기
				fileNm = multi.getFilesystemName(key); // 제일위에 new Default...() 메소드로 인해 이름을 바꿔주는 메소드(위에 default 객체 확인하기
														// 내용적어놓음)
				System.out.println("fileNm : " + fileNm); // 위에 실제 제대로 바꿧는지 확인용

				if (fileNm != null) { // 파일 선택안했으면 null이 넘어옴
					String ext = FileUtils.getExt(fileNm);
					saveFileNm = UUID.randomUUID() + ext; // 실제로 db에 저장할 파일 이름 UUID = 절대 중복되지 않는 이름.확장자 를 반환
															// 한글 이름을 영어로 바꾸기위해 ( 한글은 아직 검증되지 않음 )
															// 2e55da12~~.jpg 라고하면 randomUUID() + ext 는 제일마지막 .기준으로
															// 그뒤에(확장자명) 을 박아줌
															// 그래서 밑에 saveFileNm : + saveFilNm에서 뒤에 확장자 붙이는것

					System.out.println("saveFileNm : " + saveFileNm);
					File oldFile = new File(tempPath + "/" + fileNm);
					File newFile = new File(targetPath + "/" + saveFileNm);
					oldFile.renameTo(newFile);

					int idx = CommonUtils.parseStringToInt(key.substring(key.lastIndexOf("_") + 1)); // _다음 +1 <-- 이 _전의
																										// 문자열까지 담는다 아
																										// 이해했는데 우에
																										// 적어야될지몰겠음 밑에코드
																										// 그냥 쭉읽어봐라
					RestaurantRecommendMenuVO vo = list.get(idx);
					vo.setMenu_pic(saveFileNm);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (list != null) {
			for (RestaurantRecommendMenuVO vo : list) {
				dao.insRecommendMenu(vo);
			}
		}
		return i_rest; // return 값을 i_rest 로주는 이유는 레스트랑 컨트롤러 addRecMenusProc 메소드참고하기
	}

	public List<RestaurantRecommendMenuVO> getRecommendMenuList(int i_rest) {
		return dao.selRecommendMenuList(i_rest);
	}

	public int delRecMenu(RestaurantRecommendMenuVO param) {
		return dao.delRecommendMenu(param);
	}

	public List<RestaurantRecommendMenuVO> selMenuList(int i_rest) {
		return dao.selMenuList(i_rest);
	}
}
