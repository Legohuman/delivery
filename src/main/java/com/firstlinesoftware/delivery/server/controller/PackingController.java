package com.firstlinesoftware.delivery.server.controller;

import io.vertx.ext.web.RoutingContext;

/**
 * User: Legohuman
 * Date: 07/03/16
 */
public class PackingController {

    public static void packContainers(RoutingContext ctx) {
        ctx.response().end("\"{\"contanerSize\":[5898,2287,2698]," +
                "\"containerNum\":1," +
                "\"placementData\":[" +
                "[" +
                "{\"start\":[0,0,0],\"end\":[1000,1000,1000]}," +
                "{\"start\":[1000,0,0],\"end\":[2000,1000,1000]}," +
                "{\"start\":[2000,0,0],\"end\":[3000,1000,100]}" +
                "]" +
                "]}\"");  //20ft container from http://www.ecbgroup.com/20ft-High-Cube-Shipping-Container/Logistics-and-Forwarding/Branches/Product/content/9/22
    }
}
