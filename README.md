# Description

This library contains functions that simplify the development of JavaFX applications.<br>

- <b>Common Dialogs</b><br>
  Including information-, warning-, error-, confirmation- and input dialogs.


- <b>Complex Dialogs</b><br>
  Create complex .fxml file based dialogs.<br>
  Pass and retrieve arguments without any effort.


- <b>Standard CRUD operations</b><br>
  Create, read, update and delete every entity/entities.<br>
  No need for writing boilerplate code anymore.<br>
  Make use of a simple but powerful finding API using the builder pattern.<br>
  The complete CRUD API is built on top of the Hibernate API - feel free to use it with every database, that is
  supported by
  Hibernate.


- <b>Testing suite</b><br>
  Run unit tests that require JavaFX components with ease, just by extending your test class with one single class.<br>
  No more annoying errors like "Toolkit not found", "Toolkit already initialized","Location is not set" or "Not on FX
  application thread" during testing.


- <b>Other helpful classes</b><br>
  CommonUtils, SystemUtils.

# Requirements

This Java library was built by using JDK 17.<br>
Please make sure that your project is using at least JDK 17 too.

# Dependencies to add

##### Maven

    <!-- Maven looks in the central repository by default. -->
    <dependency>
      <groupId>com.wedasoft</groupId>
      <artifactId>simplejavafxapplicationbase</artifactId>
      <version>1.2.1</version>
    </dependency>

##### Gradle

    repositories {
      mavenCentral()
    }
    dependencies {
      implementation("com.wedasoft:simplejavafxapplicationbase:1.2.1")
    }

# Common Dialogs

##### Information dialogs

    CommonJfxDialogs.createInformationDialog(String message)
    
    CommonJfxDialogs.createInformationDialog(String message, String messageHeader)

##### Warning dialogs

    CommonJfxDialogs.createWarningDialog(String message)
    
    CommonJfxDialogs.createWarningDialog(String message, String messageHeader)

##### Error dialogs

    CommonJfxDialogs.createErrorDialog(String message)
     
    CommonJfxDialogs.createErrorDialog(String message, Exception exceptionForStacktrace)

##### Input dialogs

    CommonJfxDialogs.displayInputDialogAndGetResult(String dialogText)

##### Confirm dialogs

    CommonJfxDialogs.displayConfirmDialogAndGetResult(String headerText, String contentText)
    
    CommonJfxDialogs.displayCloseStageDialog(Stage stageToClose)
    
    CommonJfxDialogs.displayExitProgramDialog()

# Complex Dialogs

Your dream is to create a fxml based dialog and pass arguments into it, and then get the arguments in the displayed
dialog?<br>
You are at the right place.

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
Either you do this in the SceneBuilder or you do this by code in your .fxml file:

      <?xml version="1.0" encoding="UTF-8"?>

      <VBox xmlns:fx="http://javafx.com/fxml"
         fx:controller="com.wedasoft.abc.YourControllerClass">
         ...
      </VBox>

### Step 3: Create and show the fxml dialog and pass arguments

Use the builder class to create your custom fxml dialog. <br>
You can pass arguments with passArgumentsToController().

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
        FxmlDialog<YourControllerClass> dialog = dialogBuilder.get();
        dialog.showAndWait();
    }

### Step 4: Load and compute passed arguments

To load the passed arguments in your controller, simply invoke getPassedArguments().<br>
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

# Standard CRUD operations

Standard CRUD operations are implemented generically, so you do not need to write boilerplate code for this actions.<br>

The "entry class" is "HibernateQueryUtil", which has some subclasses for grouping methods.<br>
This subclasses are example given "Inserter", "Updater", "Deleter" and "Finder".

A session/transaction is created for every action.

This API builts on top of the Hibernate API.
You need to set up the file "hibernate.cfg.xml" in your resources directory.
In this file, you need to do only the standard hibernate things.

Example of the file using the H2 database and mapping the entity "Student":

    <!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

    <hibernate-configuration>
        <session-factory>
            <!-- JDBC Database connection settings -->
            <property name="connection.driver_class">org.h2.Driver</property>
            <property name="connection.url">jdbc:h2:mem:test</property>
            <property name="connection.username">sa</property>
            <property name="connection.password"></property>
            <!-- JDBC connection pool settings ... using built-in test pool -->
            <property name="connection.pool_size">1</property>
            <!-- Select our SQL dialect -->
            <property name="dialect">org.hibernate.dialect.H2Dialect</property>
            <!-- Echo the SQL to stdout -->
            <property name="show_sql">true</property>
            <!-- Set the current session context -->
            <property name="current_session_context_class">thread</property>
            <!-- Drop and re-create the database schema on startup -->
            <property name="hbm2ddl.auto">create-drop</property>
            <!-- dbcp connection pool configuration -->
            <property name="hibernate.dbcp.initialSize">5</property>
            <property name="hibernate.dbcp.maxTotal">20</property>
            <property name="hibernate.dbcp.maxIdle">10</property>
            <property name="hibernate.dbcp.minIdle">5</property>
            <property name="hibernate.dbcp.maxWaitMillis">-1</property>
            <mapping class="com.wedasoft.simpleJavaFxApplicationBase.excludeInJar.hibernateUtil.Student"/>
        </session-factory>
    </hibernate-configuration>

