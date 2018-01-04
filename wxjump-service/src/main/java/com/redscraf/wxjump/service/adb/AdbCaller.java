package com.redscraf.wxjump.service.adb;

import com.google.common.base.Throwables;
import com.redscraf.wxjump.service.config.WxJumpConfig;
import com.redscraf.wxjump.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

@Slf4j
@Component
public class AdbCaller {

//	private WxJumpConfig wxJumpConfig = SpringContextHolder.getBean("wxJumpConfig");
	@Autowired
	private WxJumpConfig wxJumpConfig ;

	/**
	 * 调用adb长按屏幕
	 * @param timeMilli
	 */
	public void longPress(double timeMilli) {
		try {
			//应该生成随机的 swipe 点
			int x = RandomUtil.random(wxJumpConfig.getScreenWidth());
			int y = RandomUtil.random(wxJumpConfig.getScreenHeight());
			String command = wxJumpConfig.getAdbPath()
					+ " shell input touchscreen swipe " + x + " " + x + " " + y + " " + y + " "
					+ (int) timeMilli;
			Process process = Runtime.getRuntime()
					.exec(command);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String s;
			while ((s = bufferedReader.readLine()) != null) {
				log.info(s);
			}
			process.waitFor();
		} catch (IOException e) {
			log.error(Throwables.getStackTraceAsString(e));
		} catch (InterruptedException e) {
			log.error(Throwables.getStackTraceAsString(e));
		}
	}

	/**
	 * 改进的截图方法<br>
	 */
	public void printScreen() {
		try {
			String[] args = new String[] { "bash", "-c", wxJumpConfig.getAdbPath() + " exec-out screencap -p > " + wxJumpConfig.getScreenshotLocation() };
			String os = System.getProperty("os.name");
			if (os.toLowerCase().startsWith("win")) {
				args[0] = "cmd";
				args[1] = "/c";
			}
			Process p1 = Runtime.getRuntime().exec(args);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p1.getErrorStream(),"GB2312"));
			String s;
			while ((s = bufferedReader.readLine()) != null) {
				log.info(s);
			}
			p1.waitFor();
		} catch (IOException e) {
			log.error(Throwables.getStackTraceAsString(e));
		} catch (InterruptedException e) {
			log.error(Throwables.getStackTraceAsString(e));
		}
	}
}
