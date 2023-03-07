package com.zwd.live;

import com.zwd.live.util.PushStream;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;


/**
 * @Description
 * @Author pumum
 * @Date 2023/3/7
 */
@SpringBootApplication
@Configuration
public class PushApplication {

    public static void main(String[] args) throws Exception {
        //设置rtmp服务器推流地址(写你自己服务器外网地址)
        String outputPath = "rtmp://192.168.174.128:1935/live/address";
        PushStream recordPush = new PushStream();
        recordPush.getRecordPush(outputPath, 25);
    }

}
