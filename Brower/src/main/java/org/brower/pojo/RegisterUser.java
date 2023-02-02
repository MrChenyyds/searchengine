package org.brower.pojo;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class RegisterUser implements Serializable {

    private String userName;

    private String userEmail;

    private String userPassword;

    private String userPasswordAgain;

    private String code;
}
