package org.example;
import java.util.regex.*;
public class DataValidation {
    public static boolean dataTypeValidation(String value, String type){
        if(type.equals("Numeric"))
            return Pattern.matches("[\\d]+",value);
        else
            return Pattern.matches("[\\s]+",value);
    }

    public static boolean dataLength(String value, int length){
        String str="[\\w]{1,"+String.valueOf(length)+"}";
        return Pattern.matches(str,value);
    }

    public static boolean specialChar(String str1,String str2){
        String s1 = "[^"+str2+"]+";
        return Pattern.matches(s1,str1);
    }

    public static boolean domainValue(String value,String arr[]){
        for(int i=0;i<arr.length;i++){
            if(Pattern.matches(arr[i],value)) return true;
        }
        return false;
    }

    public static boolean formatValidation(String date){
        return Pattern.matches("[\\d]{4}-(02-([01][\\d])?(2[\\d])?)?((0[13456789])?(1[012])?-([012][\\d])?(3[01])?)?", date);
    }


    public static boolean validateEmail(String email){
        return Pattern.matches("[\\w\\S]+@[\\w]+.[A-Za-z]{1,5}.?[A-Za-z]{1,5}", email);
    }
}