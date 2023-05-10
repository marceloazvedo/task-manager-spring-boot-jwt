package br.com.marcelo.azevedo.util;

import java.util.UUID;

public class UUIDGeneratorWithPattern {

    private final static String TASK_ID_PREFIX = "TASK_";
    private final static String USER_ID_PREFIX = "USER_";

    public static String generateTaskId() {
        return TASK_ID_PREFIX + UUID.randomUUID().toString().toUpperCase();
    }

    public static String generateUserId() {
        return USER_ID_PREFIX + UUID.randomUUID().toString().toUpperCase();
    }

}
