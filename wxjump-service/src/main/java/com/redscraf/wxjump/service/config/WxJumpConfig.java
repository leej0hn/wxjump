package com.redscraf.wxjump.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>function:
 * <p>User: leejohn
 * <p>Date: 17/12/31
 * <p>Version: 1.0
 */
@Component
@ConfigurationProperties(prefix="wxjump")
@Data
public class WxJumpConfig {
    private String adbPath;
    private String screenshotLocation;
    private int screenWidth;
    private int screenHeight;
    private float distancePressTimeRatio;
    private int screenshotInterval;
    private int model;
}
