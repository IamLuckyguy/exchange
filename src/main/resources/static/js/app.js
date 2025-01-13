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
            setInterval(() => this.cleanupOldData(), 1 * 60 * 1000); // 5분마다 실행

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

            // 각 통화별로 처리
            savedRates.forEach(rate => {
                if (rate.exchangeRateRealTime && rate.exchangeRateRealTime.length > 0) {
                    // 마지막 데이터의 시간을 기준으로 설정
                    const lastDataTime = new Date(rate.exchangeRateRealTime[rate.exchangeRateRealTime.length - 1].at);
                    const twentyFourHoursBeforeLastData = new Date(lastDataTime);
                    twentyFourHoursBeforeLastData.setHours(twentyFourHoursBeforeLastData.getHours() - 24);

                    // 마지막 데이터 시간 기준 24시간 이내의 데이터만 필터링
                    rate.exchangeRateRealTime = rate.exchangeRateRealTime.filter(data => {
                        const dataTime = new Date(data.at);
                        return dataTime >= twentyFourHoursBeforeLastData;
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

    updateRealTimeData(sseData) {
        try {
            const { exchangeRateRealTime, updatedAt } = sseData;

            // 기존 저장된 데이터 가져오기
            const savedRates = JSON.parse(localStorage.getItem('exchange.rates'));
            if (!savedRates) return;

            let hasUpdates = false;

            if (exchangeRateRealTime.length > 0) {
                // 새로운 실시간 데이터가 있는 경우
                savedRates.forEach(rate => {
                    const newData = exchangeRateRealTime.find(data => data.currencyCode === rate.currencyCode);
                    if (newData?.dailyRoundRates?.[0]) {
                        hasUpdates = true;

                        // 새로운 데이터 포맷을 기존 형식으로 변환
                        const latestRate = {
                            rv: newData.dailyRoundRates[0].rv,
                            r: newData.dailyRoundRates[0].r,
                            t: newData.dailyRoundRates[0].t,
                            tr: newData.dailyRoundRates[0].tr,
                            td: newData.dailyRoundRates[0].td,
                            live: newData.dailyRoundRates[0].live,
                            market: newData.dailyRoundRates[0].market,
                            at: newData.dailyRoundRates[0].at
                        };

                        // exchangeRateRealTime이 없으면 생성
                        if (!rate.exchangeRateRealTime) {
                            rate.exchangeRateRealTime = [];
                        }

                        // 새로운 데이터 추가
                        rate.exchangeRateRealTime.push(latestRate);

                        // 시간순 정렬
                        rate.exchangeRateRealTime.sort((a, b) => new Date(a.at) - new Date(b.at));

                        // 24시간 이전 데이터 필터링
                        const twentyFourHoursAgo = new Date(new Date(updatedAt).getTime() - (24 * 60 * 60 * 1000));
                        rate.exchangeRateRealTime = rate.exchangeRateRealTime.filter(data =>
                            new Date(data.at) >= twentyFourHoursAgo
                        );
                    }
                });
            }

            // 마지막 업데이트 시간 저장
            localStorage.setItem('exchange.lastUpdate', updatedAt);

            // 업데이트된 데이터 저장
            this.saveExchangeRatesToLocalStorage(savedRates);

            // 컴포넌트 업데이트
            if (this.calculator) this.calculator.updateRealTimeData(exchangeRateRealTime);
            if (this.chart) this.chart.updateRates(savedRates, updatedAt);

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

            const rates = await response.json();

            // 각 통화별로 실시간 데이터를 처리
            return rates.map(rate => {
                // exchangeRateRealTime이 존재하고 데이터가 있는 경우에만 처리
                if (rate.exchangeRateRealTime && rate.exchangeRateRealTime.length > 0) {
                    // 시간순으로 정렬 (최신 데이터가 0번 인덱스에 오도록)
                    const sortedRealTime = [...rate.exchangeRateRealTime].sort(
                        (a, b) => new Date(b.at) - new Date(a.at)
                    );

                    // 가장 최근 데이터 시간 기준으로 24시간 전 시간 계산
                    const latestTime = new Date(sortedRealTime[0].at);
                    const twentyFourHoursAgo = new Date(latestTime.getTime() - (24 * 60 * 60 * 1000));

                    // 24시간 이내의 데이터만 필터링
                    const filteredRealTime = sortedRealTime.filter(data => {
                        const dataTime = new Date(data.at);
                        return dataTime >= twentyFourHoursAgo;
                    });

                    return {
                        ...rate,
                        exchangeRateRealTime: filteredRealTime,
                        currentRate: filteredRealTime[0]  // 가장 최근 데이터
                    };
                }
                // exchangeRateRealTime이 없거나 비어있는 경우 원본 데이터 반환
                return rate;
            });

        } catch (error) {
            console.error('Error fetching exchange rates:', error);
            UIUtils.showAlert('환율 정보를 가져오는데 실패했습니다.');
            throw error;
        }
    }
}
