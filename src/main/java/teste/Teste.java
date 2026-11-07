package teste;

import modelos.Biblioteca;
import modelos.Leitor;

import java.util.ArrayList;

public class Teste {
    public static void main(String[] args) {
        Biblioteca b = new Biblioteca("./musicas-teste");

        b.exibirTodasMusicas();
        // b.procurarPorTitulo("A Prosa").exibirTodosMetadados();
    }
}
