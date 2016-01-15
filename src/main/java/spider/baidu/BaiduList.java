/*
 * Copyright (c) Jipzingking 2015.
 */

package spider.baidu;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@TargetUrl("http://shouji.baidu.com/*/list\\?*cid=\\d+*")
public class BaiduList implements AfterExtractor {
    private static final Pattern pagePattern = Pattern.compile("page_num=(\\d+)");

    @ExtractByUrl("http://shouji.baidu.com/.+/list\\?.*page_num=(\\d+).*")
    private int pageNum;

    @ExtractBy("//div[@class='pager']/@data-total")
    private Integer totalPage;

    private static Pattern TARGET_URL_PATTERN;

    @Override
    public void afterProcess(final Page page) {
        page.setSkip(true);
        if (pageNum < totalPage) {
            String url = page.getUrl().toString();
            Matcher matcher = pagePattern.matcher(url);
            if (matcher.find()) {
                page.addTargetRequest(matcher.replaceAll("page_num=" + (pageNum + 1)));
            } else {
                page.addTargetRequest(url + "&page_num=2");
            }
        }
        if (TARGET_URL_PATTERN != null)
            page.getTargetRequests().removeAll(page.getTargetRequests().stream()
                    .filter(request -> !TARGET_URL_PATTERN.matcher(request.getUrl()).matches())
                    .collect(Collectors.toList()));
    }

    public static void setTargetUrlPattern(Pattern targetUrlPattern) {
        TARGET_URL_PATTERN = targetUrlPattern;
    }
}
