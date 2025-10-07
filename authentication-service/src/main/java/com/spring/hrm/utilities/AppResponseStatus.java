package com.spring.hrm.utilities;

public enum AppResponseStatus {
    APP_200("APP-200"),
    APP_201("APP-201"),
    APP_204("APP-204"),
    APP_500("APP-500"),
    APP_400("APP-400"),
    APP_401("APP-401"),

    APP_400_CHECKIN("APP-400-CHECKIN"),
    APP_400_CHECKOUT("APP-400-CHECKOUT"),
    APP_400_FIELD("APP-400-FIELD"),

    APP_404_STAFF("APP-404-STAFF"),
    APP_404_DEPT("APP-404-DEPT"),
    APP_400_DEPT("APP-400-DEPT"),
    APP_404_STAFF_LIST("APP-404-STAFF-LIST"),
    APP_404_DEPT_LIST("APP-404-DEPT-LIST"),
    APP_404_URL("APP-404-URL"),

    APP_404_ATTENDANCE("APP-404-ATTENDANCE"),
    APP_404_ACCOUNT("APP-404-ACCOUNT"),

    APP_404_JOB_POSITION("APP-404-JOB-POSITION");

    String code;
    
    AppResponseStatus(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
