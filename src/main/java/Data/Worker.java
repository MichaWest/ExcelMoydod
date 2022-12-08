package Data;

import java.util.ArrayList;
import java.util.List;

public class Worker implements Comparable<Worker> {
    private final List<DateTime> workTime = new ArrayList<>();

    private String name;
    private String vorname;
    private final int number;
    private final Post post;

    public Worker(int i, boolean b){
        number = i;
        if(b){
            post = Post.Office;
        } else {
            post = Post.Worker;
        }
    }

    public void setName(String s){
        this.name = s;
    }

    public void setVorname(String s){
        this.vorname = s;
    }

    public void setWorkTime(DateTime wt){
            if ((workTime.size() != 0)) {
                DateTime dt = workTime.get(workTime.size() - 1);

                Time StartEnter1 = wt.getStartEntr1();
                Time StartEnter2 = wt.getStartEntr2();
                Time EndEnter1 = wt.getEndEntr1();
                Time EndEnter2 = wt.getEndEntr2();

                if(StartEnter1 != null){
                    if(dt.getStartEntr1()==null){
                        dt.setStartEntr1(StartEnter1);
                        dt.setLastEnterTime1(StartEnter1);
                    } else {
                        if(dt.getEndEntr1()!=null && wt.getStartEntr1().compare(dt.getEndEntr1())>10){
                            wt.setLastEnterTime1(StartEnter1);
                            workTime.add(wt);
                        }
                        dt.setLastEnterTime1(StartEnter1);
                    }
                }

                if(StartEnter2 != null){
                    if(dt.getStartEntr1()!=null) {
                        if (dt.getStartEntr2() == null) {
                            dt.setStartEntr2(StartEnter2);
                            dt.setLastEnterTime2(StartEnter2);
                        } else {
                            if(dt.getEndEntr1()==null && dt.getEndEntr2()!=null && StartEnter2.compare(dt.getEndEntr2())>10){
                                wt.setLastEnterTime1(StartEnter1);
                                wt.setLastEnterTime2(StartEnter2);
                                wt.setStartEntr1(dt.getStartEntr1());
                                workTime.add(wt);
                            }
                            dt.setLastEnterTime2(StartEnter2);
                        }
                    }
                }

                if(EndEnter1 != null){
                    if(dt.getStartEntr1()!=null){
                        dt.setEndEntr1(EndEnter1);
                        if(EndEnter1.compare(dt.getStartEntr1()) < 15) {
                            dt.setWorkTime1(Time.add(dt.getWorkTime1(), Time.sub(EndEnter1, dt.getLastEnterTime1())));
                            dt.setLastEnterTime1(null);
                        }
                    }
                }

                if(EndEnter2 != null){
                    if(dt.getStartEntr2()!=null){
                        dt.setEndEntr2(EndEnter2);
                        if (EndEnter2.compare(dt.getStartEntr2()) < 15) {
                            dt.setWorkTime2(Time.add(dt.getWorkTime2(), Time.sub(EndEnter2, dt.getLastEnterTime2())));
                            dt.setLastEnterTime2(null);
                        }
                    }
                }

                if(number==69) {
                    System.out.printf("%-25s %-25s %-25s %-25s %-25s %-25s\n", "StartEntr1: " + (dt.getStartEntr1() != null ? dt.getStartEntr1().toString() : "null"),
                            ", StartEntr2: " + (dt.getStartEntr2() != null ? dt.getStartEntr2().toString() : "null"),
                            ", LastEnter1: " + (dt.getLastEnterTime1() != null ? dt.getLastEnterTime1().toString() : "null"),
                            ", LastEnter2: " + (dt.getLastEnterTime2() != null ? dt.getLastEnterTime2().toString() : "null"),
                            ", EndEnter1: " + (dt.getEndEntr1() != null ? dt.getEndEntr1().toString() : "null"),
                            ", EndEnter2: " + (dt.getEndEntr2() != null ? dt.getEndEntr2().toString() : "null"));
                    // ", EndEnter1-LastEnter1: " + ((dt.getEndEntr1() != null && dt.getLastEnterTime1() != null) ? Time.sub(dt.getEndEntr1(), dt.getLastEnterTime1()) : "null") +
                    // ", EndEnter2-LastEnter2: " + ((dt.getEndEntr2() != null && dt.getLastEnterTime2() != null) ? Time.sub(dt.getEndEntr2(), dt.getLastEnterTime2()) : "null") +
                    // ", WorkTime1: " + (dt.getWorkTime1() != null ? dt.getWorkTime1() : "null") +
                    //", WorkTime2: " + (dt.getWorkTime2() != null ? dt.getWorkTime2() : "null"));
                }


            } else {
                if (wt.getStartEntr1() != null) {
                    wt.setLastEnterTime1(wt.getStartEntr1());
                    workTime.add(wt);
                }
            }
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

    @Override
    public int compareTo(Worker o) {
        return Integer.compare(this.number, o.number);
    }

    public Post getPost(){
        return post;
    }
}
