package org.example.wms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "manufacturers")
@EqualsAndHashCode(of = "manufacturerId")
public class ManufacturerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manufacturer_id")
    private Long manufacturerId;

    @Column(name = "company_name", nullable = false, length = 200)
    private String companyName;

    @Column(name = "location", nullable = false, length = 300)
    private String location;

    @Column(name = "contact", nullable = false, length = 50)
    private String contact;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 양방향 관계 테이블 승격
    @OneToMany(mappedBy = "manufacturer", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ProductEntity> products = new ArrayList<>();
}
