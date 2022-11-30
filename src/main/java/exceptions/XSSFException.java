package exceptions;

import java.io.IOException;

public class XSSFException extends IOException {

    public XSSFException(){
        super("Ошибка с excel файлом");
    }
}
