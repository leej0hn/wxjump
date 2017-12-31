package com.redscraf.wxjump.service.adb;

import com.google.common.base.Throwables;
import com.redscraf.wxjump.service.config.WxJumpConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
			Process process = Runtime.getRuntime()
					.exec(wxJumpConfig.getAdbPath() + " shell input touchscreen swipe 200 200 180 180 " + (int) timeMilli);
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
