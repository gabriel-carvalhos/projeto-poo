package modelos;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Player {
    private static Player instance = new Player();
    private MediaPlayer player;
    private ObjectProperty<Musica> musicaAtual = new SimpleObjectProperty<>();
    private Button btnAtual;

    public static Player getInstance() {
        return instance;
    }

    public MediaPlayer.Status getStatus() {
        if (player == null) {
            return null;
        }

        return player.getStatus();
    }

    public boolean isPlaying() {
        return player.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public void play(Musica musica, Button btnNovo) {
        // verificando se precisa trocar de musica
        if (player != null && musicaAtual.get() == musica) {
            // se nao precisa trocar, e a musica esta tocando, ele pausa
            if (isPlaying()) {
                pause();
                return;
            }

            // se nao precisa trocar, e musica nao esta tocando, ele retoma
            System.out.println("Retomando a mesma musica");
            player.play();
            btnAtual.setText("Pause");
            return;
        }

        // se precisa trocar, ele para a atual e da play na nova
        stop();

        if (btnAtual != null) {
            btnAtual.setText("Play");
        }

        System.out.println("Trocando musica");
        Media media = new Media(musica.getCaminhoURI());
        player = new MediaPlayer(media);
        player.play();
        btnNovo.setText("Pause");
        btnAtual = btnNovo;
        musicaAtual.set(musica);
    }

    public void pause() {
        if (player != null) {
            System.out.println("Pausando");
            player.pause();
        }

        btnAtual.setText("Play");
    }

    public void stop() {
        if (player != null) {
            System.out.println("Parando");
            player.stop();
            player.dispose();
            player = null;
        }

        if (btnAtual != null) {
            btnAtual.setText("Play");
        }

        musicaAtual.set(null);
        btnAtual = null;
    }

}
