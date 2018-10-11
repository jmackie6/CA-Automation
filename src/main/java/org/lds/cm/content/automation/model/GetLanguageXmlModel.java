package org.lds.cm.content.automation.model;

public class GetLanguageXmlModel
{
    private String name = "", iso1 = "", iso3 = "", code = "";

    public GetLanguageXmlModel(String n, String i1, String i3, String c)
    {
        name = n;
        iso1 = i1;
        iso3 = i3;
        code = c;

    }

    public String getName() {    return name;   }
    public String getIso1() {     return iso1;    }
    public String getIso3() {     return iso3;    }
    public String getCode() {     return code;    }


    public boolean comparison(GetLanguageXmlModel model)
    {
        if(model.getName().compareTo(name) != 0)
            return false;
        int x = Integer.parseInt(model.getCode());
        int y = Integer.parseInt(code);
        if(x != y)
            return false;

        String first = model.getIso1();
        if(model.getIso1() == null)
            first = "null";
        String second = iso1;
        if(iso1 == null)
            second = "null";
        if(first.compareTo(second) != 0)
            return false;

        first = model.getIso3();
        if(model.getIso3() == null)
            first = "null";
        second = iso3;
        if(iso3 == null)
            second = "null";
        if(first.compareTo(second) != 0)
            return false;

        if(first.compareTo(second) != 0)
            return false;

        return true;
    }

    public String toString()
    {
        return "Information - " + name + " " + iso1 + " " + iso3 + " " + code;
    }
}
