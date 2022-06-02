package com.viettel.vtskit.maria.utils;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import javax.persistence.Tuple;
import java.util.*;

public class CommonUtils {
    public static final BeanDefinition getBeanDefineByClass(BeanDefinitionRegistry registry, Class<?> clazz){
        if(registry.getBeanDefinitionNames() == null){
            return null;
        }
        String beanName = Arrays.asList(registry.getBeanDefinitionNames())
                .stream().filter(item->item.contains(clazz.getName()))
                .findFirst().orElse(null);
        if(StringUtils.isNullOrEmpty(beanName)){
            return null;
        }
        return registry.getBeanDefinition(beanName);
    }

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
