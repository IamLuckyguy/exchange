// resources/static/js/features/calculator/Calculator.js
import Sortable from 'https://cdn.jsdelivr.net/npm/sortablejs@1.15.0/modular/sortable.esm.js';
import { STORAGE_KEYS } from '../../constants/storage-keys.js';
import { CURRENCY_DEFAULTS } from '../../constants/currency-defaults.js';
import { UIUtils } from "../../utils/ui-utils.js";

export class Calculator {
    constructor(containerSelector = '#calculatorContainer') {
        // DOM 엘리먼트 참조 저장
        this.container = document.querySelector(containerSelector);
        this.currencyInputs = this.container.querySelector('#currencyInputs');
        this.selectedCountElement = this.container.querySelector('#selectedCount');
        this.calculatorTitleElement = this.container.querySelector('#calculatorTitle');
        this.baseFlagElement = this.container.querySelector('#baseFlag');

        // 상태 초기화
        this.state = {
            selectedCurrencies: [...CURRENCY_DEFAULTS.DEFAULT_CURRENCIES],
            lastInput: {
                currencyCode: CURRENCY_DEFAULTS.DEFAULT_BASE_CURRENCY,
                amount: CURRENCY_DEFAULTS.DEFAULT_AMOUNT
            },
            exchangeRates: []
        };

        // 초기화
        this.initializeState();
        this.bindEvents();
    }

    initializeState() {
        try {
            // 로컬스토리지에서 저장된 설정 불러오기
            const savedCurrencies = localStorage.getItem(STORAGE_KEYS.CALCULATOR.SELECTED_CURRENCIES);
            const lastInput = localStorage.getItem(STORAGE_KEYS.CALCULATOR.LAST_INPUT);
            const calculatorSettings = localStorage.getItem(STORAGE_KEYS.CALCULATOR.SETTINGS);

            // 선택된 통화 목록 처리
            if (savedCurrencies) {
                this.state.selectedCurrencies = JSON.parse(savedCurrencies);
            } else {
                // 기본값 설정
                this.state.selectedCurrencies = [...CURRENCY_DEFAULTS.DEFAULT_CURRENCIES];
                this.saveToLocalStorage(STORAGE_KEYS.CALCULATOR.SELECTED_CURRENCIES, this.state.selectedCurrencies);
            }

            // 마지막 입력값 처리
            if (lastInput) {
                this.state.lastInput = JSON.parse(lastInput);
            } else {
                // 기본값 설정
                this.state.lastInput = {
                    currencyCode: CURRENCY_DEFAULTS.DEFAULT_BASE_CURRENCY,
                    amount: CURRENCY_DEFAULTS.DEFAULT_AMOUNT
                };
                this.saveToLocalStorage(STORAGE_KEYS.CALCULATOR.LAST_INPUT, this.state.lastInput);
            }

            // 기타 계산기 설정 처리
            if (calculatorSettings) {
                const settings = JSON.parse(calculatorSettings);
                // 필요한 설정들을 state에 적용
                this.state = { ...this.state, ...settings };
            }

        } catch (error) {
            console.error('Failed to initialize calculator state:', error);
            // 에러 발생 시 기본값으로 초기화
            this.resetToDefaults();
        }
    }

    saveToLocalStorage(key, value) {
        try {
            localStorage.setItem(key, JSON.stringify(value));
        } catch (error) {
            console.error(`Failed to save to localStorage [${key}]:`, error);
            this.showError('설정을 저장하는 중 오류가 발생했습니다.');
        }
    }

    bindEvents() {
        // 통화 선택 다이얼로그 이벤트
        const selectCurrenciesBtn = this.container.querySelector('#selectCurrencies');
        selectCurrenciesBtn?.addEventListener('click', () => this.showCurrencyDialog());

        // 통화 입력 이벤트
        this.currencyInputs?.addEventListener('input', (e) => {
            if (e.target.matches('.currency-input')) {
                this.handleCurrencyInput(e);
            }
        });

        // 입력 필드 포커스 이벤트 추가
        this.currencyInputs?.addEventListener('focus', (e) => {
            if (e.target.matches('.currency-input')) {
                e.target.select(); // 전체 텍스트 선택
            }
        }, true);

        // 드래그 앤 드롭 설정
        this.initializeDragAndDrop();
    }


