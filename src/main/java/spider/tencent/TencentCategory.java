/*
 * Copyright (c) Jipzingking 2015.
 */

package spider.tencent;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@TargetUrl("http://sj.qq.com/myapp/category.htm\\?*")
public class TencentCategory implements AfterExtractor {
    public static final int PAGE_SIZE = 30;

    @ExtractByUrl("http://sj.qq.com/myapp/category.htm\\?(.*)")
    private String info;

    @Override
    public void afterProcess(final Page page) {
        page.setSkip(true);
        page.addTargetRequest("http://sj.qq.com/myapp/cate/appList.htm?" + info + "&pageSize=" + PAGE_SIZE + "&pageContext=" + PAGE_SIZE);
    }
}
