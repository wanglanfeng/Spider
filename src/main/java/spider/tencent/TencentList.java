/*
 * Copyright (c) Jipzingking 2015.
 */

package spider.tencent;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@TargetUrl("http://sj.qq.com/myapp/cate/appList.htm\\?*")
@ExtractBy(value = "/html/body/text()")
public class TencentList implements AfterExtractor {
    private static final Pattern pageContextPattern = Pattern.compile("pageContext=\\d+");

    @ExtractByUrl("http://sj\\.qq\\.com/myapp/cate/appList.htm\\?.*pageContext=(\\d+).*")
    private Integer pageContext;

    @ExtractByUrl("http://sj\\.qq\\.com/myapp/cate/appList.htm\\?.*pageSize=(\\d+).*")
    private Integer pageSize;

    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$.count")
    private Integer count;

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$.obj[*].pkgName")
    private List<String> pkgNames;

    @Override
    public void afterProcess(final Page page) {
        page.setSkip(true);
        for (String pkgName : pkgNames)
            page.addTargetRequest("http://sj.qq.com/myapp/detail.htm?apkName=" + pkgName);
        if (Objects.equals(count, pageSize))
            page.addTargetRequest(pageContextPattern.matcher(page.getUrl().toString()).replaceAll("pageContext=" + pageContext + pageSize));
    }
}
