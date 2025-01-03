import {CHART_COLORS, CHART_OPTIONS, CHART_PERIODS} from "../../constants/chart-options.js";
import {STORAGE_KEYS} from "../../constants/storage-keys.js";
import {UIUtils} from "../../utils/ui-utils.js";

export class ExchangeRateChart {
    constructor(containerSelector = '#chartContainer') {
        this.container = document.querySelector(containerSelector);
        this.chartInstances = new Map();
        this.resizeTimeouts = new Map();

        // 상태 초기화
        this.state = {
            period: CHART_PERIODS.WEEK,
            selectedCurrencies: [],
            exchangeRates: []
        };

        this.loadSettings();
        this.bindEvents();

        // 전역 ResizeObserver 초기화
        this.initializeResizeObserver();
    }

    initializeResizeObserver() {
        // ResizeObserver가 이미 존재하면 정리
        if (this.resizeObserver) {
            this.resizeObserver.disconnect();
        }

        // 새로운 ResizeObserver 설정
        this.resizeObserver = new ResizeObserver(entries => {
            entries.forEach(entry => {
                const chartId = entry.target.querySelector('canvas')?.id;
                if (chartId) {
                    // 기존 타임아웃 취소
                    if (this.resizeTimeouts.has(chartId)) {
                        clearTimeout(this.resizeTimeouts.get(chartId));
                    }

                    // 새로운 타임아웃 설정
                    this.resizeTimeouts.set(chartId, setTimeout(() => {
                        const chart = this.chartInstances.get(chartId.replace('chart-', ''));
                        if (chart) {
                            requestAnimationFrame(() => {
                                chart.resize();
                                chart.update('none');
                            });
                        }
                        this.resizeTimeouts.delete(chartId);
                    }, 100));
                }
            });
        });
    }

    handleResize() {
        requestAnimationFrame(() => {
            this.chartInstances.forEach(chart => {
                if (chart.canvas) {
                    chart.resize();
                }
            });
        });
    }

    // 컴포넌트 정리
    destroy() {
        this.cleanupCharts();
        if (this.resizeObserver) {
            this.resizeObserver.disconnect();
            this.resizeObserver = null;
        }
    }

    loadSettings() {
        try {
            const savedSettings = localStorage.getItem(STORAGE_KEYS.CHART.SETTINGS);
            if (savedSettings) {
                const settings = JSON.parse(savedSettings);
                this.state.period = settings.period || CHART_PERIODS.WEEK;
            }

            // 저장된 선택 통화 목록 불러오기
            const savedCurrencies = localStorage.getItem(STORAGE_KEYS.CHART.SELECTED_CURRENCIES);
            if (savedCurrencies) {
                this.state.selectedCurrencies = JSON.parse(savedCurrencies);
            }

            // 기간 선택 버튼 상태 복원
            const periodButtons = this.container.querySelectorAll('.period-btn');
            periodButtons.forEach(btn => {
                if (parseInt(btn.dataset.period) === this.state.period) {
                    btn.classList.add('selected');
                }
            });

            // 커스텀 기간 입력 상태 복원
            const customDaysInput = this.container.querySelector('#customDays');
            if (customDaysInput && !periodButtons.length) {
                customDaysInput.value = this.state.period;
            }
        } catch (error) {
            console.error('Failed to load chart settings:', error);
            this.initializeDefaultSettings();
        }
    }

    initializeDefaultSettings() {
        // 초기 상태 설정
        this.state.period = CHART_PERIODS.WEEK;
        // KRW를 제외한 모든 통화 선택
        this.state.selectedCurrencies = this.state.exchangeRates
            .filter(rate => rate.currencyCode !== 'KRW')
            .map(rate => rate.currencyCode);

        // 설정 저장
        this.saveSettings();
    }

