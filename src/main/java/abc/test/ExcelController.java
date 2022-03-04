package abc.test;

import abc.Dto.SubwayDto;
import com.fasterxml.jackson.databind.jsontype.impl.AsDeductionTypeDeserializer;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        String path = "./실시간도착_역정보_220211.xlsx";
        File file0 = new File(path);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2");
        System.out.println("파일 이름  :  "+file0.getName());
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

    @GetMapping("/excel/read0")
    //@RequestParam을 이용해서 파일 전달 자료형은 MultipartFile
    public String readExcel(Model model)throws IOException{
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        List<SubwayDto> dataList = new ArrayList<>();
        String path = "C:\\Users\\506\\Desktop\\intelliJ_box\\subway\\src\\main\\resources\\static\\file\\실시간도착_역정보_220211.xlsx";
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        // getSheetAt(0); 첫번째 페이지
        Sheet worksheet = workbook.getSheetAt(0);
        System.out.println(worksheet.toString());
        //  i = 1; 첫 열은 구분  i<worksheet.getPhysicalNumberOfRows(); 열의 길이만큼
        for(int i = 1; i<631; i++){
            // row = i번째 행의 데이터
            Row row = worksheet.getRow(i);
            SubwayDto data = new SubwayDto();
            // 행의 1번째 데이터           실수 데이터 가져오기
            data.setSubwayid(String.valueOf((int)row.getCell(0).getNumericCellValue()) );
            System.out.println("서브웨이아이디 :"+(int)row.getCell(0).getNumericCellValue());
            // 행의 2번째 데이터           문자열 데이터 가져오기
            data.setStatnid(String.valueOf((int)row.getCell(1).getNumericCellValue()));
            System.out.println("역아이디 :"+(int)row.getCell(1).getNumericCellValue());
            // 행의 3번째 데이터           문자열 데이터 가져오기
            // 논리 데이터    .getBooleanCellValue()
            data.setStatnname(row.getCell(2).getStringCellValue());
            System.out.println("역네임 :"+row.getCell(2).getStringCellValue());
            dataList.add(data);
        }
        System.out.println("23 : "+dataList.get(23).getSubwayid());
        model.addAttribute("datas",dataList);
        return "excelList";
    }

//    1AsDeductionTypeDeserializerSDF
//    ASDFGDSGFSDFGDSG
    //    1AsDeductionTypeDeserializerSDF
//    ASDFGDSGFSDFGDSG
    //    1AsDeductionTypeDeserializerSDF
//    ASDFGDSGFSDFGDSG//    1AsDeductionTypeDeserializerSDF
////    ASDFGDSGFSDFGDSG//    1AsDeductionTypeDeserializerSDF
////    ASDFGDSGFSDFGDSG//    1AsDeductionTypeDeserializerSDF
////    ASDFGDSGFSDFGDSG//    1AsDeductionTypeDeserializerSDF
////    ASDFGDSGFSDFGDSG//    1AsDeductionTypeDeserializerSDF
////    ASDFGDSGFSDFGDSG//    1AsDeductionTypeDeserializerSDF
////    ASDFGDSGFSDFGDSG//    1AsDeductionTypeDeserializerSDF
////    ASDFGDSGFSDFGDSG









}
