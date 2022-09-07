package dev.jcds.core;

public enum ResultCode {

    NO_ERROR(0),
    FORMAT_ERROR(1),
    SERVER_FAILURE(2),
    NX_DOMAIN(3),
    NOT_IMPLEMENTED(4),
    REFUSED(5);

    private final int value;

    ResultCode(int value) {
        this.value = value;
    }

    public static ResultCode fromValue(int value) {
        switch (value) {
            case 1:
                return FORMAT_ERROR;
            case 2:
                return SERVER_FAILURE;
            case 3:
                return NX_DOMAIN;
            case 4:
                return NOT_IMPLEMENTED;
            case 5:
                return REFUSED;
            default:
                return NO_ERROR;
        }
    }
}
