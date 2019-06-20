package net.faxuan.choujiang.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * FileName: GetProperties.java
 * Author: song
 * Date: 2019/6/14 13:23
 * To change this template use File | Settings | File and Code Templates | Includes | File Header
 * Description:
 */
public class GetProperties {
    private Properties properties = new Properties();
    private String getActivateUrl;
    private String drawUrl;

    public GetProperties() {
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
            this.getActivateUrl = properties.getProperty("getActivateInfo");
            this.drawUrl = properties.getProperty("draw");
        } catch (IOException e) {
            try {
                properties.load(new FileReader(new File(getConfigPath())));
                this.getActivateUrl = properties.getProperty("getActivateInfo");
                this.drawUrl = properties.getProperty("draw");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 获取jar包文件同目录下的config/config.properties文件的路径
     * @return
     */
    private String getConfigPath() {
        String path = System.getProperty("GetProperties.class.path");
        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) + 1;
        path = path.substring(firstIndex, lastIndex);
        return path + "\\config\\config.properties";
    }

    public GetProperties getProperties() {
        return this;
    }


    public String getGetActivateUrl() {
        return getActivateUrl;
    }

    public void setGetActivateUrl(String getActivateUrl) {
        this.getActivateUrl = getActivateUrl;
    }

    public String getDrawUrl() {
        return drawUrl;
    }

    public void setDrawUrl(String drawUrl) {
        this.drawUrl = drawUrl;
    }
}
