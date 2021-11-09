import ui.PlayerWindow;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Player {
    public Player() {
        ActionListener buttonListenerPlayNow = e -> { };
        ActionListener buttonListenerRemove = e -> { };
        ActionListener buttonListenerAddSong = e -> { };
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

        String[][] queueArray = new String[6][0];

        PlayerWindow window = new PlayerWindow(buttonListenerPlayNow, buttonListenerRemove, buttonListenerAddSong, buttonListenerPlayNow, buttonListenerStop, buttonListenerNext, buttonListenerPrevious, buttonListenerShuffle, buttonListenerRepeat, scrubberListenerClick, scrubberListenerMotion, windowTitle, queueArray);

    }
}

