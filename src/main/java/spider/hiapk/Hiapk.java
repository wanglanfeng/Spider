/*
 * Copyright (c) Jipzingking 2015.
 */

package spider.hiapk;

import spider.TargetData;
import spider.SpiderCommonConfig;
import spider.SpiderFactory;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@TargetUrl("http://apk.hiapk.com/appinfo/*")
@HelpUrl("http://apk.hiapk.com/(topics|games|apps)*")
@SpiderCommonConfig(domain = "apk.hipak.com", threadNum = 2, spiderClasses = {Hiapk.class})
public class Hiapk extends TargetData {
    @ExtractBy(value = "//*[@id='hidAppId']/@value")
    private String appId;

    @ExtractBy(value = "//*[@id='hidAppName']/@value")
    private String name;

    @ExtractBy(value = "//*[@id='appSoftName']/text()")
    private String nameWithVersion;

    @ExtractBy(value = "//*[@id='categoryLink']/text()")
    private String category;

    @ExtractBy(value = "//*[@id='appInfoDownUrl']/@href")
    private String downloadLink;

    @ExtractBy(value = "//*[@class='star_num']/text()")
    private Double star;

    @ExtractBy(value = "//*[@id='softIntroduce']/text()")
    private String introduction;

    @ExtractBy(value = "//div[@class=\"detail_right\"]/div[1]/div[9]/span[2]/text()")
    private String updateDate;

    @ExtractBy(value = "/html/head/link[1]/@href", notNull = true)
    private String packages;

    @ExtractBy(value = "//div[@class=\"detail_right\"]/div[1]/div[2]/span[2]/text()")
    private String author;

    public static void main(String[] args) {
        SpiderFactory.getSpider(Hiapk.class, new String[]{"http://apk.hiapk.com/apps", "http://apk.hiapk.com/games"});
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
        return nameWithVersion.substring(nameWithVersion.lastIndexOf('(') + 1, nameWithVersion.length() - 2);
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
        return packages.substring(packages.lastIndexOf('/') + 1, packages.length());
    }

    @Override
    public Integer getStar() {
        return (int) (star * 2);
    }
}
