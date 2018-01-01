package org.yajcms.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.yajcms.beans.callbacks.YajCMSCallback;
import org.yajcms.core.Entity;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BasicEntityPreAndPostProcessorImpl implements EntityPreAndPostProcessor {

    ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Entity prePut(Entity entity) {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(YajCMSCallback.class);
        List<Object> beansList = Arrays.stream(beans.values().toArray())
                .filter(b -> getClassAnnotationValue(b.getClass(), YajCMSCallback.class, "value")
                        .equals(entity.getKey())).collect(Collectors.toList());
        beansList.forEach(b -> System.out.print(""));
        return entity;
    }

    @Override
    public Entity postPut(Entity entity) {
        return entity;
    }

    @Override
    public Entity contextWork(Entity entity) {
        return entity;
    }

    public String getClassAnnotationValue(Class classType, Class annotationType, String attributeName) {
        String value = null;

        Annotation annotation = classType.getAnnotation(annotationType);
        if (annotation != null) {
            try {
                value = (String) annotation.annotationType().getMethod(attributeName).invoke(annotation);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return value;
    }

}
