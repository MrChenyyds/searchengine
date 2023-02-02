package org.brower.pojo;

import org.springframework.stereotype.Repository;

@Repository
public  class ComfirmEmailResult {

    public  static final byte SUCCESS=1;

    public  static final byte DIFFERENT=0;

    public static final byte TIMEOUT=2;

}
