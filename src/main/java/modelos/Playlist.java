package modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
        private String nome;
        private final ObservableList<Musica> musicas = FXCollections.observableArrayList();

        // caminhos salvos que não estão na pasta aberta agora (outro diretório).
        // são mantidos para não serem perdidos quando o arquivo é regravado
        private final List<String> caminhosPendentes = new ArrayList<>();

        public Playlist(String nome) {
                this.nome = nome;
        }

        public String getNome() {
                return nome;
        }

        public void setNome(String nome) {
                this.nome = nome;
        }

        public ObservableList<Musica> getMusicas() {
                return musicas;
        }

        public List<String> getCaminhosPendentes() {
                return caminhosPendentes;
        }

        public void adicionar(Musica m) {
                caminhosPendentes.remove(m.getCaminho());
                if (!musicas.contains(m)) {
                        musicas.add(m);
                }
        }

        public void adicionarPendente(String caminho) {
                if (!caminhosPendentes.contains(caminho)) {
                        caminhosPendentes.add(caminho);
                }
        }

        public void remover(Musica m) {
                musicas.remove(m);
        }

        @Override
        public String toString() {
                return nome;
        }
}
