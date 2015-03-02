/*
 * Copyright (c) 2012-2015 Microsoft Mobile.  All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * This file is based on or incorporates material from Joda-Time
 * http://www.joda.org/joda-time (collectively, "Third Party Code"). Microsoft
 * Mobile is not the original author of the Third Party Code. The original
 * copyright notice and the license, under which Microsoft Mobile received such
 * Third Party Code, are set forth below.
 *
 *
 * Copyright 2001-2013 Stephen Colebourne
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jsimple.util;

/**
 * This class represents a date/time, allowing the normal JSimple representation of time in millis (milliseconds since
 * Jan. 1, 1970) to be mapped to and from month/day/year/day of week.
 * <p/>
 * This class is based on the Joda DateTime class, though significantly simplified.  In particular, only the ISO
 * calendar (the "normal" one, used by most all the business world) is supported.  Most Joda utility methods are
 * removed, leaving the basics here--the field accessors and, importantly, the ability to convert to/from milllis and
 * RFC 3339 text representation.
 * <p/>
 * This class, as in Joda, is immutable.  It uses Joda names and Joda/ISO conventions (month & day are 1-based and day
 * of week starts with Monday = 1.
 *
 * @author Bret Johnson
 * @author Alexey Frunze (http://stackoverflow.com/questions/11188621/how-can-i-convert-seconds-since-the-epoch-to-hours-minutes-seconds-in-java/11197532#11197532)
 * @author Stephen Colebourne (original Joda)
 * @author Kandarp Shah (original Joda)
 * @author Brian S O'Neill  (original Joda)
 * @since 1.0
 */
public final class DateTime {
    long millis;             // Milliseconds since 1970-01-01T00:00:00Z

    int year;
    int dayOfYear;           // [1,366]
    int hourOfDay;           // [0,23]
    int minuteOfHour;        // [0,59]
    int secondOfMinute;      // [0,59]
    int millisOfSecond;      // [0,999]

    int monthOfYear;         // [1,12]
    int dayOfMonth;          // [1,31]

    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int MILLISECONDS_PER_MINUTE = 60000;
    public static final int MILLISECONDS_PER_HOUR = 3600000;
    public static final int MILLISECONDS_PER_DAY = 86400000;

    // 1970 - 1601 = 369 = 3*100 + 17*4 + 1 years (incl. 89 leap days) =
    // (3*100*(365+24/100) + 17*4*(365+1/4) + 1*365)*24*3600*1000 milliseconds
    private static final long MILLIS_FROM_1601_TO_1970 = 11644473600000L;

    private static final long SECONDS_PER_QUADRICENNIAL = 12622780800L;   // 400*365.2425*24*3600
    private static final long SECONDS_PER_CENTENNIAL = 3155673600L;       // 100*(365+24/100)*24*3600
    private static final long SECONDS_PER_QUADRENNIAL = 126230400;        // 4*(365+1/4)*24*3600
    private static final int SECONDS_PER_ANNUAL = 31536000;               // 365*24*3600
    private static final int SECONDS_PER_DAY = 86400;                     // 24*3600
    private static final int SECONDS_PER_HOUR = 3600;                     // 60*60s

    private static int[][] daysSinceJan1st = {
            {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365}, // 365 days, non-leap
            {0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335, 366}  // 366 days, leap
    };

    // This, somewhat arbitrary, choice for a null date is 1653-02-10 06:13:20.001
    public static final long NULL_DATE = -9999999999999L;

    //-----------------------------------------------------------------------

    /**
     * Obtains a {@code DateTime} set to the current system millisecond time using <code>ISOChronology</code> in the
     * default time zone.
     *
     * @return the current date-time, not null
     * @since 2.0
     */
    public static DateTime now() {
        return new DateTime(SystemUtils.getCurrentTimeMillis());
    }

