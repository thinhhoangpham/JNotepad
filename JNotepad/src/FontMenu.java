import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontMenu extends JDialog {

    private NotepadGUI source;
    private Font font;
    private JLabel currentlySelectedLabel;

    public FontMenu(NotepadGUI source, Font font) {

        this.source = source;
        this.font = font;

        setTitle("Font Chooser");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(425, 350);
        setLocationRelativeTo(source);
        setModal(true);
        setResizable(false);
        setLayout(null);

        addMenuComponents();



    }

    private void addMenuComponents() {
        addFontList();
        addFontSizeList();
        addFontStyleList();
    }

    private void addFontList() {
        // add font Label
        JLabel fontLabel = new JLabel("Font");
        fontLabel.setBounds(12, 5, 180, 10);
        add(fontLabel);

        // add panel to show current and select font
        JPanel fontChooserPanel = new JPanel();
        fontChooserPanel.setBounds(10, 15, 200, 160);

        // text field to show current font
        JTextField currentFontField = new JTextField(font.getFamily());
        currentFontField.setPreferredSize(new Dimension(200, 25));
        currentFontField.setEditable(false);
        fontChooserPanel.add(currentFontField);


        // add font list
        JPanel fontListPanel = new JPanel();
        fontListPanel.setLayout(new BoxLayout(fontListPanel, BoxLayout.Y_AXIS)); // set 1 column layout
        fontListPanel.setBackground(Color.WHITE);
        JScrollPane fontScrollPane = new JScrollPane(fontListPanel);
        fontScrollPane.setPreferredSize(new Dimension(200, 125));

        // retrieve fonts
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        // display font List
        for (String fontName : fontNames) {
            JLabel fontNameLabel = new JLabel(fontName);
            fontNameLabel.setOpaque(true);
            if (currentFontField.getText().equalsIgnoreCase(fontName)) {
                fontNameLabel.setBackground(Color.BLUE);
                fontNameLabel.setForeground(Color.WHITE);
                currentlySelectedLabel = fontNameLabel;
            }
            else {
                fontNameLabel.setOpaque(true);
                fontNameLabel.setBackground(null);
                fontNameLabel.setForeground(null);
            }
            fontNameLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (currentlySelectedLabel != null) {
                        currentlySelectedLabel.setBackground(null);
                        currentlySelectedLabel.setForeground(null);
                        fontNameLabel.setForeground(Color.WHITE);
                        fontNameLabel.setBackground(Color.BLUE);
                    }

                    currentFontField.setText(fontName);
                    currentlySelectedLabel = fontNameLabel;


                }
            });
            fontListPanel.add(fontNameLabel);
        }
        fontChooserPanel.add(fontScrollPane);

        add(fontChooserPanel);
    }

    private void addFontSizeList() {
        // add size label
        JLabel sizeLabel = new JLabel("Size");
        sizeLabel.setBounds(220, 5, 70, 10);
        add(sizeLabel);

        // add panel to show current and select size
        JPanel sizePanel = new JPanel();
        sizePanel.setBounds(220, 15, 70, 160);

        // add field to show current font size
        JTextField currentSize = new JTextField(String.valueOf(font.getSize()));
        currentSize.setPreferredSize(new Dimension(70, 25));
        currentSize.setEditable(false);
        sizePanel.add(currentSize);


        add(sizePanel);
    }

    private void addFontStyleList() {
        // add style label
        JLabel styleLabel = new JLabel("Style");
        styleLabel.setBounds(300, 5, 95, 10);
        add(styleLabel);

        // get current font style
        int currentStyle = font.getStyle();
        String currentStyleString = "";
        switch (currentStyle) {
            case Font.PLAIN -> currentStyleString = "Plain";
            case Font.BOLD -> currentStyleString = "Bold";
            case Font.ITALIC -> currentStyleString = "Italic";
            default -> currentStyleString = "Bold Italic";
        }



        // add panel to show current and select styles
        JPanel styePanel = new JPanel();
        styePanel.setBounds(300, 15, 95, 160);

        // add field to show current style
        JTextField currentStyleField = new JTextField(currentStyleString);
        currentStyleField.setPreferredSize(new Dimension(95, 25));
        currentStyleField.setEditable(false);
        styePanel.add(currentStyleField);

        // add list of available style
        JPanel fontListPanel = new JPanel();
        // make it have only one column
        fontListPanel.setLayout(new BoxLayout(fontListPanel, BoxLayout.Y_AXIS));


        add(styePanel);
    }
}
