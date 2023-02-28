package com.paintourcolor.odle.crawl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MelonController {
    private final MelonMapper melonMapper;

    //국내 차트 크롤링
    @GetMapping("/crawl/korea")
    public void crawlKoreaMelon() {
        MelonCrawler melonCrawler = new MelonCrawler(melonMapper);

        String url = "https://www.melon.com/new/index.htm";
        melonCrawler.crawling(url);
    }

    //해외 차트 크롤링
    @GetMapping("/crawl/global")
    public void crawlGlobalMelon() {
        MelonCrawler melonCrawler = new MelonCrawler(melonMapper);

        String url = "https://www.melon.com/new/index.htm#params%5BareaFlg%5D=O&params%5BorderBy%5D=&po=pageObj&startIndex=1";
        melonCrawler.crawling(url);
    }
}