package com.paintourcolor.odle.crawl;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MelonMapper {

    //db에 저장
    public void insert(String title, String singer, String cover);
}