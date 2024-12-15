// storageUtil.js
const STORAGE_KEYS = {
    SETTINGS: 'exchangeCalc.settings',  // namespace 추가로 충돌 방지
};

const DEFAULT_SETTINGS = {
    calculator: {
        lastInput: {
            currencyCode: 'USD',
            amount: 1
        },
        selectedCurrencies: ['USD', 'KRW', 'JPY']
    },
    chart: {
        type: 'trend',
        baseCurrency: 'USD',
        targetCurrency: 'KRW',
        selectedCurrencies: ['USD', 'KRW'],
        displayPeriod: 7
    }
};

const StorageUtil = {
    /**
     * 전체 설정 저장
     * @param {Object} newSettings - 저장할 설정 객체
     */
    saveSettings(newSettings) {
        try {
            const currentSettings = this.loadSettings();
            const mergedSettings = {
                ...currentSettings,
                ...newSettings
            };
            localStorage.setItem(STORAGE_KEYS.SETTINGS, JSON.stringify(mergedSettings));
            return mergedSettings;
        } catch (error) {
            console.error('Failed to save settings:', error);
            return DEFAULT_SETTINGS;
        }
    },

    /**
     * 전체 설정 로드
     * @returns {Object} 저장된 설정 객체
     */
    loadSettings() {
        try {
            const saved = localStorage.getItem(STORAGE_KEYS.SETTINGS);
            if (!saved) {
                return DEFAULT_SETTINGS;
            }

            const settings = JSON.parse(saved);

            // chart 설정의 selectedCurrencies를 배열로 확실하게 변환
            if (settings.chart && settings.chart.selectedCurrencies) {
                settings.chart.selectedCurrencies = Array.isArray(settings.chart.selectedCurrencies)
                    ? settings.chart.selectedCurrencies
                    : ['USD', 'KRW', 'JPY'];
            }

            return {
                ...DEFAULT_SETTINGS,
                ...settings
            };
        } catch (error) {
            console.error('Failed to load settings:', error);
            return DEFAULT_SETTINGS;
        }
    },

    /**
     * 특정 설정 섹션 업데이트
     * @param {string} section - 설정 섹션 ('calculator' 또는 'chart')
     * @param {Object} updates - 업데이트할 설정 객체
     */
    updateSection(section, updates) {
        try {
            const settings = this.loadSettings();
            settings[section] = {
                ...settings[section],
                ...updates
            };
            this.saveSettings(settings);
            return settings[section];
        } catch (error) {
            console.error(`Failed to update ${section} settings:`, error);
            return DEFAULT_SETTINGS[section];
        }
    },

    /**
     * 계산기 설정 업데이트
     * @param {Object} updates - 업데이트할 계산기 설정
     */
    updateCalculatorSettings(updates) {
        return this.updateSection('calculator', updates);
    },

    /**
     * 차트 설정 업데이트
     * @param {Object} updates - 업데이트할 차트 설정
     */
    updateChartSettings(updates) {
        try {
            const settings = this.loadSettings();

            // Set을 배열로 변환하여 저장
            if (updates.selectedCurrencies instanceof Set) {
                updates = {
                    ...updates,
                    selectedCurrencies: Array.from(updates.selectedCurrencies)
                };
            }

            settings.chart = {
                ...settings.chart,
                ...updates
            };

            this.saveSettings(settings);
            return settings.chart;
        } catch (error) {
            console.error('Failed to update chart settings:', error);
            return DEFAULT_SETTINGS.chart;
        }
    },

    /**
     * 모든 설정 초기화
     */
    resetToDefaults() {
        try {
            localStorage.setItem(STORAGE_KEYS.SETTINGS, JSON.stringify(DEFAULT_SETTINGS));
            return DEFAULT_SETTINGS;
        } catch (error) {
            console.error('Failed to reset settings:', error);
            return DEFAULT_SETTINGS;
        }
    },

    /**
     * 마지막 입력값 저장
     * @param {string} currencyCode - 통화 코드
     * @param {number} amount - 금액
     */
    saveLastInput(currencyCode, amount) {
        this.updateCalculatorSettings({
            lastInput: { currencyCode, amount }
        });
    }
};