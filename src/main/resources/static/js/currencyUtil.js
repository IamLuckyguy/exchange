// currencyUtils.js
function allowOnlyPositiveNumbers(event) {
    const input = event.target;
    let value = input.value;
    
    // 이전 값에서 소수점 개수 확인
    const prevDotCount = (value.match(/\./g) || []).length;
    
    // 소수점이 2개 이상이면 마지막 입력 제거
    if (prevDotCount > 1) {
        value = value.slice(0, -1);
    }
    
    // 숫자와 소수점만 허용
    value = value.replace(/[^0-9.]/g, '');
    
    // 소수점으로 시작하는 경우 앞에 0 추가
    if (value.startsWith('.')) {
        value = '0' + value;
    }
    
    if (value) {
        // 숫자에 천단위 콤마 추가 (소수점 이후는 제외)
        const parts = value.split('.');
        parts[0] = parts[0] ? parseInt(parts[0], 10).toLocaleString() : '0';
        value = parts.length > 1 ? parts[0] + '.' + parts[1] : parts[0];
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
    if (exchangeRate.currencyCode === 'KRW') {
        return 1;
    }
    return exchangeRate.exchangeRateHistories[exchangeRate.exchangeRateHistories.length - 1].maxRateValue;
}

function convertCurrency(amount, from, to) {
    if (from.currencyCode === "KRW") {
        return amount * to.unitAmount / getRateValue(to);
    }
    return ((amount * getRateValue(from) / from.unitAmount) / getRateValue(to)) * to.unitAmount;
}