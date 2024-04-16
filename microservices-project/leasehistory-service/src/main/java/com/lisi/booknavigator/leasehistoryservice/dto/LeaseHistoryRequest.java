package com.lisi.booknavigator.leasehistoryservice.dto;

import com.lisi.booknavigator.leasehistoryservice.model.LeaseHistoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaseHistoryRequest {

    private Long leaseId;
    private Date changeDate;
    private LeaseHistoryType changeType;
    private String previousTerms;
    private String newTerms;
}
