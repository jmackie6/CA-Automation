package org.lds.cm.content.automation.model;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class MappedErrors {

    private Map<String, Integer> errorMap;

    public Map<String, Integer> getErrorMap() {
        Map<String, Integer> tempMap = new HashMap<>();
        errorMap.forEach(tempMap::put);
        return tempMap;
    }

    public void setErrorMap(Map<String, Integer> errorMap) {
        this.errorMap = errorMap;
    }

    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner("\n", "MappedErrors{", "}");
        errorMap.forEach((key, value) -> sj.add("Error Type -> " + key + " \n Value -> " + value));
        return sj.toString();
    }
}
