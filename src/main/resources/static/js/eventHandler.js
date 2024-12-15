// eventHandler.js
const EventHandler = {
    init() {
        this.setupCurrencySelectors();
        this.setupChartTypeButtons();
        this.setupPeriodButtons();
        this.setupAmountInputs();
        this.setupCurrencyOptions();
        this.setupCustomPeriodInput();
        this.setupCurrencyDialog();
    },

    setupCurrencyDialog() {
        // 다이얼로그 열기
        $('#selectCurrencies').on('click', () => {
            Dom.updateCurrencyDialog();
            $('#currencyDialog').css('display', 'flex');
        });

        // X 버튼으로 닫기
        $('#closeDialog').on('click', () => {
            $('#currencyDialog').hide();
        });

        // 외부 영역 클릭시 닫기
        $(document).on('click', (event) => {
            if ($(event.target).is('#currencyDialog')) {
                $('#currencyDialog').hide();
            }
        });

        // 전체 선택 체크박스
        $('#selectAllCurrencies').on('change', async function () {
            const isChecked = $(this).prop('checked');
            $('.dialog-body input[type="checkbox"]').prop('checked', isChecked);

            if (isChecked) {
                $('.dialog-body input[type="checkbox"]').each(function () {
                    const currencyCode = $(this).val();
                    if (!Model.getSelectedCurrencies().includes(currencyCode)) {
                        Model.addSelectedCurrency(currencyCode);
                    }
                });
            } else {
                Model.currentState.chartSettings.selectedCurrencies = new Set();
            }

            Dom.updateSelectedCurrencies();
            await Service.updateChart();
            StorageUtil.updateChartSettings({
                selectedCurrencies: Array.from(Model.getSelectedCurrencies())
            });
        });

        // 개별 체크박스 변경시 전체 선택 상태 업데이트
        $(document).on('change', '.dialog-body input[type="checkbox"]', function() {
            const currencyCode = $(this).val();
            if (this.checked) {
                Model.addSelectedCurrency(currencyCode);
            } else {
                Model.removeSelectedCurrency(currencyCode);
            }

            // 전체 선택 체크박스 상태 업데이트
            const totalCheckboxes = $('.dialog-body input[type="checkbox"]').length;
            const checkedCheckboxes = $('.dialog-body input[type="checkbox"]:checked').length;
            $('#selectAllCurrencies').prop('checked', totalCheckboxes === checkedCheckboxes);

            // UI와 저장소 업데이트
            Dom.updateSelectedCurrencies();
            Service.updateChart();
            StorageUtil.updateChartSettings({
                selectedCurrencies: Array.from(Model.getSelectedCurrencies())
            });
        });
    },

    setupCurrencySelectors() {
        $('#baseCurrency, #targetCurrency').on('change', async function () {
            const baseCurrency = $('#baseCurrency').val();
            const targetCurrency = $('#targetCurrency').val();

            Model.updateChartSettings({
                baseCurrency,
                targetCurrency
            });

            await Service.updateChart();

            StorageUtil.updateChartSettings({
                baseCurrency,
                targetCurrency
            });
        });
    },

    setupChartTypeButtons() {
        $('.chart-type-btn').on('click', function() {
            $('.chart-type-btn').removeClass('active');
            $(this).addClass('active');
            
            const type = $(this).data('type');
            Service.updateChartType(type);
            Dom.updateChartSettingsVisibility();
        });
    },

    setupPeriodButtons() {
        $('.period-btn').on('click', function() {
            $('.period-btn').removeClass('selected');
            $(this).addClass('selected');
            
            const period = parseInt($(this).data('period'));
            Service.updatePeriod(period);
        });
    },

    setupAmountInputs() {
        $(document).on('keypress', '.currency-input', function(e) {
            if (!/[\d.]/.test(e.key)) {
                e.preventDefault();
                return;
            }

            const value = $(this).val();
            const dotIndex = value.indexOf('.');

            if (e.key === '.' && dotIndex !== -1) {
                e.preventDefault();
                return;
            }

            if (dotIndex !== -1 && e.key !== '.') {
                const cursorPos = this.selectionStart;
                if (cursorPos > dotIndex && value.length - dotIndex > 2) {
                    e.preventDefault();
                    return;
                }
            }
        });

        $(document).on('input', '.currency-input', function() {
            const currencyCode = $(this).attr('id');
            const input = this;
            const value = $(input).val();
            const cursorPos = input.selectionStart;

            let cleanValue = value.replace(/[^\d.]/g, '');

            const dotIndex = cleanValue.indexOf('.');
            if (dotIndex !== -1) {
                const beforeDot = cleanValue.substring(0, dotIndex);
                const afterDot = cleanValue.substring(dotIndex + 1).replace(/\./g, '');
                cleanValue = beforeDot + '.' + afterDot.substring(0, 2);
            }

            let formattedValue;
            if (dotIndex !== -1) {
                const integerPart = cleanValue.substring(0, dotIndex).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                const decimalPart = cleanValue.substring(dotIndex + 1, dotIndex + 3);
                formattedValue = integerPart + '.' + decimalPart;
            } else {
                formattedValue = cleanValue.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            }

            // 커서 위치 조정을 위한 콤마 개수 계산
            const beforeCursorValue = value.substring(0, cursorPos);
            const beforeCursorCommas = (beforeCursorValue.match(/,/g) || []).length;
            const newBeforeCursorValue = formattedValue.substring(0, cursorPos);
            const newBeforeCursorCommas = (newBeforeCursorValue.match(/,/g) || []).length;
            const cursorOffset = newBeforeCursorCommas - beforeCursorCommas;

            const amount = cleanValue ? parseFloat(cleanValue) : 0;
            const baseRate = Model.getCurrentExchangeRate(currencyCode);
            if (!baseRate) return;

            Dom.updateCalculatorTitle(currencyCode, amount);

            Model.exchangeRates.forEach(targetRate => {
                if (targetRate.currencyCode === currencyCode) {
                    $(input).val(formattedValue);
                    // 커서 위치 복원
                    input.setSelectionRange(cursorPos + cursorOffset, cursorPos + cursorOffset);
                    return;
                }

                const convertedAmount = Helper.calculateExchangeAmount(
                    amount,
                    baseRate,
                    targetRate
                );

                if (!isNaN(convertedAmount)) {
                    Dom.updateCurrencyInput(targetRate.currencyCode, convertedAmount, targetRate);
                }
            });

            StorageUtil.saveLastInput(currencyCode, amount);
        });
    },

    setupCurrencyOptions() {
        $('#currencySelector').on('click', '.currency-option', async function () {
            const currencyCode = $(this).data('currency');

            if ($(this).hasClass('selected')) {
                if (Model.getSelectedCurrencies().length > 1) {
                    Model.removeSelectedCurrency(currencyCode);
                    $(this).removeClass('selected');
                }
            } else {
                if (Model.getSelectedCurrencies().length < 8) {
                    Model.addSelectedCurrency(currencyCode);
                    $(this).addClass('selected');
                } else {
                    CommonLibrary.showAlert('최대 8개까지 선택할 수 있습니다.');
                }
            }

            Dom.updateSelectedCurrencies();
            await Service.updateChart();
            StorageUtil.updateChartSettings({
                selectedCurrencies: Array.from(Model.getSelectedCurrencies())
            });
        });
    },

    setupCustomPeriodInput() {
        $('#customDays').on('input', function() {
            const value = $(this).val();
            if (value === '') return;

            let days = parseInt(value);
            if (isNaN(days)) {
                days = 7;
            } else if (days < 2) {
                days = 2;
            } else if (days > 365) {
                days = 365;
            }

            $('.period-btn').removeClass('selected');
            Service.updatePeriod(days);
            $(this).val(days);
            StorageUtil.updateChartSettings({
                displayPeriod: days
            });
        });
    },

    allowOnlyNumeric(e) {
        const value = e.target.value;
        const sanitizedValue = value.replace(/[^0-9]/g, '');
        if (sanitizedValue !== value) {
            e.target.value = sanitizedValue;
            e.preventDefault();
        }
    }
};

$(document).ready(() => {

});