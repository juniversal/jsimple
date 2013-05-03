package jsimple.util;

import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Created with IntelliJ IDEA.
 *
 * @author Bret Johnson
 * @since 1/4/13 2:15 AM
 */
public class Utils {
    /**
     * Return an array of bytes containing as elements this method's parameters.  Parameters are ints but they should be
     * in a legal range for a byte (technically -128 to 127, but treating the range like 0-255 also works as expected).
     * <p/>
     * This method exists mostly to avoid casting problems populating a byte[] literals inline--casting issues caused by
     * integer literals always being ints not bytes in Java (and thus requiring a byte cast--compile issue #1) and
     * translated C# always treating hex literals as unsigned and not allowing ones > 127 to be cast to (signed) bytes
     * (compile issue #2). Anyway, that's all avoided just by passing in ints here.
     *
     * @param bytes 0 or more bytes to convert into an array of bytes
     * @return array of bytes
     */
    public static byte[] byteArrayFromBytes(int... bytes) {
        int length = bytes.length;

        byte[] byteArray = new byte[length];
        for (int i = 0; i < length; i++) {
            byteArray[i] = (byte) bytes[i];
        }

        return byteArray;
    }

    /*
    Copyright 2007, Chad Okere (ceothrow1 at gmail dotcom)
    OMG NO WARRENTY EXPRESSED OR IMPLIED!!!1
    */
     public static java.util.Date parseRFC3339Date(String datestring) throws java.text.ParseException, IndexOutOfBoundsException{
            Date d = new Date();

            //if there is no time zone, we don't need to do any special parsing.
            if(datestring.endsWith("Z")){
                try{
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");//spec for RFC3339
                    d = s.parse(datestring);
                }
                catch(java.text.ParseException pe){//try again with optional decimals
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");//spec for RFC3339 (with fractional seconds)
                    s.setLenient(true);
                    d = s.parse(datestring);
                }
                return d;
            }

            //step one, split off the timezone.
            String firstpart = datestring.substring(0,datestring.lastIndexOf('-'));
            String secondpart = datestring.substring(datestring.lastIndexOf('-'));

            //step two, remove the colon from the timezone offset
            secondpart = secondpart.substring(0,secondpart.indexOf(':')) + secondpart.substring(secondpart.indexOf(':')+1);
            datestring  = firstpart + secondpart;
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");//spec for RFC3339
            try{
                d = s.parse(datestring);
            }
            catch(java.text.ParseException pe){//try again with optional decimals
                s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");//spec for RFC3339 (with fractional seconds)
                s.setLenient(true);
                d = s.parse(datestring);
            }
            return d;
     }

    public static long parseRFC3339Long(String datestring) throws java.text.ParseException, IndexOutOfBoundsException{
        Date d = new Date();

        //if there is no time zone, we don't need to do any special parsing.
        if(datestring.endsWith("Z")){
            try{
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");//spec for RFC3339
                d = s.parse(datestring);
            }
            catch(java.text.ParseException pe){//try again with optional decimals
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");//spec for RFC3339 (with fractional seconds)
                s.setLenient(true);
                d = s.parse(datestring);
            }
            return d.getTime();
        }

        //step one, split off the timezone.
        String firstpart = datestring.substring(0,datestring.lastIndexOf('-'));
        String secondpart = datestring.substring(datestring.lastIndexOf('-'));

        //step two, remove the colon from the timezone offset
        secondpart = secondpart.substring(0,secondpart.indexOf(':')) + secondpart.substring(secondpart.indexOf(':')+1);
        datestring  = firstpart + secondpart;
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");//spec for RFC3339
        try{
            d = s.parse(datestring);
        }
        catch(java.text.ParseException pe){//try again with optional decimals
            s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");//spec for RFC3339 (with fractional seconds)
            s.setLenient(true);
            d = s.parse(datestring);
        }
        return d.getTime();
    }

    public static String makeRFC3339String(long timeValue){
        Date d = new Date(timeValue);

        String rfc3339TimeStamp = new SimpleDateFormat("yyyy-MM-dd'T'h:m:ssZ").format(new Date());

        return rfc3339TimeStamp;
    }
 }
