CREATE TABLE IF NOT EXISTS exchange_rates
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,                                          -- 기본 키
    currency_code VARCHAR(10) NOT NULL,                                                       -- 통화 코드
    unit_amount   INT                  DEFAULT 1,
    decimals      INT                  DEFAULT 1,
    rate_value    DOUBLE      NOT NULL,                                                       -- 환율 값
    updated_at    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 마지막 업데이트 시간
    INDEX idx_currency_code (currency_code)                                                   -- 통화 코드 인덱스
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE exchange_rate_histories
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    currency_code VARCHAR(10) NOT NULL,
    rate_value    DOUBLE      NOT NULL,
    updated_at    DATETIME    NOT NULL
);

CREATE TABLE country
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    currency_code VARCHAR(10) UNIQUE  NOT NULL,
    country_name  VARCHAR(100) UNIQUE NOT NULL,
    country_flag  VARCHAR(255)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


