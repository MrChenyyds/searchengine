package org.brower.pojo;

import org.springframework.stereotype.Repository;

@Repository
public class PasswordCheckResult {
    public static final byte PASSWORD_WEAK = 0;

    public static final byte PASSWORD_DIFF = 1;

    public static final byte PASSWORD_OK= 2;

    public static final byte PASSWORD_WRONG= 3;

    public static final byte PASSWORD_SHORT= 4;
}
