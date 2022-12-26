package Data.Workers;

import Data.DateTime;
import Data.Post;

import java.util.ArrayList;
import java.util.List;

public interface Employee {
    void setName(String s);
    void setVorname(String s);
    void setWorkTime(DateTime wt);
    int getNumber();
    String getName();
    String getVorname();
    List<DateTime> getWorkTime();
    Post getPost();
}
