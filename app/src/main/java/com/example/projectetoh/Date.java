package com.example.projectetoh;

import androidx.annotation.NonNull;

public class Date implements Comparable<Date>{
    public int day,  month, year;

    public Date(int d, int m, int y){
        day = d;
        month = m;
        year = y;
    }

    @NonNull
    @Override
    public String toString() {
        String dt = (day < 10) ? ("0" + day) : String.valueOf((day));
        dt += ".";
        dt += (month < 10) ? ("0" + month) : (month);
        dt += ".";
        dt += (year < 10) ? ("000" + year) : ((year < 100) ? ("00" + year) : ((year < 1000) ? ("0" + year) : (year)));
        return dt;
    }

    public Date (String deadline) {
        String cur = deadline.charAt(0) + String.valueOf(deadline.charAt(1));
        day = Integer.parseInt(cur);
        cur = deadline.charAt(3) + String.valueOf(deadline.charAt(4));
        month = Integer.parseInt(cur);
        cur = deadline.charAt(6) + String.valueOf(deadline.charAt(7)) + deadline.charAt(8) + deadline.charAt(9);
        year =    Integer.parseInt(cur);
    }


    @Override
    public int compareTo(Date o) {
        if (year != o.year)
            return Integer.compare(year, o.year);
        if (month != o.month)
            return Integer.compare(month, o.month);
        return Integer.compare(day, o.day);
    }
}
