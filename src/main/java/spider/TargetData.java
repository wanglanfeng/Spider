/*
 * Copyright (c) Jipzingking 2015.
 */

package spider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.HasKey;

import java.io.PrintWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TargetData implements HasKey, AfterExtractor {
    protected static PrintWriter downloadListWriter;
    protected String url;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    static {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate((Runnable) () -> {
            downloadListWriter.flush();
        }, 60L, 10L, TimeUnit.SECONDS);
    }

    @Override
    public void afterProcess(Page page) {
        url = page.getRequest().getUrl();
        if (getDownloadCount() != null)
            downloadListWriter.println("\"" + getPackages() + "\": " + getDownloadCount());
        int nullNum = 0;
        for (Object o : new Object[]{getAuthor(), getDownloadLink(), getCategory(), getIntroduction(), getName(), getVersion(), getUpdateDate(), getDownloadCount(), getStar()})
            if (o == null) nullNum++;
        if (nullNum != exceptNullNum())
            logger.error(key() + " missing some information");
    }

    public abstract String getDownloadLink();

    public abstract String getAuthor();

    public abstract String getCategory();

    public abstract String getIntroduction();

    public abstract String getName();

    public abstract String getVersion();

    public abstract String getUpdateDate();

    public abstract int exceptNullNum();

    public abstract Long getDownloadCount();

    @Override
    public String key() {
        return getPackages() + 'V' + getVersion();
    }

    public abstract String getPackages();

    public abstract Integer getStar();

    public String getUrl() {
        return url;
    }

    public static void setDownloadListWriter(PrintWriter downloadListWriter) {
        TargetData.downloadListWriter = downloadListWriter;
    }
}