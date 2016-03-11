package com.firstlinesoftware.delivery.server;

import com.firstlinesoftware.delivery.server.controller.PackingController;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public class DeliveryServer extends AbstractVerticle {

    public static void main(String[] args) {
        new DeliveryServer().start();
    }

    public void start() {
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        // CORS enabling
        router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PATCH)
                .allowedHeader("X-PINGARUNER")
                .allowedHeader("Content-Type"));

        router.route().handler(BodyHandler.create());

        // to avoid writing it in every handler
        router.route("/").handler(ctx -> {
            ctx.response().putHeader("content-type", "application/json");
            ctx.next();
        });

        SockJSHandlerOptions options = new SockJSHandlerOptions().setHeartbeatInterval(2000);

        SockJSHandler sockJSHandler = SockJSHandler.create(vertx, options);

        sockJSHandler.socketHandler(sockJSSocket -> {

            // Just echo the data back
            sockJSSocket.handler(sockJSSocket::write);
        });

        router.route("/websocket/*").handler(sockJSHandler);
        router.route("/assets/*").handler(StaticHandler.create("assets"));

        router.post("/packing").handler(PackingController::packContainers);

        server.requestHandler(router::accept)
                .listen(8080, "0.0.0.0");
    }
}
