/*
 * Copyright (c) Jipzingking 2015.
 */

package spider.qihu;

import spider.SpiderCommonConfig;
import spider.SpiderFactory;
import spider.TargetData;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@TargetUrl("http://zhushou.360.cn/detail/*")
@HelpUrl("http://zhushou.360.cn/(game|soft|zhuanti|list)/*")
@SpiderCommonConfig(domain = "zhushou.360.cn", spiderClasses = {Qihu.class, QihuCategory.class})
@ExtractBy("/html")//hack for getHtml will lose download link
public class Qihu extends TargetData {
    @ExtractBy("//*[@id=\"app-name\"]/span/text()")
    private String name;

    @ExtractBy("//div[@class=\"base-info\"]/table/tbody/tr[2]/td[1]/text()")
    private String version;

    @ExtractBy(value = "'downloadUrl': '(.+?)'", type = ExtractBy.Type.Regex)
    private String downloadLink;

    @ExtractBy("//*[@id=\"app-info-panel\"]/div/dl/dd/div/span[1]/text()")
    private Double star;

    @ExtractBy("//div[@class=\"breif\"]/text()")
    private String introduction;

    @ExtractBy("//div[@id=\"html-brief\"]/text()")
    private String htmlIntroduction;

    @ExtractBy("//div[@class=\"base-info\"]/table/tbody/tr[1]/td[2]/text()")
    private String updateDate;

    @ExtractBy("//div[@class=\"base-info\"]/table/tbody/tr[1]/td[1]/text()")
    private String author;

    @ExtractBy(value = "'pname': \"(.+?)\"", type = ExtractBy.Type.Regex, notNull = true)
    private String packages;

    public static void main(String[] args) {
        SpiderFactory.getSpider(Qihu.class, new String[]{"http://zhushou.360.cn/"});
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
        return null;
    }

    @Override
    public String getIntroduction() {
        if (introduction == null)
            return htmlIntroduction;
        return introduction;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version.substring(0, version.length() - 2);
    }

    @Override
    public String getUpdateDate() {
        return updateDate;
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
        return (int) (double) star;
    }
}