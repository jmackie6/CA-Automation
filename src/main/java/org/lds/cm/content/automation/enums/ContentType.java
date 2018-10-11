
package org.lds.cm.content.automation.enums;


public enum ContentType implements PersistentEnum {
	SCRIPTURES(3), 
	MAGAZINE(1), 
	MANUAL(2), 
	CONFERENCE(4), 
	BROADCAST(5);
	
	private final int enumID;
	
	private ContentType(int value) {
		this.enumID = value;
	}
	
	@Override
	public int getEnumID() {
		return this.enumID;
	}
	
	public static ContentType fromInt(int value){
	    for(ContentType e : values()){
	        if( e.enumID == value){
	            return e;
	        }
	    }
	    return null;
	}
	

}

