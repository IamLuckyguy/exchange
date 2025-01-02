import {CHART_COLORS, CHART_OPTIONS, CHART_PERIODS} from "../../constants/chart-options.js";
import {STORAGE_KEYS} from "../../constants/storage-keys.js";
import {UIUtils} from "../../utils/ui-utils.js";

export class ExchangeRateChart {
    constructor(containerSelector = '#chartContainer') {
        this.container = document.querySelector(containerSelector);
        this.chartInstances = new Map();

        // 상태 초기화
        this.state = {
            period: CHART_PERIODS.WEEK,
            selectedCurrencies: [],
            exchangeRates: []
        };

        this.loadSettings();
        this.bindEvents();
        this.resizeHandler = this.handleResize.bind(this);
        window.addEventListener('resize', this.debounce(this.resizeHandler, 250));
    }

    // 리사이즈 이벤트 디바운싱을 위한 유틸리티 함수
    debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    // 리사이즈 핸들러
    handleResize() {
        this.renderCharts();
    }

    // 컴포넌트 정리
    destroy() {
        window.removeEventListener('resize', this.resizeHandler);
        this.chartInstances.forEach(chart => chart.destroy());
        this.chartInstances.clear();
    }

    loadSettings() {
        try {
            const savedSettings = localStorage.getItem(STORAGE_KEYS.CHART.SETTINGS);
            if (savedSettings) {
                const settings = JSON.parse(savedSettings);
                this.state.period = settings.period || CHART_PERIODS.WEEK;
            }

            // 저장된 선택 통화 목록 불러오기
            const savedCurrencies = localStorage.getItem(STORAGE_KEYS.CHART.SELECTED_CURRENCIES);
            if (savedCurrencies) {
                this.state.selectedCurrencies = JSON.parse(savedCurrencies);
            }

            // 기간 선택 버튼 상태 복원
            const periodButtons = this.container.querySelectorAll('.period-btn');
            periodButtons.forEach(btn => {
                if (parseInt(btn.dataset.period) === this.state.period) {
                    btn.classList.add('selected');
                }
            });

            // 커스텀 기간 입력 상태 복원
            const customDaysInput = this.container.querySelector('#customDays');
            if (customDaysInput && !periodButtons.length) {
                customDaysInput.value = this.state.period;
            }
        } catch (error) {
            console.error('Failed to load chart settings:', error);
            this.initializeDefaultSettings();
        }
    }

    initializeDefaultSettings() {
        // 초기 상태 설정
        this.state.period = CHART_PERIODS.WEEK;
        // KRW를 제외한 모든 통화 선택
        this.state.selectedCurrencies = this.state.exchangeRates
            .filter(rate => rate.currencyCode !== 'KRW')
            .map(rate => rate.currencyCode);

        // 설정 저장
        this.saveSettings();
    }

    saveSettings() {
        try {
            // 차트 설정 저장
            const settings = {
                period: this.state.period
            };
            localStorage.setItem(STORAGE_KEYS.CHART.SETTINGS, JSON.stringify(settings));

            // 선택된 통화 목록 저장
            localStorage.setItem(
                STORAGE_KEYS.CHART.SELECTED_CURRENCIES,
                JSON.stringify(this.state.selectedCurrencies)
            );
        } catch (error) {
            console.error('Failed to save chart settings:', error);
            UIUtils.showAlert('차트 설정 저장에 실패했습니다.');
        }
    }

    bindEvents() {
        // 기간 선택
        this.container.querySelectorAll('.period-btn').forEach(btn => {
            btn.addEventListener('click', (e) => this.handlePeriodChange(e));
        });

        // 커스텀 기간 입력
        const customDaysInput = this.container.querySelector('#customDays');
        if (customDaysInput) {
            customDaysInput.addEventListener('input', (e) => this.handleCustomPeriodInput(e));
        }

        // 통화 선택
        const currencySelector = this.container.querySelector('#currencySelector');
        if (currencySelector) {
            currencySelector.addEventListener('click', (e) => {
                if (e.target.closest('.currency-option')) {
                    this.handleCurrencySelection(e);
                }
            });
        }
    }

    handlePeriodChange(event) {
        const button = event.target;
        const period = parseInt(button.dataset.period);

        // UI 업데이트
        this.container.querySelectorAll('.period-btn')
            .forEach(btn => btn.classList.remove('selected'));
        button.classList.add('selected');

        // 상태 업데이트
        this.state.period = period;
        this.saveSettings();

        // 차트 업데이트
        this.renderCharts();
    }

    handleCustomPeriodInput(event) {
        const input = event.target;
        let days = parseInt(input.value);

        if (isNaN(days) || days < 1) {
            days = CHART_PERIODS.WEEK;
        } else if (days > CHART_PERIODS.YEAR) {
            days = CHART_PERIODS.YEAR;
        }

        // 기간 버튼 선택 해제
        this.container.querySelectorAll('.period-btn')
            .forEach(btn => btn.classList.remove('selected'));

        // 상태 업데이트
        this.state.period = days;
        input.value = days;
        this.saveSettings();

        // 차트 업데이트
        this.renderCharts();
    }

