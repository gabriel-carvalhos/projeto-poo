package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import modelos.Biblioteca;
import modelos.GerenciadorPlaylist;
import modelos.Musica;
import modelos.Playlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JavaFX App
 */
public class App extends Application {
    public static int LARGURA = 640;
    public static int ALTURA = 480;

    private List<Musica> todasMusicas;
    private final GerenciadorPlaylist gerenciador = new GerenciadorPlaylist();
    private Playlist playlistAtual = null; // null = "Todas as músicas"

    private Label titulo;
    private FlowPane gridMusicas;
    private ListView<Playlist> listaPlaylists;

    @Override
    public void start(Stage stage) {
        Biblioteca b = new Biblioteca("C:\\Users\\migue\\Downloads\\projeto-poo-main(organizadorDePastas)\\projeto-poo-main\\musicas-teste");
        todasMusicas = b.getMusicas();

        // carrega as playlists salvas (associa cada caminho salvo a uma Musica da biblioteca)
        Map<String, Musica> mapa = new HashMap<>();
        for (Musica m : todasMusicas) {
            mapa.put(m.getCaminho(), m);
        }
        gerenciador.carregar(mapa);

        titulo = new Label("Músicas");
        titulo.setStyle("-fx-font-family: 'Inter 18pt Bold'; -fx-font-size: 128px; -fx-text-fill: text-color;");

        // ---------- grade de músicas ----------
        ScrollPane gridMusicasContainer = new ScrollPane();
        gridMusicasContainer.setFitToWidth(true);
        gridMusicasContainer.setFitToHeight(true);
        gridMusicasContainer.setPannable(true);
        gridMusicasContainer.setStyle("-fx-background-color: transparent;");

        // TODO: melhorar a responsividade desse grid
        gridMusicas = new FlowPane();
        gridMusicas.setPadding(new Insets(10, 10, 10, 10));
        gridMusicas.setHgap(36);
        gridMusicas.setVgap(58);
        gridMusicas.setStyle("-fx-background-color: bg-color;");
        gridMusicas.setAlignment(Pos.CENTER);
        gridMusicas.getStyleClass().add("grid-musicas");

        gridMusicasContainer.setContent(gridMusicas);

        // ---------- painel lateral de playlists ----------
        VBox painelPlaylists = criarPainelPlaylists();

        // ---------- layout principal ----------
        HBox conteudo = new HBox(painelPlaylists, gridMusicasContainer);
        HBox.setHgrow(gridMusicasContainer, Priority.ALWAYS);
        VBox.setVgrow(conteudo, Priority.ALWAYS);

        VBox vbox = new VBox(titulo, conteudo);

        atualizarGrade();

        Scene scene = new Scene(vbox, LARGURA, ALTURA);
        scene.getStylesheets().add("style.css");

        stage.setTitle("Projeto POO");
        stage.setMinWidth(LARGURA);
        stage.setMinHeight(ALTURA);
        stage.setScene(scene);
        stage.show();
    }

    // ================= PLAYLISTS =================

    private VBox criarPainelPlaylists() {
        Label lblPlaylists = new Label("Playlists");
        lblPlaylists.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        listaPlaylists = new ListView<>(gerenciador.getPlaylists());
        listaPlaylists.setStyle("-fx-font-size: 20px;");
        VBox.setVgrow(listaPlaylists, Priority.ALWAYS);

        // ao selecionar uma playlist, a grade passa a mostrar só as músicas dela
        listaPlaylists.getSelectionModel().selectedItemProperty().addListener((obs, antiga, nova) -> {
            playlistAtual = nova;
            atualizarGrade();
        });

        Button btnTodas = new Button("Todas as músicas");
        btnTodas.setMaxWidth(Double.MAX_VALUE);
        btnTodas.setOnAction(e -> listaPlaylists.getSelectionModel().clearSelection());

        Button btnNova = new Button("+ Nova playlist");
        btnNova.setMaxWidth(Double.MAX_VALUE);
        btnNova.setOnAction(e -> novaPlaylist());

        Button btnExcluir = new Button("Excluir playlist");
        btnExcluir.setMaxWidth(Double.MAX_VALUE);
        btnExcluir.disableProperty().bind(
                listaPlaylists.getSelectionModel().selectedItemProperty().isNull());
        btnExcluir.setOnAction(e -> excluirPlaylistSelecionada());

        Button botaoAdicionar = new Button("Adicionar Músicas");
        botaoAdicionar.setMaxWidth(Double.MAX_VALUE);
        botaoAdicionar.disableProperty().bind(
                listaPlaylists.getSelectionModel().selectedItemProperty().isNull());
        botaoAdicionar.setOnAction(e -> adicionarMusicasDialogo());

        VBox painel = new VBox(10, lblPlaylists, btnTodas, listaPlaylists, botaoAdicionar, btnNova, btnExcluir);

        return painel;

    }

