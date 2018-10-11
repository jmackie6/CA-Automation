package org.lds.cm.content.automation.model;

import org.lds.cm.content.automation.enums.SearchType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


//This class is used for passing information needed for searches or most forms.
//This one can hold multiple lines of information

public class MultipleSearchObject
{
    private ArrayList<String> searchUrls;
    private ArrayList<String> contentGroups;
    private ArrayList<String> users;
    private ArrayList<String> languages;
    private String status="";
    private ArrayList<String> other;
    private String startDate="";
    private String endDate="";
    private ArrayList<String> errorTypes;

    public MultipleSearchObject(Map<SearchType, String> input)
    {
        searchUrls = new ArrayList<String>();
        contentGroups = new ArrayList<String>();
        users = new ArrayList<String>();
        languages = new ArrayList<String>();
        other = new ArrayList<>();
        errorTypes = new ArrayList<>();

        Iterator it = input.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry<SearchType, String> pair = (Map.Entry)it.next();

            switch(pair.getKey())
            {
                case uri:
                case fileID:
                case fileName:
                case path:
                    searchUrls.add(pair.getValue());
                    break;
                case contentGroup: contentGroups.add(pair.getValue()); break;
                case user: users.add(pair.getValue()); break;
                case language: languages.add(pair.getValue()); break;
                case status: status = pair.getValue(); break;
                case other: other.add(pair.getValue()); break;
                case startDate: startDate = pair.getValue(); break;
                case endDate: endDate = pair.getValue(); break;
                case errorType: errorTypes.add(pair.getValue()); break;
            }
        }
    }

    //all getter, do not have setters because we don't want data changing constantly
    public ArrayList<String> getSearchUrls() {    return searchUrls;    }
    public ArrayList<String> getContentGroups() {     return contentGroups;   }
    public ArrayList<String> getUsers() {     return users;    }
    public ArrayList<String> getLanguages() {     return languages;    }
    public String getStatus() {     return status;    }
    public ArrayList<String> getOther() {     return other;    }
    public String getStartDate() {     return startDate;    }
    public String getEndDate() {     return endDate;    }
    public ArrayList<String> getErrorTypes() {     return errorTypes;    }
}
