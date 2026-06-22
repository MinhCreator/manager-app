package minhcreator.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory", indexes = {
    @Index(name = "idx_inventory_user_id", columnList = "user_id")
})
public class InventoryEntity {

    @Id
    @Column(name = "product_id")
    private int productId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "category")
    private String category;

    @Column(name = "price")
    private double price;

    @Column(name = "selling_price")
    private double sellingPrice;

    @Column(name = "quantity")
    private int quantity;

    public InventoryEntity() {}

    public InventoryEntity(int productId, int userId, String category, double price, double sellingPrice, int quantity) {
        this.productId = productId;
        this.userId = userId;
        this.category = category;
        this.price = price;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(double sellingPrice) { this.sellingPrice = sellingPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
