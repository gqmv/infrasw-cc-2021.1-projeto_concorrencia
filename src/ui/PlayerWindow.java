package ui;

import com.formdev.flatlaf.FlatLightLaf;
import ui.resources.fonts.roboto_condensed.Roboto;
import ui.support.MiniPlayerPanel;
import ui.support.QueuePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Objects;

public class PlayerWindow extends Thread {

    private final JFrame window;
    private final JPanel mainPanel;
    private final QueuePanel queuePanel;
    private final MiniPlayerPanel miniPlayerPanel;

    // TODO Sincronizar

    /**
     * @param buttonListenerPlayNow   ActionListener for the "Play Now" button.
     * @param buttonListenerRemove    ActionListener for the "Remove" button.
     * @param buttonListenerAddSong   ActionListener for the "Add Song" button.
     * @param buttonListenerPlayPause ActionListener for the "Play/Pause" button.
     * @param buttonListenerStop      ActionListener for the "Stop" button.
     * @param buttonListenerNext      ActionListener for the "Next" button.
     * @param buttonListenerPrevious  ActionListener for the "Previous" button.
     * @param buttonListenerShuffle   ActionListener for the "Shuffle" button.
     * @param buttonListenerRepeat    ActionListener for the "Repeat" button.
     * @param scrubberListenerClick   MouseListener for the Scrubber.
     * @param scrubberListenerMotion  MouseMotionListener for the Scrubber.
     * @param windowTitle             String to be used as the window title.
     * @param queueArray              String[][] with the queue. The array should contain in each position one array
     *                                from AddSongWindow.getSong().
     */
    public PlayerWindow(
            ActionListener buttonListenerPlayNow,
            ActionListener buttonListenerRemove,
            ActionListener buttonListenerAddSong,
            ActionListener buttonListenerPlayPause,
            ActionListener buttonListenerStop,
            ActionListener buttonListenerNext,
            ActionListener buttonListenerPrevious,
            ActionListener buttonListenerShuffle,
            ActionListener buttonListenerRepeat,
            MouseListener scrubberListenerClick,
            MouseMotionListener scrubberListenerMotion,
            String windowTitle,
            String[][] queueArray) {

        setEnvironment();

        window = new JFrame();
        mainPanel = new JPanel();
        queuePanel = new QueuePanel(
                buttonListenerPlayNow,
                buttonListenerRemove,
                buttonListenerAddSong,
                queueArray);
        miniPlayerPanel = new MiniPlayerPanel(
                buttonListenerPlayPause,
                buttonListenerStop,
                buttonListenerNext,
                buttonListenerPrevious,
                buttonListenerShuffle,
                buttonListenerRepeat,
                scrubberListenerClick,
                scrubberListenerMotion);

        window.setLayout(new BorderLayout());
        window.setTitle(windowTitle);
        // Unreliable behavior with setMinimumSize. Apparently when using high dpi screens with fractional scaling.
        // window.setMinimumSize(new Dimension(667, 450));
        window.setSize(718, 600);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(queuePanel);
        mainPanel.add(miniPlayerPanel);
        window.add(mainPanel);
        window.setVisible(true);
    }

