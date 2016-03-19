package com.firstlinesoftware.delivery.server.controller;

import com.firstlinesoftware.delivery.dto.PaymentInfo;
import com.firstlinesoftware.delivery.server.converter.BoxToJsonConverter;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.tirnak.binpacking.Calculator;
import org.tirnak.binpacking.model.Box;
import org.tirnak.binpacking.model.Container;
import org.tirnak.binpacking.service.PackingService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * User: Legohuman
 * Date: 07/03/16
 */
public class PackingController {

    private static int getContainersNum(List<Box> boxesPacked) {
        return boxesPacked.stream().map(box -> box.container).max(Integer::compare).get() + 1;
    }

    private static List<Box> getBoxes(PaymentInfo.ContainerType type, List<Box> boxesNotPacked) {
        Container dimensions = new Container(
                (int) (type.getWidth() * 1000),
                (int) (type.getHeight() * 1000),
                (int) (type.getLength() * 1000));

        PackingService service = new Calculator();
        return service.pack(boxesNotPacked, dimensions);
    }

    public static void packContainers(RoutingContext ctx) {
        JsonArray jsonBoxes = ctx.getBodyAsJsonArray();
        List<Box> boxesToPack = new ArrayList<>();
        for (Object obj : jsonBoxes) {
            JsonObject jsonObject = (JsonObject) obj;
            for (int i = 0; i < jsonObject.getInteger("count"); i++) {
                boxesToPack.add(Box.newBuilder().setXd((int) (jsonObject.getDouble("width") * 1000))
                        .setYd((int) (jsonObject.getDouble("length") * 1000))
                        .setZd((int) (jsonObject.getDouble("height") * 1000)).build());
            }
        }

        JsonArray containersJson = new JsonArray();

        Stream.of(PaymentInfo.ContainerType.values())
                .forEach(type ->
                {
                    List<Box> boxes = getBoxes(type, boxesToPack);
                    int containerNum = getContainersNum(boxes);
                    JsonObject obj = new JsonObject();
                    containersJson.add(new JsonObject()
                            .put("contanerSize", Arrays.asList(new Integer[]{
                                    (int) (type.getWidth() * 1000),
                                    (int) (type.getHeight() * 1000),
                                    (int) (type.getLength() * 1000)}))
                            .put("containerNum", containerNum)
                            .put("placementData", BoxToJsonConverter.convert(boxes)));
                });


        ctx.response().end(containersJson.getJsonObject(0).encode());

//        ctx.response().end("{\"contanerSize\":[5898,2287,2698]," +
//                "\"containerNum\":1," +
//                "\"placementData\":[" +
//                "[" +
//                "{\"start\":[0,0,0],\"end\":[1000,1000,1000]}," +
//                "{\"start\":[1000,0,0],\"end\":[2000,1000,1000]}," +
//                "{\"start\":[2000,0,0],\"end\":[4000,1000,1000]}" +
//                "]," +
//                "[" +
//                "{\"start\":[0,0,500],\"end\":[1000,1000,1000]}," +
//                "{\"start\":[1000,0,500],\"end\":[2000,1000,1000]}," +
//                "{\"start\":[2000,0,500],\"end\":[4000,1000,1000]}" +
//                "]" +
//                "]}");  //20ft container from http://www.ecbgroup.com/20ft-High-Cube-Shipping-Container/Logistics-and-Forwarding/Branches/Product/content/9/22
    }
}
