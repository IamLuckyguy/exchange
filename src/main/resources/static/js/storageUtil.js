// storageUtil.js
const STORAGE_KEYS = {
    SELECTED_CURRENCIES: 'selectedCurrencies',
    CHART_CURRENCIES: 'chartCurrencies',
    DISPLAY_PERIOD: 'displayPeriod',
    CUSTOM_PERIOD: 'customPeriod',
    EXCHANGE_RATES: 'exchangeRates'
};

const StorageUtil = {
    saveSettings(selectedCurr, chartCurr, displayPeriod, customPeriod,) {
        localStorage.setItem(STORAGE_KEYS.SELECTED_CURRENCIES, JSON.stringify(selectedCurr));
        localStorage.setItem(STORAGE_KEYS.CHART_CURRENCIES, JSON.stringify(Array.from(chartCurr)));
        localStorage.setItem(STORAGE_KEYS.DISPLAY_PERIOD, displayPeriod);
        localStorage.setItem(STORAGE_KEYS.EXCHANGE_RATES, JSON.stringify(Array.from([])));

        if (customPeriod) {
            localStorage.setItem(STORAGE_KEYS.CUSTOM_PERIOD, customPeriod);
        } else {
            localStorage.removeItem(STORAGE_KEYS.CUSTOM_PERIOD);
        }
    },

    loadSettings() {
        try {
            const savedSelected = localStorage.getItem(STORAGE_KEYS.SELECTED_CURRENCIES);
            const savedChart = localStorage.getItem(STORAGE_KEYS.CHART_CURRENCIES);
            const savedPeriod = localStorage.getItem(STORAGE_KEYS.DISPLAY_PERIOD);
            const savedCustomPeriod = localStorage.getItem(STORAGE_KEYS.CUSTOM_PERIOD);
            const exchangeRates = localStorage.getItem(STORAGE_KEYS.EXCHANGE_RATES);

            return {
                selectedCurrencies: savedSelected ? JSON.parse(savedSelected) : ["USD", "KRW", "JPY", "AUD"],
                chartCurrencies: savedChart ? new Set(JSON.parse(savedChart)) : new Set(),
                displayPeriod: savedPeriod || '7',
                customPeriod: savedCustomPeriod || '',
                exchangeRates: exchangeRates
            };
        } catch (error) {
            console.error('Error loading settings:', error);
            return {
                selectedCurrencies: ["USD", "KRW", "JPY", "AUD"],
                chartCurrencies: new Set(),
                displayPeriod: '7',
                customPeriod: '',
                exchangeRates: [],
            };
        }
    }
};