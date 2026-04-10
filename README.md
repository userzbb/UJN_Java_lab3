# Image Lab - 图像处理实验室

基于 Java 的图像处理工具，演示 Java 面向对象编程核心概念。

## 功能特性

- **14种图像处理操作**：灰度、亮度、对比度、模糊、锐化、边缘检测、颜色反转、翻转、旋转、缩放、阈值二值化、腐蚀、膨胀
- **双交互模式**：TUI 菜单驱动 / CLI 命令行批处理
- **纯 Java 实现**：无外部依赖，使用标准库 `javax.imageio`

## 环境要求

- JDK 17+
- Maven 3.x

查看 Java 版本：
```bash
java -version
javac -version
```

---

## 编译构建

### 编译项目

```bash
mvn compile
```

编译后的 class 文件位于 `target/classes/`

### 打包 JAR

```bash
mvn package
```

生成 JAR 文件：`target/img-lab-1.0-SNAPSHOT.jar`

### 清理构建

```bash
mvn clean
```

---

## 运行

### TUI 菜单模式（交互式）

```bash
mvn exec:java -Dexec.mainClass="com.imglab.ImageLab"
```

启动后显示文字菜单，输入数字选择操作。可多次处理同一图像。

**命令行参数**（可选）：直接指定输入图像
```bash
mvn exec:java -Dexec.mainClass="com.imglab.ImageLab" -Dexec.args="/path/to/image.png"
```

### CLI 命令行模式（单文件）

```bash
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="输入图像.png 输出图像.png 操作 [参数]"
```

示例：
```bash
# 灰度转换
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="input.png output.png grayscale"

# 亮度调整 (+30)
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="input.png output.png brightness 50"

# 旋转 90度
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="input.png output.png rotate 90"

# 缩放 2倍
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="input.png output.png scale 2.0"
```

### CLI 批处理模式

```bash
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="--batch 输入目录/ 输出目录/ 操作1 操作2 ..."
```

示例：
```bash
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="--batch ./images/ ./output/ grayscale blur"
```

---

## 支持的操作

| 操作 | 命令 | 参数说明 |
|------|------|----------|
| 灰度转换 | `grayscale` / `gray` | 无 |
| 亮度调整 | `brightness` | 整数值，如 `30`（正数变亮，负数变暗） |
| 对比度调整 | `contrast` | 小数值，如 `1.5`（大于1增加对比度） |
| 模糊 | `blur` | 无 |
| 锐化 | `sharpen` | 无 |
| 边缘检测 | `edge` | 无（Sobel算子） |
| 颜色反转 | `invert` | 无 |
| 水平翻转 | `flip-h` | 无 |
| 垂直翻转 | `flip-v` | 无 |
| 旋转 | `rotate` | 度数（90的倍数），如 `90`、`180` |
| 缩放 | `scale` | 比例，如 `0.5`（缩小）、`2.0`（放大） |
| 阈值二值化 | `threshold` | 阈值0-255，如 `128` |
| 腐蚀 | `erode` | 无 |
| 膨胀 | `dilate` | 无 |

---

## 项目结构

```
img-lab/
├── pom.xml                                    # Maven 配置
├── README.md                                  # 本文件
├── REPORT.md                                  # 实验报告
└── src/main/java/com/imglab/
    ├── ImageLab.java                          # TUI 主入口
    ├── ImageLabCLI.java                       # CLI 入口
    ├── processor/
    │   ├── ImageProcessor.java               # 接口
    │   ├── AbstractImageProcessor.java       # 抽象基类
    │   └── *Processor.java                   # 13个实现类
    └── util/
        └── ImageUtils.java                   # 工具类
```

---

## 直接运行 JAR

打包后可脱离 Maven 直接运行：

```bash
java -jar target/img-lab-1.0-SNAPSHOT.jar
```

或者带参数：
```bash
java -jar target/img-lab-1.0-SNAPSHOT.jar input.png output.png grayscale
```

---

## TUI 菜单说明

运行 TUI 模式后：

```
=== 图像处理实验室 ===

--- 主菜单 ---
当前图像: 未加载

  1. 灰度转换
  2. 亮度调整 (+30)
  3. 对比度调整 (1.5x)
  4. 模糊
  5. 锐化
  6. 边缘检测 (Sobel)
  7. 颜色反转
  8. 水平翻转
  9. 垂直翻转
 10. 旋转 90°
 11. 缩放 1.5x
 12. 阈值二值化
 13. 腐蚀
 14. 膨胀
 98. 演示 Lambda 表达式
 99. 演示 匿名内部类
  0. 退出
```

输入数字选择操作。选择后会提示输入图像路径（如果未在命令行指定）。

处理完成后可选择保存结果或继续其他操作。

---

## 演示功能

程序内置两个演示功能（菜单选项 98、99）：

- **选项 98 - Lambda 表达式**：演示 stream API + Lambda 过滤处理器列表
- **选项 99 - 匿名内部类**：演示用匿名内部类创建自定义"棕褐色"处理器

---

## 常见问题

**Q: 提示 "文件不存在"**
A: 检查图像路径是否正确，确保文件存在且格式受支持（PNG、JPG、BMP）

**Q: 编译报错 "release 版本不匹配"**
A: 确保 JDK 版本 >= 17，查看 `pom.xml` 中的 `<release>17</release>`

**Q: 批处理模式找不到文件**
A: 确保输入目录存在且包含 .png/.jpg/.jpeg/.bmp 文件
