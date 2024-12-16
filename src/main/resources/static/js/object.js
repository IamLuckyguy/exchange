// objects.js
const Objects = {
    Settings: {
        lastInput: {
            currencyCode: 'USD',
            amount: 1
        },
        selectedCurrencies: ['USD', 'KRW', 'JPY'],
        chartType: 'trend',
        baseCurrency: 'USD',
        targetCurrency: 'KRW',
        displayPeriod: 7
    },

    ChartOptions: {
        trend: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'top',
                    labels: { color: '#ffffff' }
                },
                tooltip: {
                    mode: 'index',
                    intersect: false
                }
            },
            scales: {
                x: {
                    grid: { color: '#4a4a4a' },
                    ticks: { color: '#ffffff' }
                },
                y: {
                    grid: { color: '#4a4a4a' },
                    ticks: { color: '#ffffff' }
                }
            }
        },
        change: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'top',
                    labels: { color: '#ffffff' }
                },
                tooltip: {
                    mode: 'index',
                    intersect: false
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
                        callback: value => value.toFixed(2) + '%'
                    }
                }
            }
        }
    },

    ChartColors: ['#00ff88', '#ff8800', '#ff00ff', '#00ffff', '#ffff00'],

    Periods: {
        DAY: 1,
        WEEK: 7,
        MONTH: 30,
        QUARTER: 90,
        YEAR: 365
    }
};
