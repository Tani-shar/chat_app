import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ChatClient extends Application {
    private VBox messageList;
    private ScrollPane scrollPane;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private String currentRoom;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chat Client");

        // Room code entry
        VBox roomCodeLayout = new VBox(10);
        roomCodeLayout.setAlignment(Pos.CENTER);
        roomCodeLayout.setPadding(new Insets(20));

        TextField roomCodeField = new TextField();
        roomCodeField.setPromptText("Enter room code...");
        Button joinButton = new Button("Join Room");

        roomCodeLayout.getChildren().addAll(roomCodeField, joinButton);

        Scene roomScene = new Scene(roomCodeLayout, 400, 200);
        primaryStage.setScene(roomScene);
        primaryStage.show();

        // Event for joining room
        joinButton.setOnAction(e -> {
            currentRoom = roomCodeField.getText().trim();
            if (!currentRoom.isEmpty()) {
                primaryStage.setScene(createChatScene());
                connectToServer();
                sendMessage("JOIN:" + currentRoom); // Notify the server to join the room
            }
        });

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

    private Scene createChatScene() {
        VBox root = new VBox(10);
        root.setStyle("-fx-background-color: #E5DDD5;");
        root.setPadding(new Insets(10));

        // Messages area
        messageList = new VBox(10);
        messageList.setStyle("-fx-background-color: #E5DDD5; -fx-padding: 10;");

        // Scroll pane for messages
        scrollPane = new ScrollPane(messageList);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefHeight(500);
        scrollPane.setStyle("-fx-background: #E5DDD5; -fx-background-color: #E5DDD5;");

        // Input area
        HBox inputArea = new HBox(10);
        inputArea.setAlignment(Pos.CENTER);
        TextField inputField = new TextField();
        inputField.setPrefWidth(320);
        inputField.setPromptText("Type a message...");
        inputField.setStyle("-fx-padding: 10; -fx-background-radius: 20;");

        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #128C7E; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 20;");

        inputArea.getChildren().addAll(inputField, sendButton);
        root.getChildren().addAll(scrollPane, inputArea);

        // Event handlers
        sendButton.setOnAction(e -> {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                sendMessage("Message:" + message);
                inputField.clear();
            }
        });

        inputField.setOnAction(e -> {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                sendMessage("Message:" + message);
                inputField.clear();
            }
        });

        return new Scene(root, 400, 600);
    }

    private void addMessage(String message, boolean isSent) {
        Platform.runLater(() -> {
            HBox messageContainer = new HBox();
            messageContainer.setPadding(new Insets(5));
            messageContainer.setMaxWidth(messageList.getWidth());

            Label messageLabel = new Label(message);
            messageLabel.setWrapText(true);
            messageLabel.setMaxWidth(250);
            
            // Create a region for flexible spacing
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            VBox bubble = new VBox(messageLabel);
            bubble.setMaxWidth(Control.USE_PREF_SIZE);

            if (isSent) {
                // Right-aligned sent messages (blue bubbles)
                messageContainer.getChildren().addAll(spacer, bubble);
                bubble.setStyle(
                    "-fx-background-color: #0084ff;" +
                    "-fx-background-radius: 15 15 0 15;" +
                    "-fx-padding: 10;"
                );
                messageLabel.setStyle(
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;"
                );
            } else {
                // Left-aligned received messages (gray bubbles)
                messageContainer.getChildren().addAll(bubble, spacer);
                bubble.setStyle(
                    "-fx-background-color: #e9e9e9;" +
                    "-fx-background-radius: 15 15 15 0;" +
                    "-fx-padding: 10;"
                );
                messageLabel.setStyle(
                    "-fx-text-fill: black;" +
                    "-fx-font-size: 14px;"
                );
            }

            messageList.getChildren().add(messageContainer);

            // Auto-scroll to bottom
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
                        String actualMessage = message.substring(6);
                        addMessage(actualMessage, false);
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
        out.println(message);  // Send message to the server
        addMessage(message, true);  // Display the sent message in the chat
    }
}


    public static void main(String[] args) {
        launch(args);
    }
}
