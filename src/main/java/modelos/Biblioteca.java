package modelos;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;

import java.io.File;
import java.util.ArrayList;

public class Biblioteca {
    private ArrayList<Musica> musicas = new ArrayList<>();

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
