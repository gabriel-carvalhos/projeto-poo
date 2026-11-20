package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import modelos.Biblioteca;
import modelos.GerenciadorPlaylist;
import modelos.Musica;
import modelos.Playlist;
import modelos.Rodape;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App extends Application {
    public static final int LARGURA = 900;
    public static final int ALTURA = 600;

    // [PLAYLISTS] estado das playlists
    private final GerenciadorPlaylist gerenciador = new GerenciadorPlaylist();
    private final Map<String, Musica> mapaPorCaminho = new HashMap<>();
    private List<Musica> todasMusicas = new ArrayList<>();
    private Playlist playlistAtual = null; // null = "Todas as músicas"

    private Label titulo;
    private Label vazio;
    private FlowPane gridMusicas;
    private ListView<Playlist> listaPlaylists;

    @Override
    public void start(Stage stage) {
        Image appIcon = new Image(getClass().getResourceAsStream("/imgs/logo.png"));
        stage.getIcons().add(appIcon);

        stage.setTitle("Organizador de Músicas");
        stage.setMinWidth(LARGURA);
        stage.setMinHeight(ALTURA);

        mostrarTelaAdicionarDiretorio(stage);
        stage.show();
    }

    private void mostrarTelaAdicionarDiretorio(Stage stage) {
        TelaAdicionarDiretorio tela = new TelaAdicionarDiretorio(
                stage,
                diretorio -> mostrarBiblioteca(stage, diretorio) // mostra a biblioteca ao escolher a pasta
        );
        Scene scene = tela.criarCena();
        adicionarEstilos(scene);
        stage.setScene(scene); // troca a cena por scene (tela de adicionar diretorio)
    }

    private void mostrarBiblioteca(Stage stage, File diretorio) {
        Biblioteca biblioteca = new Biblioteca(diretorio.getAbsolutePath());

        // [PLAYLISTS] prepara as músicas e carrega as playlists salvas
        todasMusicas = biblioteca.getMusicas();
        mapaPorCaminho.clear();
        for (Musica m : todasMusicas) {
            mapaPorCaminho.put(m.getCaminho(), m);
        }
        gerenciador.carregar(mapaPorCaminho);
        playlistAtual = null;

        StackPane root = new StackPane();

        VBox conteudoPrincipal = new VBox();
        conteudoPrincipal.getStyleClass().add("conteudo-biblioteca");

        BorderPane cabecalho = new BorderPane();
        cabecalho.setPadding(new Insets(0, 20, 0, 20));

        titulo = new Label("Músicas");
        titulo.setStyle("-fx-font-family: 'Inter 18pt Bold'; -fx-font-size: 128px; -fx-text-fill: text-color;");
        TextField pesquisa = new TextField();
        pesquisa.setStyle("-fx-text-fill: text-color");
        pesquisa.setPromptText("Digite a música desejada");
        pesquisa.setFocusTraversable(false);

        cabecalho.setLeft(titulo);
        cabecalho.setRight(pesquisa);
        BorderPane.setAlignment(pesquisa, Pos.CENTER);

        // [PLAYLISTS] a grade agora é montada aqui (e não em biblioteca.gridMusicas()),
        // para poder alternar entre "todas as músicas" e uma playlist
        ScrollPane gridMusicasContainer = new ScrollPane();
        gridMusicasContainer.setFitToWidth(true);
        gridMusicasContainer.setFitToHeight(true);
        gridMusicasContainer.setStyle("-fx-background-color: transparent;");

        gridMusicas = new FlowPane();
        gridMusicas.setPadding(new Insets(0, 0, 100, 0));
        gridMusicas.setHgap(36);
        gridMusicas.setVgap(58);
        gridMusicas.setStyle("-fx-background-color: bg-color;");
        gridMusicas.setAlignment(Pos.CENTER);
        gridMusicas.getStyleClass().add("grid-musicas");
        gridMusicasContainer.setContent(gridMusicas);

        vazio = new Label();
        vazio.setStyle("-fx-font-size: 20px; -fx-padding: 20px;");

        VBox painelPlaylists = criarPainelPlaylists();

        HBox corpo = new HBox(painelPlaylists, gridMusicasContainer);
        HBox.setHgrow(gridMusicasContainer, Priority.ALWAYS);
        VBox.setVgrow(corpo, Priority.ALWAYS);

        conteudoPrincipal.getChildren().addAll(cabecalho, vazio, corpo);

        HBox rodape = new Rodape(biblioteca).rodape();
        StackPane.setAlignment(rodape, Pos.BOTTOM_CENTER);

        root.getChildren().addAll(conteudoPrincipal, rodape);

        atualizarGrade();

        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        adicionarEstilos(scene);
        stage.setScene(scene); // troca a cena por scene (tela biblioteca)
    }

    private void adicionarEstilos(Scene scene) {
        var css = getClass().getResource("/style.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }
    }

    // ================= PLAYLISTS =================

    private VBox criarPainelPlaylists() {
        Label lblPlaylists = new Label("Playlists");
        lblPlaylists.setStyle("-fx-font-family: 'Inter 18pt Bold'; -fx-font-size: 28px");

        listaPlaylists = new ListView<>(gerenciador.getPlaylists());
        listaPlaylists.setStyle("-fx-font-size: 20px;");
        VBox.setVgrow(listaPlaylists, Priority.ALWAYS);

        // arrastar um card e soltar sobre o nome da playlist
        listaPlaylists.setCellFactory(lv -> {
            ListCell<Playlist> celula = new ListCell<>() {
                @Override
                protected void updateItem(Playlist item, boolean vazio) {
                    super.updateItem(item, vazio);
                    setText(vazio || item == null ? null : item.getNome());
                }
            };

            celula.setOnDragOver(e -> {
                if (celula.getItem() != null && e.getDragboard().hasString()) {
                    e.acceptTransferModes(TransferMode.COPY);
                }
                e.consume();
            });

            celula.setOnDragDropped(e -> {
                Playlist destino = celula.getItem();
                Musica m = mapaPorCaminho.get(e.getDragboard().getString());
                boolean ok = destino != null && m != null;
                if (ok) {
                    gerenciador.adicionarMusica(destino, m);
                }
                e.setDropCompleted(ok);
                e.consume();
            });
            return celula;
        });

        listaPlaylists.getSelectionModel().selectedItemProperty().addListener((obs, antiga, nova) -> {
            playlistAtual = nova;
            atualizarGrade();
        });

        Button btnTodas = new Button("Todas as músicas");
        btnTodas.setMaxWidth(Double.MAX_VALUE);
        btnTodas.setOnAction(e -> listaPlaylists.getSelectionModel().clearSelection());

        Button btnAdicionar = new Button("Adicionar músicas...");
        btnAdicionar.setMaxWidth(Double.MAX_VALUE);
        btnAdicionar.disableProperty().bind(
                listaPlaylists.getSelectionModel().selectedItemProperty().isNull());
        btnAdicionar.setOnAction(e -> adicionarMusicasDialogo());

        Button btnNova = new Button("+ Nova playlist");
        btnNova.setMaxWidth(Double.MAX_VALUE);
        btnNova.setOnAction(e -> novaPlaylist());

        Button btnExcluir = new Button("Excluir playlist");
        btnExcluir.setMaxWidth(Double.MAX_VALUE);
        btnExcluir.disableProperty().bind(
                listaPlaylists.getSelectionModel().selectedItemProperty().isNull());
        btnExcluir.setOnAction(e -> excluirPlaylistSelecionada());

        Button btnAbrirPasta = new Button("Abrir pasta");
        btnAbrirPasta.setMaxWidth(Double.MAX_VALUE);
        btnAbrirPasta.disableProperty().bind(
                listaPlaylists.getSelectionModel().selectedItemProperty().isNull());
        btnAbrirPasta.setOnAction(e -> abrirPastaDaPlaylist());

        VBox painel = new VBox(10, lblPlaylists, btnTodas, listaPlaylists, btnAdicionar, btnAbrirPasta, btnNova, btnExcluir);
        painel.setPadding(new Insets(10, 20, 10, 20));
        painel.setPrefWidth(240);
        painel.setMinWidth(240);
        painel.setStyle("-fx-font-size: 18px;");
        return painel;
    }

    private void novaPlaylist() {
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Nova playlist");
        dialogo.setHeaderText("Nome da playlist:");
        dialogo.setContentText("Nome:");
        dialogo.showAndWait().ifPresent(nome -> {
            if (nome.isBlank()) return;
            if (gerenciador.existeNome(nome)) {
                new Alert(Alert.AlertType.WARNING, "Já existe uma playlist com esse nome.").showAndWait();
                return;
            }
            Playlist p = gerenciador.criar(nome);
            listaPlaylists.getSelectionModel().select(p);
        });
    }

    // abre a pasta da playlist no explorador de arquivos
    private void abrirPastaDaPlaylist() {
        Playlist p = listaPlaylists.getSelectionModel().getSelectedItem();
        if (p == null) return;

        Path pasta = gerenciador.pastaDa(p);
        try {
            Files.createDirectories(pasta);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        getHostServices().showDocument(pasta.toUri().toString());
    }

    private void excluirPlaylistSelecionada() {
        Playlist p = listaPlaylists.getSelectionModel().getSelectedItem();
        if (p == null) return;

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir a playlist \"" + p.getNome() + "\"? A pasta da playlist e as cópias dentro dela serão apagadas. Os arquivos originais não serão tocados.",
                ButtonType.YES, ButtonType.NO);
        confirmacao.setHeaderText(null);
        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.YES) {
                listaPlaylists.getSelectionModel().clearSelection();
                gerenciador.excluir(p);
            }
        });
    }

    private void adicionarMusicasDialogo() {
        Playlist p = listaPlaylists.getSelectionModel().getSelectedItem();
        if (p == null) return;

        ListView<Musica> lista = new ListView<>();
        for (Musica m : todasMusicas) {
            if (!p.getMusicas().contains(m)) {
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

    // reconstrói a grade conforme a playlist selecionada
    private void atualizarGrade() {
        gridMusicas.getChildren().clear();

        List<Musica> exibir = (playlistAtual == null) ? todasMusicas : playlistAtual.getMusicas();
        titulo.setText(playlistAtual == null ? "Músicas" : playlistAtual.getNome());

        boolean semMusicas = exibir.isEmpty();
        vazio.setText(playlistAtual == null
                ? "Nenhuma música compatível foi encontrada nesta pasta."
                : "Esta playlist não tem músicas desta pasta. Arraste um card até ela ou use \"Adicionar músicas...\".");
        vazio.setVisible(semMusicas);
        vazio.setManaged(semMusicas);

        for (Musica m : exibir) {
            VBox card = m.cardMusica(); // o card fica em cache dentro da Musica

            // clique com o botão direito abre o menu
            card.setOnContextMenuRequested(e -> {
                criarMenuContexto(m).show(card, e.getScreenX(), e.getScreenY());
                e.consume();
            });

            // arrastar o card até uma playlist
            card.setOnDragDetected(e -> {
                Dragboard db = card.startDragAndDrop(TransferMode.COPY);
                ClipboardContent conteudo = new ClipboardContent();
                conteudo.putString(m.getCaminho());
                db.setContent(conteudo);
                e.consume();
            });

            gridMusicas.getChildren().add(card);
        }
    }

    private ContextMenu criarMenuContexto(Musica m) {
        Menu adicionarEm = new Menu("Adicionar à playlist");
        if (gerenciador.getPlaylists().isEmpty()) {
            MenuItem semPlaylist = new MenuItem("(crie uma playlist primeiro)");
            semPlaylist.setDisable(true);
            adicionarEm.getItems().add(semPlaylist);
        } else {
            for (Playlist p : gerenciador.getPlaylists()) {
                MenuItem item = new MenuItem(p.getNome());
                item.setOnAction(ev -> gerenciador.adicionarMusica(p, m));
                adicionarEm.getItems().add(item);
            }
        }

        ContextMenu menu = new ContextMenu(adicionarEm);

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
        launch(args);
    }
}
