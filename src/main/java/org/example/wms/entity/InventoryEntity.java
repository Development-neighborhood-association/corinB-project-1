package org.example.wms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "inventory",
        uniqueConstraints = { //"product_id", "warehouse_id" 조합은 유일함
                @UniqueConstraint(
                        name = "unique_product_warehouse",
                        columnNames = {"product_id", "warehouse_id"}
                )
        })
public class InventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long inventoryId;

    @Column(name = "quantity", nullable = false)
    @Builder.Default
    private Integer quantity = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 다대일 관계 - 상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    // 다대일 관계 - 창고
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private WarehouseEntity warehouse;

    // 비즈니스 메서드
    public void addQuantity(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("추가 수량은 0보다 커야 합니다.");
        }
        this.quantity += amount;
    }

    public void removeQuantity(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("출고 수량은 0보다 커야 합니다.");
        }
        if (this.quantity < amount) {
            throw new IllegalStateException("재고가 부족합니다. 현재 재고: " + this.quantity);
        }
        this.quantity -= amount;
    }
}
