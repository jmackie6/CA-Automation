package org.lds.cm.content.automation.enums;

public enum ErrorTypes {

    CUSTOM_ASSERTION(1)
    , FILE(2)
    , LINK(3)
    , MEDIA(4)
    , SCHEMA_HTML5(5)
    , SCHEMA_LDSXML(6)
    , SCRIPTURE_REF(7)
    , UNICODE(8)
    , CROSS_REF(9);

    private int enumID;

    ErrorTypes(int newValue){enumID = newValue;}

    public static ErrorTypes getTypeFromInt(int newEnumId){
        switch(newEnumId){
            case 1:
                return ErrorTypes.CUSTOM_ASSERTION;
            case 2:
                return ErrorTypes.FILE;
            case 3:
                return ErrorTypes.LINK;
            case 4:
                return ErrorTypes.MEDIA;
            case 5:
                return ErrorTypes.SCHEMA_HTML5;
            case 6:
                return ErrorTypes.SCHEMA_LDSXML;
            case 7:
                return ErrorTypes.SCRIPTURE_REF;
            case 8:
                return ErrorTypes.UNICODE;
            case 9:
                return ErrorTypes.CROSS_REF;
        }
        return null;
    }

    public static ErrorTypes getTypeFromString(String newStringValue){
        switch(newStringValue.toUpperCase()){
            case "CUSTOM_ASSERTION":
                return ErrorTypes.CUSTOM_ASSERTION;
            case "FILE":
                return ErrorTypes.FILE;
            case "LINK":
                return ErrorTypes.LINK;
            case "MEDIA":
                return ErrorTypes.MEDIA;
            case "SCHEMA_HTML5":
                return ErrorTypes.SCHEMA_HTML5;
            case "SCHEMA_LDSXML":
                return ErrorTypes.SCHEMA_LDSXML;
            case "SCRIPTURE_REF":
                return ErrorTypes.SCRIPTURE_REF;
            case "UNICODE":
                return ErrorTypes.UNICODE;
            case "CROSS_REF":
                return ErrorTypes.CROSS_REF;
            default:
                return null;
        }
    }


}
