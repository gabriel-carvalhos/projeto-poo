package teste;

import modelos.Biblioteca;
import modelos.Leitor;

import java.util.ArrayList;

public class Teste {
    public static void main(String[] args) {
        Biblioteca b = new Biblioteca();
        Leitor l = new Leitor();

        ArrayList<String> caminhos = l.lerTodosArquivosDoDiretorio("/home/gabriel/Músicas");
        for (String caminho : caminhos) {
            b.adicionarMusica(caminho);
        }

        b.exibirTodasMusicas();
        // b.procurarPorTitulo("A Prosa").exibirTodosMetadados();
    }
}
