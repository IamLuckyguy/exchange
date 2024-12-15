// commonLibrary.js
const CommonLibrary = {
    async ajaxRequest(url, method, data = null) {
        return this.ensureMinLoadingTime(
            new Promise((resolve, reject) => {
                const options = {
                    url,
                    method,
                    contentType: 'application/json',
                    success: (response) => resolve(response),
                    error: (error) => reject(error)
                };

                if (data) {
                    options.data = method === 'GET' ? data : JSON.stringify(data);
                }

                $.ajax(options);
            })
        );
    },

    ensureMinLoadingTime(promise, minTime = 500) {
        const timeoutPromise = new Promise(resolve => 
            setTimeout(resolve, minTime)
        );
        
        Dom.showLoading();
        
        return Promise.all([promise, timeoutPromise])
            .then(([result]) => result)
            .finally(() => Dom.hideLoading());
    },

    createProxySection(section, renderFunction, propertiesToWatch = null) {
        return new Proxy(section, {
            set(target, property, value) {
                target[property] = value;
                if (!propertiesToWatch || propertiesToWatch.includes(property)) {
                    renderFunction();
                }
                return true;
            }
        });
    },

    formatDate(date) {
        return new Date(date.getTime() + 9 * 60 * 60 * 1000)
            .toISOString()
            .split('T')[0];
    },

    formattedDateFactory(dateDifference) {
        const today = new Date();
        return new Date(today.getTime() + (dateDifference * 24 * 60 * 60 * 1000))
            .toISOString()
            .split('T')[0];
    },

    numberWithCommas(value, defaultValue = '') {
        if (value === '' || value === null || value === undefined) {
            return defaultValue;
        }
        return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    },

    getErrorMessage(error) {
        if (!error.responseJSON?.data?.exceptionMessage) {
            return '오류가 발생했습니다. 관리자에게 문의하세요.';
        }
        
        const message = error.responseJSON.data.exceptionMessage;
        return message.length > 50 
            ? '오류가 발생했습니다. 관리자에게 문의하세요.'
            : message;
    },

    showAlert(message, duration = 1500) {
        const alertId = Math.random().toString(36).substr(2, 9);
        
        const alertHtml = `
            <div id="alert-${alertId}" class="custom-alert">
                ${message}
            </div>
        `;

        $('body').append(alertHtml);
        
        setTimeout(() => {
            $(`#alert-${alertId}`).addClass('show');
        }, 100);

        setTimeout(() => {
            $(`#alert-${alertId}`).removeClass('show');
            setTimeout(() => {
                $(`#alert-${alertId}`).remove();
            }, 300);
        }, duration);
    },

    blink(selector, times = 3, speed = 500) {
        if (times > 0) {
            $(selector).fadeOut(speed).fadeIn(speed, () => {
                this.blink(selector, times - 1, speed);
            });
        }
    },

    debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    },

    validateNumber(value) {
        return !isNaN(parseFloat(value)) && isFinite(value);
    },
};
