package org.example;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.function.Consumer;

/** Tela inicial para o usuário escolher a pasta que contém suas músicas. */
public class TelaAdicionarDiretorio {
    private final Stage stage;
    private final Consumer<File> aoSelecionarDiretorio;

    public TelaAdicionarDiretorio(Stage stage, Consumer<File> aoSelecionarDiretorio) {
        this.stage = stage;
        this.aoSelecionarDiretorio = aoSelecionarDiretorio;
    }

    public Scene criarCena() {
        StackPane raiz = new StackPane();
        raiz.getStyleClass().add("tela-diretorio");

        // Cartão azul deslocado atrás do cartão principal, como na referência.
        StackPane cartaoSombra = new StackPane();
        cartaoSombra.getStyleClass().add("cartao-sombra");

        VBox cartao = new VBox(18);
        cartao.setPrefSize(780, 535);
        cartao.setAlignment(Pos.TOP_CENTER);
        cartao.getStyleClass().add("cartao-diretorio");

        Label titulo = new Label("ADICIONAR DIRETÓRIO");
        titulo.getStyleClass().add("titulo-diretorio");

        Label simboloMais = new Label("+");
        simboloMais.setStyle(
                "-fx-font-size: 170px;" +
                        "-fx-text-fill: #080808;" +
                        "-fx-font-family: 'Arial';"
        );

        Button botaoAdicionar = new Button();
        botaoAdicionar.setGraphic(simboloMais);
        botaoAdicionar.getStyleClass().add("botao-adicionar-diretorio");
        botaoAdicionar.setAccessibleText("Selecionar diretório de músicas");
        botaoAdicionar.setOnAction(event -> selecionarDiretorio());

        cartao.getChildren().addAll(titulo, botaoAdicionar);
        StackPane conjuntoCartoes = new StackPane(cartaoSombra, cartao);
        conjuntoCartoes.getStyleClass().add("conjunto-cartoes");

        javafx.scene.layout.Region faixaInferior = new javafx.scene.layout.Region();
        faixaInferior.setPrefHeight(74);
        faixaInferior.setMaxHeight(74);
        faixaInferior.setStyle("-fx-background-color: #3568f5;");
        StackPane.setAlignment(faixaInferior, Pos.BOTTOM_CENTER);

        raiz.getChildren().addAll(faixaInferior, conjuntoCartoes);
        return new Scene(raiz, App.LARGURA, App.ALTURA);
    }

    private void selecionarDiretorio() {
        DirectoryChooser seletor = new DirectoryChooser();
        seletor.setTitle("Selecione a pasta com suas músicas");

        File diretorio = seletor.showDialog(stage);
        // Fechar a janela de seleção sem escolher uma pasta não altera a tela.
        if (diretorio != null) {
            aoSelecionarDiretorio.accept(diretorio);
        }
    }
}
