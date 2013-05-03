package jsimple.util;

import jsimple.unit.UnitTest;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: bkwon
 * Date: 5/3/13
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class UtilsTest extends UnitTest{

    @Test
    public void parseRFC3339DateTest() throws  Exception{
            System.out.println(Utils.parseRFC3339Date("2007-05-01T15:43:26-07:00"));
            System.out.println(Utils.parseRFC3339Long("2007-05-01T15:43:26-07:00"));
            assertEquals("Tue May 01 18:43:26 EDT 2007",Utils.parseRFC3339Date("2007-05-01T15:43:26-07:00").toString() );
            long expected = Long.parseLong("1178059406000");
            assertEquals(expected, Utils.parseRFC3339Long("2007-05-01T15:43:26-07:00"));

            System.out.println(Utils.parseRFC3339Date("2007-05-01T15:43:26.3-07:00"));
            System.out.println(Utils.parseRFC3339Long("2007-05-01T15:43:26.3-07:00"));

            expected =  Long.parseLong("1178059406003");
            assertEquals(expected, Utils.parseRFC3339Long("2007-05-01T15:43:26.3-07:00"));

            System.out.println(Utils.parseRFC3339Date("2007-05-01T15:43:26.3452-07:00"));
            System.out.println(Utils.parseRFC3339Long("2007-05-01T15:43:26.3452-07:00"));
            expected = Long.parseLong("1178059409452");
            assertEquals(expected,Utils.parseRFC3339Long("2007-05-01T15:43:26.3452-07:00") );

            System.out.println(Utils.parseRFC3339Date("2007-05-01T15:43:26.3452Z"));
            System.out.println(Utils.parseRFC3339Long("2007-05-01T15:43:26.3452Z"));

            System.out.println(Utils.parseRFC3339Date("2007-05-01T15:43:26.3Z"));
            System.out.println(Utils.parseRFC3339Long("2007-05-01T15:43:26.3Z"));

            System.out.println(Utils.parseRFC3339Date("2007-05-01T15:43:26Z"));
            System.out.println(Utils.parseRFC3339Long("2007-05-01T15:43:26Z"));
    }

    @Test
    public void makeRFC3339TimeStamp() throws Exception{
        Date date = new Date();
        System.out.println("date : " + date);

        System.out.println(Utils.makeRFC3339String(date.getTime()));

        String testDate = "2013-05-03 16:11:22 EDT";

        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        Date test = simpleDateFormat.parse(testDate);
        System.out.println("test date : " + test);
        System.out.println("test new format : " + Utils.makeRFC3339String(test.getTime()));


    }
}


