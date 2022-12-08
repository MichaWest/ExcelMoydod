import Data.*;
import Data.Date;
import exceptions.XSSFException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Excel {
    private final List<Workbook> workbooks = new ArrayList<>();
    private final Map<Integer, Worker> workers = new HashMap<>();
    private final TreeMap<DateTime, Data> filedata = new TreeMap<>();
    private final XSSFWorkbook workbook = new XSSFWorkbook();
    private final Map<DateTime, Worker> latecomers = new TreeMap<>();


    public Excel(File f) throws FileNotFoundException, XSSFException {
        try {
            FileInputStream file = new FileInputStream(f);
            String loadFileName = f.getName();
            if(loadFileName.contains(".xlsx")) {
                XSSFWorkbook wbXSSF = new XSSFWorkbook(file);
                workbooks.add(new Workbook(wbXSSF, loadFileName));
            }else if(loadFileName.contains(".xls")) {
                HSSFWorkbook wbHSSF = new HSSFWorkbook(file);
                workbooks.add(new Workbook(wbHSSF, loadFileName));
            }
        }catch (IOException e){
            throw new XSSFException();
        }
    }

    public void add(File f) throws FileNotFoundException, XSSFException {
        try {
            FileInputStream file = new FileInputStream(f);
            String loadFileName = f.getName();
            if(loadFileName.contains(".xlsx")) {
                XSSFWorkbook wbXSSF = new XSSFWorkbook(file);
                workbooks.add(new Workbook(wbXSSF, loadFileName));
            }else if(loadFileName.contains(".xls")) {
                HSSFWorkbook wbHSSF = new HSSFWorkbook(file);
                workbooks.add(new Workbook(wbHSSF, loadFileName));
            }
        }catch (IOException e){
            throw new XSSFException();
        }
    }

    public File createAntwort() throws IOException {
        String filename = "NewExcelFile.xlsx";
        try {
            for(Workbook w: workbooks ) {
                if (w.filename.contains(".xlsx")) {
                    readTabelleXSSF(w.wX);
                } else {
                    readTabelleHSSF(w.wH);
                }
            }

            readData();

            writeInWorbook();

            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);

            fileOut.close();
            workbook.close();
        }catch(FileNotFoundException e){
            Files.createFile(Path.of("NewExcelFile.xlsx"));
            createAntwort();
        }
        return new File(filename);
    }

    private void readData(){
        for(Map.Entry<DateTime, Data> action : filedata.entrySet()){
            Data data = action.getValue();
            DateTime dateTime = action.getKey();
            int id = data.getId();
            boolean o = data.getOffice();
            if(workers.containsKey(id)){
                workers.get(id).setWorkTime(dateTime);
            } else {
                Worker w = new Worker(id, o);
                w.setName(data.getName());
                w.setVorname(data.getVorname());
                w.setWorkTime(dateTime);
                workers.put(id, w);
            }
        }
    }

    private void readTabelleXSSF(XSSFWorkbook wbXSSF) throws IOException {
        XSSFSheet sheet = wbXSSF.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getCell(0) != null) {
                String turniket = row.getCell(12).getStringCellValue();
                if (turniket.contains("Турникет")) {
                    String i = row.getCell(6).getStringCellValue();
                    if (i != null && !i.equals("")) {
                        int id = Integer.parseInt(i);
                        String name = row.getCell(7).getStringCellValue();
                        String vorname = row.getCell(8).getStringCellValue();
                        DateTime dateTime = getDateTime(row, id);
                        boolean o = row.getCell(10).getStringCellValue().contains("2");
                        filedata.put(dateTime, new Data(id, name, vorname, o));
                    }
                }
            }
        }
        wbXSSF.close();
    }

    private void readTabelleHSSF(HSSFWorkbook wbHSSF) throws IOException {
        HSSFSheet sheet = wbHSSF.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getCell(1) != null) {
                String turniket = row.getCell(12).getStringCellValue();
                if (turniket.contains("Турникет")) {
                    String i = row.getCell(6).getStringCellValue();
                    if (i != null && !i.equals("")) {
                        int id = Integer.parseInt(i);
                        String name = row.getCell(7).getStringCellValue();
                        String vorname = row.getCell(8).getStringCellValue();
                        DateTime dateTime = getDateTime(row, id);
                        boolean o = row.getCell(10).getStringCellValue().contains("2");
                        filedata.put(dateTime, new Data(id, name, vorname, o));
                    }
                }
            }
        }
        wbHSSF.close();
    }

    public DateTime getDateTime(Row row, int id) {
        String[] dataTime = row.getCell(1).getStringCellValue().split(" ");
        String[] data = dataTime[0].split("-");
        String[] time = dataTime[1].split(":");
        int jahr = Integer.parseInt(data[0]);
        int month = Integer.parseInt(data[1]);
        int day = Integer.parseInt(data[2]);
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);
        int second = Integer.parseInt(time[2]);
        DateTime dateTime = new DateTime(new Date(jahr, month, day, hour, minute, second), id);
        String turn = row.getCell(12).getStringCellValue();
        if (turn.contains("2")&&turn.contains("Вых")) {
            dateTime.setEndEntr2(new Time(month, day, hour, minute, second));
        } else if (turn.contains("1")&&turn.contains("Вых")) {
            dateTime.setEndEntr1(new Time(month, day, hour, minute, second));
        } else if (turn.contains("2")&&turn.contains("Вх")) {
            dateTime.setStartEntr2(new Time(month, day, hour, minute, second));
        } else if (turn.contains("1")&&turn.contains("Вх")) {
            dateTime.setStartEntr1(new Time(month, day, hour, minute, second));
        }
        return dateTime;
    }

    private int nRow = 0;
    private void writeInWorbook() {
        Sheet sheet = workbook.createSheet("Время работы");
        for(Map.Entry<Integer, Worker> worker : workers.entrySet()){
            passport(worker.getValue(), sheet);
            workTime(worker.getValue(), sheet);
        }
        Sheet late = workbook.createSheet();
        int n = 0;
        for(Map.Entry<DateTime, Worker> l: latecomers.entrySet()){
            Row row = late.createRow(n++);
            row.createCell(0).setCellValue(l.getKey().getDate().toString());
            row.createCell(1).setCellValue(l.getKey().getStartEntr1().toString());
            row.createCell(2).setCellValue(l.getValue().getName()+" "+l.getValue().getVorname());
        }
    }

    private void passport(Worker w, Sheet sh){
        Row row = sh.createRow(nRow++);
        row.createCell(0).setCellValue("Сотрудник");
        row.createCell(1).setCellValue("ID");
        row.createCell(2).setCellValue(w.getNumber());
        row = sh.createRow(nRow++);
        row.createCell(1).setCellValue("Фамилия");
        row.createCell(2).setCellValue(w.getVorname());
        row = sh.createRow(nRow++);
        row.createCell(1).setCellValue("Имя");
        row.createCell(2).setCellValue(w.getName());
        row.createCell(4).setCellValue("Количество смен");
        row.createCell(5).setCellValue(w.getWorkTime().size());
        nRow++;
    }

    private void workTime(Worker w, Sheet sh){
        headingOfTable(sh, w.getPost());

        XSSFCellStyle style= workbook.createCellStyle();
        style.setFillBackgroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        style.setFillPattern(FillPatternType.DIAMONDS);

        Row row;
        Time sum1 = null;
        Time sum2 = null;
        Time psum1 = null;
        Time psum2 = null;
        Time sumlunch = null;
        Time sumpause = null;
        for(DateTime dt: w.getWorkTime()){
            if(dt.getStartEntr1()!=null&&dt.getEndEntr1()!=null) {
                row = sh.createRow(nRow++);

                Cell cell = row.createCell(1);

                row.createCell(0).setCellValue(dt.getDate().toString());

                Time start = dt.getStartEntr1();
                cell.setCellValue(start.toString());


                if (dt.getStartEntr2() != null) row.createCell(2).setCellValue(dt.getStartEntr2().toString());
                if (dt.getEndEntr2() != null) row.createCell(3).setCellValue(dt.getEndEntr2().toString());

                row.createCell(4).setCellValue(dt.getEndEntr1().toString());

                Time res2 = Time.sub(dt.getEndEntr2(), dt.getStartEntr2());
                if (res2 != null) row.createCell(6).setCellValue(res2.toString());

                Time res1 = Time.sub(dt.getEndEntr1(), dt.getStartEntr1());
                if (res1 != null) row.createCell(7).setCellValue(res1.toString());

                if (dt.getWorkTime2() != null) row.createCell(9).setCellValue(dt.getWorkTime2().toString());
                if (dt.getWorkTime1() != null) row.createCell(10).setCellValue(dt.getWorkTime1().toString());

                sum1 = Time.add(sum1, res1);
                sum2 = Time.add(sum2, res2);
                psum1 = Time.add(psum1, dt.getWorkTime1());
                psum2 = Time.add(psum2, dt.getWorkTime2());

                if(w.getPost()==Post.Worker) {
                    if(
                            (start.compare(new Time(start.getMonth(), start.getDay(), 8, 0, 0))>0 && start.compare(new Time(start.getMonth(), start.getDay(), 17, 0, 0))<0)
                    || (start.compare(new Time(start.getMonth(), start.getDay(), 20, 0, 0))>0 && start.compare(new Time(start.getMonth(), start.getDay()+1, 6, 0, 0))<0)){
                        latecomers.put(dt, w);
                        cell.setCellStyle(style);
                    }
                    Time lunch = Time.sub(res2, dt.getWorkTime2());
                    sumlunch = Time.add(sumlunch, lunch);
                    if (lunch != null) row.createCell(12).setCellValue(lunch.toString());
                } else {
                    if(start.compare(new Time(start.getMonth(), start.getDay(), 9, 0, 0))>0){
                        latecomers.put(dt, w);
                        cell.setCellStyle(style);
                    }
                }

                Time pause = Time.sub(res1, dt.getWorkTime1());
                sumpause = Time.add(sumpause, pause);
                if (pause != null) row.createCell(13).setCellValue(pause.toString());
            }
        }

        row = sh.createRow(nRow++);
        row.createCell(4).setCellValue("Сумма");
        if(sum2!=null) row.createCell(6).setCellValue(sum2.toString());
        if(sum1!=null) row.createCell(7).setCellValue(sum1.toString());
        if(psum2!=null) row.createCell(9).setCellValue(psum2.toString());
        if(psum1!=null) row.createCell(10).setCellValue(psum1.toString());
        if(w.getPost()==Post.Worker) {
            if (sumlunch != null) row.createCell(12).setCellValue(sumlunch.toString());
        }
        if (sumpause != null) row.createCell(13).setCellValue(sumpause.toString());
        sh.createRow(nRow++);
    }

    private void headingOfTable(Sheet sh, Post post){
        Row row = sh.createRow(nRow++);
        row.createCell(0).setCellValue("Дата");
        row.createCell(1).setCellValue("Приход");
        row.createCell(2).setCellValue("Начало работы");
        row.createCell(3).setCellValue("Окончание работы");
        row.createCell(4).setCellValue("Уход");
        row.createCell(6).setCellValue("ПЕ-ПО цех");
        row.createCell(7).setCellValue("ПЕ-ПО предприятие");
        row.createCell(9).setCellValue("Полезное время цех");
        row.createCell(10).setCellValue("Полезное время предприятие");
        if(post==Post.Worker) {
            row.createCell(12).setCellValue("Обед");
        }
        row.createCell(13).setCellValue("Перекур");
    }

    class Workbook {
        XSSFWorkbook wX;
        String filename;
        HSSFWorkbook wH;

        Workbook(XSSFWorkbook wX, String s){
            wH = null;
            this.wX = wX;
            filename = s;
        }

        Workbook(HSSFWorkbook wH, String s){
            wX = null;
            this.wH = wH;
            filename = s;
        }

    }

}
