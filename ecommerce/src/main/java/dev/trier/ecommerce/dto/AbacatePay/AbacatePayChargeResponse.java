package dev.trier.ecommerce.dto.AbacatePay;

public class AbacatePayChargeResponse {

    private ResponseData data;
    private Object error; // Pode ser um objeto de erro ou null

    // Construtor padrão
    public AbacatePayChargeResponse() {}

    // Construtor completo
    public AbacatePayChargeResponse(ResponseData data, Object error) {
        this.data = data;
        this.error = error;
    }

    // Getters e Setters
    public ResponseData getData() {
        return data;
    }

    public void setData(ResponseData data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public static class ResponseData {
        private String id;
        private String url;
        private Integer amount;
        private String status;
        private Boolean devMode;
        // Outros campos relevantes da resposta (simplificado para o essencial)

        // Construtor padrão
        public ResponseData() {}

        // Construtor completo
        public ResponseData(String id, String url, Integer amount, String status, Boolean devMode) {
            this.id = id;
            this.url = url;
            this.amount = amount;
            this.status = status;
            this.devMode = devMode;
        }

        // Getters e Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Boolean getDevMode() {
            return devMode;
        }

        public void setDevMode(Boolean devMode) {
            this.devMode = devMode;
        }
    }
}