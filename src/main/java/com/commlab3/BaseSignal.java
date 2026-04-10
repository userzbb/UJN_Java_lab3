package com.commlab3;

/**
 * 信号抽象基类
 * 实现了SignalProcessor接口，提供信号处理的基类实现
 * 演示extends关键字和super关键字的使用
 */
public abstract class BaseSignal implements SignalProcessor {
    // 保护字段，子类可以直接访问
    protected double frequency;      // 信号频率 (Hz)
    protected double amplitude;       // 信号幅度
    protected String signalType;      // 信号类型

    /**
     * 构造函数，使用super调用父类初始化
     * @param signalType 信号类型
     * @param frequency 信号频率
     * @param amplitude 信号幅度
     */
    public BaseSignal(String signalType, double frequency, double amplitude) {
        this.signalType = signalType;
        this.frequency = frequency;
        this.amplitude = amplitude;
    }

    /**
     * 默认的getSignalInfo实现
     * 子类可以通过super.getSignalInfo()调用此方法
     * @return 信号基本信息
     */
    @Override
    public String getSignalInfo() {
        return String.format("%s: 频率=%.2fHz, 幅度=%.2f",
                signalType, frequency, amplitude);
    }

    /**
     * 模板方法模式 - process方法的基本实现
     * 子类可以通过重写自定义处理逻辑，但可以调用super.process()
     */
    @Override
    public void process() {
        System.out.println("[" + signalType + "] 开始处理信号...");
        System.out.println("    频率: " + frequency + " Hz");
        System.out.println("    幅度: " + amplitude);
    }

    /**
     * 放大信号实现
     * @param factor 放大因子
     */
    @Override
    public void amplify(double factor) {
        System.out.println("[" + signalType + "] 放大信号，因子=" + factor);
        this.amplitude *= factor;
        System.out.println("    放大后幅度: " + this.amplitude);
    }
}
