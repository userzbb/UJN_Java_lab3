package com.commlab3;

/**
 * 数字信号类
 * 继承自BaseSignal，演示extends关键字的使用
 * 数字信号是离散的信号
 */
public class DigitalSignal extends BaseSignal {
    private int bitRate;             // 比特率
    private String encodingScheme;   // 编码方案

    /**
     * 构造函数，使用super调用父类构造函数
     * @param frequency 信号频率
     * @param amplitude 信号幅度
     * @param bitRate 比特率
     * @param encodingScheme 编码方案
     */
    public DigitalSignal(double frequency, double amplitude, int bitRate, String encodingScheme) {
        super("数字信号", frequency, amplitude);  // 使用super调用父类构造函数
        this.bitRate = bitRate;
        this.encodingScheme = encodingScheme;
    }

    /**
     * 重写process方法，添加数字信号特有的处理逻辑
     */
    @Override
    public void process() {
        // 调用父类的process方法
        super.process();
        System.out.println("    比特率: " + bitRate + " bps");
        System.out.println("    编码方案: " + encodingScheme);
        System.out.println("[" + signalType + "] 数字信号采样/量化中...");
    }

    /**
     * 重写amplify方法，数字信号放大有特殊处理
     */
    @Override
    public void amplify(double factor) {
        System.out.println("[" + signalType + "] 数字信号放大处理...");
        // 调用父类的amplify方法
        super.amplify(factor);
        // 数字信号放大后可能需要重新量化
        System.out.println("    信号重新量化完成");
    }

    /**
     * 重写getSignalInfo，添加数字信号特有的信息
     */
    @Override
    public String getSignalInfo() {
        // 调用父类的getSignalInfo方法并扩展
        return super.getSignalInfo() + ", 比特率=" + bitRate + "bps, 编码=" + encodingScheme;
    }
}
