package modelos;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Components {
    private static Image PLAY_ICON = new Image(Musica.class.getResourceAsStream("/imgs/play-icon.png"));
    private static Image PAUSE_ICON = new Image(Musica.class.getResourceAsStream("/imgs/pause-icon.png"));

    public static ImageView playIcon() {
        return new ImageView(PLAY_ICON);
    }

    public static ImageView pauseIcon() {
        return new ImageView(PAUSE_ICON);
    }

    public static Button btnPlay() {
        ImageView playIcon = playIcon();
        Button btnPlay = new Button();
        btnPlay.setGraphic(playIcon);
        btnPlay.setStyle("-fx-background-radius: 50em; -fx-background-color: #D21E39; -fx-text-fill: white");
        return btnPlay;
    }
}
