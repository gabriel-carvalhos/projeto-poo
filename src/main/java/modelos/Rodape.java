package modelos;

import com.jfoenix.controls.JFXSlider;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Rodape {
    private Biblioteca biblioteca;

    public Rodape(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
    }

    public HBox rodape() {
        HBox rodape = new HBox();
        rodape.setPadding(new Insets(15, 20, 15, 20));
        rodape.setAlignment(Pos.CENTER_LEFT);
        rodape.setStyle("-fx-background-color: rgba(51, 51, 51, 0.5); -fx-font-size: 24px;");
        rodape.getStyleClass().add("rodape");

        int size = biblioteca.getSize();
        Label lQuantidade = new Label(size + " Músicas encontradas");
        Tooltip tQuantidade = new Tooltip(size + " Músicas encontradas");
        lQuantidade.setTooltip(tQuantidade);

        Region espaco = new Region();
        HBox.setHgrow(espaco, Priority.ALWAYS);
        // espaco.setStyle("-fx-background-color: coral;");
        Region espaco2 = new Region();
        // espaco2.setStyle("-fx-background-color: coral;");
        HBox.setHgrow(espaco2, Priority.ALWAYS);

        rodape.getChildren().addAll(lQuantidade, espaco, playerContainer(), espaco2);

        return rodape;
    }

    public HBox playerContainer() {
        HBox container = new HBox();
        container.setSpacing(6);
        container.setPrefWidth(350);

        ImageView playIcon = Components.playIcon();
        ImageView pauseIcon = Components.pauseIcon();

        Button btnPlay = Components.btnPlay();
        btnPlay.setPrefSize(50, 50);
        btnPlay.setAlignment(Pos.CENTER);

        VBox controlesMusica = new VBox();

        Label lTitulo = new Label();
        Tooltip tTitulo = new Tooltip();
        lTitulo.setTooltip(tTitulo);
        JFXSlider sliderDuracao = new JFXSlider(0, 100, 0);
        controlesMusica.getChildren().addAll(lTitulo, sliderDuracao);

        container.getChildren().addAll(btnPlay, controlesMusica);
        container.setVisible(false);

        Player player = Player.getInstance();

        btnPlay.setOnAction(ActionEvent -> {
            if (player.isPlayingProperty().get()) {
                player.pause();
            } else {
                player.retomar();
            }
        });

        player.musicaAtualProperty().addListener((obs, antiga, nova) -> {
            if (nova == null) {
                container.setVisible(false);
                return;
            }

            lTitulo.setText(nova.getTitulo());
            tTitulo.setText(nova.getTitulo());
            container.setVisible(true);
        });

        player.isPlayingProperty().addListener((obs, antiga, nova) -> {
            if (nova) {
                btnPlay.setGraphic(pauseIcon);
                return;
            }

            btnPlay.setGraphic(playIcon);
        });

        return container;
    }
}
