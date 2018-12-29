package io.github.cd871127.hodgepodge.cloud.auth.exception;

public class LoginFailedException extends AuthException {
    public LoginFailedException() {
    }

    public LoginFailedException(String message) {
        super(message);
    }
}
