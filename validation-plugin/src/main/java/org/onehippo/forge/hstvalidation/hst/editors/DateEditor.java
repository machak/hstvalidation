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

package org.onehippo.forge.hstvalidation.hst.editors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.text.DateFormat;

/**
 * DateEditor
 */
public class DateEditor extends CustomDateEditor {
    @SuppressWarnings("unused")
    private static final String SVN_ID = "$Id: DateEditor.java 16 2009-12-17 21:41:44Z mmilicevic $";
    private static Logger log = LoggerFactory.getLogger(DateEditor.class);

    public DateEditor(final DateFormat dateFormat) {
        super(dateFormat, true);
    }

    public DateEditor(DateFormat dateFormat, boolean allowEmpty) {
        super(dateFormat, allowEmpty);
    }

    public DateEditor(DateFormat dateFormat, boolean allowEmpty, int exactDateLength) {
        super(dateFormat, allowEmpty, exactDateLength);
    }
}
