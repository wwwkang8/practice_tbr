package com.practice.tbr.repository;

import com.practice.tbr.entity.SalesRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface SalesRecordRepository extends JpaRepository<SalesRecord, UUID> {
    
    List<SalesRecord> findByStoreIdAndRecordDateOrderByCreatedAtDesc(UUID storeId, LocalDate recordDate);
    
    @Query("SELECT sr FROM SalesRecord sr WHERE sr.storeId = :storeId AND sr.recordDate = :recordDate ORDER BY sr.createdAt DESC")
    List<SalesRecord> findSalesRecordsByStoreAndDate(@Param("storeId") UUID storeId, @Param("recordDate") LocalDate recordDate);
    
    @Query("SELECT COALESCE(SUM(sr.amount), 0) FROM SalesRecord sr WHERE sr.storeId = :storeId AND sr.recordDate = :recordDate")
    Long getTotalAmountByStoreAndDate(@Param("storeId") UUID storeId, @Param("recordDate") LocalDate recordDate);
    
    @Query("SELECT COALESCE(SUM(sr.customerCount), 0) FROM SalesRecord sr WHERE sr.storeId = :storeId AND sr.recordDate = :recordDate")
    Integer getTotalCustomersByStoreAndDate(@Param("storeId") UUID storeId, @Param("recordDate") LocalDate recordDate);
}
