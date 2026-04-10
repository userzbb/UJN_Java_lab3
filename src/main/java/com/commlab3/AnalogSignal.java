package com.commlab3;

/**
 * 模拟信号类
 * 继承自BaseSignal，演示extends关键字的使用
 * 模拟信号是连续变化的信号
 */
public class AnalogSignal extends BaseSignal {
    private String modulationType;  // 调制类型

    /**
     * 构造函数，使用super调用父类构造函数
     * @param frequency 信号频率
     * @param amplitude 信号幅度
     * @param modulationType 调制类型
     */
    public AnalogSignal(double frequency, double amplitude, String modulationType) {
        super("模拟信号", frequency, amplitude);  // 使用super调用父类构造函数
        this.modulationType = modulationType;
    }

    /**
     * 重写process方法，添加模拟信号特有的处理逻辑
     */
    @Override
    public void process() {
        // 调用父类的process方法
        super.process();
        System.out.println("    调制类型: " + modulationType);
        System.out.println("[" + signalType + "] 连续信号采样中...");
    }

    /**
     * 重写getSignalInfo，添加调制类型信息
     */
    @Override
    public String getSignalInfo() {
        // 调用父类的getSignalInfo方法并扩展
        return super.getSignalInfo() + ", 调制类型=" + modulationType;
    }
}
