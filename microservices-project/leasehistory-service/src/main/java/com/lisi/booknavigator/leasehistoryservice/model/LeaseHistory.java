package com.lisi.booknavigator.leasehistoryservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document(value = "property")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class LeaseHistory {
    @Id
    private Long historyId;
    private Long leaseId;
    private Date changeDate;
    private String changeType;
    private String previousTerms;
    private String newTerms;

}


