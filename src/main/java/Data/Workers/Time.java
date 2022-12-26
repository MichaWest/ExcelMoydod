package Data.Workers;

public class Time {
    int month;
    int day;
    int minute;
    int hour;
    int sek;
    boolean leapyear;

    public Time(int mo, int d, int h, int m, int s, boolean l) {
        day = d;
        hour = h;
        minute = m;
        sek = s;
        month = mo;
        leapyear = l;
    }

    private Time(int mo, int d, int h, int m, int s){
        day = d;
        hour = h;
        minute = m;
        sek = s;
        month = mo;
        leapyear = false;
    }

    @Override
    public String toString() {
        return ((hour<10?"0"+hour:hour) + ":" + (minute<10?"0"+minute:minute) + ":" + (sek<10?"0"+sek:sek));
    }

    public static Time sub(Time a, Time b) {
        if (a == null) return null;
        if (b == null) return null;
        int s = (a.day - b.day) * 24 * 3600 + (a.hour - b.hour) * 3600 + (a.minute - b.minute) * 60 + a.sek - b.sek;
        if (s >= 0) {
            int h = s / 3600;
            s = s - h * 3600;
            int m = s / 60;
            s = s - m * 60;
            return new Time(0, 0, h, m, s);
        } else {
            return null;
        }
    }

    public static Time add(Time a, Time b) {
        if (a == null) return b;
        if (b == null) return a;
        int s = (a.day + b.day) * 24 * 3600 + (a.hour + b.hour) * 3600 + (a.minute + b.minute) * 60 + a.sek + b.sek;
        if (s >= 0) {
            int h = s / 3600;
            s = s - h * 3600;
            int m = s / 60;
            s = s - m * 60;
            return new Time(0, 0, h, m, s);
        } else {
            return null;
        }
    }

    public double compare(Time a) {
        long amonth = a.month;
        long month = this.month;
        int k = getDayInMonth(month);
        int ak = getDayInMonth(amonth);
        //System.out.println(month+" " + k+", " +amonth+" "+ak);
        if(amonth==1 & month==12)
            amonth = 13;
        else if (amonth==12 & month==1)
            month = 13;
        return (( month * k - amonth * ak)*24 + (this.day - a.day) * 24 + (this.hour - a.hour) + (this.minute - a.minute) / 60.0d + (this.sek - a.sek) / 3600.0d);
    }

    private int getDayInMonth(long m){
        if(m==1 || m==3 || m==5 || m==7 || m==8 || m==10 || m==12){
            return 31;
        } else if(m==4 || m==6 || m==9){
            return 30;
        } else if (m==2){
            if(leapyear){
                return 29;
            } else {
                return 28;
            }
        } else {
            return 30;
        }
    }

    public int getHour() {
        return hour;
    }

    public int getMinute(){
        return minute;
    }

    public int getSek(){
        return sek;
    }

    public int getDay(){
        return day;
    }

    public int getMonth(){
        return month;
    }

    public boolean isLeapyear(){
        return leapyear;
    }

    public void setHour(int h){
        hour = h;
    }

}
