import javax.swing.*;
import java.io.File;

public class JNotepad {
    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        SwingUtilities.invokeLater(() -> new NotepadGUI().setVisible(true));
    }
}