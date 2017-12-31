package com.redscraf.wxjump.common.contants;

/**
 * Created by RoyZ on 2017/12/29.
 */
public class Constants {
    /**
     * adb所在位置
     */
    public static final String ADB_PATH = "F:\\Android\\sdk\\platform-tools\\adb.exe";
    /**
     * 截屏文件所在位置
     */
    public static final String SCREENSHOT_LOCATION = "C:\\s.png";

    /**
     * 窗体显示的图片宽度
     */
    public static final int RESIZED_SCREEN_WIDTH = 1080;

    /**
     * 窗体显示的图片高度
     */
    public static final int RESIZED_SCREEN_HEIGHT = 1920;

    /**
     * 在675*1200分辨率下，跳跃蓄力时间与距离像素的比率<br>
     * 可根据实际情况自行调整
     */
    public static final float RESIZED_DISTANCE_PRESS_TIME_RATIO = 1.45f;

    /**
     * 截图间隔
     */
    public static final int SCREENSHOT_INTERVAL = 1500; // ms

    /**
     * 手动模式
     */
    public static final int MODE_MANUAL = 1;
    /**
     * 自动模式
     */
    public static final int MODE_AUTO = 2;
    /**
     * 半自动模式,只需要点secondPoint
     */
    public static final int MODE_SEMI_AUTO = 3;
}
