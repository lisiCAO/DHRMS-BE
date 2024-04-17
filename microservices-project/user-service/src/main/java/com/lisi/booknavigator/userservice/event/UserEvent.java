package com.lisi.booknavigator.userservice.event;

import com.lisi.booknavigator.userservice.model.User;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEvent {
    private String userId;
    private EventType EventType;
    private User user;

    public enum EventType {
        CREATE, UPDATE, DELETE
    }
}
