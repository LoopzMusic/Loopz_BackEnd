package dev.trier.ecommerce.dto.AbacatePay;

import java.util.List;

public class AbacatePayChargeRequest {

    private String frequency;
    private List<String> methods;
    private List<Product> products;
    private String returnUrl;
    private String completionUrl;
    private Customer customer;
    private String externalId;
    private Object metadata;

    // Construtor padrão
    public AbacatePayChargeRequest() {}

    // Construtor completo
    public AbacatePayChargeRequest(String frequency, List<String> methods, List<Product> products, String returnUrl, String completionUrl, Customer customer, String externalId, Object metadata) {
        this.frequency = frequency;
        this.methods = methods;
        this.products = products;
        this.returnUrl = returnUrl;
        this.completionUrl = completionUrl;
        this.customer = customer;
        this.externalId = externalId;
        this.metadata = metadata;
    }

    // Getters e Setters
    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getCompletionUrl() {
        return completionUrl;
    }

    public void setCompletionUrl(String completionUrl) {
        this.completionUrl = completionUrl;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public static class Product {
        private String externalId;
        private String name;
        private String description;
        private Integer quantity;
        private Integer price; // Valor em centavos

        // Construtor padrão
        public Product() {}

        // Construtor completo
        public Product(String externalId, String name, String description, Integer quantity, Integer price) {
            this.externalId = externalId;
            this.name = name;
            this.description = description;
            this.quantity = quantity;
            this.price = price;
        }

        // Getters e Setters
        public String getExternalId() {
            return externalId;
        }

        public void setExternalId(String externalId) {
            this.externalId = externalId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }
    }

    public static class Customer {
        private String name;
        private String cellphone;
        private String email;
        private String taxId;

        // Construtor padrão
        public Customer() {}

        // Construtor completo
        public Customer(String name, String cellphone, String email, String taxId) {
            this.name = name;
            this.cellphone = cellphone;
            this.email = email;
            this.taxId = taxId;
        }

        // Getters e Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCellphone() {
            return cellphone;
        }

        public void setCellphone(String cellphone) {
            this.cellphone = cellphone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTaxId() {
            return taxId;
        }

        public void setTaxId(String taxId) {
            this.taxId = taxId;
        }
    }
}