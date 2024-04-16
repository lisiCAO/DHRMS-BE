package com.lisi.booknavigator.leasehistoryservice.event;

import com.lisi.booknavigator.leasehistoryservice.model.LeaseHistory;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LeaseHistoryEvent {
    private String propertyId;
    private EventType eventType;
    private LeaseHistory property;

    public enum EventType {
        CREATE, UPDATE, DELETE
    }
}
