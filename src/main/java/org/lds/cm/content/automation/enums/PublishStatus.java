package org.lds.cm.content.automation.enums;

public enum PublishStatus {
    IN_PROCESS(1)
    ,DOC_MAP_FAILURE(2)
    , DAM_FAILURE(3)
    , COMPLETE(4);

    private final int enumID;

    private PublishStatus(int value) {this.enumID = value;}


    public int getEnumID(){
        return enumID;
    }

    public static PublishStatus fromInt(int newValue){
        for(PublishStatus e : values()){
            if(e.enumID == newValue){
                return e;
            }
        }
        return null;
    }

    public static PublishStatus fromString(String newString){
       switch(newString){
           case "IN_PROCESS":
               return PublishStatus.IN_PROCESS;
           case "DOC_MAP_FAILURE":
               return PublishStatus.DOC_MAP_FAILURE;
           case "DAM_FAILURE":
               return PublishStatus.DAM_FAILURE;
           case "COMPLETE":
               return PublishStatus.COMPLETE;
           default:
               return null;

       }
    }
}
