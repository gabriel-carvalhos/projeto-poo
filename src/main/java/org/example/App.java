package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelos.Biblioteca;
import modelos.Rodape;

import java.io.File;

public class App extends Application {
    public static final int LARGURA = 640;
    public static final int ALTURA = 480;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Organizador de Músicas");
        stage.setMinWidth(700);
        stage.setMinHeight(500);

        mostrarTelaAdicionarDiretorio(stage);
        stage.show();
    }

    private void mostrarTelaAdicionarDiretorio(Stage stage) {
        TelaAdicionarDiretorio tela = new TelaAdicionarDiretorio(
                stage,
                diretorio -> mostrarBiblioteca(stage, diretorio)
        );
        Scene scene = tela.criarCena();
        adicionarEstilos(scene);
        stage.setScene(scene);
    }

    private void mostrarBiblioteca(Stage stage, File diretorio) {
        Biblioteca biblioteca = new Biblioteca(diretorio.getAbsolutePath());

        AnchorPane root = new AnchorPane();
        VBox conteudoPrincipal = new VBox();
        conteudoPrincipal.getStyleClass().add("conteudo-biblioteca");

        Label titulo = new Label("Músicas");
        titulo.setStyle("-fx-font-family: 'Inter 18pt Bold'; -fx-font-size: 128px; -fx-text-fill: text-color;");

        ScrollPane gridMusicasContainer = new ScrollPane();
        gridMusicasContainer.setFitToWidth(true);
        gridMusicasContainer.setFitToHeight(true);
        gridMusicasContainer.setPannable(true);
        gridMusicasContainer.setStyle("-fx-background-color: transparent;");
        gridMusicasContainer.setContent(biblioteca.gridMusicas());

        Label vazio = new Label("Nenhuma música compatível foi encontrada nesta pasta.");
        vazio.setVisible(biblioteca.getSize() == 0);
        vazio.setManaged(biblioteca.getSize() == 0);
        vazio.setStyle("-fx-font-size: 20px; -fx-padding: 20px;");

        conteudoPrincipal.getChildren().addAll(titulo, vazio, gridMusicasContainer);
        BorderPane rodape = new Rodape(biblioteca).rodape();

        AnchorPane.setTopAnchor(conteudoPrincipal, 0.0);
        AnchorPane.setLeftAnchor(conteudoPrincipal, 0.0);
        AnchorPane.setRightAnchor(conteudoPrincipal, 0.0);
        AnchorPane.setBottomAnchor(conteudoPrincipal, 0.0);
        AnchorPane.setBottomAnchor(rodape, 0.0);
        AnchorPane.setLeftAnchor(rodape, 0.0);
        AnchorPane.setRightAnchor(rodape, 0.0);
        root.getChildren().addAll(conteudoPrincipal, rodape);

        Scene scene = new Scene(root, LARGURA, ALTURA);
        adicionarEstilos(scene);
        stage.setScene(scene);
    }

    private void adicionarEstilos(Scene scene) {
        var css = getClass().getResource("/style.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
