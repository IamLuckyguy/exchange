// resources/static/js/features/chart/Chart.js
import { STORAGE_KEYS } from '../../constants/storage-keys.js';
import { CURRENCY_DEFAULTS } from '../../constants/currency-defaults.js';
import { CHART_OPTIONS, CHART_COLORS, CHART_PERIODS } from '../../constants/chart-options.js';
import { UIUtils } from '../../utils/ui-utils.js';

export class ExchangeRateChart {
    constructor(containerSelector = '#chartContainer') {
        // DOM 엘리먼트 참조
        this.container = document.querySelector(containerSelector);
        this.canvas = this.container.querySelector('#exchangeRateChart');
        this.chartInstance = null;

        // 상태 초기화
        this.state = {
            type: 'trend',
            period: CHART_PERIODS.WEEK,
            baseCurrency: CURRENCY_DEFAULTS.DEFAULT_BASE_CURRENCY,
            targetCurrency: CURRENCY_DEFAULTS.DEFAULT_TARGET_CURRENCY,
            selectedCurrencies: [CURRENCY_DEFAULTS.DEFAULT_BASE_CURRENCY],
            exchangeRates: []
        };

        this.loadSettings();
        this.bindEvents();
    }

    loadSettings() {
        try {
            const savedSettings = localStorage.getItem(STORAGE_KEYS.CHART.SETTINGS);
            const savedCurrencies = localStorage.getItem(STORAGE_KEYS.CHART.SELECTED_CURRENCIES);

            if (savedSettings) {
                const settings = JSON.parse(savedSettings);
                this.state = { ...this.state, ...settings };
            }

            if (savedCurrencies) {
                this.state.selectedCurrencies = JSON.parse(savedCurrencies);
            }
        } catch (error) {
            console.error('Failed to load chart settings:', error);
            UIUtils.showAlert('차트 설정을 불러오는데 실패했습니다.');
        }
    }

