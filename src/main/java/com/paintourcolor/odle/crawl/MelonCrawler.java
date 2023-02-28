package com.paintourcolor.odle.crawl;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;

@RequiredArgsConstructor
public class MelonCrawler implements Crawler{

    private final MelonMapper melonMapper;
    public void crawling(String url) {

        //페이지의 모든 소소 여기에 삽입
        Document doc = null;

        //멜론 사이트와 연결
        try {
            doc = Jsoup.connect(url)
                    //406 오류 방지용 헤더 추가
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //노래 정보 테이블 선택
        Elements element = doc.select("table");

        //title 조회
        Iterator<Element> ie1 = element.select("div.rank01").iterator();

        //singer 조회
        Iterator<Element> ie2 = element.select("div.rank02 span.checkEllipsis").iterator();

        //cover 조회
        Iterator<Element> ie3 = element.select("div.wrap img").iterator();

        //테이블의 마지막까지 조회
        while(ie1.hasNext() && ie2.hasNext() && ie3.hasNext()) {
            melonMapper.insert(ie1.next().text(), ie2.next().text(), ie3.next().attr("src"));
        }
        System.out.println("크롤링 완료");
    }
}
