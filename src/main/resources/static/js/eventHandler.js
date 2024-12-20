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
        this.setupCurrencyDragAndDrop();
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
                    Model.addCalculatorCurrency(currencyCode);  // 계산기용으로만 추가
                });
            } else {
                Model.currentState.calculator.selectedCurrencies = new Set();
            }
    
            Dom.updateSelectedCurrencies();
            StorageUtil.updateCalculatorSettings({
                selectedCurrencies: Model.getCalculatorCurrencies()
            });
        });

        // 개별 체크박스 변경시 전체 선택 상태 업데이트
        $(document).on('change', '.dialog-body input[type="checkbox"]', function() {
            const currencyCode = $(this).val();
            if (this.checked) {
                Model.addCalculatorCurrency(currencyCode);
            } else {
                Model.removeCalculatorCurrency(currencyCode);
            }

            // 전체 선택 체크박스 상태 업데이트
            const totalCheckboxes = $('.dialog-body input[type="checkbox"]').length;
            const checkedCheckboxes = $('.dialog-body input[type="checkbox"]:checked').length;
            $('#selectAllCurrencies').prop('checked', totalCheckboxes === checkedCheckboxes);

            // UI와 저장소 업데이트
            Dom.updateSelectedCurrencies();
            StorageUtil.updateCalculatorSettings({
                selectedCurrencies: Model.getCalculatorCurrencies()
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
        $('.chart-type-btn').on('click', async function () {
            $('.chart-type-btn').removeClass('active');
            $(this).addClass('active');

            const type = $(this).data('type');
            await Service.updateChartType(type);
            Dom.updateChartSettingsVisibility();
        });
    },

    setupPeriodButtons() {
        $('.period-btn').on('click', async function () {
            $('.period-btn').removeClass('selected');
            $(this).addClass('selected');

            const period = parseInt($(this).data('period'));
            await Service.updatePeriod(period);
        });
    },

    setupAmountInputs() {
        let previousValue = '';

        $(document).on('focus', '.currency-input', function() {
            previousValue = $(this).val();
            $(this).val('');
        });

        $(document).on('blur', '.currency-input', function() {
            if ($(this).val() === '') {
                $(this).val(previousValue);
            }
        });

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
                if (Model.getChartSelectedCurrencies().length > 1) {
                    Model.removeChartSelectedCurrency(currencyCode);
                    $(this).removeClass('selected');
                }
            } else {
                if (Model.getChartSelectedCurrencies().length < 8) {
                    Model.addChartSelectedCurrency(currencyCode);
                    $(this).addClass('selected');
                } else {
                    CommonLibrary.showAlert('최대 8개 까지 선택할 수 있습니다.');
                }
            }
    
            await Service.updateChart();
            StorageUtil.updateChartSettings({
                chartSelectedCurrencies: Model.getChartSelectedCurrencies()
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
            } else if (days < 1) {
                days = 1;
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

    setupCurrencyDragAndDrop() {
        const container = $('#currencyInputs');
        
        container.sortable({
            handle: '.drag-handle',
            axis: 'y',
            cursor: 'move',
            update: function(event, ui) {
                const newOrder = [];
                $('.input-group:visible').each(function() {
                    const currencyCode = $(this).find('input').attr('id');
                    newOrder.push(currencyCode);
                });
                
                // 모델 업데이트
                Model.currentState.calculator.selectedCurrencies = newOrder;
                
                // 로컬 스토리지 업데이트
                StorageUtil.updateCalculatorSettings({
                    selectedCurrencies: newOrder
                });
            }
        });
    }
};

$(document).ready(() => {

});