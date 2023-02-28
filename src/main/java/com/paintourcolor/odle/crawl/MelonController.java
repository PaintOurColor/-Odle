package com.paintourcolor.odle.crawl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crawl")
public class MelonController {
    private final MelonMapper melonMapper;

    //2시간마다 국내 차트 크롤링
    //@Scheduled(fixedRate = 60 * 1000 * 60 * 2)
    @GetMapping("/korea")
    public void crawlKoreaMelon() {
        MelonCrawler melonCrawler = new MelonCrawler(melonMapper);

        String url = "https://www.melon.com/new/index.htm";
        melonCrawler.crawling(url);
    }

    //해외 차트 크롤링
    @GetMapping("/global")
    public void crawlGlobalMelon() {
        MelonCrawler melonCrawler = new MelonCrawler(melonMapper);

        String url = "https://www.melon.com/new/index.htm#params%5BareaFlg%5D=O&params%5BorderBy%5D=&po=pageObj&startIndex=1";
        melonCrawler.crawling(url);
    }
}