CREATE TABLE t_restaurant_menu(
	i_rest INT UNSIGNED,
	seq INT UNSIGNED,
	menu_pic VARCHAR(50),
	PRIMARY KEY(i_rest, seq),
	FOREIGN KEY(i_rest) REFERENCES t_restaurant(i_rest)
);



-- 추천메뉴 테이블 / 음식메뉴, 가격, 사진담당
CREATE TABLE t_restaurant_recommend_menu(
	i_rest INT UNSIGNED,
	seq INT UNSIGNED,
	menu_nm VARCHAR(20) NOT NULL,
	menu_price INT UNSIGNED NOT NULL,
	menu_pic VARCHAR(50),
	PRIMARY KEY(i_rest, seq),
	FOREIGN KEY(i_rest) REFERENCES t_restaurant(i_rest)
);

CREATE TABLE t_user(
	i_user int unsigned PRIMARY KEY AUTO_INCREMENT,	-- unsigned (양수값만 저장하겠다 음수 제외)
	user_id varchar(30) NOT NULL unique,   -- nvarchar 색깔 안바뀜 하지만 적용은 됨
	user_pw varchar(70) NOT NULL,
	salt VARCHAR(30) NOT NULL,		-- salt : 비밀번호 암호화할때 사용 
	nm varchar(5) NOT NULL,			-- mysql 에서 varchar(5) 하면 영어5글자 한글 5글자임 (오라클이랑 다름)
	profile_img VARCHAR(50),
	r_dt DATETIME DEFAULT NOW(),
	m_dt DATETIME DEFAULT NOW()	
);

-- 음식점 등록
CREATE TABLE t_restaurant(
	i_rest INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	nm VARCHAR(20) NOT null,
	addr VARCHAR(100) NOT NULL,
	lat DOUBLE UNSIGNED NOT NULL, -- 좌표 값
	lag DOUBLE UNSIGNED NOT NULL, -- 좌표 값 
	cd_category INT unsigned NOT NULL,
	i_user INT UNSIGNED NOT NULL COMMENT '등록자',
	r_dt DATETIME DEFAULT NOW(),
	m_dt DATETIME DEFAULT NOW(),
	FOREIGN KEY (i_user) REFERENCES t_user(i_user)
);




CREATE TABLE c_code_m(
	i_m INT UNSIGNED PRIMARY KEY,
	`desc` VARCHAR(30) DEFAULT '',
	cd_nm VARCHAR(10) DEFAULT ''
);

CREATE TABLE c_code_d(
	i_m INT UNSIGNED,
	cd INT UNSIGNED,
	val VARCHAR(15) NOT NULL,
	PRIMARY KEY(i_m, cd),
	FOREIGN KEY(i_m) REFERENCES c_code_m(i_m)
);

-- 회원 관심사 테이블
CREATE TABLE t_user_favorite(
	i_rest INT UNSIGNED,
	i_user INT UNSIGNED,
	r_dt DATETIME DEFAULT NOW(),
	PRIMARY KEY(i_rest, i_user),
	FOREIGN KEY(i_rest) REFERENCES t_restaurant(i_rest),
	FOREIGN KEY(i_user) REFERENCES t_user(i_user)
);



SELECT A.val FROM c_code_d A
INNER JOIN c_code_m B
ON A.i_m = B.i_m;


SELECT * FROM t_restaurant;


-- c_code_m 에다가 컬럼값 다 넣기 




