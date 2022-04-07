package abc.controller;


import abc.Dto.SubwayDto;
import abc.service.subwayService;
import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class subController {

    @Autowired
    subwayService sservice;
    // 메인페이지 매핑[ 연결 ]
    @GetMapping("/")
    public String main(Model model) throws Exception {

        List<SubwayDto> subwaylist = sservice.readExcel("정왕");
        List<String> go = sservice.readExcel1(subwaylist.get(0).getSubwayid());

        System.out.println(go.get(0));
        System.out.println(go.get(go.size()-1));

        JSONArray uplist = sservice.getSearch("정왕",go.get(0),go.get(go.size()-1),1);
        JSONArray downlist = sservice.getSearch("정왕",go.get(0),go.get(go.size()-1),2);

        model.addAttribute("subname","정왕");
        model.addAttribute("up",subwaylist.get(0).getBack());
        model.addAttribute("down",subwaylist.get(0).getFront());
        model.addAttribute("uplist", uplist);
        model.addAttribute("downlist", downlist);
        return "main";
    }

    @SneakyThrows
    @GetMapping("/searchcontroller/{inputtext}")
    public String searchcontroller(@PathVariable("inputtext")String inputtext,Model model ){
        String searchtex = inputtext;
        int type = 0;
        List<SubwayDto> subwaylist = sservice.readExcel(searchtex);
        if(searchtex.contains("@")){
            searchtex = searchtex.split("@")[0];
            subwaylist = sservice.readExcel(searchtex);
            if(subwaylist.get(0).getBack().equals(inputtext.split("@")[1])){
                type = 1;
            }
        }
        List<String> go = sservice.readExcel1(subwaylist.get(type).getSubwayid());
        System.out.println("!!!!!!!!!!!!!!!");
        System.out.println("searchtex :" +searchtex);
        System.out.println("go.get(0) :"+go.get(0) + " go.get(go.size()-1) :"+ go.get(go.size()-1));
        System.out.println("!!!!!!!!!!!!!!!");
        JSONArray uplist = sservice.getSearch(searchtex,go.get(0),go.get(go.size()-1), 1);
        JSONArray downlist = sservice.getSearch(searchtex,go.get(0),go.get(go.size()-1), 2);
        JSONArray outlist = sservice.getSearch(searchtex,go.get(0),go.get(go.size()-1), 3);
        JSONArray inlist = sservice.getSearch(searchtex,go.get(0),go.get(go.size()-1), 4);

        System.out.println(inlist);
        model.addAttribute("subname",searchtex);
        model.addAttribute("up",subwaylist.get(type).getBack());
        model.addAttribute("down",subwaylist.get(type).getFront());
        model.addAttribute("uplist", uplist);
        model.addAttribute("downlist", downlist);
        model.addAttribute("outlist", outlist);
        model.addAttribute("inlist", inlist);
        System.out.println("inlist : " + inlist);
        if(uplist == null && downlist == null && outlist == null && inlist == null){
            return "redirect:/";
        }
        return "main";
    }



















}
