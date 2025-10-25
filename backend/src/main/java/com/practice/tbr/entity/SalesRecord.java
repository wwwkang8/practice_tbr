package com.practice.tbr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sales_record")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "store_id", nullable = false)
    private UUID storeId;
    
    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;
    
    @Column(name = "customer_count", nullable = false)
    private Integer customerCount;
    
    @Column(name = "amount", nullable = false)
    private Integer amount;
    
    @Column(name = "memo")
    private String memo;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(name = "created_by_user_id", nullable = false)
    private UUID createdByUserId;
}
