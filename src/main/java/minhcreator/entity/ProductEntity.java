package minhcreator.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_products_user_id", columnList = "user_id")
})
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "UPID")
    private String UPID;

    @Column(name = "name")
    private String name;

    public ProductEntity() {}

    public ProductEntity(int userId, String UPID, String name) {
        this.userId = userId;
        this.UPID = UPID;
        this.name = name;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUPID() { return UPID; }
    public void setUPID(String UPID) { this.UPID = UPID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