    /**
     * Constructs an instance set to the milliseconds from 1970-01-01T00:00:00Z using <code>ISOChronology</code> in the
     * default time zone.
     *
     * @param millis the milliseconds from 1970-01-01T00:00:00Z
     */
    public DateTime(long millis) {
        this.millis = millis;

        // This code was takes from:
        // http://stackoverflow.com/questions/11188621/how-can-i-convert-seconds-since-the-epoch-to-hours-minutes-seconds-in-java/11197532#11197532

        /*
          400 years:

          1st hundred, starting immediately after a leap year that's a multiple of 400:
          n n n l  \
          n n n l   } 24 times
          ...      /
          n n n l /
          n n n n

          2nd hundred:
          n n n l  \
          n n n l   } 24 times
          ...      /
          n n n l /
          n n n n

          3rd hundred:
          n n n l  \
          n n n l   } 24 times
          ...      /
          n n n l /
          n n n n

          4th hundred:
          n n n l  \
          n n n l   } 24 times
          ...      /
          n n n l /
          n n n L <- 97'th leap year every 400 years
        */

        // Re-bias from 1970 to 1601:
        // 1970 - 1601 = 369 = 3*100 + 17*4 + 1 years (incl. 89 leap days) =
        // (3*100*(365+24/100) + 17*4*(365+1/4) + 1*365)*24*3600*1000 milliseconds
        long rebiasedMills = millis + MILLIS_FROM_1601_TO_1970;

        millisOfSecond = (int) (rebiasedMills % 1000);
        long remainingSeconds = rebiasedMills / 1000;

        // Remove multiples of 400 years (incl. 97 leap days)
        int quadricentennials = (int) (remainingSeconds / SECONDS_PER_QUADRICENNIAL);
        remainingSeconds %= SECONDS_PER_QUADRICENNIAL;

        // Remove multiples of 100 years (incl. 24 leap days), can't be more than 3
        // (because multiples of 4*100=400 years (incl. leap days) have been removed)
        int centennials = (int) (remainingSeconds / SECONDS_PER_CENTENNIAL);
        if (centennials > 3)
            centennials = 3;
        remainingSeconds -= centennials * SECONDS_PER_CENTENNIAL;

        // Remove multiples of 4 years (incl. 1 leap day), can't be more than 24
        // (because multiples of 25*4=100 years (incl. leap days) have been removed)
        int quadrennials = (int) (remainingSeconds / SECONDS_PER_QUADRENNIAL);
        if (quadrennials > 24)
            quadrennials = 24;
        remainingSeconds -= quadrennials * SECONDS_PER_QUADRENNIAL;

        // Remove multiples of years (incl. 0 leap days), can't be more than 3
        // (because multiples of 4 years (incl. leap days) have been removed)
        int annuals = (int) (remainingSeconds / SECONDS_PER_ANNUAL);
        if (annuals > 3)
            annuals = 3;
        remainingSeconds -= annuals * SECONDS_PER_ANNUAL;

        // Calculate the year and determine if it's a leap year
        year = 1601 + quadricentennials * 400 + centennials * 100 + quadrennials * 4 + annuals;

        // Calculate the day of the year and the time
        dayOfYear = (int) (remainingSeconds / SECONDS_PER_DAY) + 1;
        remainingSeconds %= SECONDS_PER_DAY;
        hourOfDay = (int) (remainingSeconds / SECONDS_PER_HOUR);
        remainingSeconds %= SECONDS_PER_HOUR;
        minuteOfHour = (int) (remainingSeconds / 60);
        remainingSeconds %= 60;
        secondOfMinute = (int) remainingSeconds;

        // Calculate the month
        int leapIndex = isLeapYear(year) ? 1 : 0;
        int zeroBasedDayOfYear = dayOfYear - 1;
        for (monthOfYear = 1; monthOfYear < 13; monthOfYear++) {
            if (zeroBasedDayOfYear < daysSinceJan1st[leapIndex][monthOfYear]) {
                dayOfMonth = dayOfYear - daysSinceJan1st[leapIndex][monthOfYear - 1];
                break;
            }
        }
    }

