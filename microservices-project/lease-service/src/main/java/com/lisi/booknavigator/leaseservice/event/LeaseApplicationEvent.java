package com.lisi.booknavigator.leaseservice.event;

import com.lisi.booknavigator.leaseservice.model.LeaseApplication;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LeaseApplicationEvent {
    private Long leaseApplicationId;
    private EventType eventType;
    private LeaseApplication leaseApplication;

    public enum EventType {
        CREATE, UPDATE, DELETE
    }
}