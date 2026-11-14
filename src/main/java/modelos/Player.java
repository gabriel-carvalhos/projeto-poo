package modelos;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.media.MediaPlayer;

public class Player {
    private static Player instance = new Player();
    private MediaPlayer player;
    private ObjectProperty<Musica> musicaAtual = new SimpleObjectProperty<>();
    private ObjectProperty<Boolean> isPlaying = new SimpleObjectProperty<>();

    public static Player getInstance() {
        return instance;
    }

    public ObjectProperty<Musica> musicaAtualProperty() {
        return musicaAtual;
    }

    public ObjectProperty<Boolean> isPlayingProperty() {
        return isPlaying;
    }

    // public boolean isPlaying() {
    //    if (player == null) {
    //        return false;
    //    }
    //
    //    return player.getStatus() == MediaPlayer.Status.PLAYING;
    // }

    public void play(Musica musica) {
        // verificando se precisa trocar de musica
        if (player != null && musicaAtual.get() == musica) {
            // se nao precisa trocar, e a musica esta tocando, ele pausa
            if (isPlaying.get()) {
                pause();
                return;
            }

            // se nao precisa trocar, e musica nao esta tocando, ele retoma
            retomar();
            return;
        }

        // se precisa trocar, ele para a atual e da play na nova
        stop();

        System.out.println("Trocando musica");
        player = new MediaPlayer(musica.getMedia());
        player.play();
        isPlaying.set(true);
        musicaAtual.set(musica);
    }

    public void retomar() {
        System.out.println("Retomando a mesma musica");
        // player.setOnEndOfMedia(this::stop);
        player.play();
        isPlaying.set(true);
    }

    public void pause() {
        if (player != null) {
            System.out.println("Pausando");
            player.pause();
            isPlaying.set(false);
        }
    }

    public void stop() {
        if (player != null) {
            System.out.println("Parando");
            player.stop();
            player.dispose();
            player = null;
        }

        musicaAtual.set(null);
        isPlaying.set(false);
    }
}
