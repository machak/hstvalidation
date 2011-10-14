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

package org.onehippo.forge.hstvalidation.hst.provider;

import com.google.common.collect.Multimap;
import org.onehippo.forge.hstvalidation.api.ValidationDataProvider;
import org.onehippo.forge.hstvalidation.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

/**
 * Provides JS data for Jquery JS framework (official jquery validation framework)
 */
public class JqueryValidationDataProvider implements ValidationDataProvider {
    @SuppressWarnings("unused")
    private static final String SVN_ID = "$Id: JqueryValidationDataProvider.java 31 2010-02-16 15:18:00Z mmilicevic $";
    private static Logger log = LoggerFactory.getLogger(JqueryValidationDataProvider.class);
    private static final char COMMA_SEPARATOR = ',';
    private static final char DOUBLE_POINT_SEPARATOR = ':';

    public String getMetaData(final Class<?> clazz) {
        log.debug("Fetching meta data for: {}", clazz.getName());
        final StringBuilder builder = new StringBuilder();
        builder.append("$().ready(function() {$(\"#").append(clazz.getSimpleName()).append("\").validate({");
        builder.append("rules: {");
        final Multimap<String, ConstraintDescriptor<?>> mappings = ValidationUtils.getConstrains(clazz);

        final Map<String, Collection<ConstraintDescriptor<?>>> map = mappings.asMap();
        final int length = map.size();
        int count = 0;
        for (Map.Entry<String, Collection<ConstraintDescriptor<?>>> entry : map.entrySet()) {
            count++;
            final String key = entry.getKey();
            // start rule:
            builder.append(key).append(DOUBLE_POINT_SEPARATOR);
            // get collection for this key:

            final Collection<ConstraintDescriptor<?>> constraints = entry.getValue();
            builder.append('{');
            for (ConstraintDescriptor<?> constraint : constraints) {
                final Annotation annotation = constraint.getAnnotation();
                final String validationName = annotation.annotationType().getName();
                if (validationName.equals("javax.validation.constraints.NotNull")) {
                    builder.append("required:true,");
                } else if (validationName.equals("javax.validation.constraints.Size")) {
                    // get attributes:
                    final Map<String, Object> attributes = constraint.getAttributes();
                    for (Map.Entry<String, Object> attributeEntry : attributes.entrySet()) {
                        final String attributeName = attributeEntry.getKey();
                        final Object value = attributeEntry.getValue();
                        if (attributeName.equals("max")) {
                            final int size = getInt(value);
                            if (size != -1) {
                                builder.append("maxlength:").append(size).append(COMMA_SEPARATOR);
                            }

                        } else if (attributeName.equals("min")) {
                            final int size = getInt(value);
                            if (size != -1) {
                                builder.append("minlength:").append(size).append(COMMA_SEPARATOR);
                            }
                        }
                    }
                }
            }
            builder.replace(builder.length() - 1, builder.length(), "");
            builder.append('}');
            // add comma separator:
            if (count < length) {
                builder.append(COMMA_SEPARATOR);
            }

            //log.info("Processing entry: = " + entry.getKey());
        }
        //close rules:
        builder.append('}');

        // close method call:
        builder.append("});});");
        return builder.toString();
    }


    // cast to int:

    private int getInt(final Object object) {
        if (object == null) {
            return -1;
        }
        try {
            return (Integer) object;
        } catch (Exception e) {
            // ignore
            //log.error("", e);
        }
        return -1;

    }

}
