package proiect.events;

public class ProductRequestEvent {
    private String retailerName;
    private int    productCode;
    private int    quantity;

    public ProductRequestEvent(String retailerName, int productCode, int quantity) {
        this.retailerName = retailerName;
        this.productCode = productCode;
        this.quantity = quantity;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
