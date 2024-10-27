import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;

public class ChatRoom extends Application {
    private String roomCode;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private VBox chatLayout;

    public ChatRoom(String roomCode) {
        this.roomCode = roomCode;
    }

    @Override
    public void start(Stage primaryStage) {
        chatLayout = new VBox(10);
        chatLayout.setPrefSize(400, 600);

        Label roomLabel = new Label("Room Code: " + roomCode);
        TextField messageField = new TextField();
        Button sendButton = new Button("Send");

        sendButton.setOnAction(e -> {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                sendMessage(message);
                displayMessage("You: " + message);
                messageField.clear();
            }
        });

        chatLayout.getChildren().addAll(roomLabel, messageField, sendButton);
        Scene scene = new Scene(chatLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat Room - " + roomCode);
        primaryStage.show();

        connectToServer();
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
                        displayMessage(message);
                    }
                } catch (IOException e) {
                    displayMessage("Lost connection to server");
                }
            }).start();

        } catch (IOException e) {
            displayMessage("Could not connect to server");
        }
    }

    private void sendMessage(String message) {
        if (out != null) {
            out.println(roomCode + ": " + message); // Prefix with room code
        }
    }

    private void displayMessage(String message) {
        Platform.runLater(() -> {
            Label messageLabel = new Label(message);
            chatLayout.getChildren().add(messageLabel);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
