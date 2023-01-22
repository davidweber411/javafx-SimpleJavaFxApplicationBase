# Description

This library contains functions that simplify the development of JavaFX applications.<br>

You can easily run unit tests that require JavaFX components.<br>
No more annoying errors like "Toolkit not found", "Toolkit already initialized",
"Location is not set" or "Not on FX application thread"  during testing.

There are several helper methods for creating simple and complex dialogs.<br>
You can easily create a .fxml file based dialog and pass parameters to it on a simple way.

What you need to do:

1. Compile the JAR
2. Add the dependency to your project
3. Profit.

# 1. Compile the JAR

### Fully automated

###### Fully automated on Windows:

    1. Do NOT clone the repository, etc.

    2. Execute the batch script "download_and_compile_script.bat" from the project root from github. 
    
    3. The script will do everything for you. 
       Downloading the git repository, executing the gradle task and publishing to your local maven repository. 

###### Fully automated on Linux:

    Linux is cool though, but the shell script is not written yet - shame on me! :( 
    Please compile the JAR manually. 

### Manually

###### Manually on Windows:

    #1 Download the git repository via:
    git clone https://github.com/davidweber411/SimpleJavaFxApplicationBase

    #2 Navigate to the project root via:
    cd SimpleJavaFxApplicationBase

    #3 Run the gradle task publishToMavenLocal via:
    .\gradlew publishToMavenLocal

    The JAR should be located in your local maven repo - regularly here:
    "C:\Users\%username%\.m2\repository\com\wedasoft\SimpleJavaFxApplicationBase\..."

###### Manually on Linux:

    #1 Download the git repository via:
    git clone https://github.com/davidweber411/SimpleJavaFxApplicationBase

    #2 Navigate to the project root and run the gradle task publishToMavenLocal via:
    cd SimpleJavaFxApplicationBase

    #3 Run the gradle task publishToMavenLocal via:
    ./gradlew publishToMavenLocal

    The JAR should be located in your local maven repo - regularly here:
    "~/.m2/repository/com/wedasoft/SimpleJavaFxApplicationBase/..."

# 2. Add the dependency to your project

### Maven

    <!-- Maven looks in the local repository by default. -->
    <dependency>
      <groupId>com.wedasoft</groupId>
      <artifactId>SimpleJavaFxApplicationBase</artifactId>
      <version>version.num.ber</version> <!-- e.g.: 1.0.0 -->
    </dependency>

### Gradle

    repositories {
      mavenLocal()
    }
    dependencies {
      implementation("com.wedasoft:SimpleJavaFxApplicationBase:version.num.ber") // e.g.: 1.0.0
    }

### As JAR

    Please search for "how to add a JAR to my project in IDE X". 

# How to write tests

### Step 1: Prepare the test class(es)

Extend your wished test class(es) with the class "SimpleJavaFxTestBase". That's it.

    class UnitTests extends SimpleJavaFxTestBase { 
    }

### Step 2: Write a unit test

1. Simply write a standard unit test method in your test class.
2. To run code on the JavaFX thread, just invoke runOnJavaFxThreadAndJoin() and pass the code.<br>
   The main thread will wait until the passed code is executed.<br>
   You can invoke runOnJavaFxThreadAndJoin() as often as you like.

IMPORTANT: <br>
Do not use assertions in runOnJavaFxThreadAndJoin().<br>
JUnit will not recognize failed assertions in the JavaFX thread.

    @Test
    void myTest1() throws Exception {
        runOnJavaFxThreadAndJoin(() -> {
            // run this code on the JavaFX thread
            button = new Button("buttonlabel");
            // wait for the JavaFX thread to complete
        });
        assertEquals("buttonlabel", button.getText());
    }

# Example test class

    import javafx.geometry.Dimension2D;
    import javafx.scene.control.Button;
    import org.junit.jupiter.api.*;
    import static org.junit.jupiter.api.Assertions.*;

    class SimpleJavaFxTestBaseTestJfxInitFunction extends SimpleJavaFxTestBase {
    
        FxmlDialog.Builder builder;
        Button button;
    
        @Test
        void testButton() throws Exception {
            runOnJavaFxThreadAndJoin(() -> {
                button = new Button("buttonLabel");
            });
            assertEquals("buttonLabel", button.getText());
            assertNotEquals("wrongLabel", button.getText());
        }

        @Test
        void testDialogBuilding() throws Exception {
            runOnJavaFxThreadAndJoin(() -> {
                builder = new FxmlDialog.Builder(getClass().getResource("/com/wedasoft/simplejavafxtestbase/test_woc.fxml"), new Dimension2D(600, 500));
                builder.setStageTitle("Old StageTitle");
            });
            assertEquals("Old StageTitle", builder.get().getStage().getTitle());
            assertNotNull(builder.get());
            builder.setStageTitle("New StageTitle");
            assertEquals(600, builder.get().getStage().getScene().getWidth());
            assertEquals(500, builder.get().getStage().getScene().getHeight());
            assertEquals("New StageTitle", builder.get().getStage().getTitle());
        }

        @Test
        void multiRunsOnJavaFxThread() throws Exception {
            // step 1
            runOnJavaFxThreadAndJoin(() -> {
                builder = new FxmlDialog.Builder(getClass().getResource("/com/wedasoft/simplejavafxtestbase/test_woc.fxml"), new Dimension2D(600, 500));
                builder.setStageTitle("Old StageTitle");
            });
            assertNotNull(builder.get());
            assertEquals("Old StageTitle", builder.get().getStage().getTitle());
            assertEquals(600, builder.get().getStage().getScene().getWidth());
            assertEquals(500, builder.get().getStage().getScene().getHeight());

            // step 2
            builder.setStageTitle("New StageTitle");
            assertEquals("New StageTitle", builder.get().getStage().getTitle());
    
            // step 3
            assertThrows(Exception.class, () -> runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder(getClass().getResource("/path/does/not/exist/test_woc.fxml"), new Dimension2D(600, 500))));
    
            // step 4
            runOnJavaFxThreadAndJoin(() -> {
                builder = new FxmlDialog.Builder(getClass().getResource("/com/wedasoft/simplejavafxtestbase/test_woc.fxml"), new Dimension2D(1000, 1000));
                builder.setStageTitle("Another StageTitle");
                button2 = new Button("second init button");
            });
            assertEquals(1000, builder.get().getStage().getScene().getWidth());
            assertEquals(1000, builder.get().getStage().getScene().getHeight());
            assertEquals("Another StageTitle", builder.get().getStage().getTitle());
            
            assertNotNull(button2);
            assertEquals("second init button", button2.getText());
        }

    }

