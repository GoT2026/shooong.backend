package io.rapa.shooongbackend.common.util;

import io.rapa.shooongbackend.common.constant.ErrorCode;
import io.rapa.shooongbackend.common.exception.CustomException;

public final class PreConditions {
    public static void validate(boolean expression, ErrorCode errorCode){
        if (!expression) throw new CustomException(errorCode);
    }
}
