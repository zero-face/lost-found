package com.example.lostfound;

import cn.hutool.Hutool;
import cn.hutool.core.util.RandomUtil;
import com.zero.publish.AutoPublish;
import com.zero.publish.config.ServerConfig;
import com.zero.publish.config.StoreConfig;
import org.apache.commons.lang3.StringUtils;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

/**
 * @Author Zero
 * @Date 2021/9/2 23:34
 * @Since 1.8
 * @Description
 **/
public class MyPublish {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {


        for(int i = 0; i < 20; i++) {
            String s = "F:\\JAVA\\python_study" + RandomUtil.randomString(3);
            shua(s);
        }


    }

    private static void shua(String p) throws FileNotFoundException {
        final AutoPublish autoPublish = new AutoPublish();
        final StoreConfig storeConfig = new StoreConfig();
        storeConfig.setBranch_name("master");
        storeConfig.setGit_username("zero-c");
        storeConfig.setGit_password("");
        storeConfig.setRemote_repo_uri("https://gitee.com/zero-c/lost-found.git");
        storeConfig.setLocal_repo_path(p); //本地仓库代码路径
        storeConfig.setLocal_code_dir("F:\\JAVA\\code\\lost-found\\");//本地提交代码路径
        autoPublish.setStoreConfig(storeConfig);
        //服务器配置
        final ServerConfig serverConfig = new ServerConfig();
        serverConfig.setUsername("root");
        serverConfig.setPort(8888);
        serverConfig.setPassword("");
        serverConfig.setIp("");
        serverConfig.setDir("/application");
        serverConfig.setIsUploadFile(true);
        autoPublish.setServerConfig(serverConfig);
        autoPublish.execute();
    }
}
