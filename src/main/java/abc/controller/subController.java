package abc.controller;


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
import java.util.Calendar;
import java.util.Date;

@Controller
public class subController {

    @Autowired
    subwayService sservice;
    // 메인페이지 매핑[ 연결 ]
    @GetMapping("/")
    public String main(Model model) {

        JSONArray uplist = sservice.getMain(1);
        JSONArray downlist = sservice.getMain(2);
            //System.out.println(리스트테스트);
            //System.out.println(리스트테스트2);

        model.addAttribute("uplist", uplist);
        model.addAttribute("downlist", downlist);
        return "main";
    }
    @SneakyThrows
    @GetMapping("/searchcontroller/{inputtext}")
    public String searchcontroller(@PathVariable("inputtext")String inputtext,Model model ){
        JSONArray uplist = sservice.getSearch(inputtext, 1);
        JSONArray downlist = sservice.getSearch(inputtext, 2);
        JSONArray outlist = sservice.getSearch(inputtext, 3);
        JSONArray inlist = sservice.getSearch(inputtext, 4);
        System.out.println(inlist);
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
