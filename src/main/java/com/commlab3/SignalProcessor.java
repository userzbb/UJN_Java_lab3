package com.commlab3;

/**
 * 信号处理器接口
 * 定义了所有信号处理器必须实现的方法
 */
public interface SignalProcessor {
    /**
     * 处理信号
     */
    void process();

    /**
     * 放大信号强度
     * @param factor 放大因子
     */
    void amplify(double factor);

    /**
     * 获取信号信息
     * @return 信号信息的字符串描述
     */
    String getSignalInfo();
}
