package com.lisi.booknavigator.fileservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileSavedEvent {
    private String fileUrl;
}