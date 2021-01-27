package hska.iwi.eShopMaster.model.database.models;

public class NewProduct {

    private String name;
    private double price;
    private int categoryID;
    private String detail;

    public NewProduct(String name, double price, int category, String detail) {
        this.name = name;
        this.price = price;
        this.categoryID = category;
        this.detail = detail;
    }

    public NewProduct() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCategoryID() {
        return this.categoryID;
    }

    public void setCategoryID(int category) {
        this.categoryID = category;
    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}