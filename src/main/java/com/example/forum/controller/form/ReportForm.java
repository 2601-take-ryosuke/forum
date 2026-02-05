package com.example.forum.controller.form;

import com.example.forum.validator.annotation.Content;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReportForm {

    private int id;
    @Content(fieldNameInErrorMessage = "reportContent")
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime threadUpdatedDate;
}
