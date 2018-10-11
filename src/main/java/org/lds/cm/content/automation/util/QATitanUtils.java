package org.lds.cm.content.automation.util;

import org.json.simple.JSONObject;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.w3c.dom.Document;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class QATitanUtils {


    public boolean deleteTitanAsset(String documentID) throws Exception{
        String sqlQuery = "select cp.dam_id, d.data_aid from document d inner join content_publish cp on d.document_id = cp.document_id where d.file_id like '%" + documentID +"%' ";

        final ResultSet deleteResultSet = JDBCUtils.getResultSet(sqlQuery);

//        String urlString = "http://assetcrawler-uat.ldschurch.org/api/v2/asset?sourceType=%s&sourceId=%s";
        final Map<String, String> requestParams = new HashMap<>();


        //damIds.getString("DAM_ID"))
        while(deleteResultSet.next()){
            String currentDamID = deleteResultSet.getString("DATA_AID");
            String urlStringFormatted = Constants.epTitanAssetSearch + currentDamID;
            requestParams.put("sourceType", deleteResultSet.getString("DAM_ID"));
            requestParams.put("sourceId", currentDamID);

            if(!existsInTitan(currentDamID)){
                System.out.println("Exists");
                final Document currentDoc = NetUtils.httpDeleteRequestWithParams(urlStringFormatted, requestParams);
                boolean isDeleted = !existsInTitan(currentDamID);
                if(isDeleted) {
                    return true;
                }
            }else{
//                System.out.println("Doesn't Exist");
                return false;
            }
        }

        return false;
    }




    public boolean existsInTitan(String assetID) throws Exception {
        String urlString = Constants.epTitanContentAPI+ assetID;
        final JSONObject returnObj = NetUtils.httpGetJSONWithParams(urlString, new HashMap<String, String>());

        if(returnObj.get("status").equals("400")) {
            JSONObject tempObj = (JSONObject) returnObj.get("result");
            return tempObj.get("isDeleted").equals("false");
        }else{
            return false;
        }
    }


}
