// model.js
const Model = {
    exchangeRates: [
        {
            countryFlag: '',
            countryName: '',
            currencyCode: '',
            decimals: '',
            exchangeRateHistories: [],
            fetchedAt: '',
            unitAmount: '',
            updatedAt: '',
        }
    ],
    currentState: {
        selectedCurrency: 'USD',
        currentAmount: 1,
        calculator: {
            selectedCurrencies: ['USD', 'KRW', 'JPY', 'CNY', 'EUR']
        },
        chartSettings: {
            type: 'trend',
            period: 7,
            baseCurrency: 'USD',
            targetCurrency: 'KRW',
            chartSelectedCurrencies: ['USD', 'KRW']
        }
    },

    setExchangeRates(rates) {
        this.exchangeRates = rates;
    },

    updateChartSettings(settings) {
        try {
            // 차트용 선택된 통화 설정
            if (settings.chartSelectedCurrencies) {
                const chartCurrencies = Array.isArray(settings.chartSelectedCurrencies)
                    ? settings.chartSelectedCurrencies
                    : Object.values(settings.chartSelectedCurrencies);

                settings.chartSelectedCurrencies = chartCurrencies;
            }

            this.currentState.chartSettings = {
                ...this.currentState.chartSettings,
                ...settings
            };
        } catch (error) {
            CommonLibrary.showAlert('차트 설정 업데이트에 실패했습니다. (EM_001)');
            // 기본값으로 복구
            this.currentState.chartSettings.type = 'trend';
            this.currentState.chartSettings.chartSelectedCurrencies = ['USD', 'KRW'];
        }
    },

    // 계산기용 메서드
    getCalculatorCurrencies() {
        if (!this.currentState.calculator.selectedCurrencies) {
            this.currentState.calculator.selectedCurrencies = ['USD', 'KRW', 'JPY', 'CNY', 'EUR'];
        }
        // 항상 배열로 반환되도록 보장
        return Array.isArray(this.currentState.calculator.selectedCurrencies) 
            ? this.currentState.calculator.selectedCurrencies 
            : ['USD', 'KRW', 'JPY', 'CNY', 'EUR'];
    },

    addCalculatorCurrency(currencyCode) {
        // selectedCurrencies가 없거나 배열이 아닌 경우 초기화
        if (!this.currentState.calculator.selectedCurrencies || !Array.isArray(this.currentState.calculator.selectedCurrencies)) {
            this.currentState.calculator.selectedCurrencies = [];
        }
        
        if (!this.currentState.calculator.selectedCurrencies.includes(currencyCode)) {
            this.currentState.calculator.selectedCurrencies.push(currencyCode);
            // 변경사항을 즉시 저장
            StorageUtil.updateCalculatorSettings({
                selectedCurrencies: this.currentState.calculator.selectedCurrencies
            });
        }
    },

    removeCalculatorCurrency(currencyCode) {
        // selectedCurrencies가 없거나 배열이 아닌 경우 초기화
        if (!this.currentState.calculator.selectedCurrencies || !Array.isArray(this.currentState.calculator.selectedCurrencies)) {
            this.currentState.calculator.selectedCurrencies = [];
            return;
        }
        
        const index = this.currentState.calculator.selectedCurrencies.indexOf(currencyCode);
        if (index > -1) {
            this.currentState.calculator.selectedCurrencies.splice(index, 1);
            // 변경사항을 즉시 저장
            StorageUtil.updateCalculatorSettings({
                selectedCurrencies: this.currentState.calculator.selectedCurrencies
            });
        }
    },

    // 차트용 메서드
    getChartSelectedCurrencies() {
        return this.currentState.chartSettings.chartSelectedCurrencies || ['USD', 'KRW'];
    },

    addChartSelectedCurrency(currencyCode) {
        if (!this.currentState.chartSettings.chartSelectedCurrencies) {
            this.currentState.chartSettings.chartSelectedCurrencies = [];
        }
        if (!this.currentState.chartSettings.chartSelectedCurrencies.includes(currencyCode)) {
            this.currentState.chartSettings.chartSelectedCurrencies.push(currencyCode);
        }
    },

    removeChartSelectedCurrency(currencyCode) {
        if (this.currentState.chartSettings.chartSelectedCurrencies) {
            const index = this.currentState.chartSettings.chartSelectedCurrencies.indexOf(currencyCode);
            if (index > -1) {
                this.currentState.chartSettings.chartSelectedCurrencies.splice(index, 1);
            }
        }
    },

    getCurrentExchangeRate(currencyCode) {
        return this.exchangeRates.find(rate => rate.currencyCode === currencyCode);
    },

    setAmount(amount) {
        this.currentState.currentAmount = amount;
    },

    setSelectedCurrency(currencyCode) {
        this.currentState.selectedCurrency = currencyCode;
    }
};