    saveSettings() {
        try {
            // 차트 설정 저장
            const settings = {
                period: this.state.period
            };
            localStorage.setItem(STORAGE_KEYS.CHART.SETTINGS, JSON.stringify(settings));

            // 선택된 통화 목록 저장
            localStorage.setItem(
                STORAGE_KEYS.CHART.SELECTED_CURRENCIES,
                JSON.stringify(this.state.selectedCurrencies)
            );
        } catch (error) {
            console.error('Failed to save chart settings:', error);
            UIUtils.showAlert('차트 설정 저장에 실패했습니다.');
        }
    }

    bindEvents() {
        // 기간 선택
        this.container.querySelectorAll('.period-btn').forEach(btn => {
            btn.addEventListener('click', (e) => this.handlePeriodChange(e));
        });

        // 커스텀 기간 입력
        const customDaysInput = this.container.querySelector('#customDays');
        if (customDaysInput) {
            customDaysInput.addEventListener('input', (e) => this.handleCustomPeriodInput(e));
        }

        // 통화 선택
        const currencySelector = this.container.querySelector('#currencySelector');
        if (currencySelector) {
            currencySelector.addEventListener('click', (e) => {
                if (e.target.closest('.currency-option')) {
                    this.handleCurrencySelection(e);
                }
            });
        }
    }

    handlePeriodChange(event) {
        const button = event.target;
        const period = parseInt(button.dataset.period);

        // UI 업데이트
        this.container.querySelectorAll('.period-btn')
            .forEach(btn => btn.classList.remove('selected'));
        button.classList.add('selected');

        // 상태 업데이트
        this.state.period = period;
        this.saveSettings();

        // 차트 업데이트
        this.renderCharts();
    }

    handleCustomPeriodInput(event) {
        const input = event.target;
        let days = parseInt(input.value);

        if (isNaN(days) || days < 1) {
            days = CHART_PERIODS.WEEK;
        } else if (days > CHART_PERIODS.YEAR) {
            days = CHART_PERIODS.YEAR;
        }

        // 기간 버튼 선택 해제
        this.container.querySelectorAll('.period-btn')
            .forEach(btn => btn.classList.remove('selected'));

        // 상태 업데이트
        this.state.period = days;
        input.value = days;
        this.saveSettings();

        // 차트 업데이트
        this.renderCharts();
    }

    async handleCurrencySelection(event) {
        const option = event.target.closest('.currency-option');
        const currencyCode = option.dataset.currency;

        if (option.classList.contains('selected')) {
            this.state.selectedCurrencies = this.state.selectedCurrencies
                .filter(code => code !== currencyCode);
            option.classList.remove('selected');
        } else {
            if (this.state.selectedCurrencies.length < 8) {
                this.state.selectedCurrencies.push(currencyCode);
                option.classList.add('selected');
            } else {
                UIUtils.showAlert('최대 8개까지 선택할 수 있습니다.');
                return;
            }
        }

        // 설정 저장 및 차트 업데이트
        this.saveSettings();
        await this.renderCharts();
    }

    // 차트 데이터 준비
    prepareChartData(currencyCode, colorIndex) {
        const rate = this.getExchangeRate(currencyCode);
        if (!rate) return null;

        const histories = this.prepareHistoricalData(rate);
        if (!histories || histories.length === 0) return null;

        const color = CHART_COLORS[colorIndex % CHART_COLORS.length];
        const isRealTime = this.state.period === CHART_PERIODS.DAY;

        // JPY 표시 수정
        const label = currencyCode === 'JPY'
            ? `${rate.countryFlag} JPY(100)/KRW`
            : `${rate.countryFlag} ${currencyCode}/KRW`;

        // 포인트 크기 조정
        // 마지막 포인트는 실시간 모드일 때 6, 아닐 때 3
        // 나머지 포인트는 1로 설정
        const pointRadiuses = histories.map((_, index) => {
            if (index === histories.length - 1) {
                return isRealTime ? 6 : 3;
            }
            return 1; // 마지막 포인트가 아닌 경우 크기를 1로 설정
        });

        return {
            labels: histories.map(history => this.formatDate(history.at)),
            datasets: [{
                label: label,
                data: histories.map(history => parseFloat(history.rv)),
                borderColor: color,
                backgroundColor: `${color}22`,
                borderWidth: 2,
                tension: 0.4,
                pointStyle: 'circle',
                pointRadius: pointRadiuses,
                pointHoverRadius: 8,
                pointBackgroundColor: color,
                pointBorderColor: color,
                lastPointColor: color
            }]
        };
    }

