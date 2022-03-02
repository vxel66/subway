package abc.test;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ExcelController {

    @GetMapping("/excel")
    public String main(){
        return "excel";
    }

    @PostMapping("/excel/read")
        //@RequestParam을 이용해서 파일 전달 자료형은 MultipartFile
    public String redExcel(@RequestParam("file")MultipartFile file, Model model)throws IOException{
        List<ExcelData> dataList = new ArrayList<>();
        //commons-io 에 있는 파일 확장자 가져오기 기능을 이용해서 확장자를 가져오고
        //엑셀파일 (xls,xlsx)이 아닌 경우에는 예외를 던진다.
        // extension = 확장자명
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        Workbook workbook = null;
        System.out.println("file.getInputStream():"+file.getInputStream());
        System.out.println("file.getInputStream():"+file.getInputStream());
        if(!extension.equals("xlsx")&&!extension.equals("xls")){
            throw new IOException("엑셀파일만 업로드 해주세요.");
        }else{
            workbook = new XSSFWorkbook(file.getInputStream());
        }
                                // getSheetAt(0); 첫번째 페이지
        Sheet worksheet = workbook.getSheetAt(0);
        //  i = 1; 첫 열은 구분  i<worksheet.getPhysicalNumberOfRows(); 열의 길이만큼
        for(int i = 1; i<worksheet.getPhysicalNumberOfRows(); i++){
            // row = i번째 행의 데이터
            Row row = worksheet.getRow(i);
            ExcelData data = new ExcelData();
                        // 행의 1번째 데이터           실수 데이터 가져오기
            data.setNum((int)row.getCell(0).getNumericCellValue());
                        // 행의 2번째 데이터           문자열 데이터 가져오기
            data.setName(row.getCell(1).getStringCellValue());
                        // 행의 3번째 데이터           문자열 데이터 가져오기
                                                // 논리 데이터    .getBooleanCellValue()
            data.setEmail(row.getCell(2).getStringCellValue());
            dataList.add(data);
        }
        model.addAttribute("datas",dataList);
        return "excelList";
    }
}
