package com.firstlinesoftware.delivery.server.converter;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.tirnak.binpacking.model.Box;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by kirill on 19.03.16.
 */
public class BoxToJsonConverter {
    public static JsonObject convert(Box box) {
        Map<String, Object> objMap = new HashMap<>();
        objMap.put("start", new int[]{
                box.x0, box.y0, box.z0});

        objMap.put("end", new int[]{
                box.x0 + box.xd, box.y0 + box.yd, box.z0 + box.zd});
        return new JsonObject(objMap);
    }

    public static JsonArray convert(List<Box> boxes) {
        JsonArray arr = new JsonArray();
        boxes.stream().forEach(box -> {
            while (arr.size() < box.container + 1) {
                arr.add(new JsonArray());
            }
            arr.getJsonArray(box.container).add(BoxToJsonConverter.convert(box));
        });
        return arr;
    }

//    ctx.response().end("{\"contanerSize\":[5898,2287,2698]," +
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
}
