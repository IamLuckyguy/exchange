// model.js
const Model = {
    exchangeRates: [],
    currentState: {
        selectedCurrency: 'USD',
        currentAmount: 1,
        chartSettings: {
            type: 'trend',
            period: 7,
            baseCurrency: 'USD',
            targetCurrency: 'KRW',
            selectedCurrencies: new Set(['USD', 'KRW', 'JPY'])
        }
    },

    setExchangeRates(rates) {
        this.exchangeRates = rates;
    },

    updateChartSettings(settings) {
        try {
            if (settings.selectedCurrencies) {
                const currencies = Array.isArray(settings.selectedCurrencies)
                    ? settings.selectedCurrencies
                    : Object.values(settings.selectedCurrencies);

                settings = {
                    ...settings,
                    selectedCurrencies: new Set(currencies)
                };
            }

            this.currentState.chartSettings = {
                ...this.currentState.chartSettings,
                ...settings
            };
        } catch (error) {
            CommonLibrary.showAlert('차트 설정 업데이트에 실패했습니다. (EM_001)');
            // 기본값으로 복구
            this.currentState.chartSettings.selectedCurrencies = new Set(['USD', 'KRW', 'JPY']);
        }
    },

    getCurrentExchangeRate(currencyCode) {
        return this.exchangeRates.find(rate => rate.currencyCode === currencyCode);
    },

    getSelectedCurrencies() {
        return Array.from(this.currentState.chartSettings.selectedCurrencies);
    },

    addSelectedCurrency(currencyCode) {
        this.currentState.chartSettings.selectedCurrencies.add(currencyCode);
    },

    removeSelectedCurrency(currencyCode) {
        this.currentState.chartSettings.selectedCurrencies.delete(currencyCode);
    },

    setAmount(amount) {
        this.currentState.currentAmount = amount;
    },

    setSelectedCurrency(currencyCode) {
        this.currentState.selectedCurrency = currencyCode;
    }
};