    saveSettings() {
        try {
            const settings = {
                type: this.state.type,
                period: this.state.period,
                baseCurrency: this.state.baseCurrency,
                targetCurrency: this.state.targetCurrency
            };

            localStorage.setItem(STORAGE_KEYS.CHART.SETTINGS, JSON.stringify(settings));
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
        // 차트 타입 변경
        this.container.querySelectorAll('.chart-type-btn').forEach(btn => {
            btn.addEventListener('click', (e) => this.handleChartTypeChange(e));
        });

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
        this.bindCurrencySelectors();
    }

    bindCurrencySelectors() {
        const baseCurrencySelect = this.container.querySelector('#baseCurrency');
        const targetCurrencySelect = this.container.querySelector('#targetCurrency');
        const currencySelector = this.container.querySelector('#currencySelector');

        if (baseCurrencySelect && targetCurrencySelect) {
            baseCurrencySelect.addEventListener('change', () => this.handleCurrencyChange());
            targetCurrencySelect.addEventListener('change', () => this.handleCurrencyChange());
        }

        if (currencySelector) {
            currencySelector.addEventListener('click', (e) => {
                if (e.target.closest('.currency-option')) {
                    this.handleCurrencySelection(e);
                }
            });
        }
    }

    async handleChartTypeChange(event) {
        const button = event.target;
        const type = button.dataset.type;

        // UI 업데이트
        this.container.querySelectorAll('.chart-type-btn')
            .forEach(btn => btn.classList.remove('active'));
        button.classList.add('active');

        // 상태 업데이트
        this.state.type = type;
        this.saveSettings();
        this.updateChartSettingsVisibility();
        await this.renderChart();
    }

    async handlePeriodChange(event) {
        const button = event.target;
        const period = parseInt(button.dataset.period);

        // UI 업데이트
        this.container.querySelectorAll('.period-btn')
            .forEach(btn => btn.classList.remove('selected'));
        button.classList.add('selected');

        // 상태 업데이트
        this.state.period = period;
        this.saveSettings();
        await this.renderChart();
    }

    handleCustomPeriodInput(event) {
        const input = event.target;
        let days = parseInt(input.value);

        if (isNaN(days) || days < 1) {
            days = CHART_PERIODS.WEEK;
        } else if (days > CHART_PERIODS.YEAR) {
            days = CHART_PERIODS.YEAR;
        }

        this.container.querySelectorAll('.period-btn')
            .forEach(btn => btn.classList.remove('selected'));

        this.state.period = days;
        input.value = days;
        this.saveSettings();
        this.renderChart();
    }

    async handleCurrencyChange() {
        const baseCurrency = this.container.querySelector('#baseCurrency').value;
        const targetCurrency = this.container.querySelector('#targetCurrency').value;

        this.state.baseCurrency = baseCurrency;
        this.state.targetCurrency = targetCurrency;
        this.saveSettings();
        await this.renderChart();
    }

    async handleCurrencySelection(event) {
        const option = event.target.closest('.currency-option');
        const currencyCode = option.dataset.currency;

        if (option.classList.contains('selected')) {
            if (this.state.selectedCurrencies.length > 1) {
                this.state.selectedCurrencies = this.state.selectedCurrencies
                    .filter(code => code !== currencyCode);
                option.classList.remove('selected');
            }
        } else {
            if (this.state.selectedCurrencies.length < 8) {
                this.state.selectedCurrencies.push(currencyCode);
                option.classList.add('selected');
            } else {
                UIUtils.showAlert('최대 8개까지 선택할 수 있습니다.');
                return;
            }
        }

        this.saveSettings();
        await this.renderChart();
    }

    updateChartSettingsVisibility() {
        const trendSettings = this.container.querySelector('#trendChartSettings');
        const changeSettings = this.container.querySelector('#changeChartSettings');

        if (this.state.type === 'trend') {
            trendSettings.style.display = 'block';
            changeSettings.style.display = 'none';
        } else {
            trendSettings.style.display = 'none';
            changeSettings.style.display = 'block';
        }
    }

    // 차트 데이터 준비 및 렌더링
    prepareChartData() {
        if (this.state.type === 'trend') {
            return this.prepareTrendChartData();
        } else {
            return this.prepareChangeRateData();
        }
    }

    prepareTrendChartData() {
        const baseRate = this.getExchangeRate(this.state.baseCurrency);
        const targetRate = this.getExchangeRate(this.state.targetCurrency);

        if (!baseRate?.exchangeRateHistories || !targetRate?.exchangeRateHistories) {
            return {
                labels: [],
                datasets: [{
                    label: `${baseRate?.currencyCode || ''}/${targetRate?.currencyCode || ''}`,
                    data: [],
                    borderColor: CHART_COLORS[0],
                    backgroundColor: `${CHART_COLORS[0]}22`,
                    borderWidth: 2,
                    tension: 0.4
                }]
            };
        }

        const baseData = this.prepareHistoricalData(baseRate);
        const targetData = this.prepareHistoricalData(targetRate);

        return {
            labels: baseData.map(history => this.formatDate(history.at)),
            datasets: [{
                label: `${baseRate.currencyCode}/${targetRate.currencyCode}`,
                data: baseData.map((history, index) => {
                    const baseValue = parseFloat(history.rv);
                    const targetValue = parseFloat(targetData[index]?.rv || 0);
                    return baseValue / targetValue;
                }),
                borderColor: CHART_COLORS[0],
                backgroundColor: `${CHART_COLORS[0]}22`,
                borderWidth: 2,
                tension: 0.4
            }]
        };
    }

    prepareChangeRateData() {
        const datasets = this.state.selectedCurrencies.map((currencyCode, index) => {
            const rate = this.getExchangeRate(currencyCode);
            if (!rate?.exchangeRateHistories) return null;

            const histories = this.prepareHistoricalData(rate);
            const baseValue = parseFloat(histories[0]?.rv || 0);

            return {
                label: `${currencyCode} 변화율 (%)`,
                data: histories.map(history => {
                    const currentValue = parseFloat(history.rv);
                    return ((currentValue - baseValue) / baseValue * 100).toFixed(2);
                }),
                borderColor: CHART_COLORS[index % CHART_COLORS.length],
                backgroundColor: `${CHART_COLORS[index % CHART_COLORS.length]}22`,
                borderWidth: 2,
                tension: 0.4
            };
        }).filter(dataset => dataset !== null);

        const firstRate = this.getExchangeRate(this.state.selectedCurrencies[0]);
        const labels = this.prepareHistoricalData(firstRate)
            .map(history => this.formatDate(history.at));

        return { labels, datasets };
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

    async renderChart() {
        if (!this.canvas) return;

        // 기존 차트 제거
        if (this.chartInstance) {
            this.chartInstance.destroy();
        }

        const data = this.prepareChartData();
        const options = CHART_OPTIONS[this.state.type];

        try {
            this.chartInstance = new Chart(this.canvas, {
                type: 'line',
                data: data,
                options: options
            });
        } catch (error) {
            console.error('Failed to render chart:', error);
            UIUtils.showAlert('차트 렌더링에 실패했습니다.');
        }
    }

    updateRates(rates) {
        this.state.exchangeRates = rates;
        this.render();
    }

    render() {
        this.renderCurrencySelectors();
        this.renderChart();
        this.updateChartSettingsVisibility();
    }

    renderCurrencySelectors() {
        this.renderBaseCurrencySelectors();
        this.renderCurrencyOptions();
    }

    renderBaseCurrencySelectors() {
        const baseCurrencySelect = this.container.querySelector('#baseCurrency');
        const targetCurrencySelect = this.container.querySelector('#targetCurrency');

        if (!baseCurrencySelect || !targetCurrencySelect) return;

        const createOptions = (select, selectedValue) => {
            select.innerHTML = this.state.exchangeRates
                .map(rate => `
                    <option value="${rate.currencyCode}" 
                            ${rate.currencyCode === selectedValue ? 'selected' : ''}>
                        ${rate.countryFlag} ${rate.currencyCode}
                    </option>
                `)
                .join('');
        };

        createOptions(baseCurrencySelect, this.state.baseCurrency);
        createOptions(targetCurrencySelect, this.state.targetCurrency);
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

    // 유틸리티 메서드
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