package com.lisi.booknavigator.propertyservice.event;

import com.lisi.booknavigator.propertyservice.model.Property;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PropertyEvent {
    private String propertyId;
    private EventType eventType;
    private Property property;

    public enum EventType {
        CREATE, UPDATE, DELETE
    }
}
