import Data.*;
import Data.Date;
import exceptions.XSSFException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Excel {
    private final XSSFWorkbook wb;
    private final Map<Integer, Worker> workers = new HashMap<>();
    private final TreeMap<DateTime, Data> filedata = new TreeMap<>();
    private final XSSFWorkbook workbook = new XSSFWorkbook();


    public Excel(File f) throws FileNotFoundException, XSSFException {
        try {
            /*Workbook workbook = new Workbook();
            System.out.println(f.getPath());
            workbook.loadFromFile(f.getPath());
            workbook.getDataSorter().getSortColumns().add(1, SortComparsionType.Values, OrderBy.Ascending);
            workbook.saveToFile(f.getPath());*/
            FileInputStream file = new FileInputStream(f);
            wb = new XSSFWorkbook(file);
        }catch (IOException e){
            throw new XSSFException();
        }
    }

    public File createAntwort() throws IOException {
        String filename = "NewExcelFile.xlsx";
        try {
            readTabelle();

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

    private void readTabelle() throws IOException {
        XSSFSheet sheet = wb.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        while(rowIterator.hasNext()){
            Row row = rowIterator.next();
            if(row.getCell(0)!=null){
                String turniket = row.getCell(12).getStringCellValue();
                if (turniket.contains("Турникет")){
                    String i = row.getCell(6).getStringCellValue();
                    if( i!=null && !i.equals("")){
                        int id = Integer.parseInt(i);
                        String name = row.getCell(7).getStringCellValue();
                        String vorname = row.getCell(8).getStringCellValue();
                        DateTime dateTime = getDateTime(row, id);
                        boolean o = row.getCell(10).getStringCellValue().contains("2");
                        filedata.put(dateTime, new Data(id, name, vorname, o));
                    }
                }
            }
            wb.close();
        }

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
            dateTime.setEndEntr2(new Time(day, hour, minute, second));
        } else if (turn.contains("1")&&turn.contains("Вых")) {
            dateTime.setEndEntr1(new Time(day, hour, minute, second));
        } else if (turn.contains("2")&&turn.contains("Вх")) {
            dateTime.setStartEntr2(new Time(day, hour, minute, second));
        } else if (turn.contains("1")&&turn.contains("Вх")) {
            dateTime.setStartEntr1(new Time(day, hour, minute, second));
        }
        return dateTime;
    }

    private int nRow = 0;
    private void writeInWorbook() throws IOException {
        Sheet sheet = workbook.createSheet();
        for(Map.Entry<Integer, Worker> worker : workers.entrySet()){
            passport(worker.getValue(), sheet);
            workTime(worker.getValue(), sheet);
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
        headingOfTable(sh, w.isOfficeWorker());
        CellStyle style = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("mm/dd/yy hh:mm:ss"));
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

                row.createCell(0).setCellValue(dt.getDate().toString());
                row.setRowStyle(style);

                row.createCell(1).setCellValue(dt.getStartEntr1().toString());

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

                if(!w.isOfficeWorker()) {
                    Time lunch = Time.sub(res2, dt.getWorkTime2());
                    sumlunch = Time.add(sumlunch, lunch);
                    if (lunch != null) row.createCell(12).setCellValue(lunch.toString());
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
        if(!w.isOfficeWorker()) {
            if (sumlunch != null) row.createCell(12).setCellValue(sumlunch.toString());
        }
        if (sumpause != null) row.createCell(13).setCellValue(sumpause.toString());
        sh.createRow(nRow++);
    }

    private void headingOfTable(Sheet sh, boolean office){
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
        if(!office) {
            row.createCell(12).setCellValue("Обед");
        }
        row.createCell(13).setCellValue("Перекур");
    }

}
