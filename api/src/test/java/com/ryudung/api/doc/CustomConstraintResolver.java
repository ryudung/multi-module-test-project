package com.ryudung.api.doc;

/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.springframework.restdocs.constraints.Constraint;
import org.springframework.restdocs.constraints.ConstraintResolver;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link ConstraintResolver} that uses a Bean Validation {@link Validator} to resolve
 * constraints. The name of the constraint is the fully-qualified class name of the
 * constraint annotation. For example, a {@link NotNull} constraint will be named
 * {@code javax.validation.constraints.NotNull}.
 *
 * @author Andy Wilkinson
 */
public class CustomConstraintResolver implements ConstraintResolver {

    private final Validator validator;

    /**
     * Creates a new {@code ValidatorConstraintResolver} that will use a {@link Validator}
     * in its default configuration to resolve constraints.
     *
     * @see Validation#buildDefaultValidatorFactory()
     * @see ValidatorFactory#getValidator()
     */
    public CustomConstraintResolver() {
        this(Validation.buildDefaultValidatorFactory().getValidator());
    }

    /**
     * Creates a new {@code ValidatorConstraintResolver} that will use the given
     * {@code Validator} to resolve constraints.
     *
     * @param validator the validator
     */
    public CustomConstraintResolver(Validator validator) {
        this.validator = validator;
    }

    @Override
    public List<Constraint> resolveForProperty(String property, Class<?> clazz) {
        List<Constraint> constraints = new ArrayList<>();

        //******************************************************************************************************

         /**
         *     ConstraintResolver를 커스터마이징해서 내부 클래스에도 constraint 정보를 rest doc에 남기자.
         *
         *
         *      가능한 경우
         *         1)아래 유형일때
         *             a
         *             a.b, a.b[]
         *             c.b[].a
         *             a[].b
         *         2)request가 inner, nested class 일경우
         *
         *
         *      1.property parsing
         *      2 '.' 으로 split
         *        '마지막이 원하는 property name'
         *      3. length 1보다 크면, 뒤에서 2번째가 클래스 이름.
         *      4. '[]'.replace('')
         *      5. 해당 이름으로 클래스 찾음.
         *      6. **해당 클래스랑, property 넘김**
         **/

        String[] splitedProperty = property.split("\\.");
        String propertyName = property.replace("[]", "");

        if (splitedProperty.length > 1) {
            propertyName = splitedProperty[splitedProperty.length - 1];
            //2. class 단계 별로 분리.
            Map<Integer, List<Class>> clazzMap = getClazzByRecursion(clazz, new HashMap<>(), 0);
            int clazzLocation = splitedProperty.length - 2;
            String className = splitedProperty[splitedProperty.length - 2];

            clazz = clazzMap.get(clazzLocation).stream()
                    .filter(aClass ->
                            className.replace("[]", "").contains(aClass.getSimpleName().toLowerCase())
                    )
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("해당 클래스가 inner class에 존재하지 않습니다."));
        }

        //******************************************************************************************************

        BeanDescriptor beanDescriptor = this.validator.getConstraintsForClass(clazz);
        PropertyDescriptor propertyDescriptor = beanDescriptor
                .getConstraintsForProperty(propertyName);

        if (propertyDescriptor != null) {
            for (ConstraintDescriptor<?> constraintDescriptor : propertyDescriptor
                    .getConstraintDescriptors()) {
                constraints.add(new Constraint(
                        constraintDescriptor.getAnnotation().annotationType().getName(),
                        constraintDescriptor.getAttributes()));
            }
        }


        return constraints;
    }
    /**
     *  클래스 재귀 탐색 메소드.
     * */
    private Map<Integer, List<Class>> getClazzByRecursion(Class<?> clazz, HashMap<Integer, List<Class>> integerClassHashMap, int count) {
        List<Class> classList = new ArrayList<>();

        for (Class<?> declaredClass : clazz.getDeclaredClasses()) {
            if (!declaredClass.getName().contains("Builder")) {
                classList.add(declaredClass);
                getClazzByRecursion(declaredClass, integerClassHashMap, count + 1);
            }
        }
        integerClassHashMap.put(count, classList);
        return integerClassHashMap;
    }

}
