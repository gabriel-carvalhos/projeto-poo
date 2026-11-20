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
        rodape.setMaxWidth(350);
        rodape.setMaxHeight(80);
        rodape.setPadding(new Insets(15, 20, 15, 20));
        rodape.setStyle("-fx-background-color: rgba(51, 51, 51, 0.5); -fx-font-size: 24px;");
        rodape.getStyleClass().add("rodape");
        rodape.setVisible(false);

        HBox playerContainer = playerContainer();
        /* TextField pesquisa = new TextField();
        pesquisa.setStyle("-fx-text-fill: text-color");
        pesquisa.setPromptText("Digite a música desejada");
        pesquisa.textProperty().addListener((obs, antigo, nova) -> {
            int encontradas = biblioteca.filtrar(nova);
            String texto = encontradas + " Músicas encontradas";
            lQuantidade.setText(texto);
            tQuantidade.setText(texto);
        }); */

        rodape.getChildren().add(playerContainer);
        // rodape.setAlignment(Pos.CENTER);

        return rodape;
    }

    public HBox playerContainer() {
        HBox container = new HBox();
        container.setSpacing(6);
        container.setPrefWidth(350);
        container.setAlignment(Pos.CENTER);

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
