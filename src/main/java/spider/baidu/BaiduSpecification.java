package spider.baidu;

import spider.SpiderFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.regex.Pattern;

@TargetUrl("http://shouji.baidu.com/*/item\\?*")
public class BaiduSpecification extends Baidu {
    private static String[][] categoryUrls = new String[][]{
            {"聊天"}, {"http://shouji.baidu.com/software/list?cid=503&boardid=board_100_01"},
            {"社交"}, {"http://shouji.baidu.com/software/list?cid=503&boardid=board_100_02"},
            {"婚恋"}, {"http://shouji.baidu.com/software/list?cid=503&boardid=board_100_03"},
            {"通讯"}, {"http://shouji.baidu.com/software/list?cid=503&boardid=board_100_04"},
            {"网购"}, {"http://shouji.baidu.com/software/list?cid=510&boardid=board_100_023"},
            {"记账"}, {"http://shouji.baidu.com/software/list?cid=510&boardid=board_100_025"},
            {"彩票"}, {"http://shouji.baidu.com/software/list?cid=510&boardid=board_100_026"},
            {"股票基金"}, {"http://shouji.baidu.com/software/list?cid=510&boardid=board_100_027"},
            {"银行"}, {"http://shouji.baidu.com/software/list?cid=510&boardid=board_100_028"},
            {"医疗健康"}, {"http://shouji.baidu.com/software/list?cid=504&boardid=board_100_018"},
            {"购票"}, {"http://shouji.baidu.com/software/list?cid=509&boardid=board_100_050"},
            {"酒店"}, {"http://shouji.baidu.com/software/list?cid=509&boardid=board_100_051"}
    };

    public static void main(String[] args) {
        BaiduList.setTargetUrlPattern(Pattern.compile("http://shouji.baidu.com/.+/item\\?.+"));
        for (int i = 0; i < categoryUrls.length; i += 2) {
            SpiderFactory.setWorkPath(categoryUrls[i][0]);
            Spider spider = SpiderFactory.getSpider(BaiduSpecification.class, categoryUrls[i + 1]);
            while (spider.getStatus() != Spider.Status.Stopped) {
                try {
                    Thread.sleep(100 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Finish");
    }

    @Override
    public void afterProcess(Page page) {
        super.afterProcess(page);
        page.getTargetRequests().clear();
    }
}
