import ui.AddSongWindow;
import ui.PlayerWindow;

import java.awt.event.*;
import java.net.BindException;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class Player {
    ArrayList<String[]> queueList;
    public PlayerWindow window;

    int lastId;
    int currentlyPlayingIndex;
    boolean isPlaying;
    int currentTime;
    String mode = "standard";
    public final Lock lock = new ReentrantLock();
    Counter counter;
    private final Condition playPressedCondition = lock.newCondition();

    public Player() {
        lastId = 0;
        currentlyPlayingIndex = 0;
        currentTime = 0;

        ActionListener buttonListenerPlayNow = e -> {
            playNow();
        };
        ActionListener buttonListenerRemove = e -> {
            removeSong();
        };
        ActionListener buttonListenerAddSong = e -> {
            addSong();
        };
        ActionListener buttonListenerPlayPause = e -> {
            playPause();
        };
        ActionListener buttonListenerStop = e -> {
        };
        ActionListener buttonListenerNext = e -> {
            nextMusic();
        };
        ActionListener buttonListenerPrevious = e -> {
            previousMusic();
        };
        ActionListener buttonListenerShuffle = e -> {
            shuffle();
        };
        ActionListener buttonListenerRepeat = e -> {
            repeat();
        };
        MouseListener scrubberListenerClick = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                lock.lock();
                try {
                    isPlaying = false;

                    currentTime = window.getScrubberValue();
                    int finalTime = Integer.parseInt(getCurrentlyPlayingSong()[5]);

                    window.updateMiniplayer(true, true, false, currentTime, finalTime, currentlyPlayingIndex,
                            queueList.size());
                } catch (Throwable t) {
                }
                lock.unlock();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lock.lock();
                try {
                    isPlaying = true;
                    playPressedCondition.signalAll();
                } catch (Throwable t) {
                }
                lock.unlock();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };

        MouseMotionListener scrubberListenerMotion = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                lock.lock();
                try {
                    currentTime = window.getScrubberValue();
                    int finalTime = Integer.parseInt(getCurrentlyPlayingSong()[5]);

                    window.updateMiniplayer(true, true, false, currentTime, finalTime, currentlyPlayingIndex,
                            queueList.size());
                } catch (Throwable t) {
                }
                lock.unlock();
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        };

        String windowTitle = "Music Player";

        this.queueList = new ArrayList();

        this.window = new PlayerWindow(buttonListenerPlayNow, buttonListenerRemove, buttonListenerAddSong,
                buttonListenerPlayPause, buttonListenerStop, buttonListenerNext, buttonListenerPrevious,
                buttonListenerShuffle, buttonListenerRepeat, scrubberListenerClick, scrubberListenerMotion, windowTitle,
                this.queueList.toArray(new String[0][0]));

    }

    public Condition getPlayPressedCondition() {
        return playPressedCondition;
    }

    public class GetSong {
        AddSongWindow addSongWindow;

        public GetSong() {
            this.addSongWindow = null;
        }

        public void setWindow(AddSongWindow addSongWindow) {
            this.addSongWindow = addSongWindow;
        }

        public String[] run() {
            String[] song = this.addSongWindow.getSong();

            return song;
        }
    }

    private void addSong() {
        int id = this.lastId + 1;
        this.lastId = id;
        GetSong getSong = new GetSong();
        ActionListener buttonListenerAddSongOK = e -> {
            saveSong(getSong);
        };

        AddSongWindow addSongWindow = new AddSongWindow(Integer.toString(id), buttonListenerAddSongOK,
                this.window.getAddSongWindowListener());

        getSong.setWindow(addSongWindow);
    }

    private void saveSong(GetSong getSong) {
        String[] song = getSong.run();

        this.queueList.add(song);
        this.window.updateQueueList(this.queueList.toArray(new String[0][0]));
    }

    private void removeSong() {
        int songId = this.window.getSelectedSongID();
        int idSong = binarySearch(this.queueList, songId);
        this.queueList.remove(idSong);
        this.window.updateQueueList(this.queueList.toArray(new String[0][0]));
        if (currentlyPlayingIndex == idSong) {
            this.isPlaying = false;
            this.window.resetMiniPlayer();
        }

    }

    private int binarySearch(ArrayList<String[]> list, int id) {
        int low = 0;
        int high = list.size() - 1;
        int mid = 0;

        while (low <= high) {
            mid = (low + high) / 2;

            if (list.get(mid)[6].equals(Integer.toString(id))) {
                return mid;
            }

            if (Integer.parseInt(list.get(mid)[6]) < id) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;
    }

    public String[] getCurrentlyPlayingSong() {
        return this.queueList.get(this.currentlyPlayingIndex);
    }

    private void play(int songIndex) {
        this.currentlyPlayingIndex = songIndex;
        String[] song = this.queueList.get(songIndex);
        this.lock.lock();
        try {
            if (this.isPlaying) {
                this.counter.requestStop();
            }

            this.currentTime = 0;
            this.window.updatePlayingSongInfo(song[0], song[1], song[2]);
            this.window.enableScrubberArea();
            // Activate the play music button
            this.isPlaying = true;
            this.window.updatePlayPauseButton(this.isPlaying);
            this.window.updateMiniplayer(true, true, false, 0, Integer.parseInt(song[5]), this.currentlyPlayingIndex,
                    this.queueList.size());
        } catch (Throwable t) {
        }
        this.lock.unlock();

        this.counter = new Counter(playPressedCondition, this);
        this.counter.start();
    }

    private void playNow() {
        int songIndex = binarySearch(this.queueList, this.window.getSelectedSongID());
        play(songIndex);
    }

    private void nextMusic() {
        if (this.currentlyPlayingIndex < this.queueList.size() - 1) {
            this.currentlyPlayingIndex++;
            play(this.currentlyPlayingIndex);
        }
    }

    private void previousMusic() {
        if (this.currentlyPlayingIndex > 0) {
            this.currentlyPlayingIndex--;
            play(this.currentlyPlayingIndex);
        }
    }

    public void updateTime() {
        this.currentTime += 1;
        int finalTime = Integer.parseInt(getCurrentlyPlayingSong()[5]);

        if (finalTime >= this.currentTime) {
            this.window.updateMiniplayer(true, true, false, this.currentTime, finalTime, this.currentlyPlayingIndex,
                    this.queueList.size());
        } else {
            this.counter.requestStop();
            this.currentTime = 0;
            
            if (this.mode == "standard"){
                nextMusic();
            }
            else if (this.mode == "repeat"){
                play(this.currentlyPlayingIndex);
            }
            else if (this.mode == "shuffle"){
                Random rand = new Random();
                int randomIndex = rand.nextInt(this.queueList.size());
                play(randomIndex);
            }            
        }
    }

    private void playPause() {
        this.lock.lock();
        try {
            if (this.isPlaying) {
                this.isPlaying = false;
            } else {
                this.isPlaying = true;
                this.playPressedCondition.signalAll();
            }
            this.window.updatePlayPauseButton(this.isPlaying);
        } catch (Throwable t) {
        }
        this.lock.unlock();
    }

    private void shuffle(){
        if (this.mode == "standard" | this.mode == "repeat"){
            this.mode = "shuffle";
        }
        else if (this.mode == "shuffle"){
            this.mode = "standard";
        }
    }

    private void repeat(){
        if (this.mode == "standard" | this.mode == "shuffle"){
            this.mode = "repeat";
        }
        else if (this.mode == "repeat"){
            this.mode = "standard";
        }
    }
}
