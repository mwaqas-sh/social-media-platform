package com.waqas.social.media.platform.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentRequest {

    @NotEmpty(message = "content can't be empty")
    private String content;
}
