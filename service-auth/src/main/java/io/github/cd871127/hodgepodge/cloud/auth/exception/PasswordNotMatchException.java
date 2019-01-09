package io.github.cd871127.hodgepodge.cloud.auth.exception;

public class PasswordNotMatchException extends UserException {
    public PasswordNotMatchException() {
    }

    public PasswordNotMatchException(String message) {
        super(message);
    }
}
