// currencySelector.js
function initializeCurrencySelector(exchangeRates, selectedChartCurrencies) {

    console.log("설마 너니??");
    const container = $('#currencySelector');
    container.empty();

    exchangeRates.forEach(rate => {
        if (rate.currencyCode !== 'USD') {
            const option = $(`
                <div class="currency-option" data-currency="${rate.currencyCode}">
                    <span class="currency-flag">${rate.countryFlag}</span>
                    <span class="currency-code">${rate.currencyCode}</span>
                </div>
            `);
            if (selectedChartCurrencies.has(rate.currencyCode)) {
                option.addClass('selected');
            }
            container.append(option);
        }
    });
}

function updateCurrencyDialog(exchangeRates, selectedCurrencies) {
    console.log("누구야")
    $('.dialog-body').empty();
    exchangeRates.forEach(exchangeRate => {
        $('.dialog-body').append(`
            <label class="currency-item">
                <input type="checkbox" value="${exchangeRate.currencyCode}" ${selectedCurrencies.includes(exchangeRate.currencyCode) ? 'checked' : ''} />
                <div class="currency-info">
                    <span>
                        <span class="currency-flag">${exchangeRate.countryFlag}</span>
                        <span class="currency-code">${exchangeRate.currencyCode}</span>
                    </span>
                    <span class="currency-name">${exchangeRate.countryName}</span>
                </div>
            </label>
        `);
    });
}