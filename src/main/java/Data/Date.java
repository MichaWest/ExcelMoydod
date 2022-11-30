package Data;

public class Date implements Comparable<Date> {
    int jahre;
    int month;
    int day;
    int hour;
    int minute;
    int second;

    public Date(int j, int m, int d, int h, int min, int s){
        jahre = j;
        month = m;
        day = d;
        hour = h;
        minute = min;
        second = s;
    }

    public boolean compare(Date o){
        return (jahre==o.jahre)&&(month==o.month)&&(day==o.day);
    }


    @Override
    public String toString(){
        return jahre+"-"+month+"-"+day;
    }

    public int getDays(){
        return jahre*365+month*31+day;
    }

    @Override
    public int compareTo(Date o) {
        return (jahre-o.jahre)*365*31*24*60*60+(month-o.month)*31*24*60*60+(day-o.day)*24*60*60+(hour-o.hour)*60*60+(minute-o.minute)*60+(second-o.second);
    }

    public int getDay(){
        return day;
    }

    public int getMonth(){
        return month;
    }

    public int getJahre(){
        return jahre;
    }
}