### Insert data

    HibernateQueryUtil.Inserter.insertOne(T entity)
    HibernateQueryUtil.Inserter.insertMany(List<T> entities)

### Update data

    HibernateQueryUtil.Updater.updateOne(T entity)
    HibernateQueryUtil.Updater.updateMany(List<T> entities)

### Delete data

    HibernateQueryUtil.Deleter.deleteOne(T entity) 
    HibernateQueryUtil.Deleter.deleteMany(List<T> entities) 
    HibernateQueryUtil.Deleter.deleteAll(Class<T> entityClass)

### Find data

###### With static condition methods

    HibernateQueryUtil.Finder.findWithBuilder(Student.class)
        .addCondition(Student.Fields.firstName, isEqualTo("David"))
        .addCondition(Student.Fields.id, isEqualTo(27))
        .offset(10)
        .limit(10)
        .orderByInOrderOfList(List.of(
            new Order(Student.Fields.id, true),
            new Order(Student.Fields.lastName, true),
            new Order(Student.Fields.firstName, false)))
        .findAll();

###### With new keyword

    HibernateQueryUtil.Finder.findWithBuilder(Student.class)
        .addCondition(Student.Fields.firstName, new EqualsCondition<>("David"))
        .addCondition(Student.Fields.id, new EqualsCondition<>(27))
        .findAll();

###### Other condition types and examples

    // Matches everything, that is exactly equal to "David":
        .addCondition(Student.Fields.attributeName, isEqualTo("David"))

    // Matches everything, that is lower than 4:
        .addCondition(Student.Fields.attributeName, isLowerThan(4))

    // Matches everything, that is lower than or equal to 4:
        .addCondition(Student.Fields.attributeName, isLowerThanOrEqualTo(4))

    // Matches everything, that is greater than 4:
        .addCondition(Student.Fields.attributeName, isGreaterThan(4))

    // Matches everything, that is greater than or equal to 4:
        .addCondition(Student.Fields.attributeName, isGreaterThanOrEqualTo(4))

    // Matches everything, that is not equal to 4:
        .addCondition(Student.Fields.attributeName, isNotEqualTo(4))

    // Matches everything, that starts exactly with "Dav":
        .addCondition(Student.Fields.attributeName, isLikeCaseSensitive("Dav%"))

    // Matches everything, that ends exactly with "vid":
        .addCondition(Student.Fields.attributeName, isLikeCaseSensitive("%vid"))

    // Matches everything, that contains exactly "avi":
        .addCondition(Student.Fields.attributeName, isLikeCaseSensitive("%avi%"))

    // Matches everything, that does not contain exactly "avi":
        .addCondition(Student.Fields.attributeName, isNotLikeCaseSensitive("%avi%"))

    // Matches everything, that does not start with exactly "Dav":
        .addCondition(Student.Fields.attributeName, isNotLikeCaseSensitive("Dav%"))

    // Matches everything, that contains "avi" in every lower- and uppercase combination ("avi", "Avi", "AVi", "aVI", ...):
        .addCondition(Student.Fields.attributeName, isLikeInCaseSensitive("%avi%"))

    // Matches everything, that does not start with "dav" in every lower- and uppercase combination ("dav", "Dav", "DAV", "dAv", ...):
        .addCondition(Student.Fields.attributeName, isNotLikeInCaseSensitive("dav%"))

    // Matches everything, that does not contain "avi" in every lower- and uppercase combination ("avi", "Avi", "AVi", "aVI", ...):
        .addCondition(Student.Fields.attributeName, isNotLikeInCaseSensitive("%avi%"))

### Other things with data

    HibernateQueryUtil.Count.countAll(Class<T> entityClass)

# Testing suite

### Step 1: Prepare the test class(es)

Extend your wished test class(es) with the class "SimpleJavaFxTestBase". That's it.

    class UnitTests extends SimpleJavaFxTestBase { 
    }

### Step 2: Write a unit test

1. Simply write a standard unit test method in your test class.
2. To run code on the JavaFX thread, just invoke runOnJavaFxThreadAndJoin() and pass the code.<br>
   The main thread will wait until the passed code is executed.<br>
   You can invoke runOnJavaFxThreadAndJoin() as often as you like.

<b>IMPORTANT:</b> <br>
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

### Example test class

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

# Other

### How to embed into your project by compiling the dependency manually

1. Compile the JAR to your Maven Local repository

2. Add the dependency to your project

3. Profit.

##### 1. Compile the JAR to your Maven Local repository

###### Fully automated on Windows:

    1. Do NOT clone the repository, etc.

    2. Execute the batch script "download_and_compile_script.bat" from the project root from github. 
    
    3. The script will do everything for you. 
       Downloading the git repository, executing the gradle task and publishing to your local maven repository. 

###### Fully automated on Linux:

    Linux is cool though, but the shell script is not written yet - shame on me! :( 
    Please compile the JAR manually.

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

##### 2. Add the dependency to your project

###### Maven

    <!-- Maven looks in the central repository by default. -->
    <dependency>
      <groupId>com.wedasoft</groupId>
      <artifactId>simplejavafxapplicationbase</artifactId>
      <version>1.2.1</version>
    </dependency>

###### Gradle

    repositories {
      mavenCentral()
    }
    dependencies {
      implementation("com.wedasoft:simplejavafxapplicationbase:1.2.1")
    }

###### As JAR

    Please search for "how to add a JAR to my project in IDE X". 