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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.PropertyDescriptor;
import java.util.Set;

/**
 * ValidationFactory
 */
public class ValidationFactory{
    @SuppressWarnings("unused")
    private static final String SVN_ID = "$Id: ValidationFactory.java 16 2009-12-17 21:41:44Z mmilicevic $";
    private static Logger log = LoggerFactory.getLogger(ValidationFactory.class);
    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();


    public static Validator getValidator() {
        return VALIDATOR;
    }

    public static BeanDescriptor getDescriptorForBean(final Class<?> clazz) {
        return VALIDATOR.getConstraintsForClass(clazz);
    }


    public static Set<PropertyDescriptor> getPropertyDescriptor(final Class<?> clazz) {
        return getDescriptorForBean(clazz).getConstrainedProperties();

    }

}
