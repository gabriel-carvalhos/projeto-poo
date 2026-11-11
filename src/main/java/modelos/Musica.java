package modelos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.media.Media;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

import java.io.ByteArrayInputStream;

public class Musica {
    private int duracao;
    private String caminho;
    // private Tag tag;
    private String titulo;
    private String artista;
    private String album;
    private String genero;
    private String ano;
    private Image capa;

    // TODO: entender por que ele pode retornar null
    private static Image CAPA_PADRAO = new Image(Musica.class.getResourceAsStream("/imgs/capa-padrao.png"));

    public Musica(int duracao, String caminho, Tag tag) {
        this.caminho = caminho;
        this.duracao = duracao;
        titulo = "Título Desconhecido";
        artista = "Artista Desconhecido";
        album = "Album Desconhecido";
        genero = "Gênero Desconhecido";
        ano = "Ano Desconhecido";
        capa = null;

        // this.tag = tag;
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

            capa = carregarCapa(tag.getFirstArtwork());
        }
    }

    private Image carregarCapa(Artwork art) {
        if (art == null) {
            return CAPA_PADRAO;
        }

        try {
            byte[] imageBytes = art.getBinaryData();
            if (imageBytes == null || imageBytes.length == 0) {
                return CAPA_PADRAO;
            }

            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            Image capa = new Image(inputStream);
            // System.out.println(capa.isError());
            return capa.isError() ? CAPA_PADRAO : capa;
        } catch (Exception e) {
            e.printStackTrace();
            // capa.getException().printStackTrace();
            return CAPA_PADRAO;
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
        lTitulo.setTooltip(new Tooltip(lTitulo.getText()));
        lTitulo.setStyle("-fx-font-family: 'Inter 18pt Bold'; -fx-font-size: 48px;");

        Label lArtista = new Label(artista);
        lArtista.setTooltip(new Tooltip(lArtista.getText()));
        lArtista.setStyle("-fx-font-size: 32px;");

        Label lAlbumAno = new Label(album + " (" + ano + ")");
        lAlbumAno.setTooltip(new Tooltip(lAlbumAno.getText()));

        Label lGenero = new Label(genero);
        lGenero.setTooltip(new Tooltip(lGenero.getText()));

        Label lDuracao = new Label(getDuracaoEmMinutos());
        lDuracao.setTooltip(new Tooltip(lDuracao.getText()));

        Media midia = new Media(caminho);
        MediaPlayer tocador = new MediaPlayer(midia);
        Button play = new Button("Play");

        play.setOnAction(ActionEvent -> {
            if (tocador.getStatus() == MediaPlayer.Status.PLAYING) {
                tocador.pause();
                play.setText("Play");
            } else {
                tocador.play();
                play.setText("Pause");
            }
        });

        card.getChildren().addAll(capaMusica(), lTitulo, lArtista, lAlbumAno, lGenero, lDuracao, play);
        return card;
    }

    private ImageView capaMusica() {
        ImageView imageView = new ImageView(capa);
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        // adiciona arredondamento de borda de 14px
        Rectangle clip = new Rectangle(imageView.getBoundsInLocal().getWidth(), imageView.getBoundsInLocal().getHeight());
        clip.setArcHeight(14);
        clip.setArcWidth(14);
        imageView.setClip(clip);

        return imageView;
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
