package org.yajcms.beans.entities.nosql;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.yajcms.beans.callbacks.YajCMSCallback;
import org.yajcms.beans.callbacks.YajCMSContextWork;
import org.yajcms.beans.callbacks.YajCMSPostPut;
import org.yajcms.beans.callbacks.YajCMSPrePut;
import org.yajcms.beans.entities.Entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BasicEntityPreAndPostProcessorImpl implements EntityPreAndPostProcessor {

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Entity prePut(Entity entity) {
        return findAndExecuteCode(entity, YajCMSPrePut.class);
    }

    @Override
    public Entity postPut(Entity entity) {
        return findAndExecuteCode(entity, YajCMSPostPut.class);
    }

    @Override
    public Entity contextWork(Entity entity) {
        return findAndExecuteCode(entity, YajCMSContextWork.class);
    }

    private Optional<String> getClassAnnotationValue(Class classType, Class annotationType, String attributeName) {
        String value = null;

        Annotation annotation = classType.getAnnotation(annotationType);
        if (annotation != null) {
            try {
                value = (String) annotation.annotationType().getMethod(attributeName).invoke(annotation);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return Optional.ofNullable(value);
    }

    private Entity findAndExecuteCode(Entity entity, Class clazz) {
        List<Tuple2<Method, Object>> methodsToCall = new ArrayList<>();
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(YajCMSCallback.class);
        List<Object> beansList = Arrays.stream(beans.values().toArray())
                .filter(b -> getClassAnnotationValue(b.getClass(), YajCMSCallback.class, "value").orElse("")
                        .equals(entity.getKey())).collect(Collectors.toList());
        beansList.forEach(bean -> Arrays.stream(bean.getClass().getMethods()).forEach(m -> {
            if (m.isAnnotationPresent(clazz)) {
                methodsToCall.add(Tuple.of(m, bean));
            }
        }));
        methodsToCall.forEach(t -> {
            try {
                t._1.invoke(t._2, entity);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        return entity;
    }

}
