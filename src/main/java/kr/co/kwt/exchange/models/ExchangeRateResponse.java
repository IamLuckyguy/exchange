package kr.co.kwt.exchange.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExchangeRateResponse {
    private int result;
    @JsonProperty("cur_unit")
    private String curUnit;
    @JsonProperty("cur_nm")
    private String curNm;
    private String ttb;
    private String tts;
    @JsonProperty("deal_bas_r")
    private String dealBasR;
    private String bkpr;
    @JsonProperty("yy_efee_r")
    private String yyEfeeR;
    @JsonProperty("ten_dd_efee_r")
    private String tenDdEfeeR;
    @JsonProperty("kftc_deal_bas_r")
    private String kftcDealBasR;
    @JsonProperty("kftc_bkpr")
    private String kftcBkpr;
}