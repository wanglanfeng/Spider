/*
 * Copyright (c) Jipzingking 2015.
 */

package spider.mi;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;
import java.util.regex.Pattern;

@TargetUrl("http://app.mi.com/categotyAllListApi\\?*")
@ExtractBy(value = "/html/body/text()")
public class MiCategoryApi implements AfterExtractor {
    private static final Pattern PAGE_PATTERN = Pattern.compile("page=\\d+");

    @ExtractByUrl("http://app\\.mi\\.com/categotyAllListApi\\?.*page=(\\d+).*")
    private Integer pageID;

    @ExtractByUrl("http://app\\.mi\\.com/categotyAllListApi\\?.*pageSize=(\\d+).*")
    private Integer pageSize;

    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$.count")
    private Integer count;

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$.data[*].appId")
    private List<String> appIds;

    @Override
    public void afterProcess(final Page page) {
        page.setSkip(true);
        for (String appId : appIds)
            page.addTargetRequest("http://app.mi.com/detail/" + appId);
        if (count > ++pageID * pageSize)
            page.addTargetRequest(PAGE_PATTERN.matcher(page.getUrl().toString()).replaceAll("page=" + pageID));
    }
}