    initializeDragAndDrop() {
        if (!this.currencyInputs) return;

        // Sortable.js 나 다른 라이브러리를 사용할 수 있습니다
        const sortable = new Sortable(this.currencyInputs, {
            handle: '.drag-handle',
            animation: 150,
            onEnd: (evt) => this.handleCurrencyReorder(evt)
        });
    }

    handleCurrencyReorder(evt) {
        const newOrder = Array.from(this.currencyInputs.children)
            .map(el => el.querySelector('.currency-input').id);

        this.state.selectedCurrencies = newOrder;
        this.saveToLocalStorage(STORAGE_KEYS.CALCULATOR.SELECTED_CURRENCIES, newOrder);
    }

    handleCurrencyInput(event) {
        const input = event.target;
        const currencyCode = input.id;
        const value = this.formatInputValue(input.value);

        // 쉼표를 먼저 제거한 후에 소수점 처리
        const cleanValue = value.replace(/,/g, '');
        const amount = cleanValue.endsWith('.') ?
            parseFloat(cleanValue.slice(0, -1)) || 0 :
            parseFloat(cleanValue) || 0;

        // 입력값 업데이트
        this.state.lastInput = { currencyCode, amount };
        this.saveToLocalStorage(STORAGE_KEYS.CALCULATOR.LAST_INPUT, this.state.lastInput);

        // UI 업데이트
        this.updateCalculatorTitle(currencyCode, amount);

        // 현재 입력 필드의 값과 표시 텍스트 업데이트
        const inputWrapper = input.closest('.currency-input-wrapper');
        const displayText = inputWrapper?.querySelector('.display-text');
        if (displayText) {
            const [integerPart, decimalPart] = value.split('.');
            displayText.querySelector('.integer-part').textContent = integerPart;
            displayText.querySelector('.decimal-part').textContent = decimalPart || '00';
        }
        input.value = value;

        // 다른 통화 입력필드 업데이트
        this.updateAllCurrencyInputs(currencyCode, amount);
    }

    formatInputValue(value) {
        // 마지막 문자가 '.'인 경우를 체크
        const endsWithDot = value.endsWith('.');

        // 숫자와 소수점만 허용
        let cleanValue = value.replace(/[^\d.]/g, '');

        // 소수점이 여러 개인 경우 첫 번째 것만 유지
        const parts = cleanValue.split('.');
        if (parts.length > 2) {
            cleanValue = `${parts[0]}.${parts[1]}`;
        }

        // 소수점 둘째자리까지만 허용
        if (parts[1] && parts[1].length > 2) {
            parts[1] = parts[1].slice(0, 2);
            cleanValue = `${parts[0]}.${parts[1]}`;
        }

        // 천단위 콤마 추가 (소수점 이하는 제외)
        const integerPart = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        const decimalPart = parts[1] ? `.${parts[1]}` : '';

        // 마지막 문자가 '.'였다면 소수점 유지
        return endsWithDot ? `${integerPart}.` : integerPart + decimalPart;
    }

    updateCalculatorTitle(currencyCode, amount) {
        const exchangeRate = this.getExchangeRate(currencyCode);
        if (!exchangeRate) return;

        const formattedAmount = this.formatNumber(amount, exchangeRate.decimals);
        if (this.calculatorTitleElement) {
            this.calculatorTitleElement.textContent = `${currencyCode} ${formattedAmount} 기준 환율`;
        }
        if (this.baseFlagElement) {
            this.baseFlagElement.textContent = exchangeRate.countryFlag;
        }
    }

    updateAllCurrencyInputs(baseCurrencyCode, baseAmount) {
        const baseRate = this.getExchangeRate(baseCurrencyCode);
        if (!baseRate) return;

        this.state.selectedCurrencies.forEach(currencyCode => {
            if (currencyCode === baseCurrencyCode) return;

            const targetRate = this.getExchangeRate(currencyCode);
            if (!targetRate) return;

            const convertedAmount = this.calculateExchangeAmount(
                baseAmount,
                baseRate,
                targetRate
            );

            this.updateCurrencyInput(currencyCode, convertedAmount);
        });
    }

    calculateExchangeAmount(amount, baseRate, targetRate) {
        if (!amount || !baseRate || !targetRate) return 0;

        try {
            let result;
            const baseRateValue = this.getRateValue(baseRate);
            const targetRateValue = this.getRateValue(targetRate);

            if (baseRate.currencyCode === "KRW") {
                result = (amount * targetRate.unit) / (targetRateValue * baseRate.unit);
            } else if (targetRate.currencyCode === "KRW") {
                result = (amount * baseRateValue * targetRate.unit) / baseRate.unit;
            } else {
                result = (amount * baseRateValue * targetRate.unit) / (targetRateValue * baseRate.unit);
            }

            return result;
        } catch (error) {
            console.error('Error calculating amount:', error);
            return 0;
        }
    }

