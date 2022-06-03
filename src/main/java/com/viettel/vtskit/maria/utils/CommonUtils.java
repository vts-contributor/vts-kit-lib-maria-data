package com.viettel.vtskit.maria.utils;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonUtils {

    public static <T> List<T> convertToEntity(List<Tuple> input, Class<T> resultClass) {
        List<T> arrayList = new ArrayList<>();
        input.stream().forEach((Tuple tuple) -> {
            Map<String, Object> temp = new HashMap<>();
            tuple.getElements().stream().forEach(tupleElement->{
                Object value = tuple.get(tupleElement.getAlias());
                temp.put(tupleElement.getAlias(), value);
            });
            String mapToString = StringUtils.cvtObjToJsonString(temp);
            arrayList.add(StringUtils.cvtJsonToObjectString(mapToString, resultClass));
        });
        return arrayList;
    }
}
