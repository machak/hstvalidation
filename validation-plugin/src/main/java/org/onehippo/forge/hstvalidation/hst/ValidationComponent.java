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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.validation.ConstraintViolation;
import javax.validation.Path;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import org.hippoecm.hst.component.support.forms.BaseFormHstComponent;
import org.hippoecm.hst.component.support.forms.FormField;
import org.hippoecm.hst.component.support.forms.FormMap;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.ComponentConfiguration;
import org.onehippo.forge.hstvalidation.annotation.ValidationBean;
import org.onehippo.forge.hstvalidation.api.FrontendValidationSupport;
import org.onehippo.forge.hstvalidation.api.ValidationDataProvider;
import org.onehippo.forge.hstvalidation.exceptions.InvalidConfigurationException;
import org.onehippo.forge.hstvalidation.hst.editors.DateEditor;
import org.onehippo.forge.hstvalidation.hst.provider.JqueryValidationDataProvider;
import org.onehippo.forge.hstvalidation.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.validation.DataBinder;

/**
 * ValidationComponent  provides basic validation support for HST framework
 * TODO partial (group) validation
 */
public class ValidationComponent extends BaseFormHstComponent implements FrontendValidationSupport {
    @SuppressWarnings("unused")
    private static final String SVN_ID = "$Id: ValidationComponent.java 33 2011-07-26 23:15:14Z mmilicevic $";

    private static Logger log = LoggerFactory.getLogger(ValidationComponent.class);

    /**
     * Jquery is the default JS data provider
     */
    private ValidationDataProvider dataProvider = new JqueryValidationDataProvider();
    /**
     * Attribute name for javascript meta data.
     */
    public static final String VALIDATION_ATT_JS_META = "js_metadata";
    /**
     * Request attributes used
     */
    public static final String VALIDATION_ATT_FORM_MAP = "formMap";
    public static final String VALIDATION_ATT_FORM_ERRORS = "formErrors";
    public static final String VALIDATION_ATT_FORM_BEAN = "formBean";
    private static final String VALIDATION_ATT_FORM_ID = "formId";
    /**
     * (static) Map which contains form fields which are annotated with {@literal ValidationBean} annotation.
     * Fields are parsed only once (through reflection API) and cached values are used subsequently.
     * Each FormComponentClass can have different FormBean(s) and those contain field to validation rules mappings.
     * <p/>
     * NOTE: currently there is no way to force refreshing of the caches
     *
     * @see org.onehippo.forge.hstvalidation.annotation.ValidationBean
     */
    private static Multimap<Class<?>, String> beans = Multimaps.synchronizedMultimap(HashMultimap.<Class<?>, String>create());


    private boolean initialized;


    @Override
    public void init(final ServletContext servletContext, final ComponentConfiguration componentConfig) throws HstComponentException {
        super.init(servletContext, componentConfig);
        initialize();
    }


    /**
     * Within action we bind submitted values to our bean
     *
     * @param request  hst request
     * @param response hst response
     * @throws HstComponentException
     */
    @Override
    public void doAction(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doAction(request, response);
        // create form map
        final Collection<String> fieldNames = getFieldNames();
        final FormMap map = new FormMap(request, new ArrayList<String>(fieldNames));
        persistFormMap(request, response, map, null);
        log.info("@@@ Created FormMap for " + fieldNames.size());


    }


    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
    }

    public Object validate(final HstRequest request) {
        request.setAttribute(VALIDATION_ATT_JS_META, getMetaData());
        request.setAttribute(VALIDATION_ATT_FORM_ID, getValidationBeanClass().getSimpleName());
        final Object instance = populateBean(request);
        request.setAttribute(VALIDATION_ATT_FORM_BEAN, instance);
        request.setAttribute(VALIDATION_ATT_FORM_ERRORS, getErrors(instance, request));
        return instance;
    }


    public Map<String, String> getFormErrors(final HstRequest request) {
        final Object instance = validate(request);
        return getErrors(instance, request);
    }


    private Map<String, String> getErrors(final Object instance, final HstRequest request) {
        final FormMap formMap = new FormMap();
        super.populate(request, formMap);
        if (formMap.getFormMap().size() == 0) {
            log.info("Form not submitted");
            return Collections.emptyMap();
        }
        Set<ConstraintViolation<Object>> constraintViolationSet = ValidationUtils.validate(instance);
        if (constraintViolationSet.size() == 0) {
            return Collections.emptyMap();
        }
        final Map<String, String> errors = new HashMap<String, String>();
        for (ConstraintViolation<Object> violation : constraintViolationSet) {
            final Path path = violation.getPropertyPath();
            for (Path.Node node : path) {
                errors.put(node.getName(), violation.getMessage());
            }
        }
        log.info("Form contains errors" + constraintViolationSet.size() + " errors");
        return errors;
    }

    @SuppressWarnings({"unchecked"})
    private Object populateBean(final HstRequest request) {
        final FormMap formMap = new FormMap();
        super.populate(request, formMap);
        formMap.setPrevious(null);
        final Map<String, FormField> map = formMap.getFormMap();
        request.setAttribute(VALIDATION_ATT_FORM_MAP, map);
        final MutablePropertyValues values = new MutablePropertyValues();

        for (Map.Entry<String, FormField> entry : map.entrySet()) {
            FormField formField = entry.getValue();

            String value = formField.getValue();
            if (value != null && value.length() == 0) {
                value = null;
            }
            values.addPropertyValue(entry.getKey(), value);
        }
        final Object instance = ValidationUtils.getInstance(getValidationBeanClass());
        final DataBinder binder = new DataBinder(instance);
        initializeBinder(binder);
        // bind values:
        binder.bind(values);
        return instance;
    }

    /**
     * Initialize default provided editors which
     * are used to bind values to beans
     *
     * @param binder data binder instance
     * @see java.beans.PropertyEditorSupport
     */
    public void initializeBinder(final PropertyEditorRegistry binder) {
        binder.registerCustomEditor(Date.class, new DateEditor(new SimpleDateFormat("dd-MM-yyyy")));
    }

    /**
     * Initialize validation part of this component
     */
    public void initialize() {
        if (initialized) {
            log.warn("Validation beans are already initialized, please do not call initializeForm() explicitly ");
            return;
        }
        try {
            // get annotated class:
            Class<?> validationBeanClass = getValidationBeanClass();
            log.info("Processing form bean: ", validationBeanClass.getName());
            // add field names to collection
            final Collection<String> fieldNames = ValidationUtils.getFieldNames(validationBeanClass);
            for (String fieldName : fieldNames) {
                log.debug("Adding field name [{}]", fieldName);
                beans.put(getClass(), fieldName);
            }
        } catch (Exception e) {
            log.error("Error while initializing form validation data", e);
        } finally {
            initialized = true;
        }

    }


    /**
     * Return collection containing field names of the (Validation) bean,
     * associated with this component
     *
     * @return collection of the field names or an empty collection
     */
    public Collection<String> getFieldNames() {
        return beans.get(getClass());
    }

    /**
     * Class of a bean we are validating (backing bean/ data bean)
     *
     * @return class of to validate bean
     */
    public Class<?> getValidationBeanClass() {
        ValidationBean annotation = getClass().getAnnotation(ValidationBean.class);
        if (annotation == null) {
            throw new InvalidConfigurationException("Class must be annotated with ValidationBean annotation");
        }
        return annotation.value();
    }


    public void initializeProvider(final ValidationDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public String getMetaData() {
        if (dataProvider == null) {
            return "";
        }
        return dataProvider.getMetaData(getValidationBeanClass());
    }


}
