# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
mvn compile          # Compile the project
mvn package          # Build JAR to target/img-lab-1.0-SNAPSHOT.jar
mvn clean            # Clean build artifacts
mvn test             # Run tests (scans images/ directory, outputs to image_output/)
```

## Run Commands

```bash
# TUI menu mode
mvn exec:java -Dexec.mainClass="com.imglab.ImageLab"
mvn exec:java -Dexec.mainClass="com.imglab.ImageLab" -Dexec.args="/path/to/image.png"  # with image arg

# CLI mode
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" -Dexec.args="input.png output.png grayscale"
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" -Dexec.args="--batch input/ output/ brightness,30 blur"

# Run JAR directly
java -jar target/img-lab-1.0-SNAPSHOT.jar input.png output.png grayscale
```

## Project Architecture

Java OOP demonstration project using **interface + abstract class + concrete class** three-layer architecture:

```
ImageProcessor (interface) → AbstractImageProcessor (abstract) → *Processor (14 implementations)
```

**Core components:**
- `ImageProcessor` interface - defines `getName()`, `getDescription()`, `process(BufferedImage)`, `process(BufferedImage, BufferedImage)`
- `AbstractImageProcessor` - template method pattern; `process(input)` validates, creates output buffer, calls abstract `process(input, output)`, logs timing
- 14 processors: GrayscaleProcessor, BrightnessProcessor, ContrastProcessor, BlurProcessor, SharpenProcessor, EdgeDetectorProcessor (Sobel), InvertProcessor, FlipProcessor, RotateProcessor, ScaleProcessor, ThresholdProcessor, ErosionProcessor, DilationProcessor
- `ImageUtils` - utility class (private constructor) with `loadImage()`, `saveImage()`, `getPixel()`, `setPixel()`, `clamp()`, `applyKernel()`

**Entry points:**
- `ImageLab` - TUI menu (main class configured in pom.xml)
- `ImageLabCLI` - command-line interface with operation factory pattern using `Function<String[], ImageProcessor>`

**Design patterns:**
- Template method: `AbstractImageProcessor.process()` defines processing skeleton
- Strategy: `ImageProcessor` interface allows runtime algorithm switching
- Factory: operation name maps to processor constructor

## CLI Operations

| Operation | Parameters |
|-----------|------------|
| `grayscale` / `gray` | none |
| `brightness` | integer (e.g., 30, -50) |
| `contrast` | double (e.g., 1.5) |
| `blur` | none |
| `sharpen` | none |
| `edge` | none (Sobel) |
| `invert` | none |
| `flip-h` | none |
| `flip-v` | none |
| `rotate` | degrees (90 multiples) |
| `scale` | factor (e.g., 0.5, 2.0) |
| `threshold` | 0-255 |
| `erode` | none |
| `dilate` | none |
