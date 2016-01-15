/*
 * Copyright (c) Jipzingking 2015.
 */

package spider.mi;

import spider.SpiderCommonConfig;
import spider.SpiderFactory;
import spider.TargetData;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@HelpUrl("http://app.mi.com/((*List)|subject|category|hotCatApp)*")
@TargetUrl("http://app.mi.com/detail/*")
@SpiderCommonConfig(domain = "app.mi.com", sleepTime = 2000, spiderClasses = {Mi.class, MiCategory.class, MiCategoryApi.class})
public class Mi extends TargetData {
    @ExtractBy("//*[@class='intro-titles']/h3/text()")
    private String name;

    @ExtractBy("//div[@class=\"details\"]/ul[1]/li[4]/text()")
    private String version;

    @ExtractBy("//div[@class=\"bread-crumb\"]/ul/li[2]/a/text()")
    private String category;

    @ExtractBy("//*[@class='app-info-down']/a/@href")
    private String downloadLink;

    @ExtractBy(value = "<div class=\"star1-hover star1-(\\d+)\"></div>", type = ExtractBy.Type.Regex)
    private Integer star;

    @ExtractBy(value = "//div[@class=\"details\"]/ul[1]/li[8]/text()", notNull = true)
    private String packages;

    @ExtractBy("//p[@class='pslide']/text()")
    private String introduction;

    @ExtractBy("//div[@class=\"details\"]/ul[1]/li[6]/text()")
    private String updateDate;

    @ExtractBy("//div[@class='intro-titles']/p[1]")
    private String author;

    public synchronized static void main(String[] args) {
        final Spider spider = SpiderFactory.getSpider(Mi.class, new String[]{"http://app.mi.com/", "http://app.mi.com/game", "http://app.mi.com/subjectList"});
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            spider.getSite().getCookies().clear();
            spider.getSite().getAllCookies().clear();
        }, 1L, 1L, TimeUnit.SECONDS);
    }

    @Override
    public String getDownloadLink() {
        return downloadLink;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getIntroduction() {
        return introduction;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getUpdateDate() {
        return updateDate;
    }

    @Override
    public int exceptNullNum() {
        return 1;
    }

    @Override
    public Long getDownloadCount() {
        return null;
    }

    @Override
    public String getPackages() {
        return packages;
    }

    @Override
    public Integer getStar() {
        return star;
    }
}
