CREATE TABLE IF NOT EXISTS `exchange_rates`
(
    `id`            BIGINT                                 NOT NULL AUTO_INCREMENT,
    `currency_code` VARCHAR(10) COLLATE utf8mb4_unicode_ci NOT NULL,
    `unit_amount`   INT                                             DEFAULT '1',
    `decimals`      INT                                             DEFAULT '1',
    `rate_value`    DOUBLE                                 NOT NULL,
    `fetched_at`    DATETIME                               NOT NULL,
    `updated_at`    DATETIME                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_currency_code` (`currency_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `exchange_rate_histories`
(
    `id`            BIGINT                                 NOT NULL AUTO_INCREMENT,
    `currency_code` VARCHAR(10) COLLATE utf8mb4_general_ci NOT NULL,
    `rate_value`    DOUBLE                                 NOT NULL,
    `fetched_at`    DATETIME                               NOT NULL,
    `updated_at`    DATETIME                               NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `country`
(
    `id`            BIGINT                                  NOT NULL AUTO_INCREMENT,
    `currency_code` VARCHAR(10) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `country_name`  VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `country_flag`  VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `currency_code` (`currency_code`),
    UNIQUE KEY `country_name` (`country_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


