package io.github.cd871127.hodgepodge.cloud.lib.util;

import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;

public class ResponseHandler {
    public static <T> T handleResponse(ServerResponse<T> serverResponse) throws ResponseException {
        if (!serverResponse.getCode().endsWith("00")) {
            throw new ResponseException(serverResponse.getMessage());
        }
        return serverResponse.getData();
    }
}
