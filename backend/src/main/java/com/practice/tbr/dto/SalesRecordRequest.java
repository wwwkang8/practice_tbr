package com.practice.tbr.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesRecordRequest {
    
    @NotNull(message = "매출 발생일자는 필수입니다.")
    @PastOrPresent(message = "미래 날짜는 기록할 수 없습니다.")
    private LocalDate date;
    
    @NotNull(message = "고객 수는 필수입니다.")
    @Min(value = 0, message = "고객 수는 0 이상이어야 합니다.")
    private Integer customerCount;
    
    @NotNull(message = "금액은 필수입니다.")
    @Min(value = 0, message = "금액은 0 이상이어야 합니다.")
    private Integer amount;
    
    private String memo;
}
