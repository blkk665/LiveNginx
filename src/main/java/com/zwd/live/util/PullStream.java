package com.zwd.live.util;

/**
 * @Description
 * @Author pumum
 * @Date 2023/3/7
 */


import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.swing.*;


/**
 * 直播拉流实现
 */
public class PullStream {
    public void getPullStream(String inputPath) throws Exception, org.bytedeco.javacv.FrameRecorder.Exception {
        //创建+设置采集器
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(inputPath);
        grabber.setOption("rtsp_transport", "tcp");
        grabber.setImageWidth(960);
        grabber.setImageHeight(540);
        //开启采集器
        grabber.start();
        //直播播放窗口
        CanvasFrame canvasFrame = new CanvasFrame("陈老农正在浇花");
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvasFrame.setAlwaysOnTop(true);
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        //播流
        while (true){
            //拉流
            Frame frame = grabber.grabImage();
            // 这里有点问题
//            opencv_core.Mat mat = converter.convertToMat(frame);

            canvasFrame.showImage(frame);
        }
    }
}
