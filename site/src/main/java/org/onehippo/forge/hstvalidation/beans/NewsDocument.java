
package org.onehippo.forge.hstvalidation.beans;

import java.util.Calendar;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSetBean;

@Node(jcrType="hstvalidation:newsdocument")
public class NewsDocument extends BaseDocument{

    public String getTitle() {
        return getProperty("hstvalidation:title");
    }
    
    public String getSummary() {
        return getProperty("hstvalidation:summary");
    }
    
    public HippoHtml getHtml(){
        return getHippoHtml("hstvalidation:body");    
    }
    
    public Calendar getDate() {
        return getProperty("hstvalidation:date");
    }

    /**
     * Get the imageset of the newspage
     *
     * @return the imageset of the newspage
     */
    public HippoGalleryImageSetBean getImage() {
        return getLinkedBean("hstvalidation:image", HippoGalleryImageSetBean.class);
    }
    
}
