package org.lds.cm.content.automation.enums;

public enum ProcessStatus {
    FAILED(1)
    , SUCCESSFUL(2)
    , QUEUED(3)
    , RUNNING(4);

    private int enumID;

    public int getEnumID(){
        return enumID;
    }

    private ProcessStatus(int value){ this.enumID = value;}

    public static ProcessStatus getFromInt(int switchValue){
        switch(switchValue){
            case 1:
                return ProcessStatus.FAILED;
            case 2:
                return ProcessStatus.SUCCESSFUL;
            case 3:
                return ProcessStatus.QUEUED;
            case 4:
                return ProcessStatus.RUNNING;
            default:
                return null;
        }
    }

    public static ProcessStatus getFromString(String caseValue){
        switch(caseValue){
            case "FAILED":
                return ProcessStatus.FAILED;
            case "SUCCESSFUL":
                return ProcessStatus.SUCCESSFUL;
            case "QUEUED":
                return ProcessStatus.QUEUED;
            case "RUNNING":
                return ProcessStatus.RUNNING;
            default:
                return null;
        }
    }
}
