package com.thanhan.epos;

import java.util.HashMap;
import java.util.Map;

public class ProductType {
    private int id;
    private String typeName;

    public ProductType() {
    }

    public ProductType(int id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("typeName", typeName);

        return result;
    }
}
