package com.example.forum.controller.form;

import com.example.forum.validator.annotation.Content;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentForm {

    private int id;
    @Content(fieldNameInErrorMessage = "commentContent")
    private String content;
    private int reportId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
