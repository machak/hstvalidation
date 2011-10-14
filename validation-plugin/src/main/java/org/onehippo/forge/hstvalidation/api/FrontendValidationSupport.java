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

package org.onehippo.forge.hstvalidation.api;

import org.hippoecm.hst.core.component.HstRequest;

import java.util.Map;

/**
 * FrontendValidationSupport
 */
public interface FrontendValidationSupport {
    @SuppressWarnings("unused")
    static final String SVN_ID = "$Id: FrontendValidationSupport.java 16 2009-12-17 21:41:44Z mmilicevic $";

    /**
     * Trigger validation and return field/error mappings (if validation errors are present)
     *
     * @param request hst request
     * @return empty form if no errors are found
     */
    Map<String, String> getFormErrors(final HstRequest request);

    /**
     * Calling this method will trigger validation and populate request parameters
     *
     * @param request hst request
     * @return populated backing bean
     */
    Object validate(final HstRequest request);

    /**
     * Initialize data provider used to obtain validation meta data used by frontend technologies
     *
     * @param dataProvider instance of ValidationDataProvider
     */
    void initializeProvider(ValidationDataProvider dataProvider);

    /**
     * Returns javascript validation meta data for frontend validation
     *
     * @return empty string if no JS data provider is set, js validation call otherwise
     * @see #initializeProvider(org.onehippo.forge.hstvalidation.api.ValidationDataProvider)
     */
    String getMetaData();
}
