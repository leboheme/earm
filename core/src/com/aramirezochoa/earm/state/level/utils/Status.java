package com.aramirezochoa.earm.state.level.utils;

/**
 * Created by boheme on 18/02/14.
 */
public enum Status {
    LOCKED("locked"),
    UNLOCKED("unlocked");

    private String status;

    private Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static Status getStatus(String status) throws Exception {
        for (Status statusType : Status.values()) {
            if (statusType.getStatus().equals(status)) {
                return statusType;
            }
        }
        throw new Exception("Error while ch");
    }
}