    prepareHistoricalData(exchangeRate) {
        if (this.state.period === CHART_PERIODS.DAY) {
            // 실시간 데이터 처리 - 24시간 데이터
            return exchangeRate.exchangeRateRealTime
                ?.sort((a, b) => new Date(a.at) - new Date(b.at)) || [];
        }

        // 일별 데이터 처리
        return exchangeRate.exchangeRateHistories
            ?.slice(0, this.state.period)
            .sort((a, b) => new Date(a.at) - new Date(b.at)) || [];
    }

    bindChartEvents(currencyCode, chartInstance) {
        // 마우스 이벤트 핸들러
        const mouseMoveHandler = (e) => {
            console.log('mouseMoveHandler');
            const points = chartInstance.getElementsAtEventForMode(
                e,
                'nearest',
                { intersect: false, axis: 'x' },
                true
            );
            this.syncTooltips(chartInstance, points);
        };

        const mouseLeaveHandler = () => {
            console.log('mouseLeaveHandler');
            this.chartInstances.forEach(chart => {
                chart.tooltip.setActiveElements([], { x: 0, y: 0 });
                chart.update('none');
            });
        };

        let lastKnownDataIndex = null;  // 마지막으로 알려진 데이터 인덱스 저장

        const touchStartHandler = (e) => {
            const touch = e.touches[0];
            const rect = chartInstance.canvas.getBoundingClientRect();
            const x = touch.clientX - rect.left;
            const y = touch.clientY - rect.top;

            const points = chartInstance.getElementsAtEventForMode(
                { x, y },
                'nearest',
                { intersect: false, axis: 'x' },
                true
            );

            if (points.length > 0) {
                lastKnownDataIndex = points[0].index;  // 인덱스 저장
                this.syncTooltips(chartInstance, points);
            }
        };

        const touchMoveHandler = (e) => {
            const touch = e.touches[0];
            const rect = chartInstance.canvas.getBoundingClientRect();

            // 차트 영역 내에서의 상대적 x 위치 계산 (0~1 사이 값)
            const chartArea = chartInstance.chartArea;
            const x = touch.clientX - rect.left;

            // x 좌표가 차트 영역 내에 있는지 확인
            if (x >= chartArea.left && x <= chartArea.right) {
                const relativeX = (x - chartArea.left) / (chartArea.right - chartArea.left);

                // 데이터 포인트의 총 개수
                const totalPoints = chartInstance.data.datasets[0].data.length;

                // 상대적 위치를 기반으로 가장 가까운 데이터 포인트 인덱스 계산
                const newIndex = Math.min(
                    Math.floor(relativeX * (totalPoints - 1)),
                    totalPoints - 1
                );

                lastKnownDataIndex = newIndex;

                // 가상의 points 배열 생성
                const simulatedPoints = [{
                    datasetIndex: 0,
                    index: newIndex
                }];

                this.syncTooltips(chartInstance, simulatedPoints);
            }
        };

        const touchEndHandler = () => {
            lastKnownDataIndex = null;  // 인덱스 초기화
            this.chartInstances.forEach(chart => {
                chart.tooltip.setActiveElements([], { x: 0, y: 0 });
                chart.update('none');
            });
        };

        // 이벤트 리스너 등록
        chartInstance.canvas.addEventListener('mousemove', mouseMoveHandler);
        chartInstance.canvas.addEventListener('mouseleave', mouseLeaveHandler);
        chartInstance.canvas.addEventListener('touchstart', touchStartHandler);
        chartInstance.canvas.addEventListener('touchmove', touchMoveHandler);
        chartInstance.canvas.addEventListener('touchend', touchEndHandler);
        chartInstance.canvas.addEventListener('touchcancel', touchEndHandler);

        // 이벤트 핸들러 참조 저장
        chartInstance._eventHandlers = {
            mousemove: mouseMoveHandler,
            mouseleave: mouseLeaveHandler,
            touchstart: touchStartHandler,
            touchmove: touchMoveHandler,
            touchend: touchEndHandler,
            touchcancel: touchEndHandler
        };
    }