    private void setEnvironment() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());

            Font baseFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    Objects.requireNonNull(Roboto.class.getResourceAsStream("RobotoCondensed-Regular.ttf")));
            Font finalFont = baseFont.deriveFont(Font.PLAIN, 14);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(finalFont);

            UIManager.put("Button.font", finalFont);
            UIManager.put("ToggleButton.font", finalFont);
            UIManager.put("RadioButton.font", finalFont);
            UIManager.put("CheckBox.font", finalFont);
            UIManager.put("ColorChooser.font", finalFont);
            UIManager.put("ComboBox.font", finalFont);
            UIManager.put("Label.font", finalFont);
            UIManager.put("List.font", finalFont);
            UIManager.put("MenuBar.font", finalFont);
            UIManager.put("MenuItem.font", finalFont);
            UIManager.put("RadioButtonMenuItem.font", finalFont);
            UIManager.put("CheckBoxMenuItem.font", finalFont);
            UIManager.put("Menu.font", finalFont);
            UIManager.put("PopupMenu.font", finalFont);
            UIManager.put("OptionPane.font", finalFont);
            UIManager.put("Panel.font", finalFont);
            UIManager.put("ProgressBar.font", finalFont);
            UIManager.put("ScrollPane.font", finalFont);
            UIManager.put("Viewport.font", finalFont);
            UIManager.put("TabbedPane.font", finalFont);
            UIManager.put("Table.font", finalFont);
            UIManager.put("TableHeader.font", finalFont);
            UIManager.put("TextField.font", finalFont);
            UIManager.put("FormattedTextField.font", finalFont);
            UIManager.put("PasswordField.font", finalFont);
            UIManager.put("TextArea.font", finalFont);
            UIManager.put("TextPane.font", finalFont);
            UIManager.put("EditorPane.font", finalFont);
            UIManager.put("TitledBorder.font", finalFont);
            UIManager.put("ToolBar.font", finalFont);
            UIManager.put("ToolTip.font", finalFont);
            UIManager.put("Tree.font", finalFont);
        } catch (IOException | FontFormatException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }


    /**
     * Refreshes the queue list. Should be called whenever a song is added or removed.
     *
     * @param queueArray String[][] with the queue. The array should contain in each position one array
     *                   from AddSongWindow.getSong().
     */
    public void updateQueueList(String[][] queueArray) {
        queuePanel.updateQueueList(queueArray);
    }

    /**
     * Updates the information displayed on the miniplayer about the current song. Should be called whenever the
     * currently playing song changes.
     *
     * @param songTitle  Song title.
     * @param songAlbum  Song album.
     * @param songArtist Song artist.
     */
    public void updatePlayingSongInfo(String songTitle, String songAlbum, String songArtist) {
        miniPlayerPanel.updatePlayingSongInfo(songTitle, songAlbum, songArtist);
    }

    /**
     * Enables the scrubber and buttons. Should be called before the player becomes active and after
     * the 'Play Now' button is pressed.
     */
    public void enableScrubberArea() {
        miniPlayerPanel.enableScrubberArea();
    }

    /**
     * Disables the scrubber and buttons.
     */
    public void disableScrubberArea() {
        miniPlayerPanel.disableScrubberArea();
    }

    /**
     * Sets the icon of the play/pause button.
     *
     * @param isPlaying True if a song is currently being played (displays pause icon), false if the player is paused
     *                  (displays resume icon).
     */
    public void updatePlayPauseButton(boolean isPlaying) {
        miniPlayerPanel.updatePlayPauseButton(isPlaying);
    }

    /**
     * Updates the state of the scrubber, 'next' and 'previous' buttons and current and total times.
     *
     * @param isActive    Is the thread active or is the player completely stopped?
     * @param isPlaying   Is the player playing or is it paused?
     * @param isRepeat    Is the player set to repeat?
     * @param currentTime Progress of the current song in seconds.
     * @param totalTime   Total song time in seconds.
     * @param songIndex   Index of the song in the queue. Used to determine the state of the 'next' and 'previous' buttons.
     * @param queueSize   Total number of songs in the queue. Used to determine the state of the 'next' and 'previous' buttons.
     */
    public void updateMiniplayer(
            boolean isActive,
            boolean isPlaying,
            boolean isRepeat,
            int currentTime,
            int totalTime,
            int songIndex,
            int queueSize) {
        miniPlayerPanel.updateMiniplayer(
                isActive,
                isPlaying,
                isRepeat,
                currentTime,
                totalTime,
                songIndex,
                queueSize);
    }

    /**
     * Resets miniplayer to default values and disables buttons. Should be called whenever the 'stop' button is pressed.
     */
    public void resetMiniPlayer() {
        miniPlayerPanel.resetMiniPlayer();
    }

    /**
     * @return the ID of the selected song in the queue. Should be called whenever the 'Play Now' and 'Remove'
     * buttons are pressed.
     */
    public int getSelectedSongID() {
        return queuePanel.getSelectedSongID();
    }

    /**
     * Should be called whenever the user manually changes the scrubber state with the cursor.
     *
     * @return the current value of the scrubber.
     */
    public int getScrubberValue() {
        return miniPlayerPanel.getScrubberValue();
    }

    /**
     * @return WindowListener to be used as one of the parameters on the instantiation of AddSongWindow.
     */
    public WindowListener getAddSongWindowListener() {
        return queuePanel.getActionListener();
    }
}

