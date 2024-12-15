// main.js
$(document).ready(function () {
    const settings = StorageUtil.loadSettings();
    let selectedCurrencies = settings.selectedCurrencies;
    let selectedChartCurrencies = settings.chartCurrencies;
    let exchangeRates = settings.exchangeRates;

    init();

    function toggleLoading(show) {
        LoadingUtil.toggleLoading(show);
    }

    function attachInputListeners() {
        document.querySelectorAll('.currency-input').forEach(input => {
            input.removeEventListener('input', allowOnlyPositiveNumbers);
            input.addEventListener('input', allowOnlyPositiveNumbers);
        });
    }

    function updateCalculatorTitle(currencyCode, amount) {
        const exchangeRate = exchangeRates.find(ex => ex.currencyCode === currencyCode);
        const formattedAmount = formatNumber(amount, exchangeRate.decimals);
        $('#calculatorTitle').text(`${currencyCode} ${formattedAmount} 기준 환율`);
        $('#baseFlag').text(exchangeRate.countryFlag);
    }

    function updateSelectedCount() {
        $('#selectedCount').text(selectedCurrencies.length);
    }

    function updateCurrencyInputs() {
        $('#currencyInputs').empty();
        let baseEx = exchangeRates.find(ex => ex.currencyCode === 'USD');

        console.log("baseEx", baseEx);
        console.log("baseEx", getRateValue(baseEx));

        selectedCurrencies.forEach(code => {
            const exchangeRate = exchangeRates.find(ex => ex.currencyCode === code);
            if (exchangeRate) {
                if (code === 'KRW') {
                    console.log(code, exchangeRate, (getRateValue(baseEx) * baseEx.unitAmount, baseEx.decimals));
                    $('#currencyInputs').append(`
                    <div class="input-group">
                        <input type="text"
                               id="${code}"
                               value="${formatNumber(getRateValue(baseEx) * baseEx.unitAmount, baseEx.decimals)}"
                               class="currency-input" />
                        <button class="currency-btn">
                            <span class="currency-flag">${exchangeRate.countryFlag}</span>
                            <span class="currency-code">${exchangeRate.currencyCode}</span>
                        </button>
                    </div>
                `);
                } else {
                    $('#currencyInputs').append(`
                    <div class="input-group">
                        <input type="text"
                               id="${code}"
                               value="${formatNumber(getRateValue(baseEx) / getRateValue(exchangeRate) * exchangeRate.unitAmount, exchangeRate.decimals)}"
                               class="currency-input" />
                        <button class="currency-btn">
                            <span class="currency-flag">${exchangeRate.countryFlag}</span>
                            <span class="currency-code">${exchangeRate.currencyCode}</span>
                        </button>
                    </div>
                `);
                }
            }
        });
        attachInputListeners();
        updateSelectedCount();
    }

    function init() {
        toggleLoading(true);
        LoadingUtil.ensureMinLoadingTime(
            $.ajax({url: "/api/exchange-rates", method: "GET"})
        ).then(function (data) {
            console.log("init");
            exchangeRates = data;
            updateCurrencyInputs();
            initializeCurrencySelector(exchangeRates, selectedChartCurrencies);
            updateChart(document.getElementById('exchangeRateChart'), exchangeRates, selectedChartCurrencies, getCurrentPeriod());
            const lastInput = StorageUtil.loadLastInput();

            if (lastInput) {
                updateCalculatorTitle(lastInput.currencyCode, lastInput.amount);
                const input = $(`#${lastInput.currencyCode}`);
                if (input.length) {
                    input.val(lastInput.amount).trigger('input');
                }
            }

            if (settings.customPeriod) {
                $('#customDays').val(settings.customPeriod);
                $('.period-btn').removeClass('selected');
                updateChart(document.getElementById('exchangeRateChart'), exchangeRates, selectedChartCurrencies, parseInt(settings.customPeriod));
            } else {
                $(`.period-btn[data-period="${settings.displayPeriod}"]`).addClass('selected');
                updateChart(document.getElementById('exchangeRateChart'), exchangeRates, selectedChartCurrencies, parseInt(settings.displayPeriod));
            }

        }).catch(function (jqXHR, textStatus, errorThrown) {
            console.error("Failed to fetch exchange rates:", textStatus, errorThrown);
            alert("환율 정보를 가져오는 데 실패했습니다. 잠시 후 다시 시도해주세요.");
        }).finally(function () {
            toggleLoading(false);
        });
    }

    // 이벤트 핸들러
    $('#selectCurrencies').on('click', function () {
        updateCurrencyDialog(exchangeRates, selectedCurrencies);
        $('#currencyDialog').css('display', 'flex');
    });

    $('#closeDialog').on('click', function () {
        $('#currencyDialog').hide();
    });

    $(document).on('click', function (event) {
        if ($(event.target).is('#currencyDialog')) {
            $('#currencyDialog').hide();
        }
    });

    $(document).on('click', '.currency-option', function () {
        const currencyCode = $(this).data('currency');
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            selectedChartCurrencies.delete(currencyCode);
        } else {
            $(this).addClass('selected');
            selectedChartCurrencies.add(currencyCode);
        }

        updateChart(document.getElementById('exchangeRateChart'), exchangeRates, selectedChartCurrencies, getCurrentPeriod());
        StorageUtil.saveSettings(
            selectedCurrencies,
            selectedChartCurrencies,
            getCurrentPeriod(),
            $('#customDays').val()
        );
    });

    $(document).on('change', '.dialog-body input[type="checkbox"]', function () {
        const currencyCode = $(this).val();
        if (this.checked) {
            if (!selectedCurrencies.includes(currencyCode)) {
                selectedCurrencies.push(currencyCode);
            }
        } else {
            selectedCurrencies = selectedCurrencies.filter(code => code !== currencyCode);
        }
        updateCurrencyInputs();
        StorageUtil.saveSettings(
            selectedCurrencies,
            selectedChartCurrencies,
            getCurrentPeriod(),
            $('#customDays').val()
        );
    });

    $(document).on('input', '.currency-input', function () {
        const fromCurrencyCode = $(this).attr('id');
        const inputValue = $(this).val().replace(/,/g, '');
        const amount = parseFloat(inputValue);

        if (!isNaN(amount) && amount >= 0) {
            // 타이틀 업데이트
            updateCalculatorTitle(fromCurrencyCode, amount);

            // 로컬 스토리지에 마지막 입력값 저장
            StorageUtil.saveLastInput(fromCurrencyCode, amount);

            selectedCurrencies.forEach(toCurrencyCode => {
                if (toCurrencyCode !== fromCurrencyCode) {
                    const toEx = exchangeRates.find(ex => ex.currencyCode === toCurrencyCode);
                    const fromEx = exchangeRates.find(ex => ex.currencyCode === fromCurrencyCode);
                    // const baseEx = exchangeRates.find(ex => ex.currencyCode === 'USD');

                    if (toEx && fromEx) {
                        const convertedAmount = convertCurrency(amount, fromEx, toEx);
                        console.log("convertedAmount", convertedAmount);
                        $(`#${toCurrencyCode.replace(/([!"#$%&'()*+,./:;<=>?@[\]^`{|}~])/g, '\\$1')}`).val(formatNumber(convertedAmount, toEx.decimals));
                    }
                }
            });
        }
    });

    $('#pair1Currency, #pair2Currency').on('change', function () {
        updateChart(document.getElementById('exchangeRateChart'), exchangeRates, selectedChartCurrencies, CHART_PERIODS.WEEK);
    });

    $('#pair1Checkbox, #pair2Checkbox').on('change', function () {
        updateChart(document.getElementById('exchangeRateChart'), exchangeRates, selectedChartCurrencies, CHART_PERIODS.WEEK);
    });

    $(document).on('input', '#customDays', function () {
        const days = parseInt($(this).val());
        if (days >= 2 && days <= 365) {
            $('.period-btn').removeClass('selected');
            updateChart(document.getElementById('exchangeRateChart'), exchangeRates, selectedChartCurrencies, days);
            StorageUtil.saveSettings(
                selectedCurrencies,
                selectedChartCurrencies,
                'custom',
                days.toString()
            );
        }
    });

    $('.period-btn').on('click', function () {
        const period = $(this).data('period');
        $('.period-btn').removeClass('selected');
        $(this).addClass('selected');
        $('#customDays').val('');
        updateChart(document.getElementById('exchangeRateChart'), exchangeRates, selectedChartCurrencies, period);
        StorageUtil.saveSettings(
            selectedCurrencies,
            selectedChartCurrencies,
            period.toString()
        );
    });

    $('#selectAllCurrencies').on('change', function() {
        const isChecked = $(this).prop('checked');
        $('.dialog-body input[type="checkbox"]').prop('checked', isChecked);
        
        if (isChecked) {
            $('.dialog-body input[type="checkbox"]').each(function() {
                const currencyCode = $(this).val();
                if (!selectedCurrencies.includes(currencyCode)) {
                    selectedCurrencies.push(currencyCode);
                }
            });
        } else {
            selectedCurrencies = [];
        }
        
        updateCurrencyInputs();
        StorageUtil.saveSettings(
            selectedCurrencies,
            selectedChartCurrencies,
            getCurrentPeriod(),
            $('#customDays').val()
        );
    });

    $(document).on('change', '.dialog-body input[type="checkbox"]', function() {
        const totalCheckboxes = $('.dialog-body input[type="checkbox"]').length;
        const checkedCheckboxes = $('.dialog-body input[type="checkbox"]:checked').length;
        
        $('#selectAllCurrencies').prop('checked', totalCheckboxes === checkedCheckboxes);
    });
});