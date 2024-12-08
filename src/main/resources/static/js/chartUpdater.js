// chartUpdater.js
const CHART_PERIODS = {
    WEEK: 7,
    MONTH: 30,
    HALF_YEAR: 180,
    YEAR: 365,
    ALL: 'all'
};

function calculateExchangeRateChange(data) {
    if (!data || data.rates.length === 0) return [];
    const baseRate = data.rates[0];
    return data.rates.map(rate => ((rate - baseRate) / baseRate) * 100);
}

function preparedRowData(exchangeRate, days) {
    const data = {dates: [], rates: []};
    if (!exchangeRate?.exchangeRateHistories) return data;

    for (let i = 0; i < days; i++) {
        let history = exchangeRate.exchangeRateHistories[i];

        if (!history) {
            continue;
        }

        const date = new Date(history.updatedAt);
        const rate = parseFloat(history.rateValue).toFixed(4);

        data.dates.push(date.toISOString().split('T')[0]);
        data.rates.push(rate);
    }

    console.log(data);
    return data;
}

function updateChart(ctx, exchangeRates, selectedChartCurrencies, days) {
    if (selectedChartCurrencies.size === 0) {
        if (window.exchangeRateChart instanceof Chart) {
            window.exchangeRateChart.destroy();
        }
        return;
    }

    const datasets = [];
    const colors = ['#00ff88', '#ff8800', '#ff00ff', '#00ffff', '#ffff00'];

    const selectedArray = Array.from(selectedChartCurrencies);
    selectedArray.forEach((currencyCode, index) => {
        const exchangeRate = exchangeRates.find(rate => rate.currencyCode === currencyCode);
        const rawData = preparedRowData(exchangeRate, days);
        const changeRates = calculateExchangeRateChange(rawData);

        datasets.push({
            label: `${currencyCode} 변화율 (%)`,
            data: changeRates,
            borderColor: colors[index % colors.length],
            backgroundColor: `${colors[index % colors.length]}22`,
            borderWidth: 2,
            tension: 0.4,
            fill: false
        });
    });

    if (window.exchangeRateChart instanceof Chart) {
        window.exchangeRateChart.destroy();
    }

    const firstCurrencyCode = selectedArray[0];
    const firstExchangeRate = exchangeRates.find(rate => rate.currencyCode === firstCurrencyCode);
    const firstData = preparedRowData(firstExchangeRate, days);

    window.exchangeRateChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: firstData.dates,
            datasets: datasets
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'top',
                    labels: {
                        color: '#ffffff'
                    }
                },
                tooltip: {
                    callbacks: {
                        label: function (context) {
                            return `${context.dataset.label}: ${context.parsed.y.toFixed(2)}%`;
                        }
                    }
                }
            },
            scales: {
                x: {
                    grid: {color: '#4a4a4a'},
                    ticks: {color: '#ffffff'}
                },
                y: {
                    grid: {color: '#4a4a4a'},
                    ticks: {
                        color: '#ffffff',
                        callback: function (value) {
                            return value.toFixed(2) + '%';
                        }
                    }
                }
            }
        }
    });
}

function getCurrentPeriod() {
    const selectedPeriodBtn = $('.period-btn.selected');
    if (selectedPeriodBtn.length > 0) {
        return selectedPeriodBtn.data('period');
    }

    const customDays = parseInt($('#customDays').val());
    if (customDays >= 2 && customDays <= 365) {
        return customDays;
    }

    return 7; // 기본값
}