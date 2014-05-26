package com.capgemini.ktestmachine.component.templateparser.impl.parts;

public class TagEnd implements Part, Close {
    public static final String TAG = "END";
    
    public String getTagName() {
        return TAG;
    }
}
