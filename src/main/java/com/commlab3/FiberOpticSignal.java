package com.commlab3;

/**
 * 光纤信号类
 * 继承自BaseSignal，演示extends关键字的使用
 * 光纤信号使用光波传输数据
 */
public class FiberOpticSignal extends BaseSignal {
    private String wavelength;      // 波长
    private double lossRate;         // 损耗率

    /**
     * 构造函数，使用super调用父类构造函数
     * @param frequency 信号频率
     * @param amplitude 信号幅度
     * @param wavelength 波长
     * @param lossRate 损耗率
     */
    public FiberOpticSignal(double frequency, double amplitude, String wavelength, double lossRate) {
        super("光纤信号", frequency, amplitude);  // 使用super调用父类构造函数
        this.wavelength = wavelength;
        this.lossRate = lossRate;
    }

    /**
     * 重写process方法，添加光纤信号特有的处理逻辑
     */
    @Override
    public void process() {
        // 调用父类的process方法
        super.process();
        System.out.println("    波长: " + wavelength);
        System.out.println("    损耗率: " + lossRate + " dB/km");
        System.out.println("[" + signalType + "] 光信号转换/传输中...");
    }

    /**
     * 重写amplify方法，光纤信号放大需要特殊的光放大器
     */
    @Override
    public void amplify(double factor) {
        System.out.println("[" + signalType + "] 光纤信号放大 (使用光放大器)...");
        // 调用父类的amplify方法
        super.amplify(factor);
        // 光纤放大后需要考虑损耗补偿
        System.out.println("    光放大器补偿完成");
    }

    /**
     * 重写getSignalInfo，添加光纤信号特有的信息
     */
    @Override
    public String getSignalInfo() {
        // 调用父类的getSignalInfo方法并扩展
        return super.getSignalInfo() + ", 波长=" + wavelength + ", 损耗率=" + lossRate + "dB/km";
    }
}
