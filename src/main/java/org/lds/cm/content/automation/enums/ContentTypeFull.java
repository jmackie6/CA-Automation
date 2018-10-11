package org.lds.cm.content.automation.enums;


public enum ContentTypeFull {
	SCRIPTURES("SCRIPTURES"), 
	MAGAZINE("MAGAZINE"), 
	MANUAL("MANUAL"), 
	CONFERENCE("CONFERENCE"), 
	BROADCAST("BROADCAST"),
	TESTAMENT("TESTAMENT"),
	MASTHEAD("MASTHEAD"),
	ARTICLE("ARTICLE"),
	BOOK("BOOK"),
	SECTION("SECTION"),
	CHAPTER("CHAPTER"),
	FRONT_COVER("FRONT-COVER"),
	SPINE("SPINE"),
	BACK_COVER("BACK-COVER"),
	INSIDE_FRONT_COVER("INSIDE-FRONT-COVER"),
	INSIDE_BACK_COVER("INSIDE-BACK-COVER"),
	FIGURE("FIGURE"),
	TOPIC("TOPIC"),
	GENERAL_CONFERENCE("GENERAL-CONFERENCE"),
	GENERAL_CONFERENCE_SESSION("GENERAL-CONFERENCE-SESSION"),
	GENERAL_CONFERENCE_TALK("GENERAL-CONFERENCE-TALK");
	
	 private String text;

	 ContentTypeFull(String text) {
	    this.text = text;
	  }

	  public String getText() {
	    return this.text;
	  }

	  public static ContentTypeFull fromString(String text) {
	    if (text != null) {
	      for (ContentTypeFull b : ContentTypeFull.values()) {
	        if (text.equalsIgnoreCase(b.text)) {
	          return b;
	        }
	      }
	    }
	    throw new IllegalArgumentException("No constant with text " + text + " found");
	    //return null;
	  }
}

