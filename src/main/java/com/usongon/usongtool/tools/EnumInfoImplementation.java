package com.usongon.usongtool.tools;

import com.usongon.usongtool.annotation.EnumInfo;
import com.usongon.usongtool.dto.EnumDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author usong
 * @date 2023-11-02 22:33
 */
@Component
public class EnumInfoImplementation {
    public static Map<String, List<EnumDTO>> enumMap = new HashMap<>();
    private static final TypeFilter ANNOTATION_TYPE_FILTER = new AnnotationTypeFilter(EnumInfo.class);

    @PostConstruct
    public void init() {
        Set<Class<?>> annotatedClasses = findAnnotatedClasses();
        annotatedClasses.forEach(clazz -> {
            try {
                EnumInfo enumInfo = AnnotationUtils.findAnnotation(clazz, EnumInfo.class);
                if (enumInfo == null) {
                    return;
                }
                String showCode = enumInfo.showCode();
                String showName = enumInfo.showName();
                String enumName = enumInfo.enumName();
                Enum<?>[] enumConstants = (Enum<?>[]) clazz.getEnumConstants();
                List<EnumDTO> enumDTOList = new ArrayList<>();
                for (Enum<?> enumConstant : enumConstants) {
                    EnumDTO enumDTO = new EnumDTO();
                    try {
                        Field[] declaredFields = enumConstant.getClass().getDeclaredFields();
                        for (Field declaredField : declaredFields) {
                            declaredField.setAccessible(true);
                            if (declaredField.getName().equals(showCode)) {
                                enumDTO.setCode(declaredField.get(enumConstant).toString());
                            } else if (declaredField.getName().equals(showName)) {
                                enumDTO.setDesc(declaredField.get(enumConstant).toString());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    enumDTOList.add(enumDTO);
                }
                enumMap.put(enumName, enumDTOList);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        );
    }



    public static Set<Class<?>> findAnnotatedClasses() {
        ClassPathScanningCandidateComponentProvider provider = getScanningCandidateComponentProvider();
        provider.addIncludeFilter(ANNOTATION_TYPE_FILTER);
        Set<BeanDefinition> candidateComponents = provider.findCandidateComponents("");
        return convertToClasses(candidateComponents);
    }

    private static ClassPathScanningCandidateComponentProvider getScanningCandidateComponentProvider() {
        return new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isIndependent();
            }
        };
    }

    private static Set<Class<?>> convertToClasses(Set<BeanDefinition> beans) {
        Set<Class<?>> classes = new HashSet<>();
        for (BeanDefinition bean : beans) {
            try {
                classes.add(Class.forName(bean.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return classes;
    }
}
