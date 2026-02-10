package com.app.employeedesk.enumeration;

import com.app.employeedesk.util.DateUtil;
import lombok.Getter;


@Getter
public enum LeaveDayType {
    FIRST_HALF("FirstHalf"),
    SECOND_HALF("SecondHalf"),
    FULL_DAY("FullDay"),
    ;
    private final String day;

    LeaveDayType(String day) {
        this.day = day;
    }

}
