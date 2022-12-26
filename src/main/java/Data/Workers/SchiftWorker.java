package Data.Workers;

import Data.DateTime;
import Data.Post;

import java.util.ArrayList;
import java.util.List;

public class SchiftWorker implements Comparable<SchiftWorker>{
    private final List<DateTime> workTime = new ArrayList<>();

    private String name;
    private String vorname;
    private final int number;
    private final Post post;

    public SchiftWorker(int i, boolean b) {
        number = i;
        if(b){
            post = Post.Office;
        } else {
            post = Post.Worker;
        }
    }


    public void setWorkTime(DateTime wt){
        if ((workTime.size() != 0)) {
            DateTime dt = workTime.get(workTime.size() - 1);

            Time StartEnter1 = wt.getStartEntr1();
            Time StartEnter2 = wt.getStartEntr2();
            Time EndEnter1 = wt.getEndEntr1();
            Time EndEnter2 = wt.getEndEntr2();

            if(StartEnter1 != null){
                Time start = dt.getStartEntr1();
                if(dt.getStartEntr1()==null){ //Первый вход
                    Time t = isInInterval(start, Enter.Start);
                    dt.setStartEntr1(t);
                    dt.setLastEnterTime1(t);
                } else{
                    if(dt.getEndEntr1() != null){
                        if(StartEnter1.compare(dt.getEndEntr1())>8){  //Начало следущей смены
                            Time t = isInInterval(start, Enter.Start);
                            wt.setStartEntr1(t);
                            wt.setLastEnterTime1(t);
                            workTime.add(wt);
                        }
                    }
                    dt.setLastEnterTime1(isInInterval(start, Enter.Start));
                }
            }

            if(StartEnter2 != null){
                if(dt.getLastEnterTime1()!=null){
                    if(dt.getStartEntr2() == null){
                        dt.setStartEntr2(StartEnter2);
                        dt.setLastEnterTime2(StartEnter2);
                    } else {
                        if( dt.getEndEntr1() != null){
                            if(dt.getEndEntr1() == null && StartEnter2.compare(dt.getEndEntr2())>8){
                                wt.setLastEnterTime1(isInInterval(dt.getLastEnterTime1(), Enter.Start));
                                wt.setLastEnterTime2(StartEnter2);
                                wt.setStartEntr1(isInInterval(dt.getLastEnterTime1(), Enter.Start));
                                workTime.add(wt);
                            }
                            dt.setLastEnterTime2(StartEnter2);
                        }
                    }
                }
            }

            if(EndEnter1 != null){
                if(dt.getStartEntr1()!=null && dt.getLastEnterTime1()!=null){
                    EndEnter1 = isInInterval(EndEnter1, Enter.End);
                    dt.setEndEntr1(EndEnter1);
                    if(EndEnter1.compare(dt.getEndEntr1()) < 15){
                        dt.setWorkTime1(Time.add(dt.getWorkTime1(), Time.sub(EndEnter1, dt.getLastEnterTime1())));
                        dt.setLastEnterTime1(null);
                    }
                }
            }

            if(EndEnter2 != null){
                if(dt.getStartEntr2()!=null &&  dt.getLastEnterTime2()!=null){
                    dt.setEndEntr2(EndEnter2);
                    if (EndEnter2.compare(dt.getStartEntr2()) < 15) {
                        dt.setWorkTime2(Time.add(dt.getWorkTime2(), Time.sub(EndEnter2, dt.getLastEnterTime2())));
                        dt.setLastEnterTime2(null);
                    }
                }
            }

        } else {
            if (wt.getStartEntr1() != null) {
                wt.setLastEnterTime1(isInInterval(wt.getStartEntr1(), Enter.Start));
                wt.setStartEntr1(isInInterval(wt.getStartEntr1(), Enter.Start));
                workTime.add(wt);
            }
        }
    }

    private Time isInInterval(Time t, Enter enter){
        int day = 0;
        int night = 0;
        switch(post) {
            case Office:
                day = 9;
                night = 18;
                break;
            case Worker:
                day = 8;
                night = 20;
                break;
        }
        if(enter == Enter.Start) {
            if(t.compare(new Time(t.month, t.day, day, 0, 0, t.leapyear)) < 0 && t.compare(new Time(t.month, t.day, day - 3, 0, 0, t.leapyear)) > 0){
                return new Time(t.month, t.day, day, 0, 0, t.leapyear);
            } else if(t.compare(new Time(t.month, t.day, night, 0, 0, t.leapyear)) < 0 && t.compare(new Time(t.month, t.day, night - 3, 0, 0, t.leapyear)) > 0){
                return new Time(t.month, t.day, night, 0, 0, t.leapyear);
            } else {
                return t;
            }
        }else{
            if (t.compare(new Time(t.month, t.day, day, 0, 0, t.leapyear)) > 0 && t.compare(new Time(t.month, t.day, day + 3, 0, 0, t.leapyear)) < 0){
                return new Time(t.month, t.day, day, 0, 0, t.leapyear);
            } else if (t.compare(new Time(t.month, t.day, night, 0, 0, t.leapyear)) > 0 && t.compare(new Time(t.month, t.day, night + 3, 0, 0, t.leapyear)) < 0){
                return new Time(t.month, t.day, night, 0, 0, t.leapyear);
            } else{
                return t;
            }
        }
    }

    @Override
    public int compareTo(SchiftWorker o) {
        return this.vorname.compareTo(o.vorname);
    }

    private enum Enter {
        Start, End;
    }

    public void setName(String s){
        this.name = s;
    }

    public void setVorname(String s){
        this.vorname = s;
    }

    public int getNumber(){
        return number;
    }

    public String getName(){
        return name;
    }

    public String getVorname(){
        return vorname;
    }

    public List<DateTime> getWorkTime(){
        return workTime;
    }

    public Post getPost(){
        return post;
    }
}
