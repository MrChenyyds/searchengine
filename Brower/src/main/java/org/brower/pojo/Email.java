package org.brower.pojo;

import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Component
public class Email {
    @Setter
    @Getter
    private String[] user;

    @Setter
    @Getter
    private String subject;

    @Setter
    @Getter
    private String content;
}
