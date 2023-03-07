package com.zwd.live;

/**
 * @Description
 * @Author pumum
 * @Date 2023/3/7
 */

import com.zwd.live.util.PullStream;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * 直播拉流--启动
 */
@SpringBootApplication
@Configuration
public class PullApplication {
    public static void main(String[] args) throws Exception {
        //rtmp服务器拉流地址(自己服务器外网地址)
        String inputPath = "rtmp://192.168.174.128/live/address";
        PullStream pullStream = new PullStream();
        pullStream.getPullStream(inputPath);
    }
}
