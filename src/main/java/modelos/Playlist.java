package modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Playlist {
        private String nome;
        private final ObservableList<Musica> musicas = FXCollections.observableArrayList();

        public Playlist(String nome) { this.nome = nome; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public ObservableList<Musica> getMusicas() { return musicas; }

        public void adicionar(Musica m) {
            if (!musicas.contains(m)) musicas.add(m);
        }

        public void remover(Musica m) { musicas.remove(m); }

        @Override
        public String toString() { return nome; }
}
