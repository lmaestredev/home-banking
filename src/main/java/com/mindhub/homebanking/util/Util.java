package com.mindhub.homebanking.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class Util {
    public static Map<String, Object> makeDTO(String key, Object value){
        Map<String, Object> dto = new LinkedHashMap<>();
            dto.put(key,value);
        return dto;
    }
}
