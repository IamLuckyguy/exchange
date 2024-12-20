// service.js
const Service = {
    async initialize() {
        try {
            // 로딩
            CommonLibrary.toggleLoading(true);
            await CommonLibrary.ensureMinLoadingTime(1000);

            // 환율 데이터 로드
            const rates = await Api.fetchExchangeRates();
            console.log('Exchange Rates:', rates); // 데이터 구조 확인
            Model.setExchangeRates(rates);

            // 저장된 설정 로드
            const settings = StorageUtil.loadSettings();
            
            // 차트 설정 업데이트
            Model.updateChartSettings(settings.chart);
            
            // 계산기 설정 업데이트 추가
            Model.currentState.calculator = {
                ...Model.currentState.calculator,
                ...settings.calculator
            };

            // UI 초기화
            Dom.initializeUI();

            // 마지막 입력값 복원 및 계산
            const {lastInput} = settings.calculator;
            if (lastInput && lastInput.amount) {
                this.updateCalculations(lastInput.amount, lastInput.currencyCode);
            } else {
                // 기본값 설정
                this.updateCalculations(1, 'USD');
            }

            // 차트 업데이트
            await this.updateChart();

            return rates;
        } catch (error) {
            console.error('Failed to initialize:', error);
            CommonLibrary.showAlert("환율 정보를 가져오는 데 실패했습니다.");
            throw error;
        } finally {
            CommonLibrary.toggleLoading(false);
            CommonLibrary.typeWriter("실시간 환율 계산기", document.getElementById('animatedTitle'), 100);
        }
    },

    async updateChart() {
        const {type, period, baseCurrency, targetCurrency} = Model.currentState.chartSettings;
        const chartSelectedCurrencies = Model.getChartSelectedCurrencies();  // 수정된 부분
        const ctx = document.getElementById('exchangeRateChart');

        try {
            if (type === 'trend') {
                this.updateTrendChart(ctx, baseCurrency, targetCurrency, period);
            } else {
                this.updateChangeRateChart(ctx, chartSelectedCurrencies, period);  // 수정된 부분
            }
        } catch (error) {
            console.error('Failed to update chart:', error);
            CommonLibrary.showAlert("차트 업데이트에 실패했습니다.");
        }
    },

    updateTrendChart(ctx, baseCurrency, targetCurrency, days) {
        const baseRate = Model.getCurrentExchangeRate(baseCurrency);
        const targetRate = Model.getCurrentExchangeRate(targetCurrency);

        if (!baseRate || !targetRate) {
            console.error('Could not find exchange rates for selected currencies');
            return;
        }

        const chartData = Helper.prepareChartData(baseRate, targetRate, days);
        Dom.renderChart(ctx, chartData, 'trend');
    },

    updateChangeRateChart(ctx, selectedCurrencies, days) {
        const datasets = Array.from(selectedCurrencies)
            .map((currencyCode, index) => {
                const rate = Model.getCurrentExchangeRate(currencyCode);
                return Helper.prepareDataset(rate, currencyCode, index, days);
            })
            .filter(dataset => dataset !== null);

        const labels = Helper.prepareHistoricalData(
            Model.getCurrentExchangeRate(Array.from(selectedCurrencies)[0]),
            days
        ).map(history => Helper.formatDate(history.at));

        Dom.renderChart(ctx, {
            labels,
            datasets
        }, 'change');
    },

    updateCalculations(amount, currencyCode) {
        Model.setAmount(amount);
        Model.setSelectedCurrency(currencyCode);

        const exchangeRate = Model.getCurrentExchangeRate(currencyCode);
        if (!exchangeRate) return;

        Dom.updateCalculatorTitle(currencyCode, amount);
        this.calculateAndUpdateRates(amount, exchangeRate);

        StorageUtil.saveLastInput(currencyCode, amount);

    },

    calculateAndUpdateRates(amount, baseExchangeRate) {
        if (!amount || !baseExchangeRate) return;

        // 모든 환율에 대해 계산 (선택 여부와 관계없이)
        Model.exchangeRates.forEach(targetRate => {
            if (targetRate.currencyCode === baseExchangeRate.currencyCode) {
                Dom.updateCurrencyInput(targetRate.currencyCode, amount, targetRate);
                return;
            }

            const convertedAmount = Helper.calculateExchangeAmount(
                amount,
                baseExchangeRate,
                targetRate
            );

            if (!isNaN(convertedAmount)) {
                Dom.updateCurrencyInput(targetRate.currencyCode, convertedAmount, targetRate);
            }
        });
    },

    async updateChartType(type) {
        Model.updateChartSettings({type});
        StorageUtil.updateChartSettings({type});
        await this.updateChart();
    },

    async updatePeriod(period) {
        Model.updateChartSettings({period});
        StorageUtil.updateChartSettings({displayPeriod: period});
        await this.updateChart();
    }
};
