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

        Model.exchangeRates.forEach(exchangeRate => {
            baseCurrencySelect.append(`<option value="${exchangeRate.currencyCode}" 
            ${exchangeRate.currencyCode === Model.currentState.chartSettings.baseCurrency ? 'selected' : ''}>
            ${exchangeRate.countryFlag} ${exchangeRate.currencyCode}
        </option>`);

            targetCurrencySelect.append(`<option value="${exchangeRate.currencyCode}"
            ${exchangeRate.currencyCode === Model.currentState.chartSettings.targetCurrency ? 'selected' : ''}>
            ${exchangeRate.countryFlag} ${exchangeRate.currencyCode}
        </option>`);
        });
    },

    initializeCurrencyInputs() {
        const container = $('#currencyInputs');
        container.empty();

        // 선택된 통화 순서대로 처리
        const selectedCurrencies = Model.getCalculatorCurrencies();
        selectedCurrencies.forEach(currencyCode => {
            const exchangeRate = Model.exchangeRates.find(rate => rate.currencyCode === currencyCode);
            if (!exchangeRate) return;

            container.append(`
                <div class="input-group">
                    <div class="drag-handle" title="순서 변경"></div>
                    <div class="currency-info">
                        <span class="currency-flag">${exchangeRate.countryFlag}</span>
                        <span class="currency-code">${exchangeRate.currencyCode}</span>
                    </div>
                    <input type="text"
                        inputmode="decimal"
                        pattern="[0-9]*"
                        id="${exchangeRate.currencyCode}"
                        class="currency-input"
                        value="0" />
                </div>
            `);
        });

        // 나머지 통화들도 숨김 상태로 추가
        Model.exchangeRates.forEach(exchangeRate => {
            if (!selectedCurrencies.includes(exchangeRate.currencyCode)) {
                container.append(`
                    <div class="input-group" style="display: none;">
                        <div class="drag-handle" title="순서 변경"></div>
                        <div class="currency-info">
                            <span class="currency-flag">${exchangeRate.countryFlag}</span>
                            <span class="currency-code">${exchangeRate.currencyCode}</span>
                        </div>
                        <input type="text"
                            inputmode="decimal"
                            pattern="[0-9]*"
                            id="${exchangeRate.currencyCode}"
                            class="currency-input"
                            value="0" />
                    </div>
                `);
            }
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
            const isSelected = Model.getChartSelectedCurrencies().includes(rate.currencyCode);
            container.append(`
                <div class="currency-option ${isSelected ? 'selected' : ''}" 
                     data-currency="${rate.currencyCode}">
                    <span class="currency-flag">${rate.countryFlag}</span>
                    <span class="currency-code">${rate.currencyCode}</span>
                </div>
            `);
        });
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
                           ${Model.getCalculatorCurrencies().includes(exchangeRate.currencyCode) ? 'checked' : ''} />
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
        const selectedCurrencies = Model.getCalculatorCurrencies();
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

    updateCurrencyInput(currencyCode, amount, exchangeRate) {
        const inputGroup = $(`#${currencyCode}`).closest('.input-group');
        if (!inputGroup.length) {
            const newInputGroup = `
                <div class="input-group">
                    <div class="drag-handle" title="순서 변경"></div>
                    <div class="currency-info">
                        <span class="currency-flag">${exchangeRate.countryFlag}</span>
                        <span class="currency-code">${currencyCode}</span>
                    </div>
                    <input type="text" 
                           id="${currencyCode}"
                           class="currency-input" 
                           value="${Helper.formatNumber(amount, exchangeRate.decimals)}"
                           data-decimals="${exchangeRate.decimals}">
                </div>
            `;
            $('#currencyInputs').append(newInputGroup);
        } else {
            const input = inputGroup.find('input');
            input.val(Helper.formatNumber(amount, exchangeRate.decimals));
        }
    },

    attachEventListeners() {
        EventHandler.init();
    }
};
