package org.lds.cm.content.automation.model.APIModels;

import org.lds.cm.content.automation.enums.ControllerType;
import org.lds.cm.content.automation.util.JDBCUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class API
{
    private String name = "";
    private String errorMessage = "";
    private ControllerType controller;
    private static final String apiClassQuery = "select * from api_rules where name=?";
    private String uri = "";

    public API (String name, ControllerType controller, String uri) throws SQLException {
        this.uri = uri;
        this.name = name;
        setErrorMessage();
        this.controller = controller;
    }

    // Create getters and setters for private variables
    public String getUri(){
        return uri;
    }
    public void setName(String n){ name = n; }
    public String getName(){ return name; }

    public void setErrorMessage() throws SQLException
    {
        ArrayList<String>  fillIn = new ArrayList<>();
        fillIn.add(name);
        ResultSet rs = JDBCUtils.getResultSet(apiClassQuery, fillIn);
        if (rs.next())
            errorMessage = rs.getString("custom_message");
        rs.close();
    }
    public String getErrorMessage(){ return errorMessage; }

    public void setController(ControllerType c) { controller = c; }
    public ControllerType getController(){ return controller; }

    public void setApi_rules_id() throws SQLException {


    }

}