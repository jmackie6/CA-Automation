package org.lds.cm.content.automation.enums;

public enum TransformationContentTypeID {
	 	BROADCAST(402),
	    LIAHONA(3),
	    ENSIGN(2),
	    FRIEND(5),
	    NEW_ERA(4),
	    GENERAL_CONFERENCE(1),
	    YOUTH_CONTENT(601),
	    SCRIPTURES(301),
	    MANUAL(401),
	    TEST_DEFAULT(1101);

	    private final int enumID;

	    TransformationContentTypeID(int value) {
	    	this.enumID = value;
	    }
	    
	    public int enumID() {
	    	return enumID;
	    }

	    @Override
	    public String toString(){
	        return String.valueOf(enumID);
        }
}
