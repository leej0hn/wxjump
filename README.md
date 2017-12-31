# wxJump
微信跳一跳
---

## 原理
用usb调试安卓手机，用adb截图并用鼠标测量距离，然后计算按压时间后模拟按压。
```
adb shell input swipe <x1> <y1> <x2> <y2> [duration(ms)] (Default: touchscreen) # 模拟长按
adb shell screencap <filename> # 保存截屏到手机
adb pull /sdcard/screen.png # 下载截屏文件到本地
```

## 使用方法
1. 在电脑上下载好adb，并安装JDK7及以上版本以及配置好java环境变量
2. 打开安卓手机的usb调试模式并授权连接的电脑
>  如果是小米手机，在USB调试下方有``USB调试（安全设置）``打开允许模拟点击 感谢[@wotermelon](https://github.com/wotermelon)
3. 打开微信跳一跳，并点击开始
4. 用终端打开adb，并执行一下adb shell，确认adb已经连接上手机后输入exit离开adb shell
> 如果没有连接上，请通过搜索引擎查找原因
5. IDE上运行

### 说明
* 手动模式(manual-mode):弹出的窗口中先点击小人底部适当位置，然后再点想要跳的箱子的中心位置即可完成.
* 半自动模式(semi-mode):只需要点击一次鼠标,即只需要点击终点的中心点就可以了.(默认模式)
* 全自动模式(auto-mode):也就是挂机模式,不需要人工操作,启动后既可以自动识别算法自动帮你玩跳一跳.该模式会忽略 -s 参数,强制使用手机分辨率.

算法源于 https://github.com/easyworld/PlayJumpJumpWithMouse.git 该项目



