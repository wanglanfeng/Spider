/*
 * Copyright (c) Jipzingking 2015.
 */

package spider.mumayi;

import spider.TargetData;
import spider.SpiderCommonConfig;
import spider.SpiderFactory;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

@TargetUrl("http://www.mumayi.com/android-\\d+.html")
@HelpUrl("http://www.mumayi.com/android/*")
@SpiderCommonConfig(domain = "mumayi.com", threadNum = 2, spiderClasses = {Mumayi.class})
public class Mumayi extends TargetData {
    private static final Pattern NAME_AND_VERSION_PATTERN = Pattern.compile(" V");

    @ExtractBy( "//*[@class='iappname']/text()")
    private String nameWithVersion;

    @ExtractBy( "//*[@id=\"classlists\"]/a[3]/text()")
    private String category;

    @ExtractBy( "//a[@class='download']/@href")
    private String downloadLink;

    @ExtractBy(value = "<div id=\"starlist\" class=\"rank_now(\\d+)\"></div>",type = ExtractBy.Type.Regex)
    private Integer star;

    @ExtractBy(value = "//ul[@class='author']/li[2]/text()", notNull = true)
    private String packages;

    @ExtractBy( "//div[@class='ibox']/p[2]/text()")
    private String introduction;

    @ExtractBy( "/html/body/div[5]/div[1]/div[2]/ul/li[3]/text()")
    private String updateDate;

    @ExtractBy( "//ul[@class='author']/li[1]/text()")
    private String author;

    public static void main(String[] args) {
        SpiderFactory.getSpider(Mumayi.class, new String[]{"http://www.mumayi.com/"});
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
        return NAME_AND_VERSION_PATTERN.split(nameWithVersion)[0];
    }

    @Override
    public String getVersion() {
        String version = NAME_AND_VERSION_PATTERN.split(nameWithVersion)[1];
        return version.substring(0, version.length() - 1);
    }

    @Override
    public String getUpdateDate() {
        if (updateDate == null)
            updateDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
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
        return star * 2 / 10;
    }
}
