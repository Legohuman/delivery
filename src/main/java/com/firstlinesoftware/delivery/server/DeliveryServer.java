package com.firstlinesoftware.delivery.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

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
        vertx.createHttpServer().requestHandler(req -> {
            req.response()
                    .putHeader("content-type", "application/json")
                    .end("{contanerSize: [5898,2287,2698], containerNum: 1, placementData: [" +
                            "[" +
                            "    { start: [0,0,0], end: [1000,1000,1000] }," +
                            "    { start: [1000,0,0], end: [2000,1000,1000] }," +
                            "    { start: [2000,0,0], end: [3000,1000,100] }," +
                            "]" +
                            "]}"); //20ft container from http://www.ecbgroup.com/20ft-High-Cube-Shipping-Container/Logistics-and-Forwarding/Branches/Product/content/9/22
        }).listen(8080);
    }
}
