package org.brower.control;

import lombok.extern.log4j.Log4j2;
import org.brower.pojo.Article;
import org.brower.service.ArticleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
@Controller
public class SearchContentPageControl {

    private static final Logger log = LoggerFactory.getLogger(SearchContentPageControl.class);

    @Autowired
    private ArticleService articleService;

    @GetMapping(value = "search/q/{search_input}/{pageNum}")
    @ResponseBody
    public String searchContent(Model model,@PathVariable("search_input")String search_input,@PathVariable("pageNum")int pageNum){
        //使用queryStringQuery完成单字符串查询
        double startTime = System.currentTimeMillis();
        List<Article> ArticleList = articleService.queryByKey(search_input,10,pageNum);
        double endTime = System.currentTimeMillis();
        model.addAttribute("ArticleList",ArticleList);
        model.addAttribute("size",ArticleList.size());
        model.addAttribute("time",(endTime-startTime)/1000);
        return "search_result";
    }
}
