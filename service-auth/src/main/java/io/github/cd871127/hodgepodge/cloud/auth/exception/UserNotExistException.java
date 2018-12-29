package io.github.cd871127.hodgepodge.cloud.auth.exception;

public class UserNotExistException extends UserException {
    public UserNotExistException() {
    }

    public UserNotExistException(String message) {
        super(message);
    }
}
