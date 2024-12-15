// helper.js
const Helper = {
    formatNumber(number, decimals = 2, roundingType = 'round') {
        if (isNaN(number)) return '0.00';

        const multiplier = 1000; // 소수점 3자리까지 계산

        // 올림, 반올림, 내림 처리
        let rounded;
        switch(roundingType.toLowerCase()) {
            case 'ceil':
                rounded = Math.ceil(number * multiplier) / multiplier;
                break;
            case 'floor':
                rounded = Math.floor(number * multiplier) / multiplier;
                break;
            case 'round':
            default:
                rounded = Math.round(number * multiplier) / multiplier;
        }

        // JPY나 KRW처럼 decimals가 0인 경우는 정수로 표시
        // 그 외의 경우는 최소 2자리까지 표시
        const displayDecimals = decimals === 0 ? 0 : 2;

        return rounded.toLocaleString('en-US', {
            minimumFractionDigits: displayDecimals,
            maximumFractionDigits: displayDecimals
        });
    },

    prepareChartData(baseRate, targetRate, days) {
        if (!baseRate?.exchangeRateHistories || !targetRate?.exchangeRateHistories) {
            console.error('Invalid exchange rate data');
            return {
                labels: [],
                datasets: [{
                    label: `${baseRate?.currencyCode || ''}/${targetRate?.currencyCode || ''}`,
                    data: [],
                    borderColor: Objects.ChartColors[0],
                    backgroundColor: `${Objects.ChartColors[0]}22`,
                    borderWidth: 2,
                    tension: 0.4
                }]
            };
        }

        const baseHistories = this.prepareHistoricalData(baseRate, days);
        const targetHistories = this.prepareHistoricalData(targetRate, days);

        const minLength = Math.min(baseHistories.length, targetHistories.length);
        const validBaseHistories = baseHistories.slice(0, minLength);
        const validTargetHistories = targetHistories.slice(0, minLength);

        return {
            labels: validBaseHistories.map(history => this.formatDate(history.fetchedAt)),
            datasets: [{
                label: `${baseRate.currencyCode}/${targetRate.currencyCode}`,
                data: validBaseHistories.map((history, index) => {
                    const baseValue = parseFloat(history.maxRateValue || history.rateValue);
                    const targetValue = parseFloat(validTargetHistories[index].maxRateValue || validTargetHistories[index].rateValue);
                    return baseValue / targetValue;
                }),
                borderColor: Objects.ChartColors[0],
                backgroundColor: `${Objects.ChartColors[0]}22`,
                borderWidth: 2,
                tension: 0.4
            }]
        };
    },

    prepareDataset(exchangeRate, currencyCode, index, days) {
        const histories = this.prepareHistoricalData(exchangeRate, days);
        if (!histories.length) return null;

        const baseValue = parseFloat(histories[0].maxRateValue || histories[0].rateValue);
        
        return {
            label: `${currencyCode} 변화율 (%)`,
            data: histories.map(history => {
                const currentValue = parseFloat(history.maxRateValue || history.rateValue);
                return ((currentValue - baseValue) / baseValue * 100).toFixed(2);
            }),
            borderColor: Objects.ChartColors[index % Objects.ChartColors.length],
            backgroundColor: `${Objects.ChartColors[index % Objects.ChartColors.length]}22`,
            borderWidth: 2,
            tension: 0.4,
            fill: false
        };
    },

    prepareHistoricalData(exchangeRate, days) {
        if (!exchangeRate?.exchangeRateHistories) {
            return [];
        }

        // 최근 days일 만큼의 데이터만 사용
        return exchangeRate.exchangeRateHistories
            .slice(0, days)
            .sort((a, b) => new Date(a.fetchedAt) - new Date(b.fetchedAt));
    },

    calculateExchangeAmount(amount, baseRate, targetRate) {
        if (!amount || !baseRate || !targetRate) return 0;

        try {
            let result;
            const baseRateValue = this.getRateValue(baseRate);
            const targetRateValue = this.getRateValue(targetRate);

            if (baseRate.currencyCode === "KRW") {
                // 원화 -> 다른 통화
                result = (amount * targetRate.unitAmount) / (targetRateValue * baseRate.unitAmount);
            } else if (targetRate.currencyCode === "KRW") {
                // 다른 통화 -> 원화
                result = (amount * baseRateValue * targetRate.unitAmount) / baseRate.unitAmount;
            } else {
                // 그 외 통화 간 변환
                result = (amount * baseRateValue * targetRate.unitAmount) / (targetRateValue * baseRate.unitAmount);
            }

            // 소수점 3자리에서 반올림하지 않고 있는 그대로 반환
            return result;
        } catch (error) {
            console.error('Error calculating amount:', error);
            return 0;
        }
    },

    getRateValue(exchangeRate) {
        if (!exchangeRate || !exchangeRate.exchangeRateHistories || exchangeRate.exchangeRateHistories.length === 0) {
            return 0;
        }

        if (exchangeRate.currencyCode === 'KRW') {
            return 1;
        }

        const lastHistory = exchangeRate.exchangeRateHistories[exchangeRate.exchangeRateHistories.length - 1];
        return parseFloat(lastHistory.maxRateValue || lastHistory.rateValue);
    },

    validatePeriod(period) {
        if (isNaN(period) || period < 1) return 7;
        if (period > 365) return 365;
        return period;
    },

    formatDate(date) {
        const d = new Date(date);
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        return `${month}.${day}`;
    }
}; 