    updateCurrencyInput(currencyCode, amount) {
        const inputGroup = this.currencyInputs?.querySelector(`#${currencyCode}`)?.closest('.input-group');
        if (!inputGroup) return;

        const rate = this.getExchangeRate(currencyCode);
        if (!rate) return;

        // 금액을 해당 통화의 소수점 자리수에 맞게 포맷팅
        const formattedValue = this.formatNumber(amount, rate.decimals);
        const [integerPart, decimalPart] = formattedValue.split('.');

        // 입력 필드와 표시 텍스트 모두 업데이트
        const inputWrapper = inputGroup.querySelector('.currency-input-wrapper');
        if (inputWrapper) {
            const input = inputWrapper.querySelector('.currency-input');
            const displayText = inputWrapper.querySelector('.display-text');

            input.value = formattedValue;

            displayText.querySelector('.integer-part').textContent = integerPart;
            displayText.querySelector('.decimal-part').textContent = decimalPart || '00';
        }
    }

    // 환율 데이터 업데이트
    updateRates(rates) {
        this.state.exchangeRates = rates;
        this.render(); // 새로운 환율로 UI 업데이트

        const { currencyCode, amount } = this.state.lastInput;
        this.updateCalculatorTitle(currencyCode, amount);
        this.updateAllCurrencyInputs(currencyCode, amount);
    }

    // UI 렌더링
    render() {
        this.renderCurrencyInputs();
        this.updateSelectedCount();
    }

    renderCurrencyInputs() {
        if (!this.currencyInputs) return;

        this.currencyInputs.innerHTML = this.state.selectedCurrencies
            .map(currencyCode => {
                const rate = this.getExchangeRate(currencyCode);
                if (!rate) return '';

                let value = 0;
                if (currencyCode === this.state.lastInput.currencyCode) {
                    value = this.state.lastInput.amount;
                } else if (this.state.lastInput.amount > 0) {
                    const baseRate = this.getExchangeRate(this.state.lastInput.currencyCode);
                    value = this.calculateExchangeAmount(
                        this.state.lastInput.amount,
                        baseRate,
                        rate
                    );
                }

                const formattedValue = this.formatNumber(value, rate.decimals);
                const [integerPart, decimalPart] = formattedValue.split('.');

                return `
                <div class="input-group">
                    <div class="drag-handle" title="순서 변경"></div>
                    <div class="currency-info">
                        <span class="currency-flag">${rate.countryFlag}</span>
                        <span class="currency-code">${rate.currencyCode}</span>
                    </div>
                    <div class="currency-input-wrapper">
                        <input type="text"
                            inputmode="decimal"
                            pattern="[0-9]*"
                            id="${rate.currencyCode}"
                            class="currency-input"
                            value="${formattedValue}" />
                        <div class="display-text">
                            <span class="integer-part">${integerPart}</span>
                            <span class="decimal-separator">.</span>
                            <span class="decimal-part">${decimalPart || '00'}</span>
                        </div>
                    </div>
                </div>
            `;
            })
            .join('');
    }

    updateSelectedCount() {
        if (this.selectedCountElement) {
            this.selectedCountElement.textContent = this.state.selectedCurrencies.length;
        }
    }

    // 유틸리티 메서드들
    getExchangeRate(currencyCode) {
        return this.state.exchangeRates.find(rate => rate.currencyCode === currencyCode);
    }

    getRateValue(exchangeRate) {
        if (!exchangeRate?.exchangeRateRealTime?.length) return 0;
        if (exchangeRate.currencyCode === 'KRW') return 1;

        const lastExchangeRate = exchangeRate.exchangeRateRealTime[exchangeRate.exchangeRateRealTime.length - 1];
        return parseFloat(lastExchangeRate.rv);
    }

    formatNumber(number, decimals = 2) {
        return number.toLocaleString('en-US', {
            minimumFractionDigits: decimals,
            maximumFractionDigits: decimals
        });
    }

    showError(message) {
        // 에러 메시지 표시 로직
        console.error(message);
        UIUtils.showAlert(message);
    }
}