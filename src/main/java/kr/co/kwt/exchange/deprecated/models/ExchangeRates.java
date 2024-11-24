package kr.co.kwt.exchange.deprecated.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("exchange_rates")
public class ExchangeRates {
    @Id
    private Long id;
    private int result;          // 요청 결과
    private String curUnit;      // 통화코드
    private String curNm;        // 국가/통화명
    private String ttb;          // 전신환(송금) 받으실때
    private String tts;          // 전신환(송금) 보내실때
    private String dealBasR;     // 매매 기준율
    private String bkpr;         // 장부가격
    private String yyEfeeR;      // 년환가료율
    private String tenDdEfeeR;   // 10일환가료율
    private String kftcDealBasR; // 한국은행 매매 기준율
    private String kftcBkpr;     // 한국은행 장부가격
    private String searchDate;   // 검색 날짜
}