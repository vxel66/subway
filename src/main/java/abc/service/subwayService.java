package abc.service;

import abc.Dto.SubwayDto;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class subwayService {

    @SneakyThrows
    public JSONArray getSearch(String inputtext,String up,String down, int type){
        System.out.println("up :"+up+" down :"+down);
        String encode= URLEncoder.encode(inputtext, "utf-8");
        String search = "http://swopenapi.seoul.go.kr/api/subway/6f58714f6c64686534356359587644/json/realtimeStationArrival/0/8/"+encode;
        URL url = new URL(search);
        // url : http://swopenapi.seoul.go.kr/api/subway/인증키/요청파일타입/서비스명/시작위치/끝위치/역이름
        // 2. 스트림 버퍼를 통한 URL내 HTML 읽어오기                      // 호출 개수 : page="시작번호"&perpage="마지막번호"
        BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        // 3. 읽어온 내용 문자열 담기
        String result = bf.readLine(); // .readLine() : 모든 문자열 읽어오기
        // 3. 읽어온 내용을 json으로 파싱 하기
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
        // 1.JSONparser json데이터 넣어서 파싱  // 2.jsonobject 형 변환
        // System.out.println("rul 내용을 json 변환[ json ] : " +  jsonObject );
        String qwe = String.valueOf(jsonObject.get("total"));
        if(qwe.equals("0")){
            return null;
        }
        JSONArray jsonArray = (JSONArray) jsonObject.get("realtimeArrivalList"); // 실제 지하철 정보 리스트
        // "data" 라는 키 요청 해서 리스트 담기
        // System.out.println("data 키 호출 해서 리스트 담기 : " +  jsonArray );
        JSONArray 상행리스트 = new JSONArray();
        JSONArray 하행리스트 = new JSONArray();
        JSONArray 외선리스트 = new JSONArray();
        JSONArray 내선리스트 = new JSONArray();
        JSONArray 리스트테스트 = new JSONArray(); //상행 리스트
        JSONArray 리스트테스트2 = new JSONArray(); //하행 리스트
        JSONArray 리스트테스트3 = new JSONArray(); //외선 리스트
        JSONArray 리스트테스트4 = new JSONArray(); //내선 리스트

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject content = (JSONObject) jsonArray.get(i);
            String 상하행 = (String) content.get("updnLine");
            String trainLineNm = (String) content.get("trainLineNm");
            String LineNm = trainLineNm.split("-")[0];
            System.out.println(trainLineNm);
            System.out.println("@"+LineNm);
            if (상하행.equals("상행")&&LineNm.equals(up+"행 ")) {
                상행리스트.add((JSONObject) jsonArray.get(i));
            } else if(상하행.equals("하행")&&LineNm.equals(down+"행 ")){
                하행리스트.add(jsonArray.get(i));
            } else if(상하행.equals("외선")){
                외선리스트.add(jsonArray.get(i));
            } else if(상하행.equals("내선")){
                내선리스트.add(jsonArray.get(i));
            }
        }
        Date date = new Date();
        // 시간 더하기
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        for (int i = 0; i < 상행리스트.size(); i++) {
            JSONObject content = (JSONObject) 상행리스트.get(i);
            int 도착예정시간 = 0;
            String s = (String) content.get("arvlMsg2");
            if (s.indexOf("]") == -1) {
                도착예정시간 = 0;
            }else{
                도착예정시간 = Integer.parseInt(s.substring(1, s.indexOf("]"))) * 3;
                cal.add(Calendar.MINUTE, 도착예정시간);
                SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm");
                String time = sdformat.format(cal.getTime());
                content.put("name2",time);
                리스트테스트.add(content);
                cal.setTime(date);
            }

        }

        for(int i = 0; i<외선리스트.size(); i++){
            JSONObject content = (JSONObject) 외선리스트.get(i);
            String s = (String) content.get("arvlMsg2");
            int 도착예정시간 = 0;
            if(s.contains("분")) {
                도착예정시간 = Integer.parseInt(s.substring(0,1));
                cal.add(Calendar.MINUTE, 도착예정시간);
                SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm");
                String time = sdformat.format(cal.getTime());
                content.put("arvlMsg2",time);
                리스트테스트3.add(content);
                cal.setTime(date);
            }else{
                리스트테스트3.add(content);
            }
        }


        for(int i = 0; i<내선리스트.size(); i++){
            JSONObject content = (JSONObject) 내선리스트.get(i);
            String s = (String) content.get("arvlMsg2");
            int 도착예정시간 = 0;
            if(s.contains("분")) {
                도착예정시간 = Integer.parseInt(s.substring(0,1));
                cal.add(Calendar.MINUTE, 도착예정시간);
                SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm");
                String time = sdformat.format(cal.getTime());
                content.put("arvlMsg2",time);
                리스트테스트4.add(content);
                cal.setTime(date);
            }else{
                리스트테스트4.add(content);
            }
        }

        for (int i = 0; i < 하행리스트.size(); i++) {
            JSONObject content = (JSONObject) 하행리스트.get(i);
            int 도착예정시간 = 0;
            String s = (String) content.get("arvlMsg2");
            if (s.indexOf("]") == -1) {
                도착예정시간 = 0;
            } else {
                도착예정시간 = Integer.parseInt(s.substring(1, s.indexOf("]"))) * 3;
                cal.add(Calendar.MINUTE, 도착예정시간);
                SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm");
                String time = sdformat.format(cal.getTime());
                content.put("name2", time);
                리스트테스트2.add(content);
                cal.setTime(date);
            }
        }
        if(type == 1){
            return 리스트테스트;
        }else if(type == 2){
            return 리스트테스트2;
        }else if(type == 3){
            return 리스트테스트3;
        }else if(type == 4){
            return 리스트테스트4;
        }
        return null;
    }

    //@RequestParam을 이용해서 파일 전달 자료형은 MultipartFile
    public List<SubwayDto> readExcel(String serch) throws Exception {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        List<SubwayDto> dataList = new ArrayList<>();
        List<String> frontback = new ArrayList<>();
        String path = "C:\\Users\\minwook\\Desktop\\새 폴더\\subway0\\src\\main\\resources\\static\\file\\실시간도착_역정보_220211.xlsx";
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        // getSheetAt(0); 첫번째 페이지
        Sheet worksheet = workbook.getSheetAt(0);
        //  i = 1; 첫 열은 구분  i<worksheet.getPhysicalNumberOfRows(); 열의 길이만큼
        for(int i = 1; i<631; i++){
            // row = i번째 행의 데이터
            Row row = worksheet.getRow(i);
            if(row.getCell(2).getStringCellValue().equals(serch)){
                SubwayDto subdata = new SubwayDto();
                Row row2 = worksheet.getRow(i+1);
                Row row3 = worksheet.getRow(i-1);
                subdata.setSubwayid(String.valueOf((int)row.getCell(0).getNumericCellValue()));
                subdata.setBack(row3.getCell(2).getStringCellValue());
                subdata.setFront(row2.getCell(2).getStringCellValue());
                dataList.add(subdata);
            }
        }
        System.out.println(dataList.toString());
        return dataList;
    }

    //@RequestParam을 이용해서 파일 전달 자료형은 MultipartFile
    public List<String> readExcel1(String serch) throws Exception {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        List<String> frontback = new ArrayList<>();
        String path = "C:\\Users\\minwook\\Desktop\\새 폴더\\subway0\\src\\main\\resources\\static\\file\\실시간도착_역정보_220211.xlsx";
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        // getSheetAt(0); 첫번째 페이지
        Sheet worksheet = workbook.getSheetAt(0);
        //  i = 1; 첫 열은 구분  i<worksheet.getPhysicalNumberOfRows(); 열의 길이만큼
        for(int i = 1; i<631; i++){
            // row = i번째 행의 데이터
            Row row = worksheet.getRow(i);
            if(String.valueOf((int)row.getCell(0).getNumericCellValue()).equals(serch)){
                frontback.add(row.getCell(2).getStringCellValue());
            }
        }
        System.out.println(frontback.toString());
        return frontback;
    }










}
