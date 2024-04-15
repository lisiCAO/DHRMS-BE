package com.lisi.booknavigator.searchservice.event;

import com.lisi.booknavigator.searchservice.entity.ElasticProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class PropertyEvent {

    private String propertyId;
    private EventType eventType;
    private ElasticProperty elasticProperty;

    public enum EventType {
        CREATE, UPDATE, DELETE
    }
}

