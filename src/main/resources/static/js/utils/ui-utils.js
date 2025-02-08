// exchange/resources/static/js/utils/ui-utils.js
export class UIUtils {
    static MIN_LOADING_TIME = 800; // 최소 로딩 시간 (ms)

    static async ensureMinLoadingTime(promise, minLoadingTime = UIUtils.MIN_LOADING_TIME) {
        const startTime = Date.now();

        try {
            // 실제 작업과 최소 시간 보장을 위한 Promise.all
            const [result] = await Promise.all([
                promise,
                new Promise(resolve => {
                    const elapsed = Date.now() - startTime;
                    const remaining = Math.max(0, minLoadingTime - elapsed);
                    setTimeout(resolve, remaining);
                })
            ]);

            return result;
        } catch (error) {
            // 에러가 발생해도 최소 시간은 보장
            const elapsed = Date.now() - startTime;
            const remaining = Math.max(0, minLoadingTime - elapsed);

            await new Promise(resolve => setTimeout(resolve, remaining));
            throw error; // 원래 에러를 다시 throw
        }
    }

    static async showLoadingAsync(promise, minLoadingTime = UIUtils.MIN_LOADING_TIME) {
        const mainContent = document.querySelector('.main-content');
        this.toggleLoading(true, mainContent);

        try {
            return await this.ensureMinLoadingTime(promise, minLoadingTime);
        } finally {
            this.toggleLoading(false, mainContent);
        }
    }

    static showAlert(message, duration = 1500) {
        const alertId = Math.random().toString(36).substr(2, 9);

        const alertHtml = `
            <div id="alert-${alertId}" class="custom-alert">
                ${message}
            </div>
        `;

        document.body.insertAdjacentHTML('beforeend', alertHtml);
        const alertElement = document.getElementById(`alert-${alertId}`);

        // Trigger reflow for animation
        setTimeout(() => {
            alertElement.classList.add('show');
        }, 100);

        setTimeout(() => {
            alertElement.classList.remove('show');
            setTimeout(() => {
                alertElement.remove();
            }, 300);
        }, duration);
    }

    static toggleLoading(show, mainContent) {
        const overlay = document.querySelector('.loading-overlay');
        if (!overlay || !mainContent) return;

        if (show) {
            // 메인 컨텐츠에 loading 클래스 추가
            mainContent.classList.add('loading');

            // 오버레이 표시
            overlay.style.display = 'flex';
            overlay.offsetHeight; // force reflow
            overlay.classList.add('visible');
        } else {
            // 오버레이 숨기기
            overlay.classList.remove('visible');

            setTimeout(() => {
                overlay.style.display = 'none';
                // 메인 컨텐츠의 loading 클래스 제거
                mainContent.classList.remove('loading');
            }, 300);
        }
    }

    static typeWriter(text, element, delay = 100) {
        if (!element) return;

        element.innerHTML = '';
        let index = 0;

        const type = () => {
            if (index < text.length) {
                element.innerHTML += text.charAt(index);
                index++;
                setTimeout(type, delay);
            }
        };

        type();
    }

    /**
     * UIUtils.blink('.some-element', 3, 500);
     * @param selector
     * @param times
     * @param speed
     */
    static blink(selector, times = 3, speed = 500) {
        const element = document.querySelector(selector);
        if (!element) return;

        let count = times;

        const fadeInOut = () => {
            if (count <= 0) return;

            // Fade out
            element.style.transition = `opacity ${speed}ms`;
            element.style.opacity = '0';

            setTimeout(() => {
                // Fade in
                element.style.opacity = '1';
                count--;

                if (count > 0) {
                    setTimeout(fadeInOut, speed);
                }
            }, speed);
        };

        fadeInOut();
    }
}