package com.practice.tbr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesRecordResponse {
    
    private LocalDate date;
    private Long totalAmount;
    private Integer totalCustomers;
    private List<SalesRecordItem> items;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalesRecordItem {
        private UUID id;
        private LocalDate recordDate;
        private Integer customerCount;
        private Integer amount;
        private String memo;
        private LocalDateTime createdAt;
    }
}
