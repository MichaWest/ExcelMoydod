package Data.Workers;

import Data.DateTime;
import Data.Post;

import java.util.ArrayList;
import java.util.List;

public class SchiftWorker implements Comparable<SchiftWorker>, Employee{
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


    public void setWorkTime(DateTime w){
        DateTime wt = w.copy();
        if ((workTime.size() != 0)) {
            DateTime dt = workTime.get(workTime.size() - 1);

            Time StartEnter1 = wt.getStartEntr1();
            Time StartEnter2 = wt.getStartEntr2();
            Time EndEnter1 = wt.getEndEntr1();
            Time EndEnter2 = wt.getEndEntr2();

            if(StartEnter1 != null){
                if(dt.getStartEntr1()==null){

                    setWorkType(StartEnter1);
                    StartEnter1 = isInInterval(StartEnter1, Enter.Start);
                    dt.setStartEntr1(StartEnter1);

                    dt.setLastEnterTime1(StartEnter1);
                } else {
                    if(dt.getEndEntr1() !=null) {
                        if(StartEnter1.compare(dt.getEndEntr1())>8) {

                            setWorkType(StartEnter1);
                            StartEnter1 = isInInterval(StartEnter1, Enter.Start);
                            wt.setLastEnterTime1(StartEnter1);
                            wt.setStartEntr1(StartEnter1);

                            workTime.add(wt);
                        }
                    }
                    StartEnter1 = isInInterval(StartEnter1, Enter.Start);
                    dt.setLastEnterTime1(StartEnter1);
                }
            }

            if(StartEnter2 != null){

                if(dt.getLastEnterTime1()!=null) {
                    if (dt.getStartEntr2() == null) {

                        setWorkType(StartEnter2);
                        StartEnter2 = isInInterval(StartEnter2, Enter.Start);
                        dt.setStartEntr2(StartEnter2);

                        dt.setLastEnterTime2(StartEnter2);
                    } else {
                        if(dt.getEndEntr2()!=null) {
                            if (dt.getEndEntr1() == null && StartEnter2.compare(dt.getEndEntr2()) > 8) {

                                setWorkType(StartEnter2);
                                StartEnter2 = isInInterval(StartEnter2, Enter.Start);
                                wt.setLastEnterTime1(dt.getLastEnterTime1());
                                wt.setLastEnterTime2(StartEnter2);
                                wt.setStartEntr1(dt.getLastEnterTime1());

                                workTime.add(wt);
                            }
                            StartEnter2 = isInInterval(StartEnter2, Enter.Start);
                            dt.setLastEnterTime2(StartEnter2);
                        }
                    }
                }
            }

            if(EndEnter1 != null){
                if(dt.getStartEntr1()!=null &&  dt.getLastEnterTime1()!=null){
                    System.out.println(EndEnter1+"-"+dt.getStartEntr1()+"="+EndEnter1.compare(dt.getStartEntr1()));
                    if(EndEnter1.compare(dt.getStartEntr1())>0) {
                        dt.setEndEntr1(isInInterval(EndEnter1, Enter.End));
                        if (EndEnter1.compare(dt.getStartEntr1()) < 15) {
                            EndEnter1 = isInInterval(EndEnter1, Enter.End);
                            dt.setWorkTime1(Time.add(dt.getWorkTime1(), Time.sub(EndEnter1, dt.getLastEnterTime1())));
                            dt.setLastEnterTime1(null);
                        }
                    } else {
                        dt.setEndEntr1(dt.getStartEntr1());
                    }
                }
            }

            if(EndEnter2 != null){
                if(dt.getStartEntr2()!=null &&  dt.getLastEnterTime2()!=null){
                    System.out.println(EndEnter2+"-"+dt.getStartEntr2()+"="+EndEnter2.compare(dt.getStartEntr2()));
                    if(EndEnter2.compare(dt.getStartEntr2())>0) {
                        dt.setEndEntr2(isInInterval(EndEnter2, Enter.End));
                        if (EndEnter2.compare(dt.getStartEntr2()) < 15) {
                            EndEnter2 = isInInterval(EndEnter2, Enter.End);
                            dt.setWorkTime2(Time.add(dt.getWorkTime2(), Time.sub(EndEnter2, dt.getLastEnterTime2())));
                            dt.setLastEnterTime2(null);
                        }
                    }else {
                        dt.setEndEntr2(dt.getStartEntr2());
                    }
                }
            }

            if(number==65) {
                    System.out.printf("%-25s %-25s %-25s %-25s %-25s %-25s\n",
                            "StartEntr1: " + (dt.getStartEntr1() != null ? dt.getStartEntr1().toString() : "null")+", ",
                            "StartEntr2: " + (dt.getStartEntr2() != null ? dt.getStartEntr2().toString() : "null")+", ",
                            "LastEnter1: " + (dt.getLastEnterTime1() != null ? dt.getLastEnterTime1().toString() : "null")+", ",
                            "LastEnter2: " + (dt.getLastEnterTime2() != null ? dt.getLastEnterTime2().toString() : "null")+", ",
                            "EndEnter1: " + (dt.getEndEntr1() != null ? dt.getEndEntr1().toString() : "null")+", ",
                            "EndEnter2: " + (dt.getEndEntr2() != null ? dt.getEndEntr2().toString() : "null"));
                            //"WorkTime1: " + (dt.getWorkTime1() != null ? dt.getWorkTime1() : "null")+", ",
                            //"WorkTime2: " + (dt.getWorkTime2() != null ? dt.getWorkTime2() : "null"));

                    // ", EndEnter1-LastEnter1: " + ((dt.getEndEntr1() != null && dt.getLastEnterTime1() != null) ? Time.sub(dt.getEndEntr1(), dt.getLastEnterTime1()) : "null") +
                    // ", EndEnter2-LastEnter2: " + ((dt.getEndEntr2() != null && dt.getLastEnterTime2() != null) ? Time.sub(dt.getEndEntr2(), dt.getLastEnterTime2()) : "null") +
                    // ", WorkTime1: " + (dt.getWorkTime1() != null ? dt.getWorkTime1() : "null") +
                    //", WorkTime2: " + (dt.getWorkTime2() != null ? dt.getWorkTime2() : "null"));
                }
        } else {
            if (wt.getStartEntr1() != null) {
                setWorkType(wt.getStartEntr1());
                wt.setLastEnterTime1(isInInterval(wt.getStartEntr1(), Enter.Start));
                wt.setStartEntr1(isInInterval(wt.getStartEntr1(), Enter.Start));
                workTime.add(wt);
            }
        }
    }

