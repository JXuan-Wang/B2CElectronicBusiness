package org.example.system.manager.finalArgs;

public enum RedisKeyPrefix {
    USER_LOGIN("user:login"),
    USER_VALIDATE("user:validate")
    ;
    private final String content;

    RedisKeyPrefix(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return content;
    }
}