# How to create a fxml based dialog and pass arguments

### Step 1: Extend your dialog controller with FxmlDialogControllerBase

      public class YourControllerClass extends FxmlDialogControllerBase {
           
             @Override
             public void onFxmlDialogReady() {
                  // This method is executed, after the fxml dialog creation is complete. 
                  // Load arguments here etc.
             }
         
             @Override
             public void initialize(URL location, ResourceBundle resources) {
                  // This is the JavaFx initialize().
             }
      
      }

### Step 2: Bind the controller class to your fxml file

This is the same like in standard JavaFX.<br>
Either you do this in the SceneBuilder or you do this by code:

      <?xml version="1.0" encoding="UTF-8"?>

      <VBox xmlns:fx="http://javafx.com/fxml"
         fx:controller="com.wedasoft.abc.YourControllerClass">
         ...
      </VBox>

### Step 3: Create and show the fxml dialog and pass arguments

Use the builder class to create your custom fxml dialog. <br>
You can pass argument with passArgumentsToController().

    public void openDialog() throws Exception {
        FxmlDialog.Builder<YourControllerClass> dialogBuilder = new FxmlDialog.Builder<YourControllerClass>(getClass().getResource("/path/to/your/fxml-file.fxml"), null)
                .setStageTitle("My stage title")
                .setStageResizable(true)
                .setModal(false)
                .setCallbackOnDialogClose(() -> System.out.println("CALLBACK ON DIALOG CLOSE!"))
                .setKeySetToCloseDialog(Set.of(KeyCode.ESCAPE))
                .passArgumentsToController(new HashMap<>() {{
                    put("firstname", "David");
                    put("age", "31");
                    put("isMale", "true");
                }});
        FxmlDialog<TestController> dialog = dialogBuilder.get();
        dialog.showAndWait();
    }

### Step 4: Load and compute passed arguments

To load the passed arguments simply invoke getPassedArguments().<br>
After that, just get the wanted argument by its String key out of the map.

      public class YourControllerClass extends FxmlDialogControllerBase {
           
          @Override
          public void onFxmlDialogReady() {
              if (getPassedArguments() != null) {
                  String firstname = getPassedArguments().getOrDefault("firstname", "default name");
                  int age = Integer.parseInt(getPassedArguments().getOrDefault("age", "-1"));
                  boolean isMale = Boolean.parseBoolean(getPassedArguments().getOrDefault("isMale", "false"));
                  System.out.println("firstname=" + firstname);
                  System.out.println("age=" + age);
                  System.out.println("isMale=" + isMale);
              }
          }
      
      }

