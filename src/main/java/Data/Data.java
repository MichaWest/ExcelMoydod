package Data;

public class Data {
    private final int id;
    private final String name;
    private final String vorname;
    private final boolean office;

    public Data(int id, String name, String vorname, boolean o){
        this.id = id;
        this.name = name;
        this.vorname = vorname;
        this.office = o;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getVorname(){
        return vorname;
    }

    public boolean getOffice(){
        return office;
    }
}