    private void adicionarMusicasDialogo() {
        Playlist p = listaPlaylists.getSelectionModel().getSelectedItem();
        if (p == null) return;

        ListView<Musica> lista = new ListView<>();
        for (Musica m : todasMusicas) {
            if (!p.getMusicas().contains(m)) {   // só as que ainda não estão na playlist
                lista.getItems().add(m);
            }
        }
        lista.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lista.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Musica m, boolean vazio) {
                super.updateItem(m, vazio);
                setText(vazio || m == null ? null : m.getTitulo() + " — " + m.getArtista());
            }
        });
        lista.setPrefSize(450, 350);

        Dialog<ButtonType> dialogo = new Dialog<>();
        dialogo.setTitle("Adicionar músicas");
        dialogo.setHeaderText("Selecione as músicas para \"" + p.getNome() + "\"");
        dialogo.getDialogPane().setContent(lista);
        dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialogo.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                for (Musica m : new ArrayList<>(lista.getSelectionModel().getSelectedItems())) {
                    gerenciador.adicionarMusica(p, m);
                }
                atualizarGrade();
            }
        });
    }


    private void novaPlaylist() {
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Nova playlist");
        dialogo.setHeaderText("Nome da playlist:");
        dialogo.setContentText("Nome:");
        dialogo.showAndWait().ifPresent(nome -> {
            if (!nome.isBlank()) {
                Playlist p = gerenciador.criar(nome);
                listaPlaylists.getSelectionModel().select(p);
            }
        });
    }

    private void excluirPlaylistSelecionada() {
        Playlist p = listaPlaylists.getSelectionModel().getSelectedItem();
        if (p == null) return;

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir a playlist \"" + p.getNome() + "\"? As músicas não serão apagadas do disco.",
                ButtonType.YES, ButtonType.NO);
        confirmacao.setHeaderText(null);
        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.YES) {
                listaPlaylists.getSelectionModel().clearSelection();
                gerenciador.excluir(p);
            }
        });
    }

    // reconstrói a grade conforme a playlist selecionada
    private void atualizarGrade() {
        gridMusicas.getChildren().clear();

        List<Musica> exibir = (playlistAtual == null) ? todasMusicas : playlistAtual.getMusicas();
        titulo.setText(playlistAtual == null ? "Músicas" : playlistAtual.getNome());

        for (Musica m : exibir) {
            VBox card = m.cardMusica();
            // clique com o botão direito no card abre o menu
            card.setOnContextMenuRequested(e -> {
                criarMenuContexto(m).show(card, e.getScreenX(), e.getScreenY());
                e.consume();
            });
            gridMusicas.getChildren().add(card);
        }
    }

    private ContextMenu criarMenuContexto(Musica m) {
        Menu adicionarEm = new Menu("Adicionar à playlist");
        if (gerenciador.getPlaylists().isEmpty()) {
            MenuItem vazio = new MenuItem("(crie uma playlist primeiro)");
            vazio.setDisable(true);
            adicionarEm.getItems().add(vazio);
        } else {
            for (Playlist p : gerenciador.getPlaylists()) {
                MenuItem item = new MenuItem(p.getNome());
                item.setOnAction(ev -> gerenciador.adicionarMusica(p, m));
                adicionarEm.getItems().add(item);
            }
        }

        ContextMenu menu = new ContextMenu(adicionarEm);

        // só aparece quando estou dentro de uma playlist
        if (playlistAtual != null) {
            Playlist atual = playlistAtual;
            MenuItem remover = new MenuItem("Remover desta playlist");
            remover.setOnAction(ev -> {
                gerenciador.removerMusica(atual, m);
                atualizarGrade();
            });
            menu.getItems().add(remover);
        }
        return menu;
    }

    public static void main(String[] args) {
        launch();
    }
}