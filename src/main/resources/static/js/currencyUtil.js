// currencyUtils.js
function allowOnlyPositiveNumbers(event) {
    const input = event.target;
    let value = input.value;
    value = value.replace(/[^0-9]/g, '');
    if (value) {
        value = parseInt(value, 10).toLocaleString();
    }
    input.value = value;
}

function formatNumber(number, decimals) {
    return number.toLocaleString('en-US', {
        minimumFractionDigits: decimals,
        maximumFractionDigits: decimals
    });
}

function getRateValue(exchangeRate) {
    return exchangeRate.exchangeRateHistories[exchangeRate.exchangeRateHistories.length - 1].rateValue;
}

function convertCurrency(amount, from, to) {
    if (from.currencyCode === "KRW") {
        return amount * to.unitAmount / getRateValue(to);
    }
    return ((amount * getRateValue(from) / from.unitAmount) / getRateValue(to)) * to.unitAmount;
}