package com.practice.tbr.controller;

import com.practice.tbr.dto.SalesRecordRequest;
import com.practice.tbr.dto.SalesRecordResponse;
import com.practice.tbr.service.SalesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Slf4j
public class SalesController {
    
    private final SalesService salesService;
    
    @PostMapping
    public ResponseEntity<?> createSalesRecord(
            @Valid @RequestBody SalesRecordRequest request,
            HttpServletRequest httpRequest) {
        
        try {
            String userIdStr = (String) httpRequest.getAttribute("userId");
            String storeIdStr = (String) httpRequest.getAttribute("storeId");
            
            if (userIdStr == null || storeIdStr == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("인증 정보가 없습니다.");
            }
            
            UUID userId = UUID.fromString(userIdStr);
            UUID storeId = UUID.fromString(storeIdStr);
            
            salesService.createSalesRecord(request, storeId, userId);
            
            return ResponseEntity.ok().body("저장되었습니다.");
            
        } catch (Exception e) {
            log.error("Error creating sales record: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("매출 기록 저장 중 오류가 발생했습니다.");
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getSalesRecords(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpServletRequest httpRequest) {
        
        try {
            String storeIdStr = (String) httpRequest.getAttribute("storeId");
            
            if (storeIdStr == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("인증 정보가 없습니다.");
            }
            
            UUID storeId = UUID.fromString(storeIdStr);
            SalesRecordResponse response = salesService.getSalesRecordsByDate(date, storeId);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error getting sales records: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("매출 조회 중 오류가 발생했습니다.");
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Sales API is healthy");
    }
}
