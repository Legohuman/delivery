package com.firstlinesoftware.delivery.server.controller;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.tirnak.binpacking.model.Box;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: Legohuman
 * Date: 07/03/16
 */
public class PackingController {

    public static void packContainers(RoutingContext ctx) {
            JsonArray JsonBoxes = ctx.getBodyAsJsonArray();
            List<Box> boxesToPack = JsonBoxes.stream().map(
                    obj -> {
                            JsonObject jsonObject = (JsonObject) obj;
                            return new Box(jsonObject.getInteger("width"),
                                    jsonObject.getInteger("length"),
                                    jsonObject.getInteger("height"));

                    }).collect(Collectors.toList());



        ctx.response().end("{\"contanerSize\":[5898,2287,2698]," +
                "\"containerNum\":1," +
                "\"placementData\":[" +
                "[" +
                "{\"start\":[0,0,0],\"end\":[1000,1000,1000]}," +
                "{\"start\":[1000,0,0],\"end\":[2000,1000,1000]}," +
                "{\"start\":[2000,0,0],\"end\":[4000,1000,1000]}" +
                "]," +
                "[" +
                "{\"start\":[0,0,500],\"end\":[1000,1000,1000]}," +
                "{\"start\":[1000,0,500],\"end\":[2000,1000,1000]}," +
                "{\"start\":[2000,0,500],\"end\":[4000,1000,1000]}" +
                "]" +
                "]}");  //20ft container from http://www.ecbgroup.com/20ft-High-Cube-Shipping-Container/Logistics-and-Forwarding/Branches/Product/content/9/22
    }
}
