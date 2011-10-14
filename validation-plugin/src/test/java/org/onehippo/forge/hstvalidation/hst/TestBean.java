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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * TestBean
 */

public class TestBean {
    @SuppressWarnings("unused")
    private static final String SVN_ID = "$Id: TestBean.java 16 2009-12-17 21:41:44Z mmilicevic $";
    private static Logger log = LoggerFactory.getLogger(TestBean.class);

    @NotNull
    @Size(min = 5, max = 10,message = "Value must be minimum 5 and maximum 10 characters")
    private String aString;
    private Long aLong;
    private Boolean aBoolean;
    private Date aDate;
    private Double aDouble;
    private Float aFloat;
    private boolean aPrimitiveBoolean;


    public String getaString() {
        return aString;
    }

    public void setaString(final String aString) {
        this.aString = aString;
    }

    public Long getaLong() {
        return aLong;
    }

    public void setaLong(final Long aLong) {
        this.aLong = aLong;
    }

    public Boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(final Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public Date getaDate() {
        return aDate;
    }

    public void setaDate(final Date aDate) {
        this.aDate = aDate;
    }

    public Double getaDouble() {
        return aDouble;
    }

    public void setaDouble(final Double aDouble) {
        this.aDouble = aDouble;
    }

    public Float getaFloat() {
        return aFloat;
    }

    public void setaFloat(final Float aFloat) {
        this.aFloat = aFloat;
    }

    public boolean isaPrimitiveBoolean() {
        return aPrimitiveBoolean;
    }

    public void setaPrimitiveBoolean(final boolean aPrimitiveBoolean) {
        this.aPrimitiveBoolean = aPrimitiveBoolean;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TestBean");
        sb.append("{aString='").append(aString).append('\'');
        sb.append(", aLong=").append(aLong);
        sb.append(", aBoolean=").append(aBoolean);
        sb.append(", aDate=").append(aDate);
        sb.append(", aDouble=").append(aDouble);
        sb.append(", aFloat=").append(aFloat);
        sb.append(", aPrimitiveBoolean=").append(aPrimitiveBoolean);
        sb.append('}');
        return sb.toString();
    }
}
