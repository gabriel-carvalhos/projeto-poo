package modelos;

import javafx.beans.property.*;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Player {
    private static Player instance = new Player();
    private MediaPlayer player;
    private ObjectProperty<Musica> musicaAtual = new SimpleObjectProperty<>();
    private BooleanProperty isPlaying = new SimpleBooleanProperty(false);
    private DoubleProperty progresso = new SimpleDoubleProperty(0);

    public static Player getInstance() {
        return instance;
    }

    public MediaPlayer getMediaPlayer() {
        return player;
    }

    public ObjectProperty<Musica> musicaAtualProperty() {
        return musicaAtual;
    }

    public BooleanProperty isPlayingProperty() {
        return isPlaying;
    }

    public DoubleProperty progressoProperty() {
        return progresso;
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
        player.currentTimeProperty().addListener((obs, antigo, nova) -> atualizarProgresso());

        player.play();
        isPlaying.set(true);
        musicaAtual.set(musica);
    }

    private void atualizarProgresso() {
        if (player == null) {
            return;
        }

        Duration total = player.getTotalDuration();
        if (total == null || total.isUnknown() || total.isIndefinite() || total.toMillis() == 0) {
            return;
        }

        progresso.set(player.getCurrentTime().toMillis() / total.toMillis() * 100);
    }

    public void seekPercentual(double percentual) {
        if (player == null) {
            return;
        }

        Duration total = player.getTotalDuration();
        if (total == null || total.isUnknown() || total.isIndefinite()) {
            return;
        }

        player.seek(total.multiply(percentual / 100));
    }

    public void retomar() {
        if (player != null) {
            System.out.println("Retomando a mesma musica");
            // player.setOnEndOfMedia(this::stop);
            player.play();
            isPlaying.set(true);
        }
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

        progresso.set(0);
        musicaAtual.set(null);
        isPlaying.set(false);
    }
}