    async handleCurrencySelection(event) {
        const option = event.target.closest('.currency-option');
        const currencyCode = option.dataset.currency;

        if (option.classList.contains('selected')) {
            this.state.selectedCurrencies = this.state.selectedCurrencies
                .filter(code => code !== currencyCode);
            option.classList.remove('selected');
        } else {
            if (this.state.selectedCurrencies.length < 8) {
                this.state.selectedCurrencies.push(currencyCode);
                option.classList.add('selected');
            } else {
                UIUtils.showAlert('최대 8개까지 선택할 수 있습니다.');
                return;
            }
        }

        // 설정 저장 및 차트 업데이트
        this.saveSettings();
        await this.renderCharts();
    }

    prepareChartData(currencyCode, colorIndex) {
        const rate = this.getExchangeRate(currencyCode);
        if (!rate?.exchangeRateHistories) return null;

        const histories = this.prepareHistoricalData(rate);
        const color = CHART_COLORS[colorIndex % CHART_COLORS.length];

        return {
            labels: histories.map(history => this.formatDate(history.at)),
            datasets: [{
                label: `${rate.countryFlag} ${currencyCode}/KRW`,
                data: histories.map(history => parseFloat(history.rv)),
                borderColor: color,
                backgroundColor: `${color}22`,
                borderWidth: 2,
                tension: 0.4
            }]
        };
    }

    prepareHistoricalData(exchangeRate) {
        if (this.state.period === CHART_PERIODS.DAY) {
            return exchangeRate.exchangeRateRealTime
                ?.sort((a, b) => a.r - b.r) || [];
        }

        return exchangeRate.exchangeRateHistories
            ?.slice(0, this.state.period)
            .sort((a, b) => new Date(a.at) - new Date(b.at)) || [];
    }

    async renderCharts() {
        const chartsContainer = this.container.querySelector('.charts-container');
        if (!chartsContainer) return;

        // 기존 차트 인스턴스들 제거
        this.chartInstances.forEach(chart => chart.destroy());
        this.chartInstances.clear();
        chartsContainer.innerHTML = '';

        // 차트 그리드 컨테이너 생성
        const chartGrid = document.createElement('div');
        chartGrid.className = 'chart-grid';
        if (this.state.selectedCurrencies.length === 1) {
            chartGrid.classList.add('single-chart');
        }
        chartsContainer.appendChild(chartGrid);

        // 각 통화별 차트 생성
        this.state.selectedCurrencies.forEach((currencyCode, index) => {
            const chartWrapper = document.createElement('div');
            chartWrapper.className = 'chart-wrapper';

            const canvas = document.createElement('canvas');
            canvas.id = `chart-${currencyCode}`;
            chartWrapper.appendChild(canvas);
            chartGrid.appendChild(chartWrapper);

            const data = this.prepareChartData(currencyCode, index);
            if (!data) return;

            const ctx = canvas.getContext('2d');
            const chartInstance = new Chart(ctx, {
                type: 'line',
                data: data,
                options: {
                    ...CHART_OPTIONS.change,
                    maintainAspectRatio: false,
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'bottom',
                            align: 'start',
                            labels: {
                                color: '#ffffff',
                                boxWidth: 15,
                                padding: 15
                            }
                        },
                        title: {
                            display: true,
                            text: `${this.getExchangeRate(currencyCode)?.countryFlag || ''} ${currencyCode} 원화 환율`,
                            color: '#ffffff',
                            font: {
                                size: 16,
                                weight: 'bold'
                            },
                            padding: {
                                top: 10,
                                bottom: 20
                            }
                        }
                    },
                    scales: {
                        x: {
                            grid: { color: '#4a4a4a' },
                            ticks: { color: '#ffffff' }
                        },
                        y: {
                            grid: { color: '#4a4a4a' },
                            ticks: {
                                color: '#ffffff',
                                callback: value => value.toLocaleString('ko-KR')
                            }
                        }
                    }
                }
            });

            this.chartInstances.set(currencyCode, chartInstance);
        });
    }

    renderCurrencyOptions() {
        const selector = this.container.querySelector('#currencySelector');
        if (!selector) return;

        selector.innerHTML = this.state.exchangeRates
            .filter(rate => rate.currencyCode !== 'KRW')
            .map(rate => `
                <div class="currency-option ${this.state.selectedCurrencies.includes(rate.currencyCode) ? 'selected' : ''}" 
                     data-currency="${rate.currencyCode}">
                    <span class="currency-flag">${rate.countryFlag}</span>
                    <span class="currency-code">${rate.currencyCode}</span>
                </div>
            `)
            .join('');
    }

    updateRates(rates) {
        this.state.exchangeRates = rates;

        // 최초 실행 시 선택된 통화가 없다면 모든 통화 선택
        if (this.state.selectedCurrencies.length === 0) {
            this.state.selectedCurrencies = rates
                .filter(rate => rate.currencyCode !== 'KRW')
                .map(rate => rate.currencyCode);
            this.saveSettings();
        }

        this.render();
    }

    render() {
        this.renderCurrencyOptions();
        this.renderCharts();
    }

    getExchangeRate(currencyCode) {
        return this.state.exchangeRates.find(rate => rate.currencyCode === currencyCode);
    }

    formatDate(date) {
        const d = new Date(date);

        if (this.state.period === CHART_PERIODS.DAY) {
            return `${String(d.getDate()).padStart(2, '0')}일 ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`;
        }

        const year = String(d.getFullYear()).slice(2);
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');

        return `${year}.${month}.${day}`;
    }
}