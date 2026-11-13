package modelos;

import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Player {
    private static Player instance = new Player();
    private MediaPlayer player;
    private Media mediaAtual;
    private Button btnAtual;

    public static Player getInstance() {
        return instance;
    }

    public Media getMediaAtual() {
        return mediaAtual;
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

    public void play(Media media, Button btnNovo) {
        if (player != null && mediaAtual == media) {
            if (isPlaying()) {
                pause();
                return;
            }

            System.out.println("Retomando a mesma musica");
            player.play();
            btnAtual.setText("Pause");
            return;
        }

        stop();

        if (btnAtual != null) {
            btnAtual.setText("Play");
        }

        System.out.println("Tocando musica nova");
        player = new MediaPlayer(media);
        player.play();
        btnNovo.setText("Pause");
        btnAtual = btnNovo;
        mediaAtual = media;
    }

    public void pause() {
        if (player != null) {
            System.out.println("Pausando");
            player.pause();
        }

        if (btnAtual != null) {
            btnAtual.setText("Play");
        }
    }

    public void stop() {
        if (player != null) {
            System.out.println("Parando");
            player.stop();
            player.dispose();
            player = null;
        }
        mediaAtual = null;
        if (btnAtual != null) {
            btnAtual.setText("Play");
        }
        btnAtual = null;
    }

}