    private boolean dayWork;
    private void setWorkType(Time t){
        switch(post) {
            case Office:
                dayWork = true;
                break;
            case Worker:
                int day = 8;
                int night = 20;
                if(t.compare(new Time(t.month, t.day, day+3, 0, 0, t.leapyear)) < 0 && t.compare(new Time(t.month, t.day, day - 3, 0, 0, t.leapyear)) > 0){
                    dayWork = true;
                } else if(t.compare(new Time(t.month, t.day, night+3, 0, 0, t.leapyear)) < 0 && t.compare(new Time(t.month, t.day, night - 3, 0, 0, t.leapyear)) > 0){
                    dayWork = false;
                }
                break;
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
            if(dayWork && t.compare(new Time(t.month, t.day, day, 0, 0, t.leapyear)) < 0 ){
                return new Time(t.month, t.day, day, 0, 0, t.leapyear);
            } else if(!dayWork && t.compare(new Time(t.month, t.day, night, 0, 0, t.leapyear)) < 0 ){
                return new Time(t.month, t.day, night, 0, 0, t.leapyear);
            } else {
                return t;
            }
        }else{
            if (!dayWork && t.compare(new Time(t.month, t.day+1, day, 0, 0, t.leapyear)) > 0 ) {
                return new Time(t.month, t.day, day, 0, 0, t.leapyear);
            } else if (dayWork && t.compare(new Time(t.month, t.day, night, 0, 0, t.leapyear)) > 0){
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
        Start, End
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
