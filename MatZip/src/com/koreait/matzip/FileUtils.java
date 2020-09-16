package com.koreait.matzip;

import java.io.File;

import javax.servlet.http.Part;

public class FileUtils {
	
	public static void makeFolder(String path) {
		File dir = new File(path);		
		if(!dir.exists()) { // 만약 폴더가 없다면 ? 폴더만들어라 
			dir.mkdirs(); // mkdirs, mkdir 이있는데 그냥 무조건 midirs 만들어라(굳이 좋은거냅두고 왜 mkdir씀 ? 복수형써라 그냥)
		}
	}
	
	public static String getExt(String fileNm) {
		return fileNm.substring(fileNm.lastIndexOf("."));
	}
	
	public static String getFileName(Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
    }
}
