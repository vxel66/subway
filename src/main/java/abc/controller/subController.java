package abc.controller;


import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@Controller("/")
public class subController {

    // 메인페이지 매핑[ 연결 ]
    @GetMapping("/")
    public String main(Model model) {
        try{

        URL url = new URL("http://swopenapi.seoul.go.kr/api/subway/6f58714f6c64686534356359587644/json/realtimeStationArrival/0/8/%ED%95%9C%EB%8C%80%EC%95%9E");
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
        JSONArray jsonArray = (JSONArray) jsonObject.get("realtimeArrivalList"); // 실제 지하철 정보 리스트
        // "data" 라는 키 요청 해서 리스트 담기
        // System.out.println("data 키 호출 해서 리스트 담기 : " +  jsonArray );
        JSONArray 상행리스트 = new JSONArray();
        JSONArray 하행리스트 = new JSONArray();
        JSONArray 리스트테스트 = new JSONArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject content = (JSONObject) jsonArray.get(i);
            String 상하행 = (String) content.get("updnLine");

            if (상하행.equals("상행")) {
                상행리스트.add((JSONObject) jsonArray.get(i));
            } else {
                하행리스트.add(jsonArray.get(i));
            }
        }

        for (int i = 0; i < 상행리스트.size(); i++) {
            JSONObject content = (JSONObject) jsonArray.get(i);
            int 도착예정시간 = 0;
            String s = (String) content.get("arvlMsg2");
            if (s.indexOf("]") == -1) {
                도착예정시간 = 0;
            }else{
                도착예정시간 = Integer.parseInt(s.substring(1, s.indexOf("]"))) * 3;
                //,"arvlCd":"99"
                String abc = String.valueOf(상행리스트.get(i));
                int cutnum = abc.length();
                String zxc =abc.substring(0,abc.length()-1);
                System.out.println("1 :"+상행리스트.get(i));
                System.out.println("2 :"+zxc);
                String name = "name";
                String comple = zxc+",\""+name+"\":\""+도착예정시간+"\"}";
                System.out.println("@@@ :"+ comple);
                JSONParser parser = new JSONParser();
                JSONObject jsonObject2 = (JSONObject) parser.parse(comple);
                리스트테스트.add(jsonObject2);
            }
            System.out.println("@@@@@@@@abc :"+리스트테스트.toString());



            model.addAttribute("uplist", 리스트테스트);


        }

        }catch(Exception e){

        }
        return "main";
    }
}