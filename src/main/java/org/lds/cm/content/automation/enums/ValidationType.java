package org.lds.cm.content.automation.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum ValidationType {
    CUSTOM_ASSERTION(1)
    , FILE(2)
    , LINK(3)
    , MEDIA(4)
    , SCHEMA_HTML5(5)
    , SCHEMA_LDSXML(6)
    , SCRIPTURE_REF(7)
    , UNICODE(8)
    , CROSS_REF(9);



    private int value;

    private ValidationType(int newInt){
        this.value = newInt;
    }

    public String getStringValue(int id){
        final Map<Integer, String> validationTypes = new HashMap<>();
        validationTypes.put(1, "CUSTOM_ASSERTION");
        validationTypes.put(2, "FILE");
        validationTypes.put(3, "LINKS");
        validationTypes.put(4, "MEDIA");
        validationTypes.put(5, "SCHEMA_HTML5");
        validationTypes.put(6, "SCHEMA_LDSXML");
        validationTypes.put(7, "SCRIPTURE_REF");
        validationTypes.put(8, "UNICODE");
        validationTypes.put(9, "CROSS_REF");


        return validationTypes.get(id);
    }

}
