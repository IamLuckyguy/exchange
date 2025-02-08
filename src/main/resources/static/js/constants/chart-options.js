// exchange/resources/static/js/constants/chart-options.js
export const CHART_OPTIONS = {
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
};

export const CHART_COLORS = ['#00ff88', '#ff8800', '#ff00ff', '#00ffff', '#ffff00'];

export const CHART_PERIODS = {
    DAY: 1,
    WEEK: 7,
    MONTH: 30,
    QUARTER: 90,
    YEAR: 365
};