    /**
     * Constructs an instance from datetime field values using <code>ISOChronology</code> in the default time zone.
     *
     * @param year         the year
     * @param monthOfYear  the month of the year, from 1 to 12
     * @param dayOfMonth   the day of the month, from 1 to 31
     * @param hourOfDay    the hour of the day, from 0 to 23
     * @param minuteOfHour the minute of the hour, from 0 to 59
     * @since 2.0
     */
    public DateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour) {
        this(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, 0, 0);
    }

    //-----------------------------------------------------------------------

    /**
     * Constructs an instance from datetime field values using <code>ISOChronology</code> in the default time zone.
     *
     * @param year           the year
     * @param monthOfYear    the month of the year, from 1 to 12
     * @param dayOfMonth     the day of the month, from 1 to 31
     * @param hourOfDay      the hour of the day, from 0 to 23
     * @param minuteOfHour   the minute of the hour, from 0 to 59
     * @param secondOfMinute the second of the minute, from 0 to 59
     * @since 2.0
     */
    public DateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute) {
        this(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, 0);
    }

    //-----------------------------------------------------------------------

    /**
     * Constructs an instance from datetime field values using <code>ISOChronology</code> in the default time zone.
     *
     * @param year           the year
     * @param monthOfYear    the month of the year, from 1 to 12
     * @param dayOfMonth     the day of the month, from 1 to 31
     * @param hourOfDay      the hour of the day, from 0 to 23
     * @param minuteOfHour   the minute of the hour, from 0 to 59
     * @param secondOfMinute the second of the minute, from 0 to 59
     * @param millisOfSecond the millisecond of the second, from 0 to 999
     */
    public DateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute,
                    int millisOfSecond) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
        this.hourOfDay = hourOfDay;
        this.minuteOfHour = minuteOfHour;
        this.secondOfMinute = secondOfMinute;
        this.millisOfSecond = millisOfSecond;

        int remainingYears = year - 1601;

        // Calculate total seconds since Jan 1, 1601
        long totalSeconds = (remainingYears / 400) * SECONDS_PER_QUADRICENNIAL;
        remainingYears %= 400;

        totalSeconds += (remainingYears / 100) * SECONDS_PER_CENTENNIAL;
        remainingYears %= 100;

        totalSeconds += (remainingYears / 4) * SECONDS_PER_QUADRENNIAL;
        remainingYears %= 4;

        totalSeconds += remainingYears * SECONDS_PER_ANNUAL;

        // Calculate the day of year
        int leapIndex = isLeapYear(year) ? 1 : 0;
        dayOfYear = daysSinceJan1st[leapIndex][monthOfYear - 1] + dayOfMonth;

        totalSeconds += secondOfMinute + minuteOfHour * 60 + hourOfDay * SECONDS_PER_HOUR +
                (dayOfYear - 1) * SECONDS_PER_DAY;

        // Bias back so relative to 1970
        millis = totalSeconds * 1000L - MILLIS_FROM_1601_TO_1970 + millisOfSecond;
    }

    @Override public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;

        DateTime otherDateTime = (DateTime) other;
        if (millis != otherDateTime.millis)
            return false;

        return true;
    }

    @Override public int hashCode() {
        return (int) (millis ^ (millis >>> 32));
    }

    @Override public String toString() {
        if (toMillis() == NULL_DATE)
            return "NULL_DATE";

        // To make the string a little more readable, we change the date/time delimiter to a space & get rid of the "Z"
        // suffix for UTC
        return toRFC3339String().replace("T", " ").replace("Z", "");
    }

    public static boolean isLeapYear(int year) {
        return (year % 4) == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    public long toMillis() {
        return millis;
    }

    public int getYear() {
        return year;
    }

    /**
     * Get the month of year field value.
     *
     * @return the month of year
     */
    public int getMonthOfYear() {
        return monthOfYear;
    }

    /**
     * Get the day of year field value.
     *
     * @return the day of year
     */
    public int getDayOfYear() {
        return dayOfYear;
    }

    /**
     * Get the day of month field value.
     *
     * @return the day of month
     */
    public int getDayOfMonth() {
        return dayOfMonth;
    }

    /**
     * Get the day of week.  The valid range is 1-7, for Monday-Sunday, per ISO standard.
     *
     * @return the day of week
     */
    public int getDayOfWeek() {
        long rebiasedMills = millis + MILLIS_FROM_1601_TO_1970;
        int dayOfWeek = (int) (rebiasedMills / 1000 / 86400 + 1) % 7;    // Sunday = 0
        if (dayOfWeek == 0)           // Switch from Sunday = 0 to ISO standard (Sunday = 7)
            dayOfWeek = 7;
        return dayOfWeek;
    }

    /**
     * Get the hour of day field value.
     *
     * @return the hour of day
     */
    public int getHourOfDay() {
        return hourOfDay;
    }

    /**
     * Get the minute of hour field value.
     *
     * @return the minute of hour
     */
    public int getMinuteOfHour() {
        return minuteOfHour;
    }

    /**
     * Get the second of minute field value.
     *
     * @return the second of minute
     */
    public int getSecondOfMinute() {
        return secondOfMinute;
    }

    /**
     * Get the millis of second field value.
     *
     * @return the millis of second
     */
    public int getMillisOfSecond() {
        return millisOfSecond;
    }

    /**
     * Returns a copy of this datetime plus the specified number of millis.
     *
     * @param millis the amount of millis to add, may be negative
     * @return the new datetime plus the increased millis
     */
    public DateTime plusMillis(long millis) {
        if (millis == 0)
            return this;
        else return new DateTime(this.millis + millis);
    }

    public DateTime plusSeconds(long seconds) {
        return plusMillis(seconds * 1000);
    }

    public DateTime plusMinutes(long minutes) {
        return plusMillis(minutes * 60 * 1000);
    }

    public DateTime plusHours(long hours) {
        return plusMillis(hours * 60 * 60 * 1000);
    }

    /**
     * Given the RFC3339 date/time string, return the time in millis (milliseconds since Jan 1, 1970, UTC).  If the RFC
     * 3339 string isn't in a valid format, an InvalidFormatException is thrown.
     *
     * @param rfc3339DateTime date/time string, in RFC3339 format
     * @return time in millis
     */
    public static long rfc3339ToMillis(String rfc3339DateTime) {
        return parseRFC3339(rfc3339DateTime).toMillis();
    }

    public static DateTime parseRFC3339(String rfc3339DateTime) {
        StringIterator stringIterator = new StringIterator(rfc3339DateTime);

        int year = parseFixedDigitsInt(stringIterator, 4);
        stringIterator.checkAndAdvance('-');
        int month = parseFixedDigitsInt(stringIterator, 2);
        stringIterator.checkAndAdvance('-');
        int day = parseFixedDigitsInt(stringIterator, 2);

        if (month < 1 || month > 12)
            throw new InvalidFormatException("Month {} is outside expected range in {}", month, rfc3339DateTime);
        if (day < 1 || day > 31)
            throw new InvalidFormatException("Day {} is outside expected range in {}", day, rfc3339DateTime);

        stringIterator.checkAndAdvance('T');

        int hour = parseFixedDigitsInt(stringIterator, 2);
        stringIterator.checkAndAdvance(':');
        int minute = parseFixedDigitsInt(stringIterator, 2);
        stringIterator.checkAndAdvance(':');
        int second = parseFixedDigitsInt(stringIterator, 2);

        if (hour < 0 || hour > 23)
            throw new InvalidFormatException("Hour {} is outside expected range in {}", hour, rfc3339DateTime);
        if (minute < 0 || minute > 59)
            throw new InvalidFormatException("Minute {} is outside expected range in {}", minute, rfc3339DateTime);
        if (second < 0 || second > 60)   // This is 60 instead of 59 to allow for leap second (per spec)
            throw new InvalidFormatException("Second {} is outside expected range in {}", second, rfc3339DateTime);

        double fractionalSeconds = 0.0;
        if (stringIterator.curr() == '.') {
            stringIterator.advance();

            double divisor = 0.1;
            while (stringIterator.isDigit()) {
                char c = stringIterator.read();
                fractionalSeconds = fractionalSeconds + (c - '0') * divisor;
                divisor /= 10.0;
            }
        }
        int millisecond = (int) (1000.0 * fractionalSeconds + 0.5);

        DateTime dateTime = new DateTime(year, month, day, hour, minute, second, millisecond);

        // If a time zone offset is specified, map the time back to UTC
        char timeZoneOffsetChar = stringIterator.read();
        if (timeZoneOffsetChar == 'Z') {
            // Do nothing--already UTC
        } else if (timeZoneOffsetChar == '+' || timeZoneOffsetChar == '-') {
            int timeZoneOffsetMinutes = parseFixedDigitsInt(stringIterator, 2) * 60;

            // According to the RFC 3339 and ISO 8601 specs, the colon is mandatory.  However, the Microsoft SkyDrive
            // API uses +0000 to mean UTC so we're more lenient here, allowing the colon to be optional.
            if (stringIterator.curr() == ':')
                stringIterator.advance();

            timeZoneOffsetMinutes += parseFixedDigitsInt(stringIterator, 2);

            // Apply the reverse of the time zone offset, to map time back to UTC
            int multiplier = timeZoneOffsetChar == '+' ? -1 : 1;
            dateTime = dateTime.plusMillis(multiplier * timeZoneOffsetMinutes * 60 * 1000);
        } else throw new InvalidFormatException("Expected time zone offset (Z, +, or -); encountered '{}'",
                timeZoneOffsetChar);

        return dateTime;
    }

    private static int parseFixedDigitsInt(StringIterator stringIterator, int numberOfDigits) {
        int value = 0;

        for (int digitCount = 0; digitCount < numberOfDigits; ++digitCount) {
            if (!stringIterator.isDigit())
                throw new InvalidFormatException("Expected digit; encountered '{}' at '{}' in '{}'", stringIterator.curr(),
                        stringIterator.getRemaining(), stringIterator.getString());
            char c = stringIterator.read();
            value = 10 * value + (c - '0');
        }

        return value;
    }

    public String toRFC3339String() {
        if (year < 1000 || year > 9999)
            throw new InvalidFormatException("Years < 1000 or > 9999 aren't supported");

        StringBuilder dateTimeString = new StringBuilder();
        dateTimeString.append(year);
        append2DigitInt("-", dateTimeString, monthOfYear);
        append2DigitInt("-", dateTimeString, dayOfMonth);
        append2DigitInt("T", dateTimeString, hourOfDay);
        append2DigitInt(":", dateTimeString, minuteOfHour);
        append2DigitInt(":", dateTimeString, secondOfMinute);
        if (millisOfSecond > 0)
            append3DigitInt(".", dateTimeString, millisOfSecond);
        dateTimeString.append("Z");

        return dateTimeString.toString();
    }

    private static void append2DigitInt(String prefix, StringBuilder dateTimeString, int value) {
        dateTimeString.append(prefix);
        if (value < 10)
            dateTimeString.append("0" + value);
        else dateTimeString.append(value);
    }

    private static void append3DigitInt(String prefix, StringBuilder dateTimeString, int value) {
        dateTimeString.append(prefix);
        if (value < 10)
            dateTimeString.append("00" + value);
        else if (value < 100)
            dateTimeString.append("0" + value);
        else dateTimeString.append(value);
    }
}
