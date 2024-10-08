import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class NotepadGUI extends JFrame implements WindowListener {

    File file;
    String dir, fileName;
    JTextArea textArea;
    UndoManager undoManager;
    boolean isModified;

    public NotepadGUI() {

        // setup file
        file = new File("newfile");
        //fileName = file.getName();
        setTitle("JNotepad - " + file.getName());

        //setup GUI
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // add GUI components
        addMenuBar();
        addTextArea("");
        addUndoManager();

    }

    public NotepadGUI(File file) {

        // setup file
        this.file = file;
        dir = file.getPath();
        fileName = file.getName();
        setTitle("JNotepad - " + fileName);

        //setup GUI
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // add GUI components
        addMenuBar();
        addTextArea(readFile());
        addUndoManager();

    }

    private void addWindowListener() {
    }

    private void addMenuBar() {
        MenuBar menuBar = new MenuBar();

        // add menus
        menuBar.add(addFileMenu());
        menuBar.add(addEditMenu());
        menuBar.add(addFormatMenu());
        menuBar.add(addHelpMenu());

        setMenuBar(menuBar);
    }

    private Menu addHelpMenu() {
        Menu helpMenu =  new Menu("Help");
        return helpMenu;
    }

    private Menu addFormatMenu() {
        Menu formatMenu = new Menu("Formalet");

        // add menu items
        formatMenu.add(addWordWrapMenuItem());
        formatMenu.add(addFontMenuItem());
        return formatMenu;
    }

    private MenuItem addFontMenuItem() {
        MenuItem fontMenuItem = new MenuItem("Font...");
        fontMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FontMenu(NotepadGUI.this, textArea.getFont()).setVisible(true);
            }
        });
        return fontMenuItem;
    }

    private MenuItem addWordWrapMenuItem() {
        CheckboxMenuItem wordWrapMenuItem = new CheckboxMenuItem("Word wrap");
        wordWrapMenuItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Logic for word wrapping
                textArea.setLineWrap(wordWrapMenuItem.getState());
                textArea.setWrapStyleWord(wordWrapMenuItem.getState());
            }
        });
        return wordWrapMenuItem;
    }

    private Menu addEditMenu() {
        Menu editMenu = new Menu("Edit");

        // add menu items
        editMenu.add(addUndoMenuItem());
        editMenu.add(addRedoMenuItem());

        editMenu.addSeparator();

        editMenu.add(addCutMenuItem());
        editMenu.add(addCopyMenuItem());
        editMenu.add(addPasteMenuItem());

        return editMenu;
    }

    private MenuItem addPasteMenuItem() {
        MenuItem pasteMenuItem = new MenuItem("Paste");
        pasteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.paste();
            }
        });
        return pasteMenuItem;
    }

    private MenuItem addCopyMenuItem() {
        MenuItem copyMenuItem = new MenuItem("Copy");
        copyMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.copy();
            }
        });
        return copyMenuItem;
    }

    private MenuItem addCutMenuItem() {
        MenuItem cutMenuItem = new MenuItem("Cut");
        cutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.cut();
            }
        });
        return cutMenuItem;
    }

    private MenuItem addRedoMenuItem() {
        MenuItem redoMenuItem = new MenuItem("Redo");
        redoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                }
            }
        });
        return redoMenuItem;
    }

    private MenuItem addUndoMenuItem() {
        MenuItem undoMenuItem = new MenuItem("Undo");
        undoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
            }
        });
        return undoMenuItem;
    }

    private Menu addFileMenu() {
        Menu fileMenu = new Menu("File");

        // add menu items
        fileMenu.add(addNewMenuItem());
        fileMenu.add(addOpenMenuItem());

        fileMenu.addSeparator();

        fileMenu.add(addSaveMenuItem());
        fileMenu.add(addSaveAsMenuItem());

        return fileMenu;
    }

    private MenuItem addSaveAsMenuItem() {
        MenuItem saveAsMenuItem = new MenuItem("Save as...");
        saveAsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAs(textArea.getText());
            }
        });
        return saveAsMenuItem;
    }

    private MenuItem addSaveMenuItem() {
        MenuItem saveMenuItem = new MenuItem("Save");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dir == null && fileName == null) {
                    fileName = file.getName();
                    saveAs(textArea.getText());
                }
                else {
                    try {
                        FileWriter writer = new FileWriter(dir + fileName);
                        writer.write(textArea.getText());
                        writer.close();
                        isModified = false;
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
        return saveMenuItem;
    }

    private MenuItem addOpenMenuItem() {
        MenuItem openMenuItem = new MenuItem("Open...");
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(NotepadGUI.this, "Open File");
                fd.setMode(FileDialog.LOAD);
                fd.setVisible(true);
                String dir = fd.getDirectory();
                String fileName = fd.getFile();

                if (dir != null && fileName != null) {
                    File openFile = new File(dir, fileName);
                    new NotepadGUI(openFile).setVisible(true);
                } else {
                    System.out.println("File selection was canceled.");
                }
            }
        });
        return openMenuItem;
    }

    private MenuItem addNewMenuItem() {
        MenuItem newMenuItem = new MenuItem("New");
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NotepadGUI().setVisible(true);
            }
        });
        return newMenuItem;
    }

    private void addTextArea(String content) {
        textArea = new JTextArea();
        JScrollPane scoll = new JScrollPane(textArea);
        add(scoll, BorderLayout.CENTER);
        textArea.setText(content);

    }

    // Logic to read from file
    private String readFile() {
        String content = "", line;
        try {
            BufferedReader bf = new BufferedReader(new FileReader(file));
            while ((line = bf.readLine()) != null) {
                content = content + line + '\n';
            }
            bf.close();

        } catch (Exception exp) {
            // TODO Auto-generated catch block
            exp.printStackTrace();
        }
        return content;
    }

    // logic to save as to file
    private boolean saveAs(String content) {
        // set mode to file dialog
        FileDialog fd = new FileDialog(NotepadGUI.this, "Save as...");
        fd.setMode(FileDialog.SAVE);
        // check if file ends with .txt
        if (!fileName.toLowerCase().endsWith(".txt")) {
            fileName += ".txt";
            fd.setFile(fileName);
        }
        fd.setDirectory(dir);
        fd.setFile(fileName);
        fd.setVisible(true);
        dir = fd.getDirectory();
        fileName = fd.getFile();
        // if the user click cancel, return false
        if (dir == null || fileName == null) {
            return false;
        }
        // if both dir and file name are chosen successfully, perform save action and return true
        else if (dir != null && fileName != null) {
            try {
                FileWriter writer = new FileWriter(dir + fileName);
                writer.write(content);
                writer.close();
                isModified = false;

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return true;
    }

    private void addUndoManager() {
        undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
            }
        });
        // Using document listener to track changes
        isModified = false;
        textArea.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                isModified = true;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                isModified = true;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                isModified = true;
            }

        });
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (isModified) {
            String[] options = {"Save...", "Don't save", "Cancel"};
            int choice = JOptionPane.showOptionDialog(NotepadGUI.this, "Do you want to save changes before closing?", "Unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == JOptionPane.YES_OPTION) {
                if (saveAs(textArea.getText())) {
                    dispose();
                }
            }
            else if (choice == JOptionPane.NO_OPTION) {
                dispose();
            }
        }
        else {
            dispose();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
}
