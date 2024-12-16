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

    MIN_LOADING_TIME: 1000,

    toggleLoading(show) {
        const overlay = document.querySelector('.loading-overlay');
        if (show) {
            overlay.style.display = 'flex';
            requestAnimationFrame(() => {
                overlay.classList.add('visible');
            });
        } else {
            overlay.classList.remove('visible');
            setTimeout(() => {
                if (!overlay.classList.contains('visible')) {
                    overlay.style.display = 'none';
                }
            }, 300);
        }
    },

    typeWriter(text, element, delay) {
        let index = 0;
        element.innerHTML = ''; // 초기화
        const cursor = $('.cursor');
        
        const type = () => {
            if (index < text.length) {
                element.innerHTML += text.charAt(index);
                index++;
                setTimeout(type, delay);
            }
        };
        type();
    },

    ensureMinLoadingTime(promise, minLoadingTime = 1000) {
        const startTime = Date.now();
        return Promise.all([
            promise,
            new Promise(resolve => {
                const elapsed = Date.now() - startTime;
                const remaining = Math.max(0, minLoadingTime - elapsed);
                setTimeout(resolve, remaining);
            })
        ]).then(([result]) => result);
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
};
