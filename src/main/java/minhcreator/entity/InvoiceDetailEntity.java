package minhcreator.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "invoice_details", indexes = {
    @Index(name = "idx_invoice_details_invoice_id", columnList = "invoice_id"),
    @Index(name = "idx_invoice_details_user_id", columnList = "user_id")
})
public class InvoiceDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private int detailId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "invoice_id")
    private int invoiceId;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unit_price")
    private double unitPrice;

    public InvoiceDetailEntity() {}

    public InvoiceDetailEntity(int userId, int invoiceId, int productId, int quantity, double unitPrice) {
        this.userId = userId;
        this.invoiceId = invoiceId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getDetailId() { return detailId; }
    public void setDetailId(int detailId) { this.detailId = detailId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
}
