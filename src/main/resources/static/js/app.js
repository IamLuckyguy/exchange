// resources/static/js/app.js
import { Calculator } from './features/calculator/Calculator.js';
import { ExchangeRateChart } from './features/chart/Chart.js';
import { UIUtils } from './utils/ui-utils.js';

export class App {
    constructor() {
        this.calculator = null;
        this.chart = null;
        this.initialize();
    }

    async initialize() {
        try {
            UIUtils.toggleLoading(true);

            // 환율 데이터 가져오기
            /** @type {ExchangeRatesResponse} */
            const rates = await UIUtils.showLoadingAsync(
                this.fetchExchangeRates()
            );

            // 컴포넌트 초기화
            this.calculator = new Calculator('#calculatorContainer');
            this.chart = new ExchangeRateChart('#chartContainer');

            // 환율 데이터 설정
            this.calculator.updateRates(rates);
            this.chart.updateRates(rates);

            // 타이틀 애니메이션
            UIUtils.typeWriter(
                "실시간 환율 계산기",
                document.getElementById('animatedTitle'),
                100
            );

        } catch (error) {
            console.error('Failed to initialize app:', error);
            UIUtils.showAlert('애플리케이션 초기화에 실패했습니다.');
        } finally {
            UIUtils.toggleLoading(false);
        }
    }

    /**
     * 환율 데이터를 가져오는 함수
     * @returns {Promise<ExchangeRatesResponse>} 환율 데이터 Promise
     * @throws {Error} API 호출 실패시 에러
     */
    async fetchExchangeRates() {
        try {
            const response = await fetch('/api/exchange-rates');
            if (!response.ok) {
                throw new Error('Failed to fetch exchange rates');
            }

            return await response.json();
        } catch (error) {
            console.error('Error fetching exchange rates:', error);
            UIUtils.showAlert('환율 정보를 가져오는데 실패했습니다.');
            throw error;
        }
    }
}
