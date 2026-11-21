package modelos;

import com.jfoenix.controls.JFXSlider;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class Rodape {
    private Biblioteca biblioteca;
    private Player player;
    private HBox rodape;

    public Rodape(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
    }

    public HBox rodape() {
        rodape = new HBox();
        rodape.setMaxWidth(400);
        rodape.setMaxHeight(80);
        rodape.setPadding(new Insets(15, 20, 15, 20));
        rodape.setStyle("-fx-background-color: rgba(51, 51, 51, 0.8); -fx-background-radius: 7px; -fx-font-size: 24px;");
        rodape.getStyleClass().add("rodape");
        rodape.setVisible(false);

        HBox playerContainer = playerContainer();

        rodape.getChildren().add(playerContainer);

        return rodape;
    }

    public HBox playerContainer() {
        HBox container = new HBox();
        container.setSpacing(6);
        container.setPrefWidth(400);
        container.setAlignment(Pos.CENTER);

        ImageView playIcon = Componentes.playIcon();
        ImageView pauseIcon = Componentes.pauseIcon();

        Button btnPlay = Componentes.btnPlay();
        btnPlay.setPrefSize(50, 50);
        btnPlay.setAlignment(Pos.CENTER);

        VBox controlesMusica = new VBox();

        Label lTitulo = new Label();
        Tooltip tTitulo = new Tooltip();
        lTitulo.setTooltip(tTitulo);

        JFXSlider sliderDuracao = new JFXSlider(0, 100, 0);
        sliderDuracao.setPrefWidth(294);

        controlesMusica.getChildren().addAll(lTitulo, sliderDuracao);

        container.getChildren().addAll(btnPlay, controlesMusica);

        player = Player.getInstance();

        btnPlay.setOnAction(ActionEvent -> {
            if (player.isPlayingProperty().get()) {
                player.pause();
            } else {
                player.retomar();
            }
        });

        player.musicaAtualProperty().addListener((obs, antiga, nova) -> {
            rodape.setVisible(nova != null);
            if (nova != null) {
                lTitulo.setText(nova.getTitulo());
                tTitulo.setText(nova.getTitulo());
            }
        });

        player.isPlayingProperty().addListener((obs, antiga, nova) -> {
            btnPlay.setGraphic(nova ? pauseIcon : playIcon);
        });

        player.progressoProperty().addListener((obs, antigo, nova) -> {
            if (!sliderDuracao.isValueChanging() && !sliderDuracao.isPressed()) {
                sliderDuracao.setValue(nova.doubleValue());
            }
        });

        sliderDuracao.setOnMousePressed(ActionEvent -> {
            player.seekPercentual(sliderDuracao.getValue());
        });
        sliderDuracao.setOnMouseReleased(ActionEvent -> player.seekPercentual(sliderDuracao.getValue()));

        return container;
    }

}
