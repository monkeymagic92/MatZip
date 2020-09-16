package com.koreait.matzip.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface JdbcSelectInterface {
	void prepared(PreparedStatement ps) throws SQLException;
	void executeQuery(ResultSet rs) throws SQLException;
}
