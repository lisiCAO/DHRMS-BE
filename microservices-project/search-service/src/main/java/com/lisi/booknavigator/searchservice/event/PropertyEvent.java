package com.lisi.booknavigator.searchservice.event;

import com.lisi.booknavigator.searchservice.entity.Property;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class PropertyEvent {

    private String propertyId;
    private EventType eventType;
    private Property property;

    public enum EventType {
        CREATE, UPDATE, DELETE
    }
}

