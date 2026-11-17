package modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GerenciadorPlaylist {
        private final ObservableList<Playlist> playlists = FXCollections.observableArrayList();
        private final Path arquivo = Paths.get(System.getProperty("user.home"),
                ".minha-biblioteca", "playlists.txt");

        public ObservableList<Playlist> getPlaylists() { return playlists; }

        public Playlist criar(String nome) {
            Playlist p = new Playlist(nome.trim());
            playlists.add(p);
            salvar();
            return p;
        }

        public void excluir(Playlist p) {
            playlists.remove(p);
            salvar();
        }

        public void adicionarMusica(Playlist p, Musica m) {
            p.adicionar(m);
            salvar();
        }

        public void removerMusica(Playlist p, Musica m) {
            p.remover(m);
            salvar();
        }

        public void salvar() {
            List<String> linhas = new ArrayList<>();
            for (Playlist p : playlists) {
                linhas.add("P|" + p.getNome());
                for (Musica m : p.getMusicas()) linhas.add("M|" + m.getCaminho());
            }
            try {
                Files.createDirectories(arquivo.getParent());
                Files.write(arquivo, linhas);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void carregar(Map<String, Musica> biblioteca) {
            if (!Files.exists(arquivo)) return;
            try {
                Playlist atual = null;
                for (String linha : Files.readAllLines(arquivo)) {
                    if (linha.startsWith("P|")) {
                        atual = new Playlist(linha.substring(2));
                        playlists.add(atual);
                    } else if (linha.startsWith("M|") && atual != null) {
                        Musica m = biblioteca.get(linha.substring(2));
                        if (m != null) atual.adicionar(m);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
