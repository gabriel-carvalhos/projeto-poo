package modelos;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Rodape {
    private Biblioteca biblioteca;

    public Rodape(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
    }

    public HBox rodape() {
        HBox rodape = new HBox();
        rodape.setPadding(new Insets(0, 20, 0, 20));
        rodape.setPrefHeight(60);
        rodape.setAlignment(Pos.CENTER_LEFT);
        rodape.setStyle("-fx-background-color: rgba(51, 51, 51, 0.5); -fx-font-size: 24px;");
        rodape.getStyleClass().add("rodape");

        Label lQuantidade = new Label(biblioteca.getSize() + " Músicas Encontradas");

        rodape.getChildren().addAll(lQuantidade);

        return rodape;
    }
}
