package Data;

public class Time {
    int day;
    int minute;
    int hour;
    int sek;

    public Time(int d, int h, int m, int s) {
        day = d;
        hour = h;
        minute = m;
        sek = s;
    }

    @Override
    public String toString() {
        return (hour<10?"0"+hour:hour) + ":" + (minute<10?"0"+minute:minute) + ":" + (sek<10?"0"+sek:sek);
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
            return new Time(0, h, m, s);
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
            return new Time(0, h, m, s);
        } else {
            return null;
        }
    }

    public float compare(Time a) {
        return ((long) (this.day - a.day) * 24 + (this.hour - a.hour) + (this.minute - a.minute) / 60.0f + (this.sek - a.sek) / 3600.0f);
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

}
