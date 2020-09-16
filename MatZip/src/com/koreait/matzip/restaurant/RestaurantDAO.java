package com.koreait.matzip.restaurant;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.koreait.matzip.db.JdbcSelectInterface;
import com.koreait.matzip.db.JdbcTemplate;
import com.koreait.matzip.db.JdbcUpdateInterface;
import com.koreait.matzip.vo.RestaurantDomain;
import com.koreait.matzip.vo.RestaurantRecommendMenuVO;
import com.koreait.matzip.vo.RestaurantVO;


public class RestaurantDAO {
	
	
	public List<RestaurantRecommendMenuVO> selMenuList(int i_rest) {
		List<RestaurantRecommendMenuVO> list = new ArrayList();
		
		String sql = " SELECT seq, menu_pic FROM t_restaurant_menu "
				+ " WHERE i_rest = ? ";
		
		JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {

			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				ps.setInt(1, i_rest);
			}

			@Override
			public void executeQuery(ResultSet rs) throws SQLException {
				while(rs.next()) {
					RestaurantRecommendMenuVO vo = new RestaurantRecommendMenuVO();
					vo.setSeq(rs.getInt("seq"));
					vo.setMenu_pic(rs.getNString("menu_pic"));
					list.add(vo);
				}
			}
		});
		return list;
	}
	
	
	// 다수사진 등록 메소드 
	public int insMenu(RestaurantRecommendMenuVO param) {
		String sql = " INSERT INTO t_restaurant_menu "
				+ " (i_rest, seq, menu_pic) "
				+ " SELECT ?, IFNULL(MAX(seq), 0) + 1, ? "
				+ " FROM t_restaurant_menu "
				+ " WHERE i_rest = ? ";
		
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {		
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_rest());
				ps.setString(2, param.getMenu_pic());
				ps.setInt(3, param.getI_rest());
			}
		});
	}
	
	// 가게 디테일 음식정보 삭제하는 메소드
	public int delRecommendMenu(RestaurantRecommendMenuVO param) {
		
		String sql = " DELETE A "
				+ " FROM t_restaurant_recommend_menu A "
				+ " INNER JOIN t_restaurant B "
				+ " ON A.i_rest = B.i_rest "
				+ " AND B.i_user = ? "
				+ " WHERE A.i_rest = ? "
				+ " AND A.seq = ? ";
		
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {

			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_user());
				ps.setInt(2, param.getI_rest());
				ps.setInt(3, param.getSeq());
				System.out.println(param.getI_rest());
				System.out.println(param.getSeq());
			}
		});
	}
	
	
	public List<RestaurantRecommendMenuVO> selRecommendMenuList(int i_rest) {
		List<RestaurantRecommendMenuVO> list = new ArrayList();
		
		String sql = " SELECT seq, menu_nm, menu_price, menu_pic "
				+ " FROM t_restaurant_recommend_menu " 
				+ " WHERE i_rest = ? ";
		
		JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {

			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				ps.setInt(1, i_rest);
			}

			@Override
			public void executeQuery(ResultSet rs) throws SQLException {
				while(rs.next()) {
					RestaurantRecommendMenuVO vo = new RestaurantRecommendMenuVO();
					vo.setSeq(rs.getInt("seq"));
					vo.setMenu_nm(rs.getString("menu_nm"));
					vo.setMenu_price(rs.getInt("menu_price"));
					vo.setMenu_pic(rs.getString("menu_pic"));
					
					list.add(vo);
				}
			}
		});
		return list;
	}
	
	public RestaurantDomain selRest(RestaurantVO param) {
		RestaurantDomain vo = new RestaurantDomain(); 
		
		String sql = " SELECT A.nm, A.addr, A.i_user, A.hits "
				+ " , B.val AS cd_category_nm "
				+ " , IFNULL(C.cnt, 0) AS cntFavorite "
				+ " FROM t_restaurant A "
				+ " LEFT JOIN c_code_d B "
				+ " ON A.cd_category = B.cd "
				+ " AND B.i_m = 1 "
				+ " LEFT JOIN ( "
				+ "		SELECT i_rest, COUNT(i_rest) AS cnt "
				+ " 	FROM t_user_favorite "
				+ "		WHERE i_rest = ? "
				+ " 	GROUP BY i_rest "
				+ " ) C "
				+ " ON A.i_rest = C.i_rest "
				+ " WHERE A.i_rest = ? ";
		
		JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {

			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_rest());
				ps.setInt(2, param.getI_rest());				
			}
			
			@Override
			public void executeQuery(ResultSet rs) throws SQLException {
				if(rs.next()) {
					vo.setI_rest(param.getI_rest());
					vo.setNm(rs.getNString("nm"));
					vo.setAddr(rs.getNString("addr"));
					vo.setI_user(rs.getInt("i_user"));
					vo.setCntHits(rs.getInt("hits"));
					vo.setCd_category_nm(rs.getNString("cd_category_nm"));
					vo.setCntFavorite(rs.getInt("cntFavorite"));
				}
			}
		});	
		return vo;
		
	}
	
	
	
	
	
	
	
	public int insRest(RestaurantVO param) {
		String sql = " INSERT INTO t_restaurant "
				+ " (nm, addr, lat, lng, cd_category, i_user) "
				+ " VALUES "
				+ "	(?, ?, ?, ?, ?, ?) ";
		
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setNString(1, param.getNm());
				ps.setNString(2, param.getAddr());
				ps.setDouble(3, param.getLat());
				ps.setDouble(4, param.getLng());
				ps.setInt(5, param.getCd_category());
				ps.setInt(6, param.getI_user());
			}
		});	
	}
	
	
	public List<RestaurantDomain> selRestList() {
		List<RestaurantDomain> list = new ArrayList();
		
		String sql = " SELECT i_rest, nm, lat, lng FROM t_restaurant ";
		
		JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {

			@Override
			public void prepared(PreparedStatement ps) throws SQLException {}

			@Override
			public void executeQuery(ResultSet rs) throws SQLException {
				while(rs.next()) {
					RestaurantDomain vo = new RestaurantDomain();
					vo.setI_rest(rs.getInt("i_rest"));
					vo.setNm(rs.getNString("nm"));
					vo.setLat(rs.getDouble("lat"));
					vo.setLng(rs.getDouble("lng"));
					list.add(vo);
				}
			}
		});
		return list;
	}
	
	public int insRecommendMenu(RestaurantRecommendMenuVO param) {
		
		String sql = " INSERT INTO t_restaurant_recommend_menu "
				+ " (seq, i_rest, menu_nm, menu_price, menu_pic) "
				+ " SELECT IFNULL(MAX(seq), 0) + 1, ?, ?, ?, ? "
				+ " FROM t_restaurant_recommend_menu "
				+ " WHERE i_rest = ? ";
		
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {

			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_rest());
				ps.setNString(2, param.getMenu_nm());
				ps.setInt(3, param.getMenu_price());
				ps.setNString(4, param.getMenu_pic());
				ps.setInt(5, param.getI_rest());
			}
		});
	}
}
