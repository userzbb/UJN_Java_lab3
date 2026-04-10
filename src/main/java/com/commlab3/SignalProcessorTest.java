package com.commlab3;

/**
 * 信号处理器测试类
 * 演示Java OOP特性：
 * 1. 接口的使用
 * 2. 抽象类和继承
 * 3. super关键字
 * 4. 匿名内部类
 * 5. Lambda表达式
 */
public class SignalProcessorTest {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   通信工程信号处理系统 - OOP演示");
        System.out.println("========================================\n");

        // 演示1: 多态性 - 不同类型的信号
        System.out.println("【演示1】多态性 - 不同类型的信号");
        System.out.println("----------------------------------------");

        SignalProcessor analog = new AnalogSignal(1000.0, 5.0, "AM");
        SignalProcessor digital = new DigitalSignal(2000.0, 3.0, 9600, "曼彻斯特编码");
        SignalProcessor fiber = new FiberOpticSignal(5000.0, 10.0, "1550nm", 0.2);

        analog.process();
        System.out.println();
        digital.process();
        System.out.println();
        fiber.process();
        System.out.println();

        // 演示2: 使用super关键字调用父类方法
        System.out.println("【演示2】使用super关键字调用父类方法");
        System.out.println("----------------------------------------");
        System.out.println(analog.getSignalInfo());
        System.out.println(digital.getSignalInfo());
        System.out.println(fiber.getSignalInfo());
        System.out.println();

        // 演示3: 匿名内部类 - 创建自定义信号
        System.out.println("【演示3】匿名内部类 - 自定义信号类型");
        System.out.println("----------------------------------------");

        SignalProcessor customSignal = new SignalProcessor() {
            private String name = "自定义无线信号";
            private double freq = 750.0;
            private double amp = 8.0;

            @Override
            public void process() {
                System.out.println("[" + name + "] 开始处理...");
                System.out.println("    频率: " + freq + " MHz");
                System.out.println("    幅度: " + amp);
                System.out.println("    执行特殊的无线信号处理算法...");
            }

            @Override
            public void amplify(double factor) {
                System.out.println("[" + name + "] 无线信号放大，因子=" + factor);
                this.amp *= factor;
                System.out.println("    放大后幅度: " + this.amp);
            }

            @Override
            public String getSignalInfo() {
                return String.format("%s: 频率=%.2fMHz, 幅度=%.2f", name, freq, amp);
            }
        };

        customSignal.process();
        System.out.println();
        customSignal.amplify(2.5);
        System.out.println();
        System.out.println(customSignal.getSignalInfo());
        System.out.println();

        // 演示4: Lambda表达式 - 使用函数式接口
        System.out.println("【演示4】Lambda表达式 - 函数式接口");
        System.out.println("----------------------------------------");

        // 创建信号数组用于后续Lambda演示
        SignalProcessor[] signals = {
            new AnalogSignal(1500.0, 4.0, "FM"),
            new DigitalSignal(3000.0, 2.5, 19200, "NRZ编码"),
            new FiberOpticSignal(8000.0, 12.0, "1310nm", 0.15),
            customSignal  // 使用之前创建的匿名内部类信号
        };

        // Lambda表达式用于快速信号分析
        // 注意：这里使用匿名内部类实现单一抽象方法的接口来演示Lambda
        SignalProcessor lambdaSignal = createLambdaSignalProcessor();
        lambdaSignal.process();
        System.out.println();

        // 使用Lambda表达式处理多个信号操作
        System.out.println("[Lambda演示] 使用Lambda表达式进行信号批量处理:");
        processSignalsWithLambda(signals, (s) -> {
            System.out.println("    [Lambda] 处理信号: " + s.getSignalInfo());
        });
        System.out.println();

        // 演示5: 数组中的多态性
        System.out.println("【演示5】数组中的多态性");
        System.out.println("----------------------------------------");

        System.out.println("处理所有信号:");
        for (SignalProcessor signal : signals) {
            System.out.println("  -> " + signal.getSignalInfo());
            signal.amplify(1.5);
            System.out.println();
        }

        System.out.println("========================================");
        System.out.println("   OOP特性演示完成!");
        System.out.println("========================================");
    }

    /**
     * 使用匿名内部类创建Lambda风格的信号处理器
     * 演示如何通过单一方法接口使用类似Lambda的风格
     */
    private static SignalProcessor createLambdaSignalProcessor() {
        return new SignalProcessor() {
            private String name = "Lambda信号处理器";

            @Override
            public void process() {
                System.out.println("[" + name + "] 这是一个类似Lambda风格创建的信号处理器");
                System.out.println("    执行快速信号分析...");
                System.out.println("    完成频谱分析...");
            }

            @Override
            public void amplify(double factor) {
                System.out.println("[" + name + "] 自动增益控制，因子=" + factor);
            }

            @Override
            public String getSignalInfo() {
                return "Lambda风格信号处理器: 快速分析模式";
            }
        };
    }

    /**
     * 使用Lambda表达式进行批量信号处理
     * @param signals 信号数组
     * @param processor Lambda表达式处理的函数式接口
     */
    private static void processSignalsWithLambda(SignalProcessor[] signals,
            java.util.function.Consumer<SignalProcessor> processor) {
        for (SignalProcessor signal : signals) {
            processor.accept(signal);
        }
    }
}
