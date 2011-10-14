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

package org.onehippo.forge.hstvalidation.hst;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.onehippo.forge.hstvalidation.annotation.ValidationBean;
import org.onehippo.forge.hstvalidation.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

import static org.testng.Assert.assertTrue;

/**
 * ValidationComponentTest
 */
@ValidationBean(TestBean.class)
public class ValidationComponentTest {


    @org.testng.annotations.Test(groups = "validation")
    public void testDataBinding() throws Exception {

        final TestComponent component = new TestComponent();
        final Collection<String> fieldNames = component.getFieldNames();
        assertTrue(fieldNames.size() == 7, "Expected 7 fieldNames but got: " + fieldNames.size());
        assertTrue(component.getValidationBeanClass().equals(TestBean.class), "Expected TestBean class to be components bean class");
        // test bindings
        final Class<?> beanClass = component.getValidationBeanClass();
        final Object instance = ValidationUtils.getInstance(beanClass);

        assertTrue(instance.getClass().equals(beanClass), "Expected testBean instance but got: " + instance.getClass());
        final TestBean testBean = (TestBean) instance;
        final MutablePropertyValues mutableValues = new MutablePropertyValues();
        final Map<String, String> map = createInvalidData(fieldNames);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            mutableValues.addPropertyValue(entry.getKey(), entry.getValue());
        }


        final DataBinder binder = new DataBinder(testBean);
        component.initializeBinder(binder);
        // bind values:
        binder.bind(mutableValues);
        assertTrue(testBean.isaBoolean(), "Expected value to be true, but got:" + testBean.isaBoolean());
        assertTrue(testBean.getaDate() == null, "Expected value to be null, but got:" + testBean.getaDate());
        assertTrue(testBean.getaDouble() == null, "Expected value to be null, but got:" + testBean.getaDouble());
        Set<ConstraintViolation<TestBean>> constraintViolationSet = ValidationUtils.validate(testBean);
        assertTrue(constraintViolationSet.size() == 1, "Expected 1 but got: " + constraintViolationSet.size() + " constraint validations");
        testBean.setaString("123456");
        constraintViolationSet = ValidationUtils.validate(testBean);
        assertTrue(constraintViolationSet.size() == 0, "Expected 0 but got: " + constraintViolationSet.size() + " constraint validations");

    }

    private Map<String, String> createInvalidData(final Collection<String> fieldNames) {
        Map<String, String> map = new HashMap<String, String>();
        for (String fieldName : fieldNames) {
            map.put(fieldName, "true");
        }
        return map;
    }

    @org.testng.annotations.Test
    public void testDoAction() throws Exception {

    }

    @org.testng.annotations.Test
    public void testGetMetaData() throws Exception {
    }

    @org.testng.annotations.Test
    public void testGetFieldNames() throws Exception {
    }

    @org.testng.annotations.Test
    public void testInitializeProvider() throws Exception {
    }
}
