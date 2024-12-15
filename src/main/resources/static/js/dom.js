// dom.js
const Dom = {
    initializeUI() {
        this.initializeChartCurrencySelectors();
        this.initializeChartTypeSelector();
        this.initializeCurrencySelector();
        this.initializePeriodButtons();
        this.attachEventListeners();
        this.initializeCurrencyInputs();
    },

    initializeChartCurrencySelectors() {
        const baseCurrencySelect = $('#baseCurrency');
        const targetCurrencySelect = $('#targetCurrency');

        baseCurrencySelect.empty();
        targetCurrencySelect.empty();

        Model.exchangeRates.forEach(rate => {
            baseCurrencySelect.append(`<option value="${rate.currencyCode}" 
            ${rate.currencyCode === Model.currentState.chartSettings.baseCurrency ? 'selected' : ''}>
            ${rate.countryFlag} ${rate.currencyCode}
        </option>`);

            targetCurrencySelect.append(`<option value="${rate.currencyCode}"
            ${rate.currencyCode === Model.currentState.chartSettings.targetCurrency ? 'selected' : ''}>
            ${rate.countryFlag} ${rate.currencyCode}
        </option>`);
        });
    },

    initializeCurrencyInputs() {
        const container = $('#currencyInputs');
        container.empty();

        // 모든 통화에 대한 input-group 생성
        Model.exchangeRates.forEach(exchangeRate => {
            container.append(`
            <div class="input-group" style="display: none;">
                <input type="text"
                       id="${exchangeRate.currencyCode}"
                       class="currency-input"
                       value="0" />
                <button class="currency-btn">
                    <span class="currency-flag">${exchangeRate.countryFlag}</span>
                    <span class="currency-code">${exchangeRate.currencyCode}</span>
                </button>
            </div>
        `);
        });

        // 초기 선택된 통화들만 보이게 처리
        this.updateSelectedCurrencies();
    },

    initializeChartTypeSelector() {
        const chartType = Model.currentState.chartSettings.type;
        $(`.chart-type-btn[data-type="${chartType}"]`).addClass('active');
        this.updateChartSettingsVisibility();
    },

    initializeCurrencySelector() {
        const container = $('#currencySelector');
        container.empty();

        Model.exchangeRates.forEach(rate => {
            const isSelected = Model.currentState.chartSettings.selectedCurrencies.has(rate.currencyCode);
            container.append(`
                <div class="currency-option ${isSelected ? 'selected' : ''}" 
                     data-currency="${rate.currencyCode}">
                    <span class="currency-flag">${rate.countryFlag}</span>
                    <span class="currency-code">${rate.currencyCode}</span>
                </div>
            `);
        });

        this.updateSelectedCurrencies();
    },

    initializePeriodButtons() {
        const currentPeriod = Model.currentState.chartSettings.period;
        $(`.period-btn[data-period="${currentPeriod}"]`).addClass('selected');
    },

    renderChart(ctx, data, type) {
        if (window.exchangeRateChart instanceof Chart) {
            window.exchangeRateChart.destroy();
        }

        window.exchangeRateChart = new Chart(ctx, {
            type: 'line',
            data: data,
            options: Objects.ChartOptions[type]
        });
    },

    updateCurrencyDialog() {
        const dialogBody = $('.dialog-body');
        dialogBody.empty();

        Model.exchangeRates.forEach(exchangeRate => {
            dialogBody.append(`
            <label class="currency-item">
                <input type="checkbox" 
                       value="${exchangeRate.currencyCode}" 
                       ${Model.getSelectedCurrencies().includes(exchangeRate.currencyCode) ? 'checked' : ''} />
                <div class="currency-info">
                    <span>
                        <span class="currency-flag">${exchangeRate.countryFlag}</span>
                        <span class="currency-code">${exchangeRate.currencyCode}</span>
                    </span>
                    <span class="currency-name">${exchangeRate.countryName}</span>
                </div>
            </label>
        `);
        });
    },

    updateSelectedCurrencies() {
        const selectedCurrencies = Model.getSelectedCurrencies();
        $('#selectedCount').text(selectedCurrencies.length);

        // 모든 input-group 숨김
        $('.input-group').hide();

        // 선택된 통화만 보이게 처리
        selectedCurrencies.forEach(code => {
            $(`#${code}`).closest('.input-group').show();
        });
    },

    updateChartSettingsVisibility() {
        const type = Model.currentState.chartSettings.type;
        if (type === 'trend') {
            $('#trendChartSettings').show();
            $('#changeChartSettings').hide();
        } else {
            $('#trendChartSettings').hide();
            $('#changeChartSettings').show();
        }
    },

    updateCalculatorTitle(currencyCode, amount) {
        const exchangeRate = Model.getCurrentExchangeRate(currencyCode);
        if (!exchangeRate) return;

        const formattedAmount = Helper.formatNumber(amount, exchangeRate.decimals, 'floor');
        $('#calculatorTitle').text(`${currencyCode} ${formattedAmount} 기준 환율`);
        $('#baseFlag').text(exchangeRate.countryFlag);
    },

    updateCurrencyInput(code, amount, exchangeRate) {
        if (!code || !amount || !exchangeRate) return;

        const formattedAmount = Helper.formatNumber(amount, exchangeRate.decimals, 'floor');
        $(`#${code}`).val(formattedAmount);
    },

    showLoading() {
        $('#loadingSpinner').show();
    },

    hideLoading() {
        $('#loadingSpinner').hide();
    },

    attachEventListeners() {
        EventHandler.init();
    }
};
