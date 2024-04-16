package com.lisi.booknavigator.leaseservice.event;

import com.lisi.booknavigator.leaseservice.model.Lease;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LeaseEvent {
    private Long leaseId;
    private EventType eventType;
    private Lease lease;

    public enum EventType {
        CREATE, UPDATE, DELETE
    }
}
