/*
 * Copyright (c) Jipzingking 2015.
 */

package spider.baidu;

import spider.SpiderCommonConfig;
import spider.SpiderFactory;
import spider.TargetData;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@HelpUrl("http://shouji.baidu.com/*/list\\?*")
@TargetUrl("http://shouji.baidu.com/*/item\\?*")
@SpiderCommonConfig(domain = "shouji.baidu.com", spiderClasses = {Baidu.class, BaiduList.class})
public class Baidu extends TargetData {
    @ExtractBy("//*[@class=\"app-name\"]/span/text()")
    private String name;

    @ExtractBy("//*[@class=\"version\"]/text()")
    private String version;

    @ExtractBy("//*[@class=\"app-nav\"]/div[@class=\"nav\"]/span[3]/a/text()")
    private String category;

    @ExtractBy("//a[@class=\"apk\"]/@href")
    private String downloadLink;

    @ExtractBy(value = "<span class=\"star-percent\" style=\"width:(\\d+)%\"></span>", type = ExtractBy.Type.Regex)
    private Integer star;

    @ExtractBy(value = "//a[@data_package]/@data_package", notNull = true)
    private String packages;

    @ExtractBy("//p[@class=\"content\"]/text()")
    private String introduction;

    @ExtractBy("//div[@class='detail']/div/span[2]/span[2]/text()")
    private String author;

    @ExtractBy("//div[@class='detail']/div/a/text()")
    private String author2;

    public static void main(String[] args) {
        SpiderFactory.getSpider(Baidu.class, new String[]{"http://shouji.baidu.com/game/", "http://shouji.baidu.com/software/"});
    }

    @Override
    public String getDownloadLink() {
        return downloadLink;
    }

    @Override
    public String getAuthor() {
        return author == null ? author2 : author;
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
        return version.substring(4, version.length());
    }

    @Override
    public String getUpdateDate() {
        return null;
    }

    @Override
    public int exceptNullNum() {
        return 2;
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
        return star / 10;
    }
}
