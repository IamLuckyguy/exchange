# CREATE TABLE IF NOT EXISTS exchange_rates (
#     ID BIGINT AUTO_INCREMENT PRIMARY KEY,
#     RESULT INT NOT NULL COMMENT '1 : 성공, 2 : DATA코드 오류, 3 : 인증코드 오류, 4 : 일일제한횟수 마감',
#     CUR_UNIT VARCHAR(10) NOT NULL COMMENT '통화코드',
#     CUR_NM VARCHAR(100) COMMENT '국가/통화명',
#     TTB VARCHAR(20) COMMENT '전신환(송금) 받으실때',
#     TTS VARCHAR(20) COMMENT '전신환(송금) 보내실때',
#     DEAL_BAS_R VARCHAR(20) COMMENT '매매 기준율',
#     BKPR VARCHAR(20) COMMENT '장부가격',
#     YY_EFEE_R VARCHAR(20) COMMENT '년환가료율',
#     TEN_DD_EFEE_R VARCHAR(20) COMMENT '10일환가료율',
#     KFTC_DEAL_BAS_R VARCHAR(20) COMMENT '서울외국환중계매매기준율',
#     KFTC_BKPR VARCHAR(20) COMMENT '서울외국환중계장부가격',
#     SEARCH_DATE VARCHAR(8) NOT NULL COMMENT '조회일자',
#     CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP,
#     INDEX IDX_SEARCH_DATE (SEARCH_DATE)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE IF NOT EXISTS exchange_rates
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,                                          -- 기본 키
    country_code  VARCHAR(10) NOT NULL,                                                       -- 국가 코드
    currency_code VARCHAR(10) NOT NULL,                                                       -- 통화 코드
    unit_amount   INT                  DEFAULT 1,
    rate_value    DOUBLE      NOT NULL,                                                       -- 환율 값
    updated_at    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 마지막 업데이트 시간
    INDEX idx_country_code (country_code),                                                    -- 국가 코드 인덱스
    INDEX idx_currency_code (currency_code)                                                   -- 통화 코드 인덱스
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE Country
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    currency_code VARCHAR(10) UNIQUE  NOT NULL,
    country_name  VARCHAR(100) UNIQUE NOT NULL,
    country_flag  VARCHAR(255)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;