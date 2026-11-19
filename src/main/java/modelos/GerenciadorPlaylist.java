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

    // arquivo que guarda as playlists (nome + caminhos das músicas)
    private final Path arquivo = Paths.get(System.getProperty("user.home"),
            ".minha-biblioteca", "playlists.txt");

    // pasta onde cada playlist vira uma subpasta no explorador de arquivos.
    // Fica FORA da pasta da biblioteca para as cópias não aparecerem como músicas duplicadas.
    // Para mudar o local, altere esta linha.
    private final Path pastaRaiz = Paths.get(System.getProperty("user.home"),
            "Music", "Minhas Playlists");

    public ObservableList<Playlist> getPlaylists() {
        return playlists;
    }

    public Path pastaDa(Playlist p) {
        return pastaRaiz.resolve(nomeSeguro(p.getNome()));
    }

    // duas playlists com nomes que viram a mesma pasta (ex.: "Rock" e "rock")
    public boolean existeNome(String nome) {
        String n = nomeSeguro(nome).toLowerCase();
        return playlists.stream().anyMatch(p -> nomeSeguro(p.getNome()).toLowerCase().equals(n));
    }

    // ---------- operações ----------

    public Playlist criar(String nome) {
        Playlist p = new Playlist(nome.trim());
        playlists.add(p);
        salvar();
        try {
            Files.createDirectories(pastaDa(p));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

    public void excluir(Playlist p) {
        playlists.remove(p);
        salvar();

        // apaga só as cópias que o app criou; se sobrar algo na pasta (arquivos seus), ela é mantida
        Path pasta = pastaDa(p);
        for (Musica m : p.getMusicas()) {
            apagarCopia(pasta, m);
        }
        try {
            Files.deleteIfExists(pasta);
        } catch (IOException e) {
            // pasta não vazia: deixa como está
        }
    }

    public void adicionarMusica(Playlist p, Musica m) {
        if (p.getMusicas().contains(m)) return;
        p.adicionar(m);
        salvar();
        copiarParaPasta(p, m);
    }

    public void removerMusica(Playlist p, Musica m) {
        p.remover(m);
        salvar();
        apagarCopia(pastaDa(p), m);
    }

    // ---------- disco: pastas e cópias ----------

    private void copiarParaPasta(Playlist p, Musica m) {
        Path origem = Paths.get(m.getCaminho()).toAbsolutePath().normalize();
        Path destino = pastaDa(p).resolve(nomeArquivo(m)).toAbsolutePath().normalize();
        if (origem.equals(destino)) return;

        // em segundo plano para não travar a interface com arquivos grandes
        Thread t = new Thread(() -> {
            try {
                Files.createDirectories(destino.getParent());
                if (!Files.exists(destino)) {
                    Files.copy(origem, destino);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    private void apagarCopia(Path pasta, Musica m) {
        Path origem = Paths.get(m.getCaminho()).toAbsolutePath().normalize();
        Path copia = pasta.resolve(nomeArquivo(m)).toAbsolutePath().normalize();
        if (origem.equals(copia)) return; // nunca apaga o arquivo original
        try {
            Files.deleteIfExists(copia);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // a cópia usa o mesmo nome do arquivo original
    private String nomeArquivo(Musica m) {
        return Paths.get(m.getCaminho()).getFileName().toString();
    }

    // remove caracteres proibidos em nomes de pasta do Windows
    private static String nomeSeguro(String s) {
        String limpo = s.replaceAll("[\\\\/:*?\"<>|]", "_").replaceAll("[. ]+$", "").trim();
        return limpo.isEmpty() ? "Sem nome" : limpo;
    }

    // ---------- persistência ----------

    public void salvar() {
        List<String> linhas = new ArrayList<>();
        for (Playlist p : playlists) {
            linhas.add("P|" + p.getNome());
            for (Musica m : p.getMusicas()) {
                linhas.add("M|" + m.getCaminho());
            }
            for (String pendente : p.getCaminhosPendentes()) {
                linhas.add("M|" + pendente);
            }
        }
        try {
            Files.createDirectories(arquivo.getParent());
            Files.write(arquivo, linhas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // biblioteca: mapa caminho -> Musica das músicas da pasta aberta agora
    public void carregar(Map<String, Musica> biblioteca) {
        playlists.clear(); // o app pode abrir outra pasta e chamar de novo
        if (!Files.exists(arquivo)) return;
        try {
            Playlist atual = null;
            for (String linha : Files.readAllLines(arquivo)) {
                if (linha.startsWith("P|")) {
                    atual = new Playlist(linha.substring(2));
                    playlists.add(atual);
                } else if (linha.startsWith("M|") && atual != null) {
                    String caminho = linha.substring(2);
                    Musica m = biblioteca.get(caminho);
                    if (m != null) {
                        atual.adicionar(m);
                    } else {
                        atual.adicionarPendente(caminho); // de outra pasta: guarda sem perder
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
