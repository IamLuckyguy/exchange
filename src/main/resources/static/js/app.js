// resources/static/js/app.js
import { Calculator } from './features/calculator/Calculator.js';
import { ExchangeRateChart } from './features/chart/Chart.js';
import { UIUtils } from './utils/ui-utils.js';

export class App {
    constructor() {
        this.calculator = null;
        this.chart = null;
        this.sseConnection = null;
        this.initialize();
    }

    async initialize() {
        try {
            UIUtils.toggleLoading(true);

            // 환율 데이터 가져오기
            const rates = await UIUtils.showLoadingAsync(
                this.fetchExchangeRates()
            );

            // 초기 데이터 로컬 스토리지 저장
            this.saveExchangeRatesToLocalStorage(rates);

            // SSE 연결 설정
            this.setupSSEConnection();

            // 컴포넌트 초기화
            this.calculator = new Calculator('#calculatorContainer');
            this.chart = new ExchangeRateChart('#chartContainer');

            // 환율 데이터 설정
            this.calculator.updateRates(rates);
            this.chart.updateRates(rates);

            // 24시간이 지난 데이터 정리를 위한 인터벌 설정
            setInterval(() => this.cleanupOldData(), 5 * 60 * 1000); // 5분마다 실행

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

    saveExchangeRatesToLocalStorage(rates) {
        try {
            localStorage.setItem('exchange.rates', JSON.stringify(rates));
        } catch (error) {
            console.error('Failed to save exchange rates to localStorage:', error);
        }
    }

    cleanupOldData() {
        try {
            const savedRates = JSON.parse(localStorage.getItem('exchange.rates'));
            if (!savedRates) return;

            const twentyFourHoursAgo = new Date();
            twentyFourHoursAgo.setHours(twentyFourHoursAgo.getHours() - 24);

            // 각 통화별로 24시간 이내의 데이터만 유지
            savedRates.forEach(rate => {
                if (rate.exchangeRateRealTime) {
                    rate.exchangeRateRealTime = rate.exchangeRateRealTime.filter(data => {
                        const dataTime = new Date(data.at);
                        return dataTime >= twentyFourHoursAgo;
                    });
                }
            });

            this.saveExchangeRatesToLocalStorage(savedRates);

            // 컴포넌트 업데이트
            if (this.calculator) this.calculator.updateRates(savedRates);
            if (this.chart) this.chart.updateRates(savedRates);
        } catch (error) {
            console.error('Failed to cleanup old data:', error);
        }
    }

    updateRealTimeData(newRealTimeData) {
        try {
            const savedRates = JSON.parse(localStorage.getItem('exchange.rates'));
            if (!savedRates) return;

            savedRates.forEach(rate => {
                const newData = newRealTimeData.find(data => data.currencyCode === rate.currencyCode);
                if (newData && newData.latestRate) {
                    const rateData = newData.latestRate;

                    // 이미 존재하는 고시 회차인지 확인
                    const existingIndex = rate.exchangeRateRealTime.findIndex(
                        item => item.r === rateData.r
                    );

                    if (existingIndex === -1) {
                        // 새로운 고시 회차 데이터 추가
                        rate.exchangeRateRealTime.push(rateData);
                    } else {
                        // 기존 고시 회차 데이터 업데이트
                        rate.exchangeRateRealTime[existingIndex] = rateData;
                    }

                    // 24시간 이전 데이터 필터링
                    const twentyFourHoursAgo = new Date();
                    twentyFourHoursAgo.setHours(twentyFourHoursAgo.getHours() - 24);

                    rate.exchangeRateRealTime = rate.exchangeRateRealTime.filter(data => {
                        const dataTime = new Date(data.at);
                        return dataTime >= twentyFourHoursAgo;
                    });
                }
            });

            this.saveExchangeRatesToLocalStorage(savedRates);

            // 컴포넌트 업데이트
            if (this.calculator) this.calculator.updateRates(savedRates);
            if (this.chart) this.chart.updateRates(savedRates);

        } catch (error) {
            console.error('Failed to update real-time data:', error);
        }
    }

    setupSSEConnection() {
        try {
            const eventSource = new EventSource('/api/exchange-rates/event/subscribe');

            eventSource.onmessage = (event) => {
                const data = JSON.parse(event.data);
                this.updateRealTimeData(data);
            };

            eventSource.onerror = (error) => {
                console.error('SSE connection error:', error);
                eventSource.close();
                // 연결 재시도 로직
                setTimeout(() => this.setupSSEConnection(), 5000);
            };

            this.sseConnection = eventSource;
        } catch (error) {
            console.error('Failed to setup SSE connection:', error);
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