    async renderCharts() {
        const chartsContainer = this.container.querySelector('.charts-container');
        if (!chartsContainer) return;

        this.cleanupCharts();
        chartsContainer.innerHTML = '';

        const chartGrid = document.createElement('div');
        chartGrid.className = 'chart-grid';
        if (this.state.selectedCurrencies.length === 1) {
            chartGrid.classList.add('single-chart');
        }
        chartsContainer.appendChild(chartGrid);

        this.state.selectedCurrencies.forEach((currencyCode, index) => {
            const chartWrapper = document.createElement('div');
            chartWrapper.className = 'chart-wrapper';

            const canvas = document.createElement('canvas');
            canvas.id = `chart-${currencyCode}`;
            chartWrapper.appendChild(canvas);
            chartGrid.appendChild(chartWrapper);

            const data = this.prepareChartData(currencyCode, index);
            if (!data) return;

            const ctx = canvas.getContext('2d');
            const chartInstance = new Chart(ctx, {
                type: 'line',
                data: data,
                options: this.getChartOptions(currencyCode)
            });

            if (this.state.period === CHART_PERIODS.DAY) {
                this.applyLastPointAnimation(chartInstance);
            }

            this.chartInstances.set(currencyCode, chartInstance);

            // 차트 이벤트 바인딩 추가
            this.bindChartEvents(currencyCode, chartInstance);

            // ResizeObserver가 존재하면 차트 래퍼 관찰 시작
            if (this.resizeObserver) {
                this.resizeObserver.observe(chartWrapper);
            }
        });
    }

    getChartOptions(currencyCode) {
        return {
            ...CHART_OPTIONS.change,
            maintainAspectRatio: false,
            responsive: true,
            layout: {
                padding: {
                    top: 10,
                    right: 10,
                    bottom: 10,
                    left: 10
                }
            },
            animation: {
                duration: 0
            },
            interaction: {
                mode: 'nearest',
                axis: 'x',
                intersect: false
            },
            plugins: {
                tooltip: {
                    enabled: true,
                    mode: 'index',
                    intersect: false,
                    position: 'nearest',
                },
                legend: {
                    position: 'bottom',
                    align: 'start',
                    labels: {
                        color: '#ffffff',
                        boxWidth: 15,
                        padding: 15
                    }
                },
                title: {
                    display: true,
                    text: this.generateChartTitle(currencyCode),
                    color: this.getChartTitleColor(currencyCode),
                    font: {
                        size: 16,
                        weight: 'bold'
                    },
                    padding: {
                        top: 10,
                        bottom: 20
                    }
                }
            },
            scales: {
                x: {
                    grid: { color: '#4a4a4a' },
                    ticks: { color: '#ffffff' }
                },
                y: {
                    grid: { color: '#4a4a4a' },
                    ticks: {
                        color: '#ffffff',
                        callback: value => Math.round(value).toLocaleString('ko-KR')
                    }
                }
            }
        };
    }

