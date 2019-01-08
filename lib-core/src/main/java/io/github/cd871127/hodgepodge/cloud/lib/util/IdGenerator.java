package io.github.cd871127.hodgepodge.cloud.lib.util;

import java.util.UUID;

public class IdGenerator {
    private IdGenerator() {
    }

    public static String getId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
