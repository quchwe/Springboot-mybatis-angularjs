package com.quchwe.util.editor;


import com.quchwe.util.DateHelper;

import java.beans.PropertyEditorSupport;

/**
 * @author zhangxd
 */
public class DateEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) {
        setValue(DateHelper.parseDate(text));
    }

}
