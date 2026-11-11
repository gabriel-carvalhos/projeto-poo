package teste;

import modelos.Biblioteca;

public class Teste {
    public static void main(String[] args) {
        Biblioteca b = new Biblioteca("./musicas-teste");

        b.exibirTodasMusicas();
        // b.procurarPorTitulo("A Prosa").exibirTodosMetadados();
    }
}
