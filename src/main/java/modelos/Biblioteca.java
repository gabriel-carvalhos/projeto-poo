package modelos;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;

import java.io.File;
import java.util.ArrayList;

public class Biblioteca {
    private ArrayList<Musica> musicas = new ArrayList<>();

    public Biblioteca(String caminho) {
        adicionarMusica(new Leitor().lerTodosArquivosDoDiretorio(caminho));
    }

    public ArrayList<Musica> getMusicas() {
        return musicas;
    }

    // le o arquivo com base no caminho, e adiciona a musica ao ArrayList
    public void adicionarMusica(String caminho) {
        try {
            File file = new File(caminho);

            AudioFile audioFile = AudioFileIO.read(file);
            int duracao = audioFile.getAudioHeader().getTrackLength();
            Tag tag = audioFile.getTag();

            musicas.add(new Musica(duracao, caminho, tag));
        } catch (Exception e) {
            e.printStackTrace();
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
        gridMusicas.setPadding(new Insets(10, 10, 10, 10));
        gridMusicas.setHgap(36);
        gridMusicas.setVgap(58);
        gridMusicas.setStyle("-fx-background-color: bg-color;");
        gridMusicas.setAlignment(Pos.CENTER);
        gridMusicas.getStyleClass().add("grid-musicas");

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
