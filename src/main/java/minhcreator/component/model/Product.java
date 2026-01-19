package minhcreator.component.model;

public class Product {
    private int id;
    private String UPID; // Mã định danh sản phẩm (Unique Product ID)
    private String name;
    private String category;
    private double price;
    private double selling_price;
    private int quantity;

    // Constructors, Getters và Setters
    public Product(int id, String UPID, String name, String category, double price, double selling_price, int quantity) {
        this.id = id;
        this.UPID = UPID;
        this.name = name;
        this.category = category;
        this.price = price;
        this.selling_price = selling_price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUPID() {
        return UPID;
    }

    public void setUPID(String UPID) {
        this.UPID = UPID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(double selling_price) {
        this.selling_price = selling_price;
    }
}