// api.js
const Api = {
    async fetchExchangeRates() {
        try {
            return await CommonLibrary.ajaxRequest(
                "/api/exchange-rates",
                "GET"
            );
        } catch (error) {
            console.error('Failed to fetch exchange rates:', error);
            throw error;
        }
    }
}; 