    // 차트 제목 생성
    generateChartTitle(currencyCode) {
        const rate = this.getExchangeRate(currencyCode);
        if (!rate?.exchangeRateRealTime?.length) {
            return `${rate?.countryFlag || ''} ${currencyCode === 'JPY' ? 'JPY(100)' : currencyCode} 원화 환율`;
        }

        const latestRate = rate.exchangeRateRealTime[rate.exchangeRateRealTime.length - 1];

        if (this.state.period === CHART_PERIODS.DAY) {
            // 실시간 모드
            const isUp = latestRate.t === '상승';
            const arrow = isUp ? '▲' : '▼';
            return `${rate.countryFlag} ${currencyCode === 'JPY' ? 'JPY(100)' : currencyCode} 원화 환율    ${arrow} ${Math.abs(latestRate.td).toFixed(2)} (${Math.abs(latestRate.tr).toFixed(2)}%)`;
        } else {
            // 기간별 모드
            const historicalRates = this.prepareHistoricalData(rate);
            if (!historicalRates?.length) return `${rate.countryFlag} ${currencyCode === 'JPY' ? 'JPY(100)' : currencyCode} 원화 환율`;

            const firstRate = parseFloat(historicalRates[0].rv);
            const lastRate = parseFloat(historicalRates[historicalRates.length - 1].rv);
            const change = lastRate - firstRate;
            const changePercent = (change / firstRate) * 100;

            const isUp = change >= 0;
            const arrow = isUp ? '▲' : '▼';

            return `${rate.countryFlag} ${currencyCode === 'JPY' ? 'JPY(100)' : currencyCode} 원화 환율    ${arrow} ${Math.abs(change).toFixed(2)} (${Math.abs(changePercent).toFixed(2)}%)`;
        }
    }

    getChartTitleColor(currencyCode) {
        const rate = this.getExchangeRate(currencyCode);
        if (!rate?.exchangeRateRealTime?.length) return '#ffffff';

        let isUp;
        if (this.state.period === CHART_PERIODS.DAY) {
            // 실시간 모드
            const latestRate = rate.exchangeRateRealTime[rate.exchangeRateRealTime.length - 1];
            isUp = latestRate.t === '상승';
        } else {
            // 기간별 모드
            const historicalRates = this.prepareHistoricalData(rate);
            if (!historicalRates?.length) return '#ffffff';

            const firstRate = parseFloat(historicalRates[0].rv);
            const lastRate = parseFloat(historicalRates[historicalRates.length - 1].rv);
            isUp = lastRate >= firstRate;
        }

        return isUp ? '#ff6b6b' : '#4dabf7';
    }

    cleanupCharts() {
        // ResizeObserver가 존재하면 연결 해제
        if (this.resizeObserver) {
            this.resizeObserver.disconnect();
        }

        // 타임아웃 정리
        if (this.resizeTimeouts) {
            this.resizeTimeouts.forEach(timeout => clearTimeout(timeout));
            this.resizeTimeouts.clear();
        }

        // 차트 인스턴스 정리
        if (this.chartInstances) {
            this.chartInstances.forEach(chart => {
                if (chart._eventHandlers) {
                    Object.entries(chart._eventHandlers).forEach(([event, handler]) => {
                        if (chart.canvas) {
                            chart.canvas.removeEventListener(event, handler);
                        }
                    });
                }
                chart.destroy();
            });
            this.chartInstances.clear();
        }
    }

    applyLastPointAnimation(chartInstance) {
        let alpha = 1;
        let decreasing = true;
        const minAlpha = 0.4;
        const maxAlpha = 1;
        const stepSize = 0.02;
        let frameCount = 0;
        const frameSkip = 2;

        const animate = () => {
            if (!chartInstance.data?.datasets?.[0]) return;

            const dataset = chartInstance.data.datasets[0];
            const lastIndex = dataset.data.length - 1;

            frameCount++;

            if (frameCount % frameSkip !== 0) {
                chartInstance._animationFrame = requestAnimationFrame(animate);
                return;
            }

            if (decreasing) {
                alpha -= stepSize;
                if (alpha <= minAlpha) {
                    alpha = minAlpha;
                    decreasing = false;
                }
            } else {
                alpha += stepSize;
                if (alpha >= maxAlpha) {
                    alpha = maxAlpha;
                    decreasing = true;
                }
            }

            if (lastIndex >= 0) {
                const baseColor = dataset.lastPointColor;
                const rgbaColor = this.getRGBAColor(baseColor, alpha);

                dataset.pointBackgroundColor = dataset.data.map((_, i) =>
                    i === lastIndex ? rgbaColor : dataset.lastPointColor
                );

                chartInstance.update('none');
            }

            if (this.state.period === CHART_PERIODS.DAY) {
                chartInstance._animationFrame = requestAnimationFrame(animate);
            }
        };

        chartInstance._animationFrame = requestAnimationFrame(animate);
    }

