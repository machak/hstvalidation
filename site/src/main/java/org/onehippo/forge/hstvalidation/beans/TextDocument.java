
package org.onehippo.forge.hstvalidation.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

@Node(jcrType="hstvalidation:textdocument")
public class TextDocument extends BaseDocument{


    public String getTitle() {
        return getProperty("hstvalidation:title");
    }
    
    public String getSummary() {
        return getProperty("hstvalidation:summary");
    }
    
    public HippoHtml getHtml(){
        return getHippoHtml("hstvalidation:body");    
    }
}
