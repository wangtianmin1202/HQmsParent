package com.hand.wfl.util;

/**
 * description
 *
 * @author KOCE3G3 2020/03/26 11:33 AM
 */
import com.hand.hap.core.exception.BaseException;


public class ActException extends BaseException {

    //编码不统一，暂时用于PPAP
    private static final String CODE = "ppapfile";

    public ActException(String descriptionKey, Object[] parameters) {
        super(CODE, descriptionKey, parameters);
    }

    public ActException(String descriptionKey) {
        super(CODE, descriptionKey, null);
    }
}
