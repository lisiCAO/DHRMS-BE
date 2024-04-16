package com.lisi.booknavigator.leasehistoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaseHistoryResponse {
        private Long historyId;
        private Long leaseId;
        private Date changeDate;
        private String changeType;
        private String previousTerms;
        private String newTerms;
}
