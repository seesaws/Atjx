package com.atjx.mobile.model;




import lombok.Data;

import java.util.Map;

@Data
public class ResultInfo {
    private int Type;
    private Map<String,String> Data;
    private String Message;

}
