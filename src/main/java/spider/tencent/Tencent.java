/*
 * Copyright (c) Jipzingking 2015.
 */

package spider.tencent;

import com.google.common.collect.ImmutableMap;
import org.slf4j.LoggerFactory;
import spider.TargetData;
import spider.SpiderCommonConfig;
import spider.SpiderFactory;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@HelpUrl("http://sj.qq.com/myapp/category.htm\\?*")
@TargetUrl("http://sj.qq.com/myapp/detail.htm\\?*")
@SpiderCommonConfig(domain = "sj.qq.com", threadNum = 5, spiderClasses = {Tencent.class, TencentCategory.class, TencentList.class})
public class Tencent extends TargetData {
    @ExtractBy("//*[@class='det-name-int']/text()")
    private String name;

    @ExtractBy("//*[@id=\"J_DetCate\"]/text()")
    private String category;

    @ExtractBy("//a[@data-apkurl]/@data-apkurl")
    private String downloadLink;

    @ExtractBy("//*[@class='det-star-box']/div[2]/text()")
    private String star;

    @ExtractBy(value = "//a[@apk]/@apk", notNull = true)
    private String packages;

    @ExtractBy("//*[@id=\"J_DetAppDataInfo\"]/div/text()")
    private String introduction;

    @ExtractBy("//div[@class='det-othinfo-container']/div[2]/text()")
    private String version;

    @ExtractBy("//*[@id=\"J_DetDataContainer\"]/div/div[3]/div[6]/text()")
    private String author;

    private static final Map<Pattern, Long> downloadCountPattern = ImmutableMap.of(
            Pattern.compile("(\\d+|\\d+\\.\\d+)亿下载"), 100000000L,
            Pattern.compile("(\\d+|\\d+\\.\\d+)万下载"), 10000L,
            Pattern.compile("(\\d+)下载"), 1L
    );

    @ExtractBy("//*[@id=\"J_DetDataContainer\"]/div/div[1]/div[2]/div[3]/div[1]/text()")
    private String downloadCount;

    public static void main(String[] args) {
        SpiderFactory.getSpider(Tencent.class, new String[]{"http://sj.qq.com/myapp/"});
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
        return version.substring(1, version.length()).replace(':', '.');
    }

    @Override
    public String getUpdateDate() {
        return null;
    }

    @Override
    public int exceptNullNum() {
        return getAuthor() == null ? 2 : 1;
    }

    @Override
    public Long getDownloadCount() {
        Long downloadCount = null;
        for (Map.Entry<Pattern, Long> entry : downloadCountPattern.entrySet()) {
            Matcher matcher = entry.getKey().matcher(this.downloadCount);
            if (matcher.matches())
                if (downloadCount != null)
                    LoggerFactory.getLogger(getClass()).error("Multi download count found: " + downloadCount + " " + (long) (Double.parseDouble(matcher.group(1)) * entry.getValue()));
                else
                    downloadCount = (long) (Double.parseDouble(matcher.group(1)) * entry.getValue());
        }
        return downloadCount;
    }

    @Override
    public String getPackages() {
        return packages;
    }

    @Override
    public Integer getStar() {
        return (int) (Double.valueOf(star.substring(0, star.length() - 1)) * 2);
    }
}
