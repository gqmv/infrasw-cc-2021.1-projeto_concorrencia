import ui.AddSongWindow;
import ui.PlayerWindow;

import java.awt.event.*;
import java.util.ArrayList;

public class Player {
    ArrayList<String[]> queueList;
    PlayerWindow window;

    int lastId;

    public Player() {
        lastId = 0;

        ActionListener buttonListenerPlayNow = e -> { };
        ActionListener buttonListenerRemove = e -> { };
        ActionListener buttonListenerAddSong = e -> { addSong(); };
        ActionListener buttonListenerStop = e -> { };
        ActionListener buttonListenerNext = e -> { };
        ActionListener buttonListenerPrevious = e -> { };
        ActionListener buttonListenerShuffle = e -> { };
        ActionListener buttonListenerRepeat = e -> { };
        MouseListener scrubberListenerClick = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

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

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        };

        String windowTitle = "Music Player";

        this.queueList = new ArrayList();

        this.window = new PlayerWindow(buttonListenerPlayNow, buttonListenerRemove, buttonListenerAddSong, buttonListenerPlayNow, buttonListenerStop, buttonListenerNext, buttonListenerPrevious, buttonListenerShuffle, buttonListenerRepeat, scrubberListenerClick, scrubberListenerMotion, windowTitle, this.queueList.toArray(new String[0][0]));

    }

    public class GetSong{
        AddSongWindow addSongWindow;

        public GetSong(){
            this.addSongWindow = null;
        }

        public void setWindow(AddSongWindow addSongWindow){
            this.addSongWindow = addSongWindow;
        }

        public String[] run(){
            String[] song = this.addSongWindow.getSong();

            return song;
        }
    }

    private void addSong(){
        int id = this.lastId + 1;
        this.lastId = id;
        GetSong getSong = new GetSong();
        ActionListener buttonListenerAddSongOK = e -> {saveSong(getSong); };

        AddSongWindow addSongWindow = new AddSongWindow(Integer.toString(id), buttonListenerAddSongOK, this.window.getAddSongWindowListener());

        getSong.setWindow(addSongWindow);
    }

    private void saveSong(GetSong getSong){
        String[] song = getSong.run();

        this.queueList.add(song);
        this.window.updateQueueList(this.queueList.toArray(new String[0][0]));
    }
}

