# Description

This library contains functions that simplify the development of JavaFX applications.<br>

- <b>Powerful dialog API</b><br>
  Create complex .fxml file based dialogs. Pass and retrieve arguments without any effort.<br>
  Profit from predefined common information-, warning-, error-, confirmation- and input dialogs.<br>


- <b>Powerful database API</b><br>
  Perform standard CRUD operations on every entity/entities. No need for writing boilerplate code anymore.<br>
  Make use of a simple but very powerful finding API using the builder pattern.<br>
  The complete CRUD API is built on top of the Hibernate API - feel free to use it with every database, that is
  supported by Hibernate.<br>


- <b>Testing suite</b><br>
  Run unit tests that require JavaFX components with ease, just by extending your test class with one single class.<br>
  No more annoying errors like "Toolkit not found", "Toolkit already initialized","Location is not set" or "Not on FX
  application thread" during testing.


- <b>Other helpful classes</b><br>
  CommonUtils, SystemUtils, SuppressWarningStrings.

# Requirements

This Java library was built by using JDK 17.<br>
Please make sure that your project is using at least JDK 17 too.

# Documentation

A complete documentation of this framework can be found on https://wedasoft.com/simple-java-fx-application-base/.

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

    JfxDialogUtil.createInformationDialog(String message)
    
    JfxDialogUtil.createInformationDialog(String message, String messageHeader)

##### Warning dialogs

    JfxDialogUtil.createWarningDialog(String message)
    
    JfxDialogUtil.createWarningDialog(String message, String messageHeader)

##### Error dialogs

    JfxDialogUtil.createErrorDialog(String message)
     
    JfxDialogUtil.createErrorDialog(String message, Exception exceptionForStacktrace)

##### Input dialogs

    JfxDialogUtil.displayInputDialogAndGetResult(String dialogText)

##### Confirm dialogs

    JfxDialogUtil.displayConfirmDialogAndGetResult(String headerText, String contentText)
    
    JfxDialogUtil.displayCloseStageDialog(Stage stageToClose)
    
    JfxDialogUtil.displayExitProgramDialog()

# Complex Dialogs

The entrypoint is the class <code>JfxDialogUtil</code>.

Your dream is to create a fxml based dialog and pass arguments into it, and then get the arguments in the displayed
dialog? You are at the right place.

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
Either you do this in the SceneBuilder or you do this by code in your *.fxml file:

      <?xml version="1.0" encoding="UTF-8"?>

      <VBox xmlns:fx="http://javafx.com/fxml"
         fx:controller="com.wedasoft.abc.YourControllerClass">
         ...
      </VBox>

### Step 3: Create and show the fxml dialog and pass arguments

