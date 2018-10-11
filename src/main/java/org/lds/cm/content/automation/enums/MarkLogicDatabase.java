package org.lds.cm.content.automation.enums;

public enum MarkLogicDatabase {
	PUBLISHED ("/published/content-automation/content"), 
	URI_MAPPING ("/preview/content-automation/uri-mapping/");

	private final String contentRoot;
	private final String webmlRoot = "/webml/ldsorg content";
	
	private MarkLogicDatabase (String contentRoot) {
		this.contentRoot = contentRoot;
	}
	
	public String getContentRoot () {
		return contentRoot;
	}
	
	public String getWebmlRoot () {
		return webmlRoot;
	}
}

