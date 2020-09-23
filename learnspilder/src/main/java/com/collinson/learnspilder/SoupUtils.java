package com.collinson.learnspilder;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.rmi.server.ExportException;

/**
 * @author 许瑞锐
 * @date 2020/8/3 17:09
 * @description {java类描述}
 */
@Slf4j
public class SoupUtils {


    public static Document getDocuByUrl(String url) throws Exception {
         log.info(String.format("对地址【%s】发起请求",url));
         return Jsoup.connect(url).get();
    }

}


