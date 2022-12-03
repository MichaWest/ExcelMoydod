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

                if (wt.getStartEntr1() != null) {
                    if (dt.getStartEntr1() == null) {
                        dt.setStartEntr1(wt.getStartEntr1());
                        dt.setLastEnterTime1(dt.getStartEntr1());
                    } else {
                        if (dt.getEndEntr1() != null && wt.getStartEntr1().compare(dt.getEndEntr1()) > 10) {
                            wt.setLastEnterTime1(wt.getStartEntr1());
                            workTime.add(wt);
                        } else {
                            dt.setLastEnterTime1(wt.getStartEntr1());
                        }
                    }
                }

                if (wt.getStartEntr2() != null) {
                    if (dt.getStartEntr1() != null) { //проверяем входил ли человек на производство
                        if (dt.getStartEntr2() == null) {
                            dt.setStartEntr2(wt.getStartEntr2());
                            dt.setLastEnterTime2(wt.getStartEntr2());
                        }
                        dt.setLastEnterTime2(wt.getStartEntr2());
                    }
                }

                if (wt.getEndEntr1() != null) {
                    if (dt.getStartEntr1() != null) {
                        if (wt.getEndEntr1().compare(dt.getStartEntr1()) < 14) {
                            dt.setEndEntr1(wt.getEndEntr1());
                            dt.setWorkTime1(Time.add(dt.getWorkTime1(), Time.sub(wt.getEndEntr1(), dt.getLastEnterTime1())));
                            dt.setLastEnterTime1(null);
                        }
                    }
                }

                if (wt.getEndEntr2() != null) {
                    if (dt.getStartEntr2() != null) {
                        if (wt.getEndEntr2().compare(dt.getStartEntr2()) < 14) {
                            dt.setEndEntr2(wt.getEndEntr2());
                            dt.setWorkTime2(Time.add(dt.getWorkTime2(), Time.sub(wt.getEndEntr2(), dt.getLastEnterTime2())));
                            dt.setLastEnterTime2(null);
                        }
                    }
                }

                /*System.out.println("StartEntr1: " + (dt.getStartEntr1() != null ? dt.getStartEntr1().toString() : "null") +
                        ", StartEntr2: " + (dt.getStartEntr2() != null ? dt.getStartEntr2().toString() : "null") +
                        ", LastEnter1: " + (dt.getLastEnterTime1() != null ? dt.getLastEnterTime1().toString() : "null") +
                        ", LastEnter2: " + (dt.getLastEnterTime2() != null ? dt.getLastEnterTime2().toString() : "null") +
                        ", EndEnter1: " + (dt.getEndEntr1() != null ? dt.getEndEntr1().toString() : "null") +
                        ", EndEnter2: " + (dt.getEndEntr2() != null ? dt.getEndEntr2().toString() : "null") +
                        ", EndEnter1-LastEnter1: " + ((dt.getEndEntr1() != null && dt.getLastEnterTime1() != null) ? Time.sub(dt.getEndEntr1(), dt.getLastEnterTime1()) : "null") +
                        ", EndEnter2-LastEnter2: " + ((dt.getEndEntr2() != null && dt.getLastEnterTime2() != null) ? Time.sub(dt.getEndEntr2(), dt.getLastEnterTime2()) : "null") +
                        ", WorkTime1: " + (dt.getWorkTime1() != null ? dt.getWorkTime1() : "null") +
                        ", WorkTime2: " + (dt.getWorkTime2() != null ? dt.getWorkTime2() : "null"));*/

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
