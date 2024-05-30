create table stock_krx(
    Code varchar(30) primary key comment '종목코드',
    ISU_CD varchar(30) not null,
    Name varchar(100) not null comment '종목명',
    Market varchar(30) comment '시장구분',
    Dept varchar(100) default null comment '소속부',
    Close integer comment '종가',
    Change_Code integer,
    Changes integer comment '대비',
    Chages_Ratio decimal (6,2) comment '등락률',
    Open integer comment '시가',
    High integer comment '고가',
    Low integer comment '저가',
    Volume integer comment '거래량',
    Amount integer comment '거래대금',
    Marcap integer comment '시가총액',
    Stocks integer comment '상장주식수',
    Market_Id varchar(30),
    renew_date datetime not null default CURRENT_TIMESTAMP() comment '갱신일자'
) comment '전종목 시세';

create table daily_price(
    id int auto_increment primary key,
    Code varchar(30) not null comment '종목코드',
    Date varchar(50) not null comment '날짜',
    Open integer comment '시가',
    High integer comment '고가',
    Low integer comment '저가',
    Close integer comment '종가',
    Volume integer comment '거래량',
    Changes decimal (20,10) comment '등락률*100',
    MA decimal (20,10) default null,
    RSI decimal (20,10) default null,
    RSI_signal decimal (20,10) default null,
    BBAND_UPPER decimal (20,10) default null,
    BBAND_MIDDLE decimal (20,10) default null,
    BBAND_LOWER decimal (20,10) default null,
    constraint fk_Code foreign key (Code) references stock_krx(Code) on delete cascade
) comment '개별종목 시세 추이';

create table tb_member(
    id int auto_increment primary key,
    login_id varchar(20) unique not null comment '아이디',
    password varchar(120) not null comment '비밀번호',
    email varchar(50) not null comment '이메일',
    name varchar(20) not null comment '이름',
    reg_no varchar(64) not null comment '주민등록번호',
    gender varchar(1) not null comment '성별',
    delete_yn int not null default 0 comment '삭제 여부',
    created_date datetime not null default current_timestamp() comment '가입일자',
    modified_date datetime default null comment '최종 수정일시',
    mailauth int not null default 0,
    mailkey varchar(50) not null
) comment '회원';

create table tb_post(
    id int auto_increment primary key,
    service varchar(30) not null comment '서비스',
    apikey varchar(100) not null comment 'API키',
    email varchar(50) not null comment '등록 사용자',
    delete_yn int not null default 0 comment '삭제 여부',
    created_date datetime not null default current_timestamp() comment '생성일시',
    modified_date datetime default null comment '최종 수정일시'
) comment 'API 리스트';

-- create table email_auth(
--     email varchar(50) primary key ,
--     mail_key varchar(50) not null ,
--     mail_auth int not null default 0 ,
--     created_date datetime not null default current_timestamp() ,
--     expire_date datetime not null default (current_timestamp + INTERVAL 5 minute)
-- );

create table email_auth(
    uuid varchar(36) primary key ,
    email varchar(50) unique ,
    mailkey varchar(50) not null ,
    mailauth int not null default 0 ,
    created_date datetime not null default current_timestamp()
);