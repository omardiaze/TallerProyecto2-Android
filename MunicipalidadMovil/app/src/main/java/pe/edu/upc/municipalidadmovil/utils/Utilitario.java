package pe.edu.upc.municipalidadmovil.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Brayan on 28/01/2018.
 */

public class Utilitario {
 
    public static boolean isInteger(String pNumber)
    {
        try {
            double vDecimal = Double.parseDouble(pNumber);
            if(vDecimal == (int) vDecimal){
                return true;
            }else{
                return false;
            }
        }catch (NumberFormatException ex){
            return false;
        }
    }

    public static boolean isNumeric(String pNumber)
    {
        try {
            double number = Double.parseDouble(pNumber);
            return true;
        }catch (NumberFormatException ex){
            return false;
        }
    }

    public static boolean isEmail(String pEmail) {
        //^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,6}$
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(pEmail);
        return matcher.find();
    }

    public static double roundDecimals(double d, int decimals)
    {
        String pattern = "#0.";
        for(int i=0; i<decimals; i++){
            pattern = pattern + "#";
        }
        DecimalFormat twoDForm = new DecimalFormat(pattern);
        return Double.valueOf(twoDForm.format(d));
    }


    public static String inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder aswer  = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader rd = new BufferedReader(isr);

        try{
            while ((rLine = rd.readLine())!=null){
                aswer.append(rLine);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return  aswer.toString();
    }

}
