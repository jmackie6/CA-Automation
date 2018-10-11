package org.lds.cm.content.automation.enums;

public enum TransformType {
	WEBML("WEBML"), 
	LDSXML("LDSXML"),
	HTML("HTML"),
	DOCX("DOCX");

		
		 private String text;

		 TransformType(String text) {
		    this.text = text;
		  }

		  public String getText() {
		    return this.text;
		  }

		  public static TransformType fromString(String text) {
		    if (text != null) {
		      for (TransformType b : TransformType.values()) {
		        if (text.equalsIgnoreCase(b.text)) {
		          return b;
		        }
		      }
		    }
		    throw new IllegalArgumentException("No constant with text " + text + " found");
		    //return null;
		  }
	}
