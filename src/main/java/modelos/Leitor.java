package modelos;

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
    public ArrayList<String> lerTodosArquivosDoDiretorio(String caminho) {
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

    public ArrayList<String> buscaRecursiva(Path raiz) {
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
