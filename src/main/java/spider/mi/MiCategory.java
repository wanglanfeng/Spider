/*
 * Copyright (c) Jipzingking 2015.
 */

package spider.mi;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@TargetUrl("http://app.mi.com/category/\\d+")
public class MiCategory implements AfterExtractor {
    public static final int PAGE_SIZE = 30;

    @ExtractByUrl("http://app\\.mi\\.com/category/(\\d+)")
    private int categoryID;

    @Override
    public void afterProcess(final Page page) {
        page.setSkip(true);
        page.addTargetRequest("http://app.mi.com/categotyAllListApi?page=1&pageSize=" + PAGE_SIZE + "&categoryId=" + categoryID);
    }
}
