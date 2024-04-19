package com.lisi.booknavigator.maintenanceservice.event;

import com.lisi.booknavigator.maintenanceservice.model.Maintenance;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MaintenanceEvent {
    private Integer  requestId;
    private EventType eventType;
    private Maintenance maintenance;

    public enum EventType {
        CREATE, UPDATE, DELETE
    }
}
