Use [GluonFX plugin for Gradle](https://github.com/gluonhq/gluonfx-gradle-plugin) to produce application native code so it can directly be executed as a native application on the target platform.
And also [this jlink plugin](https://github.com/beryx/badass-jlink-plugin).

Make sure to install graalvm and visual studio see https://docs.gluonhq.com/#platforms_windows for required components
make sure to add the following entry to the system path:
C:\Program Files (x86)\Microsoft Visual Studio\2019\Community\VC\Tools\MSVC\14.25.28610\bin\HostX64\x64

make sure to run the gradle task with the x64 native tools command prompt for vs 2019
make sure to run it as administrator
make sure to run gradlew clean successfully
make sure the app runs successfully (with gradlew run)
make sure to restart the system
make sure to Run vcvarsall.bat located on your machine for example:
"C:\Program Files (x86)\Microsoft Visual Studio 10.0\VC\vcvars[all].bat
see https://stackoverflow.com/a/65423249/8583692
see https://stackoverflow.com/a/50242347/8583692

---

See [this sample build file](https://github.com/acejump/TraceJump/blob/89bbe1e7dea13f20234a1410601895d9eba16174/build.gradle.kts) for how to setup jlink plugin and gluon gradle plugin and so on.

---

See [this repository](https://github.com/acejump/TraceJump/blob/master/src/main/kotlin/org/acejump/tracejump/TraceJump.kt)
for how to create a transparent layer on the screen (can be good for creating a screen dimmer app).

---

If Intellij does not open an fxml file in Scene Builder tab of IDEA and shows error,
it might be because of URLs used in images or something similar.
Try to remove the URLs and then restart the IDEA to fix the problem.

---
To take a screenshot from a node do this preferably when the scene is completely initialized
for example by putting this code in a onClick() callback of a button on that scene.
Note that the class `SwingFXUtils` is in `javafx.swing` module so be sure to add it in gradle.

```Kotlin
val file = File("snapshot${Random().nextInt()}.png")
val snapshot = root.scene.snapshot(null)
val bufferedImage = SwingFXUtils.fromFXImage(snapshot, null)
val imageRGB = BufferedImage(bufferedImage.width, bufferedImage.height, BufferedImage.TRANSLUCENT)
val graphics = imageRGB.createGraphics()
graphics.drawImage(bufferedImage, 0, 0, null)
ImageIO.write(imageRGB, "png", file)
```
---

[Cross-platform tray icon support](https://github.com/dorkbox/SystemTray)

---

Use https://icoconvert.com/ to make .ico files

See [this](https://docs.microsoft.com/en-us/windows/win32/uxguide/vis-icons) for Windows icon design principles.

For how to create custom (big) start menu icon for the app see [this](https://superuser.com/a/1033361/926959).

---

To localize strings in the app, the main class should load the resource bundle like this:

    val resources: ResourceBundle = ResourceBundle.getBundle("messages")

Then whenever loading a new fxml file -in main class or in a controller class-
the variable should be passed around like this:

    FXMLLoader.load(javaClass.getResource("scene.fxml"), resources)

In the controller of the target fxml file declare a resource bundle field like this
and it will be injected automatically by the FXMLLoader:

    @FXML private lateinit var resources: ResourceBundle

Note that name of the field should be "resources".

See [this](https://stackoverflow.com/q/26325403),
[this](https://stackoverflow.com/q/20107463)
and [this](https://stackoverflow.com/q/44124202) for more info.

---

For making the layout right-to-left, set the orientation of the scene and parent node
like this:

    node.nodeOrientation = NodeOrientation.RIGHT_TO_LEFT
    
See [docs](https://wiki.openjdk.java.net/display/OpenJFX/Node+Orientation+in+JavaFX) for more info.

---

See [this post](https://stackoverflow.com/a/49833163) for how to add custom nodes (and enable working with scenes that
contain them) in scene builder

---

See javafx.beans.binding.Bindings class for creating useful bindings with its static methods.

---

To get the controller of a scene do like this:

    val fxmlLoader = FXMLLoader("/fxml/scene-settings.fxml".toURL())
    val settingsController: SettingsController = fxmlLoader.getController()
    val root: Parent = fxmlLoader.load()

---

For how javafx timeline works see [this](https://stackoverflow.com/a/36366805/8583692)

---

// To use spring for dependency injection see [this page](http://www.greggbolinger.com/let-spring-be-your-javafx-controller-factory/)

---

Add shortcut of the app .exe file to `C:\Users\X\AppData\Roaming\Microsoft\Windows\Start Menu\Programs\Startup`
for the app to automatically start on Windows login

---

To make the app follow windows theme refer to these:
 https://stackoverflow.com/q/62289/ and https://stackoverflow.com/q/60837862
 and https://jregistry.sourceforge.io/ and https://stackoverflow.com/q/60321706
 and https://stackoverflow.com/q/38734615/

---

The jRegistry library which is used to acess windows registry values (system theme etc.)
requires two dll files (provided along with the library jar file) to work: reg.dll and reg_x64.dll  
These two files are placed in the `/resources/` directory to be accessed by classpath loader
and also to be available in the Application jar file (because everything in resources is copied into
the jar file; see the exe guide for more info).
At application startup we check to see if these two files are available in the current directory,
and if not, copy them from the classpath (or jar) to the current directory.  
See [here](https://stackoverflow.com/q/1611357) and [here](https://stackoverflow.com/q/2546665)
and [here](https://stackoverflow.com/q/4691095) and [here](https://stackoverflow.com/q/4764347)
and [here](https://stackoverflow.com/q/9006127) and [here](https://stackoverflow.com/a/24738004)
and [here](https://stackoverflow.com/q/2937406) and [here](https://github.com/adamheinrich/native-utils)
and [here](http://www.jdotsoft.com/JarClassLoader.php)

---

See this library for an alternative to javafx: https://github.com/JetBrains/skiko

---

See [kotlin coroutines for javafx](https://github.com/Kotlin/kotlinx.coroutines/tree/master/ui) and its
[guide](https://github.com/Kotlin/kotlinx.coroutines/blob/master/ui/coroutines-guide-ui.md) for how to use kotlin coroutines in a javafx application.

---

A similar application:
https://github.com/roldanjr/pomatez
https://snapcraft.io/stretchly

---

https://medium.com/graalvm/graalvm-to-level-up-javafx-and-gluon-substrate-7ec77a76c386
