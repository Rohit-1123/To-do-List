import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TodoApp extends Application {

    private final ObservableList<String> tasks = FXCollections.observableArrayList();
    private final String FILE_NAME = "tasks.txt";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("📝 To-Do List App");

        // --- UI Elements ---
        TextField taskInput = new TextField();
        taskInput.setPromptText("Enter a new task...");

        Button addButton = new Button("Add");
        Button deleteButton = new Button("Delete Selected");
        Button clearButton = new Button("Clear All");

        ListView<String> taskListView = new ListView<>(tasks);

        // --- Button Actions ---
        addButton.setOnAction(e -> {
            String task = taskInput.getText().trim();
            if (!task.isEmpty()) {
                tasks.add(task);
                taskInput.clear();
                saveTasks();
            }
        });

        deleteButton.setOnAction(e -> {
            String selected = taskListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                tasks.remove(selected);
                saveTasks();
            }
        });

        clearButton.setOnAction(e -> {
            tasks.clear();
            saveTasks();
        });

        // --- Layout ---
        HBox inputLayout = new HBox(10, taskInput, addButton);
        HBox buttonLayout = new HBox(10, deleteButton, clearButton);
        VBox mainLayout = new VBox(15, inputLayout, taskListView, buttonLayout);
        mainLayout.setPadding(new Insets(15));

        // --- Load tasks from file ---
        loadTasks();

        Scene scene = new Scene(mainLayout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadTasks() {
        try {
            if (Files.exists(Paths.get(FILE_NAME))) {
                tasks.addAll(Files.readAllLines(Paths.get(FILE_NAME)));
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }

    private void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String task : tasks) {
                writer.write(task);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
