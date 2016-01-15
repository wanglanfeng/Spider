/*
 * Copyright (c) Jipzingking 2015.
 */

package spider;

import org.apache.commons.collections.functors.ExceptionClosure;
import org.apache.log4j.*;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.pipeline.JsonFilePageModelPipeline;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

import javax.management.JMException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SpiderFactory {
    public static final int RETRY_TIMES = 100;
    public static final String ROOT_PATH = "Spider";
    public static final Level logLevel = Level.WARN;

    static {
        Logger.getRootLogger().setLevel(logLevel);
    }

    private static String workPath = "";

    public static Spider getSpider(final Class<? extends TargetData> targetData, final String[] startUrls) {
        try {
            SpiderCommonConfig spiderCommonConfig = targetData.getAnnotation(SpiderCommonConfig.class);
            Class<?>[] extraClasses = spiderCommonConfig.spiderClasses();
            extraClasses[0] = targetData;
            String path = ROOT_PATH + File.separator + spiderCommonConfig.domain() + workPath;
            Logger.getRootLogger().removeAllAppenders();
            BasicConfigurator.configure();
            Logger.getRootLogger().addAppender(new FileAppender(new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN), path + File.separator + "log.txt"));
            File file = new File(path + File.separator + spiderCommonConfig.domain());
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
            final Site site = new Site().setDomain(spiderCommonConfig.domain()).setSleepTime(spiderCommonConfig.sleepTime()).setCycleRetryTimes(RETRY_TIMES);
            Spider spider = OOSpider.create(site, new JsonFilePageModelPipeline(path), extraClasses)
                    .addUrl(startUrls)
                    .thread(spiderCommonConfig.threadNum())
                    .setScheduler(new FileCacheQueueScheduler(path))
                    .setExitWhenComplete(true);
            SpiderMonitor.instance().register(spider);
            TargetData.setDownloadListWriter(new PrintWriter(new FileWriter(path + File.separator + "download.json", true)));
            spider.start();
            return spider;
        } catch (Exception e) {
            //just let it die.
            throw new RuntimeException(e);
        }
    }

    public static void setWorkPath(String workPath) {
        if (workPath == null)
            SpiderFactory.workPath = "";
        else
            SpiderFactory.workPath = File.separator + workPath;
    }
}
