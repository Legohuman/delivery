package com.firstlinesoftware.delivery.misc.rates;

import com.firstlinesoftware.delivery.dto.SegmentVal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * User: Legohuman
 * Date: 19/03/16
 */
public class ExcelSegmentBasedProcessor {

    protected ConcurrentNavigableMap<Integer, List<SegmentVal>> segments;

    protected void addSegment(int fromCode, SegmentVal segment) {
        List<SegmentVal> segmentVals = segments.get(fromCode);
        if (segmentVals == null) {
            segmentVals = new ArrayList<>();
            segmentVals.add(segment);
        } else {
            segmentVals.add(segment);
        }
        segments.put(fromCode, segmentVals);
    }
}
