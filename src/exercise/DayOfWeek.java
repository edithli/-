package exercise;

import java.util.Calendar;
import java.util.Date;

public class DayOfWeek {
    public static void main(String[] args){
        int y = 1953, m = 8, d = 2;
        Calendar c = Calendar.getInstance();
        c.set(y, m-1, d);
        System.out.println(c.get(Calendar.DAY_OF_WEEK));
        /**
         * y0 = y − (14 − m) / 12
         * x = y0 + y0/4 − y0/100 + y0/400
         * m0 = m + 12 × ((14 − m) / 12) − 2
         * d0 = (d + x + 31m0 / 12) mod 7
         */
    }
}
