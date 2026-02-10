package com.app.employeedesk.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class DateUtil {

    static final String DATE_TIME_FORMAT_DB = "yyyy-MM-dd HH:mm:ss";
    static final String DATE_FORMAT_DB = "yyyy-MM-dd";
    static final String VIEW_DATE_FORMAT = "dd/MM/yyyy hh:mm aa";
    static final SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    static final DateFormat dateFormat = new SimpleDateFormat("d/MM/yyyy");
    static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static final DateTimeFormatter formatter12Hour = DateTimeFormatter.ofPattern("hh:mm a");
    static final DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm");
    static final DateFormat dbDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static final DateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static final DateFormat dateTimeFormatReceivingFormat = new SimpleDateFormat("dd/MM/yyyy, hh:mm:ss a");

    static final DateFormat dateTimeDisplayFormat = new SimpleDateFormat("dd/MM/yyyy, hh:mm a");

    static final DateFormat yearLastTwoDigitsDF = new SimpleDateFormat("yy"); // Just the year, with 2 digits

    public static Date getUTCdatetimeAsDate() {
        return stringDateToDate(getUTCdatetimeAsString());
    }

    public static String getUTCdatetimeAsString() {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT_DB);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());
        return utcTime;
    }

    public static Date stringDateToDate(String StrDate) {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DB);
        try {
            dateToReturn = (Date) dateFormat.parse(StrDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateToReturn;
    }

    public static String getViewDateFormat(String dateStr) {
        SimpleDateFormat dbFormat = new SimpleDateFormat(DATE_TIME_FORMAT_DB);
        Date dbDate = null;
        try {
            dbDate = dbFormat.parse(dateStr);
            SimpleDateFormat dateFormat = new SimpleDateFormat(VIEW_DATE_FORMAT);
            return dateFormat.format(dbDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseDate(String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    public static boolean isValidLocalDate(String date) {
        try {
            LocalDate.parse(date, dateFormatter);
            return true;
        } catch (DateTimeParseException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    public static String parseString(LocalDate date) {
        try {
            return date.format(dateFormatter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getViewDateFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(VIEW_DATE_FORMAT);
        return dateFormat.format(date);
    }

    public static Date getDateFromInputDate(String stDate) {
        Date dateToReturn = null;
        try {
            dateToReturn = inputDateFormat.parse(stDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateToReturn;
    }

    public static String getStringFormatFromDbDate(String dateStr) {
        String returnString = null;
        SimpleDateFormat dbFormat = new SimpleDateFormat(DATE_TIME_FORMAT_DB);
        Date dbDate = null;
        try {
            dbDate = dbFormat.parse(dateStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        returnString = inputDateFormat.format(dbDate);
        return returnString;
    }

    public static LocalDate parseLocalDate(String date) {
        try {
            return LocalDate.parse(date, dateFormatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static LocalTime parseLocalTime(String time){

        try{
            return LocalTime.parse(time, formatter12Hour);
        }
        catch (DateTimeParseException e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isValidateTime(String time){
        try {
            LocalTime.parse(time, formatter12Hour);
            return false;
        }
        catch (DateTimeParseException e){
            e.printStackTrace();
            return true;
        }
    }
    public static String parseStringTme(LocalTime time){
        try {
            return time.format(formatter12Hour);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static LocalDate todayTime() {
        return LocalDate.now();
    }

    public static String formatString(Date date) {
        return dateFormat.format(date);
    }

    public static String formatDateTimeToString(Date date) {
        return dateTimeDisplayFormat.format(date);
    }

    public static Date parseDateWithTime(String dateStr) {
        try {
            return dateTimeFormatReceivingFormat.parse(dateStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static String getFormatedDateFromDbDateTimeFormat(String dateStr) {
        String returnString = null;
        Date dbDate = null;
        try {
            dbDate = dbDateTimeFormat.parse(dateStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        returnString = dateTimeDisplayFormat.format(dbDate);
        return returnString;
    }

    public static String getFormatedDateFromDbDateFormat(String dateStr) {
        String returnString = null;
        Date dbDate = null;
        try {
            dbDate = dbDateFormat.parse(dateStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        returnString = dateFormat.format(dbDate);
        return returnString;
    }

    public static String convertToDbDateFormat(Date date) {
        return dbDateFormat.format(date);
    }

    public static LocalDate getNextDayOfWeekDate(Date date, DayOfWeek dayOfWeek) {
        LocalDate dt = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate destinationDate = dt.with(TemporalAdjusters.next(dayOfWeek));
        return destinationDate;
    }

    public static void mainFinal(String[] args) {

        LocalDate destinationDate = getNextDayOfWeekDate(new Date(), DayOfWeek.MONDAY);
        Date recentMonday = Date.from(destinationDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDate destinationDate1 = getNextDayOfWeekDate(recentMonday, DayOfWeek.MONDAY);

        System.out.println(destinationDate + "-------" + destinationDate1);
    }


    public static void mainFindNextDayFromDayOfWeek(String[] args) {
        LocalDate dt = LocalDate.now();
        System.out.println("\nNext Friday: " + dt.with(TemporalAdjusters.next(DayOfWeek.TUESDAY)));
        System.out.println("Previous Friday: " + dt.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY)) + "\n");
    }

    public static void mainold(String args[]) {
        LocalDate localDate = LocalDate.now();


        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        System.out.println(sdf.format(localDate));

    }

    public static String getETAOrETDFormat(Date date) {
        String parsedDate = dateFormat.format(date);
        parsedDate = parsedDate.substring(0, parsedDate.length() - 5);
        return parsedDate;
    }

    public static Integer noOfdaysBetweenTwoDateStr(String fromDateStr, String toDateStr) {
        Date fromDate = parseDate(fromDateStr);
        Date toDate = parseDate(toDateStr);
        return (int) ((fromDate.getTime() - toDate.getTime()) / (1000 * 60 *60 * 24));
    }

    public static Date getNewDateAddingDays(Date fromDate, Integer noOfDays) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(fromDate);
        calendar.set(Calendar.DAY_OF_MONTH, noOfDays);
        return calendar.getTime();
    }

    public static String getShortYear() {
        String formattedDate = yearLastTwoDigitsDF.format(Calendar.getInstance().getTime());
        return formattedDate;
    }

    public static boolean timeValidation(String time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        try {
            LocalTime date1= LocalTime.parse(time,formatter);
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return true;

        }

    }
    public static LocalTime stringToTime(String time, List<LocalTime> timeList){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        try{
            LocalTime date1= LocalTime.parse(time,formatter);
            return date1;

        }catch (Exception e){
            return null;
        }

    }
}
