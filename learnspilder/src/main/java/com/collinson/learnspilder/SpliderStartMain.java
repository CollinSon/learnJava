package com.collinson.learnspilder;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;

/**
 * @author 许瑞锐
 * @date 2020/8/3 16:58
 * @description 爬虫启动类
 */
@Slf4j
public class SpliderStartMain {

    public static final String NO_CONTENT_TIP = "本页没有章节";

    private static String BASE_WEBSITE_URL="http://www.sikshu.com/";

    private static  Map<String,Object> failSpliderMap=new HashMap<>();
    
    private static Map<String,String> chapterMap=new TreeMap<>();
    
    
    private static void addChapterToMap(Document document,String pattern){
        List<Element> elements = document.select("#chapterlist");

        log.info(elements.toString());


        /*for (Element element : elements){
            String text=element.toString();
            if (text.contains(pattern)){
                String href=element.attr("href").substring(1);
                chapterMap.put(element.text(),BASE_WEBSITE_URL+href);
            }
        }*/
    }

    private static void startSplider(String pattern){
        int page=1;
        String bookUrl = BASE_WEBSITE_URL+ pattern ;
        log.info(String.format("启动爬虫，爬取的书籍是【%s】",bookUrl));
        Document document;
        try {
            document=SoupUtils.getDocuByUrl(bookUrl);
        }catch (Exception e){
            log.error("获取书目录页面内容失败，程序终止",e);
            return;
        }

        addChapterToMap(document,pattern);


        page++;
        //准备爬取第二页

        String nextPageUrl=BASE_WEBSITE_URL+ pattern + "_"+page+"/";
        try {
            document=SoupUtils.getDocuByUrl(nextPageUrl);
        }catch (Exception e){
            log.error("获取下一页页面内容失败，程序终止",e);
            return;
        }

        


    }


    public static void main(String[] args)   {


//        startSplider("90/90723/");

        String url = "http://10.35.0.95:9091/crEnDecryptPro/encryptReqAndSend";

        String json = JSON.toJSONString(new HashMap());
        String response;

        try {
            response= new String(Request.Post(url)
                    .bodyString(json, ContentType.APPLICATION_JSON).connectTimeout(60000).socketTimeout(60000)
                    .execute().returnContent().asBytes(),"UTF-8");
        }catch(Exception e){
            Map<String,Object> badResponseMap=new HashMap<>();
            badResponseMap.put("resultCode","1");
            badResponseMap.put("resultMessage",e.getMessage());
            badResponseMap.put("data",new HashMap());
            response=JSON.toJSONString(badResponseMap);
        }

        Map<String,Object> map = JSON.parseObject(response,HashMap.class);
        System.out.println(map.toString());
    }



}
