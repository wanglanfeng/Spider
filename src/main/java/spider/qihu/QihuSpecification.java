package spider.qihu;

import spider.SpiderFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.regex.Pattern;

@TargetUrl("http://zhushou.360.cn/detail/*")
public class QihuSpecification extends Qihu {
    private static String[][] categoryUrls = new String[][]{
            {"通讯社交"}, {"http://zhushou.360.cn/list/index/cid/12/"},
            {"购物优惠"}, {"http://zhushou.360.cn/list/index/cid/102230/"},
            {"金融理财"}, {"http://zhushou.360.cn/list/index/cid/102139/"},
            {"健康医疗"}, {"http://zhushou.360.cn/list/index/cid/102233/"}
    };

    public static void main(String[] args) {
        QihuCategory.setTargetUrlPattern(Pattern.compile("http://zhushou.360.cn/detail/.+"));
        for (int i = 0; i < categoryUrls.length; i += 2) {
            SpiderFactory.setWorkPath(categoryUrls[i][0]);
            Spider spider = SpiderFactory.getSpider(QihuSpecification.class, categoryUrls[i + 1]);
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
