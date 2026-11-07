package modelos;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class Musica {
    private int duracao;
    private String caminho;
    private Tag tag;
    private String titulo;
    private String artista;
    private String album;
    private String genero;
    private String ano;

    public Musica(int duracao, String caminho, Tag tag) {
        this.caminho = caminho;
        this.duracao = duracao;
        titulo = "Título Desconhecido";
        artista = "Artista Desconhecido";
        album = "Album Desconhecido";
        genero = "Gênero Desconhecido";
        ano = "Ano Desconhecido";

        this.tag = tag;
        if (tag != null) {
            titulo = tag.getFirst(FieldKey.TITLE);
            titulo = titulo.isEmpty() ? "Título Desconhecido" : titulo;

            artista = tag.getFirst(FieldKey.ARTIST);
            artista = artista.isEmpty() ? "Artista Desconhecido" : artista;

            album = tag.getFirst(FieldKey.ALBUM);
            album = album.isEmpty() ? "Album Desconhecido" : album;

            genero = tag.getFirst(FieldKey.GENRE);
            genero = genero.isEmpty() ? "Gênero Desconhecido" : genero;

            // TODO: verificar se essa solucao com substring funciona para todos formatos de arquivo
            ano = tag.getFirst(FieldKey.YEAR);
            ano = ano.isEmpty() || ano.length() < 4 ? "Ano Desconhecido" : ano.substring(0, 4);
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDuracaoEmMinutos() {
        int minutos = duracao / 60;
        int segundos = duracao % 60;

        return String.format("%d:%02d", minutos, segundos);
    }

    @Override
    public String toString() {
        return "Título: " + titulo + "\n" +
               "Artista: " + artista + "\n" +
               "Album: " + album + " (" + ano + ")\n" +
               "Gênero: " + genero + "\n" +
               "Duração: " + getDuracaoEmMinutos();
    }

    public VBox cardMusica() {
        VBox card = new VBox();
        card.setSpacing(6);
        card.setPrefWidth(400);
        card.setStyle("-fx-font-size: 24px;");
        card.getStyleClass().add("card-musica");

        Label lTitulo = new Label(titulo);
        // lTitulo.setWrapText(true);
        lTitulo.setStyle("-fx-font-family: 'Inter 18pt Bold'; -fx-font-size: 48px;");

        Label lArtista = new Label(artista);
        // lArtista.setWrapText(true);
        lArtista.setStyle("-fx-font-size: 32px;");

        Label lAlbumAno = new Label(album + " (" + ano + ")");
        // lAlbumAno.setWrapText(true);

        Label lGenero = new Label(genero);
        // lGenero.setWrapText(true);

        Label lDuracao = new Label(getDuracaoEmMinutos());
        // lDuracao.setWrapText(true);

        card.getChildren().addAll(lTitulo, lArtista, lAlbumAno, lGenero, lDuracao);
        return card;
    }

    // exibe todos metadados presentes no arquivo
//    public void exibirTodosMetadados() {
//        Iterator<TagField> fields = tag.getFields();
//        while(fields.hasNext()) {
//            TagField field = fields.next();
//            System.out.println(field.getId() + ": " + field);
//        }
//    }
}
