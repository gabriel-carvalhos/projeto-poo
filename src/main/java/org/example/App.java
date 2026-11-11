package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import modelos.Biblioteca;

/**
 * JavaFX App
 */
public class App extends Application {
    public static int LARGURA = 640;
    public static int ALTURA = 480;

    @Override
    public void start(Stage stage) {
        Biblioteca b = new Biblioteca("./musicas-teste");

        // ArrayList<Musica> musicas = b.getMusicas();

        VBox vbox = new VBox();

        Label titulo = new Label("Músicas");
        titulo.setStyle("-fx-font-family: 'Inter 18pt Bold'; -fx-font-size: 128px; -fx-text-fill: text-color;");

        ScrollPane gridMusicasContainer = new ScrollPane();
        // o conteudo dentro do ScrollPane fica com a largura e altura da janela
        gridMusicasContainer.setFitToWidth(true);
        gridMusicasContainer.setFitToHeight(true);
        gridMusicasContainer.setPannable(true);
        // remove a borda do ScrollPane
        gridMusicasContainer.setStyle("-fx-background-color: transparent;");

        gridMusicasContainer.setContent(b.gridMusicas());

        vbox.getChildren().add(titulo);
        vbox.getChildren().add(gridMusicasContainer);

        Scene scene = new Scene(vbox, LARGURA, ALTURA);
        // carregando arquivo style.css
        scene.getStylesheets().add("style.css");

        stage.setTitle("Projeto POO");
        stage.setMinWidth(LARGURA);
        stage.setMinHeight(ALTURA);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}