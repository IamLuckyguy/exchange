// @ts-check

/**
 * 실시간 환율 데이터
 * @typedef {Object} ExchangeRateRealTime
 * @property {number} rv - 환율 값
 * @property {number} r - 환율 고시 회차
 * @property {string} t - 상승/하락
 * @property {number} tv - 변동금액 (직전 회차 대비 변동 금액)
 * @property {number} tr - 변동률
 * @property {string} live - 실시간/종료
 * @property {string} market - 시장 상태
 * @property {string} at - 날짜 (YYYY-MM-DDTHH:mm:ss 형식)
 */

/**
 * 환율 히스토리 데이터
 * @typedef {Object} ExchangeRateHistory
 * @property {string} at - 날짜 (YYYY-MM-DD 형식)
 * @property {string} rv - 환율 값
 */

/**
 * 개별 통화의 환율 정보
 * @typedef {Object} ExchangeRate
 * @property {string} countryFlag - 국가 플래그 이모지
 * @property {string} countryName - 국가 이름
 * @property {string} currencyCode - 통화 코드 (예: USD, KRW)
 * @property {number} decimals - 소수점 자릿수
 * @property {ExchangeRateHistory[]} exchangeRateHistories - 과거 환율 데이터 배열
 * @property {ExchangeRateRealTime[]} exchangeRateRealTime - 실시간 환율 데이터 배열
 * @property {number} unit - 환율 단위
 */

/**
 * /api/exchange-rates API 응답 구조
 * @typedef {ExchangeRate[]} ExchangeRatesResponse
 */

/**
 * 환율 API 응답의 예시 데이터
 * @type {ExchangeRatesResponse}
 */
const exampleResponse = [
    {
        countryFlag: "🇺🇸",
        countryName: "미국",
        currencyCode: "USD",
        decimals: 2,
        exchangeRateHistories: [
            {
                at: "2024-01-01",
                rv: "1321.90"
            }
        ],
        exchangeRateRealTime: [
            {
                "rv": 1521.28,
                "r": 403,
                "t": "하락",
                "tr": -9.83,
                "live": "실시간",
                "market": "OPEN",
                "at": "2025-01-02T11:00:20"
            }
        ],
        unit: 1
    }
];