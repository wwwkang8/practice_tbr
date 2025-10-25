package com.practice.tbr.service;

import com.practice.tbr.dto.SalesRecordRequest;
import com.practice.tbr.dto.SalesRecordResponse;
import com.practice.tbr.entity.SalesRecord;
import com.practice.tbr.repository.SalesRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesService {
    
    private final SalesRecordRepository salesRecordRepository;
    
    @Transactional
    public SalesRecord createSalesRecord(SalesRecordRequest request, UUID storeId, UUID userId) {
        log.info("Creating sales record for store: {}, user: {}", storeId, userId);
        
        SalesRecord salesRecord = SalesRecord.builder()
                .storeId(storeId)
                .recordDate(request.getDate())
                .customerCount(request.getCustomerCount())
                .amount(request.getAmount())
                .memo(request.getMemo())
                .createdByUserId(userId)
                .build();
        
        return salesRecordRepository.save(salesRecord);
    }
    
    public SalesRecordResponse getSalesRecordsByDate(LocalDate date, UUID storeId) {
        log.info("Getting sales records for store: {}, date: {}", storeId, date);
        
        List<SalesRecord> records = salesRecordRepository.findSalesRecordsByStoreAndDate(storeId, date);
        Long totalAmount = salesRecordRepository.getTotalAmountByStoreAndDate(storeId, date);
        Integer totalCustomers = salesRecordRepository.getTotalCustomersByStoreAndDate(storeId, date);
        
        List<SalesRecordResponse.SalesRecordItem> items = records.stream()
                .map(record -> SalesRecordResponse.SalesRecordItem.builder()
                        .id(record.getId())
                        .recordDate(record.getRecordDate())
                        .customerCount(record.getCustomerCount())
                        .amount(record.getAmount())
                        .memo(record.getMemo())
                        .createdAt(record.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        
        return SalesRecordResponse.builder()
                .date(date)
                .totalAmount(totalAmount)
                .totalCustomers(totalCustomers)
                .items(items)
                .build();
    }
}
