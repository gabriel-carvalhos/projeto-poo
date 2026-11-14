package modelos;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;

public class Biblioteca {
    private ArrayList<Musica> musicas = new ArrayList<>();

    public Biblioteca(String caminho) {
        adicionarMusica(Leitor.lerTodosArquivosDoDiretorio(caminho));
    }

    public ArrayList<Musica> getMusicas() {
        return musicas;
    }

    public int getSize() {
        return musicas.size();
    }

    // le o arquivo com base no caminho, e adiciona a musica ao ArrayList
    public void adicionarMusica(String caminho) {
        if (caminho == null) {
            return;
        }

        Musica musica = Leitor.lerMusica(caminho);
        if (musica != null) {
            musicas.add(musica);
        }
    }

    public void adicionarMusica(ArrayList<String> caminhos) {
        for (String caminho : caminhos) {
            adicionarMusica(caminho);
        }
    }

    public void exibirTodasMusicas() {
        for (Musica m : musicas) {
            System.out.println(m + "\n\n=========\n");
        }
    }

    public FlowPane gridMusicas() {
        // TODO: melhorar a responsividade desse grid
        FlowPane gridMusicas = new FlowPane();
        // setando o padding inferior para 80, para distanciar o rodape do gridMusicas
        gridMusicas.setPadding(new Insets(0, 0, 80, 0));
        gridMusicas.setHgap(36);
        gridMusicas.setVgap(58);
        gridMusicas.setStyle("-fx-background-color: bg-color;");
        gridMusicas.setAlignment(Pos.CENTER);
        // gridMusicas.getStyleClass().add("grid-musicas");

        for (Musica m : musicas) {
            gridMusicas.getChildren().add(m.cardMusica());
        }

        return gridMusicas;
    }

//    public Musica procurarPorTitulo(String titulo) {
//        for (Musica m : musicas) {
//            if (m.getTitulo().equals(titulo)) {
//                return m;
//            }
//        }
//
//        return null;
//    }

}
