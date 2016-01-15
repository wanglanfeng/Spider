/*
 * Copyright (c) Jipzingking 2015.
 */

package spider.qihu;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@TargetUrl("http://zhushou.360.cn/list/index/*")
public class QihuCategory implements AfterExtractor {
    private static final Pattern pagePattern = Pattern.compile("page=(\\d+)");
    private static Pattern TARGET_URL_PATTERN;

    @ExtractByUrl("http://zhushou.360.cn/list/index/.*page=(\\d+).*")
    private int pageNum;

    @ExtractBy(value = "pg.pageCount = (\\d+)", type = ExtractBy.Type.Regex)
    private Integer totalPage;

    @Override
    public void afterProcess(Page page) {
        page.setSkip(true);
        if (pageNum < totalPage) {
            String url = page.getUrl().toString();
            Matcher matcher = pagePattern.matcher(url);
            if (matcher.find()) {
                page.addTargetRequest(matcher.replaceAll("page=" + (pageNum + 1)));
            } else {
                if (url.lastIndexOf('?') == -1)
                    url += '?';
                else
                    url += '&';
                page.addTargetRequest(url + "page=2");
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
