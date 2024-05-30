package com.syu.capstone_stock.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestTimeTracker {
    private static final Map<String, Long> requestTimes = new ConcurrentHashMap<>();

    public static boolean canProcessRequest(String clientIp, long intervalSeconds){
        long currentTime = System.currentTimeMillis() / 1000;
        Long lastRequestTime = requestTimes.get(clientIp);

        if(lastRequestTime == null || currentTime - lastRequestTime >= intervalSeconds){
            requestTimes.put(clientIp, currentTime);
            return true;
        } else {
            return false;
        }
    }
}