Use the builder class to create your custom fxml dialog. <br>
You can pass arguments with passArgumentsToController().

    public void openDialog() throws Exception {
        FxmlDialog.Builder<YourControllerClass> dialogBuilder = JfxDialogUtil.createFxmlDialogBuilder(YourControllerClass.class, getClass().getResource("/path/to/your/fxml-file.fxml"))
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
        dialogBuilder.get().showAndWait();
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

The entrypoint is the class <code>HibernateQueryUtil</code>.This class has some subclasses for grouping methods.<br>
This subclasses are example given <code>Inserter</code>, <code>Updater</code>, <code>Deleter</code> and <code>
Finder</code>.

A session/transaction is created for every action.

This API is built on top of the Hibernate API.
You need to set up the file <code>hibernate.cfg.xml</code> in your resources directory.
In this file, you need to do only the standard hibernate things.

Example of the file using the H2 database and mapping the entity "Student":

    <!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

    <hibernate-configuration>
        <session-factory>
            <!-- JDBC Database connection settings -->
            <property name="connection.driver_class">org.h2.Driver</property>

            <!-- Use an in memory database named "test.h2.db" (no file on your computer) -->
            <property name="connection.url">jdbc:h2:mem:test</property>
            <!-- Use a database named "test.h2.db" located at "/home/username/test.h2.db" (file on your computer) -->
            <!-- <property name="connection.url">jdbc:h2:file:/home/username/test</property> -->

            <!-- database login username and password (is set to this properties automatically if you create a new database -->
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
            <!-- Other possible values: -->
            <!-- none:         does nothing with the schema/database. -->
            <!-- validate:     database schema will be validated using the entity mappings. -->
            <!-- create-only:  database schema creation will be generated. -->
            <!-- drop:         database schema will be dropped. -->
            <!-- create:       database schema will first be dropped and then created afterward. -->
            <!-- create-drop:  database schema will be created and will be dropped when the SessionFactory is closed explicitly (application stops). -->
            <!-- update:       database schema will be updated by comparing the existing database schema with the entity mappings. -->

            <!-- dbcp connection pool configuration -->
            <property name="hibernate.dbcp.initialSize">5</property>
            <property name="hibernate.dbcp.maxTotal">20</property>
            <property name="hibernate.dbcp.maxIdle">10</property>
            <property name="hibernate.dbcp.minIdle">5</property>
            <property name="hibernate.dbcp.maxWaitMillis">-1</property>
 
            <!-- mappings from your classes to hibernate/database -->
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
    HibernateQueryUtil.Deleter.deleteAll(Class<T> entityClass, securityFlag)

### Find data

###### With static condition methods

    HibernateQueryUtil.Finder.findWithBuilder(Student.class)
        .addCondition(Student_.FIRST_NAME, isEqualTo("David"))
        .addCondition(Student_.ID, isEqualTo(27))
        .offset(10)
        .limit(10)
        .orderByInOrderOfList(List.of(
            new Order(Student_.ID, true), // 1. id ASC
            new Order(Student_.LAST_NAME, true), // 2. lastName ASC 
            new Order(Student_.FIRST_NAME, false))) // 3. firstName DESC
        .findAll();

###### Other condition types and examples

    // Matches everything, that is exactly equal to "David":
        .addCondition(Student_.<FIELD_NAME>, isEqualTo("David"))

    // Matches everything, that is lower than 4:
        .addCondition(Student_.<FIELD_NAME>, isLowerThan(4))

    // Matches everything, that is lower than or equal to 4:
        .addCondition(Student_.<FIELD_NAME>, isLowerThanOrEqualTo(4))

    // Matches everything, that is greater than 4:
        .addCondition(Student_.<FIELD_NAME>, isGreaterThan(4))

    // Matches everything, that is greater than or equal to 4:
        .addCondition(Student_.<FIELD_NAME>, isGreaterThanOrEqualTo(4))

    // Matches everything, that is not equal to 4:
        .addCondition(Student_.<FIELD_NAME>, isNotEqualTo(4))

    // Matches everything, that starts exactly with "Dav":
        .addCondition(Student_.<FIELD_NAME>, isLikeCaseSensitive("Dav%"))

    // Matches everything, that ends exactly with "vid":
        .addCondition(Student_.<FIELD_NAME>, isLikeCaseSensitive("%vid"))

    // Matches everything, that contains exactly "avi":
        .addCondition(Student_.<FIELD_NAME>, isLikeCaseSensitive("%avi%"))

    // Matches everything, that does not contain exactly "avi":
        .addCondition(Student_.<FIELD_NAME>, isNotLikeCaseSensitive("%avi%"))

    // Matches everything, that does not start with exactly "Dav":
        .addCondition(Student_.<FIELD_NAME>, isNotLikeCaseSensitive("Dav%"))

    // Matches everything, that contains "avi" in every lower- and uppercase combination ("avi", "Avi", "AVi", "aVI", ...):
        .addCondition(Student_.<FIELD_NAME>, isLikeInCaseSensitive("%avi%"))

    // Matches everything, that does not start with "dav" in every lower- and uppercase combination ("dav", "Dav", "DAV", "dAv", ...):
        .addCondition(Student_.<FIELD_NAME>, isNotLikeInCaseSensitive("dav%"))

    // Matches everything, that does not contain "avi" in every lower- and uppercase combination ("avi", "Avi", "AVi", "aVI", ...):
        .addCondition(Student_.<FIELD_NAME>, isNotLikeInCaseSensitive("%avi%"))

###### Count datasets

    HibernateQueryUtil.Finder.countAll(Class<T> entityClass)

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
  
