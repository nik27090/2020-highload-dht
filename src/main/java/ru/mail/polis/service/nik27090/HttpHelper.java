package ru.mail.polis.service.nik27090;

import one.nio.http.HttpClient;
import one.nio.http.HttpException;
import one.nio.http.HttpSession;
import one.nio.http.Request;
import one.nio.http.Response;
import one.nio.pool.PoolException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class HttpHelper {
    private static final Logger log = LoggerFactory.getLogger(HttpHelper.class);

    public void sendResponse(@NotNull final HttpSession session, @NotNull final Response response) {
        try {
            session.sendResponse(response);
        } catch (IOException e) {
            log.error("Can't send response", e);
        }
    }

    @NotNull
    public Response proxy(
            @NotNull final String node,
            @NotNull final Request request,
            @NotNull final Map<String, HttpClient> nodeToClient) {
        request.addHeader("X-Proxy-For: " + node);
        try {
            return nodeToClient.get(node).invoke(request);
        } catch (InterruptedException | PoolException | HttpException | IOException e) {
            log.error("Can't proxy request", e);
            return new Response(Response.INTERNAL_ERROR);
        }
    }
}
