import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.*;
import java.net.Socket;

public class ChatClient extends Application {
    private VBox messageList;
    private ScrollPane scrollPane;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chat");

        // Main container
        VBox root = new VBox(10);
        root.setStyle("-fx-background-color: white;");
        root.setPadding(new Insets(10));

        // Messages area
        messageList = new VBox(10);
        messageList.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10;");

        // Scroll pane for messages
        scrollPane = new ScrollPane(messageList);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefHeight(500);
        scrollPane.setStyle("-fx-background: #f0f0f0; -fx-background-color: #f0f0f0;");

        // Input area
        HBox inputArea = new HBox(10);
        inputArea.setAlignment(Pos.CENTER);
        TextField inputField = new TextField();
        inputField.setPrefWidth(320);
        inputField.setPromptText("Type a message...");
        inputField.setStyle("-fx-padding: 10; -fx-background-radius: 20;");

        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #0084ff; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 20;");

        inputArea.getChildren().addAll(inputField, sendButton);

        root.getChildren().addAll(scrollPane, inputArea);

        // Event handlers
        sendButton.setOnAction(e -> {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                inputField.clear();
            }
        });

        inputField.setOnAction(e -> {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                inputField.clear();
            }
        });

        Scene scene = new Scene(root, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Connect to server
        connectToServer();

        // Handle window closing
        primaryStage.setOnCloseRequest(e -> {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            Platform.exit();
        });
    }

    private void addMessage(String message, boolean isSent) {
        Platform.runLater(() -> {
            HBox messageContainer = new HBox();
            messageContainer.setPadding(new Insets(5, 10, 5, 10));
            messageContainer.setMaxWidth(Double.MAX_VALUE);

            Label messageLabel = new Label(message);
            messageLabel.setWrapText(true);
            messageLabel.setMaxWidth(250);

            VBox bubble = new VBox(messageLabel);
            bubble.setMaxWidth(Control.USE_PREF_SIZE);

            if (isSent) {
                messageContainer.setAlignment(Pos.CENTER_RIGHT);
                bubble.setStyle(
                    "-fx-background-color: #0084ff;" +
                    "-fx-background-radius: 20;" +
                    "-fx-padding: 10;"
                );
                messageLabel.setStyle(
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;"
                );
            } else {
                messageContainer.setAlignment(Pos.CENTER_LEFT);
                bubble.setStyle(
                    "-fx-background-color: #e9e9e9;" +
                    "-fx-background-radius: 20;" +
                    "-fx-padding: 10;"
                );
                messageLabel.setStyle(
                    "-fx-text-fill: black;" +
                    "-fx-font-size: 14px;"
                );
            }

            messageContainer.getChildren().add(bubble);
            messageList.getChildren().add(messageContainer);

            // Auto scroll to bottom
            scrollPane.setVvalue(1.0);
        });
    }

    private void addSystemMessage(String message) {
        Platform.runLater(() -> {
            HBox messageContainer = new HBox();
            messageContainer.setAlignment(Pos.CENTER);
            messageContainer.setPadding(new Insets(5));

            Label systemLabel = new Label(message);
            systemLabel.setStyle(
                "-fx-text-fill: #666666;" +
                "-fx-font-size: 12px;" +
                "-fx-padding: 5;"
            );

            messageContainer.getChildren().add(systemLabel);
            messageList.getChildren().add(messageContainer);
            scrollPane.setVvalue(1.0);
        });
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        if (message.startsWith("User: ")) {
                            // Remove "User: " prefix for received messages
                            String actualMessage = message.substring(6);
                            addMessage(actualMessage, false);  // Set to false for received messages
                        } else {
                            addSystemMessage(message);
                        }
                    }
                } catch (IOException e) {
                    if (!socket.isClosed()) {
                        addSystemMessage("Lost connection to server");
                    }
                }
            }).start();

        } catch (IOException e) {
            addSystemMessage("Could not connect to server");
        }
    }

    private void sendMessage(String message) {
        if (out != null) {
            out.println(message);
            addMessage(message, true);  // Set to true for sent messages
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}