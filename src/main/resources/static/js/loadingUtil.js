// loadingUtil.js
const LoadingUtil = {
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

    ensureMinLoadingTime(promise) {
        const startTime = Date.now();
        return Promise.all([
            promise,
            new Promise(resolve => {
                const elapsed = Date.now() - startTime;
                const remaining = Math.max(0, this.MIN_LOADING_TIME - elapsed);
                setTimeout(resolve, remaining);
            })
        ]).then(([result]) => result);
    }
};