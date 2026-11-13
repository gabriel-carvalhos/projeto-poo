package modelos;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class Leitor {

    // TODO: validar se o arquivo é suportado pela biblioteca
    public static ArrayList<String> lerTodosArquivosDoDiretorio(String caminho) {
        File pasta = new File(caminho);

        if (!pasta.exists() || !pasta.isDirectory()) {
            System.out.println("O caminho fornecido não é uma pasta");
            return null;
        }

        // lista todos arquivos e subpastas dentro de "pasta"
        File[] arquivos = pasta.listFiles();

        if (arquivos == null) {
            System.out.println("O diretório está vazio");
            return null;
        }

        ArrayList<String> caminhos = buscaRecursiva(pasta.toPath());

        return caminhos;
    }

    private static ArrayList<String> buscaRecursiva(Path raiz) {
        ArrayList<String> caminhos = new ArrayList<>();
        try {
            Files.walkFileTree(raiz, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                    if (attributes.isRegularFile()) {
                        caminhos.add(file.toAbsolutePath().toString());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    // ignora arquivos sem permissão
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return caminhos;
    }

    public static Musica lerMusica(String caminho) {
        try {
            File file = new File(caminho);
            String caminhoURI = file.toURI().toString();
            AudioFile audioFile = AudioFileIO.read(file);
            int duracao = audioFile.getAudioHeader().getTrackLength();
            Tag tag = audioFile.getTag();

            return new Musica(duracao, caminho, tag, caminhoURI);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public void buscaRecursiva(File[] arquivos, ArrayList<String> caminhos) {
//        if (arquivos == null) {
//            return;
//        }
//
//        for (File arquivo : arquivos) {
//            if (arquivo.isDirectory()) {
//                File[] filhos = arquivo.listFiles();
//                if (filhos != null) {
//                    buscaRecursiva(filhos, caminhos);
//                }
//            } else {
//                caminhos.add(arquivo.getAbsolutePath());
//            }
//        }
//    }
}
