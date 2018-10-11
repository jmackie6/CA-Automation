package org.lds.cm.content.automation.model;

import org.lds.cm.content.automation.enums.SearchType;

import java.util.Iterator;
import java.util.Map;


//This class is used for passing information needed for search or many forms
//This will only accept a single string for each SearchType
public class SingleSearchObject
{
    private String uri = "";
    private String fileID = "";
    private String fileName = "";
    private String path="";
    private String contentGroup="";
    private String user="";
    private String language="";
    private String status="";
    private String other="";
    private String startDate="";
    private String endDate="";
    private String errorType="";

    public SingleSearchObject(Map<SearchType, String> input)
    {
        Iterator it = input.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry<SearchType, String> pair = (Map.Entry)it.next();

            switch(pair.getKey())
            {
                case uri: uri = pair.getValue();  break;
                case fileID: fileID = pair.getValue(); break;
                case fileName: fileName = pair.getValue(); break;
                case path: path = pair.getValue(); break;
                case contentGroup: contentGroup = pair.getValue(); break;
                case user: user = pair.getValue(); break;
                case language: language = pair.getValue(); break;
                case status: status = pair.getValue(); break;
                case other: other = pair.getValue(); break;
                case startDate: startDate = pair.getValue(); break;
                case endDate: endDate = pair.getValue(); break;
                case errorType: errorType = pair.getValue(); break;
            }
        }
    }

//all getter, do not have setters because we don't want data changing constantly
    public String getUri() {   return uri;  }
    public String getFileID() {   return fileID;  }
    public String getFileName() {    return fileName;  }
    public String getPath() {     return path;  }
    public String getContentGroup() {     return contentGroup;   }
    public String getUser() {     return user;   }
    public String getLanguage() {     return language;    }
    public String getStatus() {     return status;    }
    public String getOther() {     return other;    }
    public String getStartDate() {     return startDate;    }
    public String getEndDate() {     return endDate;    }
    public String getErrorType() {     return errorType;    }

}
