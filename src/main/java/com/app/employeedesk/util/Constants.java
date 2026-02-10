package com.app.employeedesk.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class Constants {

    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;
    public static final String SIGNING_KEY = "EbrainTech2016";
    public static final String TOKEN_PREFIX = "BslogiKey ";
    public static final String HEADER_STRING = "Authorization";
}
