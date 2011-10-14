/*
 * Copyright 2009 Hippo
 *
 *   Licensed under the Apache License, Version 2.0 (the  "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS"
 *   BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.onehippo.forge.hstvalidation.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;


public final class ValidationUtils {
    @SuppressWarnings("unused")
    private static final String SVN_ID = "$Id: ValidationUtils.java 16 2009-12-17 21:41:44Z mmilicevic $";
    private static Logger log = LoggerFactory.getLogger(ValidationUtils.class);


    /**
     * Returns a collection of all declared fields within a given class
     *
     * @param clazz class to scan
     * @return field collection (or empty collection)
     */
    public static Collection<Field> getClassFields(Class<?> clazz) {

        Map<String, Field> fields = new HashMap<String, Field>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    // skip static fields
                    continue;
                }
                if (!fields.containsKey(field.getName())) {
                    fields.put(field.getName(), field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields.values();
    }

    /**
     * Get fields of an class which are annotated with specific
     * annotation and set them accessible (if necessary)
     *
     * @param clazz           class we are scanning for annotated fields.
     * @param annotationClass annotation we are interested in
     * @return a collection containing fields we've found (or an empty collection)
     */
    public static Collection<Field> getAnnotatedFields(final Class<?> clazz, final Class<? extends Annotation> annotationClass) {
        Collection<Field> fields = getClassFields(clazz);
        Iterator<Field> iterator = fields.iterator();
        while (iterator.hasNext()) {
            Field field = iterator.next();
            if (!field.isAnnotationPresent(annotationClass)) {
                iterator.remove();
            } else if (!field.isAccessible()) {
                try {
                    field.setAccessible(true);
                }
                catch (SecurityException se) {
                    throw new RuntimeException("Error parsing FormBean, security exception....", se);
                }
            }
        }
        return fields;
    }


    public static Collection<String> getFieldNames(final Class<?> clazz) {
        final Collection<String> fieldNames = new ArrayList<String>();
        final Collection<Field> fieldCollection = getClassFields(clazz);
        for (Field field : fieldCollection) {
            fieldNames.add(field.getName());
        }
        return fieldNames;
    }

    /**
     * Create new instance of given class
     *
     * @param clazz class to instantiate
     * @param <E>   type
     * @return null if failed to create new instance
     */
    public static <E> E getInstance(final Class<E> clazz) {
        try {
            return clazz.newInstance();
        }
        catch (InstantiationException e) {
            log.error("", e);
        }
        catch (IllegalAccessException e) {
            log.error("", e);
        }
        return null;
    }


    /**
     * Process validation
     *
     * @param instance intsance of a bean
     * @param <E>      return type
     * @return set containing constraint validations
     */
    public static <E> Set<ConstraintViolation<E>> validate(final E instance) {
        return ValidationFactory.getValidator().validate(instance);
    }

    /**
     * Returns a multimap containing {@code propertyname, descriptor collection} key/values
     * @param clazz class of a bean we are validating
     * @return empty map if no constrains are defined
     */
    public static Multimap<String, ConstraintDescriptor<?>> getConstrains(final Class<?> clazz) {
        final Multimap<String, ConstraintDescriptor<?>> mappings = HashMultimap.create();
        final BeanDescriptor beanDescriptor = ValidationFactory.getValidator().getConstraintsForClass(clazz);
        final Set<PropertyDescriptor> descriptorSet = beanDescriptor.getConstrainedProperties();
        for (PropertyDescriptor descriptor : descriptorSet) {
            final Set<ConstraintDescriptor<?>> beanDescriptors = descriptor.getConstraintDescriptors();
            for (ConstraintDescriptor<?> constraintDescriptor : beanDescriptors) {
                mappings.put(descriptor.getPropertyName(), constraintDescriptor);
            }
        }
        return mappings;
    }

    // do not instantiate

    private ValidationUtils() {
    }

}
