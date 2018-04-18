package com.pentateuch.watersupply.utils;

import java.util.UUID;

public class Helper {
    public String getTransactionID(){
       return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase().substring(0,10);
    }
}
