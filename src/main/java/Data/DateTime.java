package Data;

public class DateTime implements Comparable<DateTime>{
    final private Date date;
    private Time workTimeP;
    private Time workTimeZ;
    private Time lastEnterTimeP;
    private Time lastEnterTimeZ;
    private Time startEntr1;
    private Time startEntr2;
    private Time endEntr1;
    private Time endEntr2;
    private int id;

    public DateTime(Date d, int id){
        date = d;
        this.id = id;
    }

    public Date getDate(){
        return date;
    }

    @Override
    public String toString(){
        return date+" "+startEntr1+" "+startEntr2+" "+endEntr1+" "+endEntr2;
    }

    public void setStartEntr1(Time time){
        startEntr1 = time;
    }

    public void setStartEntr2(Time time){
        startEntr2 = time;
    }

    public void setEndEntr1(Time time){
        endEntr1 = time;
    }

    public void setEndEntr2(Time time){
        endEntr2 = time;
    }

    public Time getStartEntr1(){
        return startEntr1;
    }

    public Time getStartEntr2(){
        return startEntr2;
    }

    public Time getEndEntr1(){
        return endEntr1;
    }

    public Time getEndEntr2(){
        return endEntr2;
    }

    public void setLastEnterTime1(Time p){
        lastEnterTimeP = p;
    }

    public void setLastEnterTime2(Time p){
        lastEnterTimeZ = p;
    }

    public void setWorkTime1(Time p){
        workTimeP = p;
    }

    public void setWorkTime2(Time p){
        workTimeZ = p;
    }

    public Time getLastEnterTime1(){
        return lastEnterTimeP;
    }

    public Time getLastEnterTime2(){
        return lastEnterTimeZ;
    }

    public Time getWorkTime1(){
        return workTimeP;
    }

    public Time getWorkTime2(){
        return workTimeZ;
    }

    @Override
    public int compareTo(DateTime o) {
        int dateCompare = date.compareTo(o.getDate());
        if(dateCompare ==0){
            return id - o.id;
        } else{
            return dateCompare;
        }
    }

}