    getRGBAColor(color, alpha) {
        if (color.startsWith('rgb')) {
            return color.replace('rgb', 'rgba').replace(')', `, ${alpha})`);
        }
        return `rgba(0, 255, 136, ${alpha})`; // 기본 색상
    }

    syncTooltips(sourceChart, points) {
        if (!points.length) return;

        const dataIndex = points[0].index;
        const sourceChartArea = sourceChart.chartArea;

        // 모든 차트에 대해 동일한 데이터 포인트의 툴팁 표시
        this.chartInstances.forEach(targetChart => {
            if (!targetChart.data?.datasets?.[0]?.data[dataIndex]) return;

            const dataset = targetChart.data.datasets[0];
            const yValue = dataset.data[dataIndex];
            const yPixel = targetChart.scales.y.getPixelForValue(yValue);

            // 차트 영역의 중앙에 툴팁 표시
            const targetChartArea = targetChart.chartArea;
            const targetX = (targetChartArea.left + targetChartArea.right) / 2;

            targetChart.tooltip.setActiveElements([
                {
                    datasetIndex: 0,
                    index: dataIndex
                }
            ], {
                x: targetX,
                y: yPixel
            });

            targetChart.update('none');
        });
    }

    renderCurrencyOptions() {
        const selector = this.container.querySelector('#currencySelector');
        if (!selector) return;

        selector.innerHTML = this.state.exchangeRates
            .filter(rate => rate.currencyCode !== 'KRW')
            .map(rate => `
            <div class="currency-option ${this.state.selectedCurrencies.includes(rate.currencyCode) ? 'selected' : ''}" 
                 data-currency="${rate.currencyCode}">
                <span class="currency-flag">${rate.countryFlag}</span>
                <span class="currency-code">${rate.currencyCode === 'JPY' ? 'JPY(100)' : rate.currencyCode}</span>
            </div>
        `)
            .join('');
    }

    updateRates(rates) {
        this.state.exchangeRates = rates;

        // 최초 실행 시 선택된 통화가 없다면 모든 통화 선택
        if (this.state.selectedCurrencies.length === 0) {
            this.state.selectedCurrencies = rates
                .filter(rate => rate.currencyCode !== 'KRW')
                .map(rate => rate.currencyCode);
            this.saveSettings();
        }

        this.render();
    }

    render() {
        this.renderCurrencyOptions();
        this.renderCharts();
    }

    getExchangeRate(currencyCode) {
        const rate = this.state.exchangeRates.find(rate => rate.currencyCode === currencyCode);
        if (!rate) return null;

        // KRW인 경우 특별 처리
        if (currencyCode === 'KRW') {
            return {
                ...rate,
                currentRate: {
                    rv: 1,  // KRW의 경우 항상 1
                    t: '유지',
                    tr: 0,
                    td: 0
                },
                exchangeRateRealTime: [{
                    rv: 1,
                    t: '유지',
                    tr: 0,
                    td: 0,
                    at: new Date().toISOString()
                }],
                exchangeRateHistories: [{
                    rv: '1',
                    at: new Date().toISOString()
                }]
            };
        }

        // 실시간 데이터가 있는지 확인
        if (!rate.exchangeRateRealTime || rate.exchangeRateRealTime.length === 0) {
            return null;
        }

        // 마지막 실시간 데이터 반환
        return {
            ...rate,
            currentRate: rate.exchangeRateRealTime[rate.exchangeRateRealTime.length - 1]
        };
    }

    formatDate(date) {
        const d = new Date(date);

        if (this.state.period === CHART_PERIODS.DAY) {
            return `${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`;
        }

        const year = String(d.getFullYear()).slice(2);
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');

        return `${year}.${month}.${day}`;
    }
}