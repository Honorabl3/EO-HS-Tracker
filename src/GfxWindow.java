import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.Random;

public class GfxWindow extends JPanel implements Runnable
{
    private boolean running;
    public Tracker tracker;
    
    int sizeX, sizeY;
    
    public JPanel xpList, loadingPanel, loadingPanel2, loadingLabel2Panel, topPanel, topLeftPanel, topMiddlePanel, topRightPanel, changelogPanel, settingsPanel;
    public JLabel loadingChinaLabel, loadingLabel1, loadingLabel2, loadingLabel3, changelogLabel1, changelogLabel2, updateTimer;
    
    //  Buttons
    public JButton infoButton, changelogButton, refreshButton, addButton;
    
    //  SETTINGS PANEL
    public JLabel settingsButton, settingsLabel1, settingsFontSizeLabel, settingsGraphLabel1, settingsDataLabel1, settingsSearchSize1Label, settingsSearchSize2Label;   //  settingsButton is a Label that behaves as a button
    public JPanel settingsColorSchemaPanel, settingsGraphPanel, settingsDataPanel, settingsBottomPanel;
    public Color colorSchemaColor;
    public JTextField colorSchema1TextField, colorSchema2TextField, colorSchema3TextField, settingsFontSize, settingsGraphNodeSpacing, settingsDataPullSize, settingsDataSearchSize;
    public JButton applySettingsButton, resetSettingsButton;
    public JComboBox fontComboBox;
    public JCheckBox debugCheckBox;
    
    //  ICONS
    
    public JTextField addTextField;
    public ImageIcon redXIcon, settingsIcon, chinaIcon1, chinaIcon2, logoIcon;
    
    //  FONTS
    public Font lineFont;
    
    
    public ArrayList<JLabel> stringList;
    
    // testing vars
    long timestamp = 0;
    
    public GfxWindow(Tracker t)
    {
        tracker = t;
        sizeX = 800;
        sizeY = 400;
        
        //  Load relevant images/icons
        //  Red X image
        redXIcon = new ImageIcon("EO-HS-Tracker/images/x2.png");
        settingsIcon = new ImageIcon("EO-HS-Tracker/images/settings.png");
        chinaIcon1 = new ImageIcon("EO-HS-Tracker/images/china1.png");
        chinaIcon2 = new ImageIcon("EO-HS-Tracker/images/china2.png");
        logoIcon = new ImageIcon("EO-HS-Tracker/images/logo.png");
        
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(sizeX, sizeY));
        setLayout(new BorderLayout());
        
        topPanel = new JPanel();
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.setPreferredSize(new Dimension(300, 20));
        topPanel.setLayout(new BorderLayout());
        topPanel.setVisible(true);
        this.add(topPanel, BorderLayout.NORTH);
        
        changelogPanel = new JPanel();
        changelogPanel.setBackground(Color.BLACK);
        changelogPanel.setSize(500, 300);
        changelogPanel.setLayout(new BoxLayout(changelogPanel, BoxLayout.Y_AXIS));
        changelogPanel.setVisible(false);
        this.add(changelogPanel);
        
        changelogLabel1 = new JLabel();
        changelogLabel1.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        changelogLabel1.setForeground(Color.WHITE);
        changelogLabel1.setText("<html><p style=\"text-align: center;\"> EO Highscore Tracker v0.3</p></html>");
        changelogPanel.add(changelogLabel1);
        
        changelogLabel2 = new JLabel();
        changelogLabel2.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        changelogLabel2.setForeground(Color.WHITE);
        changelogLabel2.setText("<html><p>+ Added Settings Panel<br/><ul><li>Grid Color, Font, Font Color, Font Size, and Pull Size options added.</li></ul>= Fixed XP/Rate sometimes displaying wrong value in special cases<br/>= Fixed Manual Reset button not dumping old data<br/><br/>v0.2<br/>+ New Graphical Interface<br/>+ Searchable Player Name Tracking<br/>+ Manual Reset Button Added</p><br/><br/><br/><br/><br/><br/></html>");
        changelogPanel.add(changelogLabel2);
        
        
        
        // TOP LEFT EDGE PANEL
        
        topLeftPanel = new JPanel();
        topLeftPanel.setBackground(Color.LIGHT_GRAY);
        topLeftPanel.setPreferredSize(new Dimension(155, 20));
        topLeftPanel.setLayout(new BorderLayout());
        topLeftPanel.setVisible(true);
        topPanel.add(topLeftPanel, BorderLayout.WEST);
        
        // INFO BUTTON
        
        infoButton = new JButton("?");
        infoButton.setSize(infoButton.getPreferredSize());
        topLeftPanel.add(infoButton, BorderLayout.WEST);
        
        // Add an ActionListener to the button
        infoButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Define the action to be performed when the button is clicked
                JOptionPane.showMessageDialog(xpList, "Disclaimer: (Made in China) This tool is currently in its infancy.\nThere are many bugs/issues still being worked on.\nYou can report new bugs by reaching out to in-game user \"China\" !");
            }
        });
        
        
        // CHANGELOG BUTTON
        changelogButton = new JButton("Changelog");
        changelogButton.setSize(changelogButton.getPreferredSize());
        topLeftPanel.add(changelogButton, BorderLayout.CENTER);
        
        // Add an ActionListener to the button
        changelogButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Define the action to be performed when the button is clicked
                if(changelogPanel.isVisible())
                {
                    changelogPanel.setVisible(false);
                    settingsPanel.setVisible(false);
                    
                    xpList.setVisible(true);
                }
                else
                {
                    changelogPanel.setLocation((sizeX/2)-(changelogPanel.getWidth()/2), (sizeY/2)-(changelogPanel.getHeight()/2));
                    //changelogPanel.setLocation(tracker.panel.getContentPane().getWidth(), tracker.panel.getContentPane().getHeight());
                    //changelogPanel.setLocation(50, 50);
                    xpList.setVisible(false);
                    settingsPanel.setVisible(false);
                    
                    changelogPanel.setVisible(true);
                }
            }
        });
        
        
        //  SETTINGS PANEL
        
        colorSchemaColor = new Color(tracker.settings.colorSchemaR, tracker.settings.colorSchemaG, tracker.settings.colorSchemaB);
        
        settingsPanel = new JPanel();
        settingsPanel.setBackground(Color.BLACK);
        settingsPanel.setSize(600, 360);
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setVisible(false);
        this.add(settingsPanel);
        
        settingsColorSchemaPanel = new JPanel();
        settingsColorSchemaPanel.setBackground(colorSchemaColor);
        settingsColorSchemaPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        settingsColorSchemaPanel.setPreferredSize(settingsPanel.getPreferredSize());
        settingsPanel.add(settingsColorSchemaPanel);
        
        settingsLabel1 = new JLabel();
        settingsLabel1.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        settingsLabel1.setForeground(Color.WHITE);
        settingsLabel1.setText("<html><p style=\"text-align: left;\">Grid Color </p></html>");
        settingsColorSchemaPanel.add(settingsLabel1);
        
        colorSchema1TextField = new JTextField(tracker.settings.colorSchemaR + "");
        colorSchema1TextField.setPreferredSize(new Dimension(40, 20));
        settingsColorSchemaPanel.add(colorSchema1TextField);
        
        ((AbstractDocument)colorSchema1TextField.getDocument()).setDocumentFilter(new DocumentFilter()
        {
            Pattern regEx = Pattern.compile("\\d*");
    
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
            {          
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches())
                {
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });
        
        colorSchema2TextField = new JTextField(tracker.settings.colorSchemaG + "");
        colorSchema2TextField.setPreferredSize(new Dimension(40, 20));
        settingsColorSchemaPanel.add(colorSchema2TextField);
        
        ((AbstractDocument)colorSchema2TextField.getDocument()).setDocumentFilter(new DocumentFilter()
        {
            Pattern regEx = Pattern.compile("\\d*");
    
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
            {          
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches())
                {
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });
        
        colorSchema3TextField = new JTextField(tracker.settings.colorSchemaB + "");
        colorSchema3TextField.setPreferredSize(new Dimension(40, 20));
        settingsColorSchemaPanel.add(colorSchema3TextField);
        
        ((AbstractDocument)colorSchema3TextField.getDocument()).setDocumentFilter(new DocumentFilter()
        {
            Pattern regEx = Pattern.compile("\\d*");
    
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
            {
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches())
                {
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });
        
        //lineFont = new Font("Courier New", Font.PLAIN, 12);
        lineFont = new Font(tracker.settings.font, Font.PLAIN, tracker.settings.fontSize);
        // Create an array of strings to populate the combo box
        String[] fontList = {"Courier New", "Lucida Console", "Monaco", "Consolas", "Inconsolata", "Menlo", "Liberation Mono", "DejaVu Sans Mono", "Source Code Pro"};

        // Create a combo box and add the font array as its items
        fontComboBox = new JComboBox<>(fontList);
        fontComboBox.setSelectedItem((String) tracker.settings.font);
        fontComboBox.setMaximumSize(new Dimension(140, 30));
        fontComboBox.setPreferredSize(fontComboBox.getPreferredSize());
        settingsColorSchemaPanel.add(fontComboBox);
        
        settingsFontSizeLabel = new JLabel();
        settingsFontSizeLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        settingsFontSizeLabel.setForeground(Color.WHITE);
        settingsFontSizeLabel.setText("<html><p style=\"text-align: left;\"> Font Size </p></html>");
        settingsColorSchemaPanel.add(settingsFontSizeLabel);
        
        settingsFontSize = new JTextField(tracker.settings.fontSize + ""); // ------------------
        settingsFontSize.setPreferredSize(new Dimension(40, 20));
        settingsColorSchemaPanel.add(settingsFontSize);
        
        ((AbstractDocument)colorSchema1TextField.getDocument()).setDocumentFilter(new DocumentFilter()
        {
            Pattern regEx = Pattern.compile("\\d*");
    
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
            {          
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches())
                {
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });
        
        settingsGraphPanel = new JPanel();
        settingsGraphPanel.setBackground(Color.BLACK);
        settingsGraphPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        settingsGraphPanel.setPreferredSize(settingsPanel.getPreferredSize());
        settingsPanel.add(settingsGraphPanel);
        
        settingsGraphLabel1 = new JLabel();
        settingsGraphLabel1.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        settingsGraphLabel1.setForeground(Color.WHITE);
        settingsGraphLabel1.setText("<html><p style=\"text-align: left;\">Graph Node Spacing </p></html>");
        settingsGraphPanel.add(settingsGraphLabel1);
        
        // Add MouseListener to the label
        settingsGraphLabel1.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
            	JOptionPane.showMessageDialog(settingsPanel, "Spacing of horizontal data points in the unit of pixels.");
            }
        });
        
        settingsGraphNodeSpacing = new JTextField(tracker.settings.graphNodeSpacing + "");
        settingsGraphNodeSpacing.setPreferredSize(new Dimension(40, 20));
        settingsGraphPanel.add(settingsGraphNodeSpacing);
        
        ((AbstractDocument)settingsGraphNodeSpacing.getDocument()).setDocumentFilter(new DocumentFilter()
        {
            Pattern regEx = Pattern.compile("\\d*");
    
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
            {          
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches())
                {
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });
        
        
        settingsDataPanel = new JPanel();
        settingsDataPanel.setBackground(Color.BLACK);
        settingsDataPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        settingsDataPanel.setPreferredSize(settingsPanel.getPreferredSize());
        settingsPanel.add(settingsDataPanel);
        
        settingsDataLabel1 = new JLabel();
        settingsDataLabel1.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        settingsDataLabel1.setForeground(Color.WHITE);
        settingsDataLabel1.setText("<html><p style=\"text-align: left;\">Top Rank Size </p></html>");
        settingsDataPanel.add(settingsDataLabel1);
        
        settingsDataPullSize = new JTextField(tracker.settings.pullSize + "");
        settingsDataPullSize.setPreferredSize(new Dimension(40, 20));
        settingsDataPanel.add(settingsDataPullSize);
        
        ((AbstractDocument)colorSchema1TextField.getDocument()).setDocumentFilter(new DocumentFilter()
        {
            Pattern regEx = Pattern.compile("\\d*");
    
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
            {          
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches())
                {
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });
        
        settingsSearchSize1Label = new JLabel();
        settingsSearchSize1Label.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        settingsSearchSize1Label.setForeground(Color.WHITE);
        settingsSearchSize1Label.setText("<html><p style=\"text-align: left;\"> Data Search Size </p></html>");
        settingsDataPanel.add(settingsSearchSize1Label);
        
        Border border = BorderFactory.createLineBorder(Color.WHITE, 2);
        settingsSearchSize2Label = new JLabel("?");
        settingsSearchSize2Label.setMinimumSize(new Dimension(20, 20));
        settingsSearchSize2Label.setMaximumSize(new Dimension(20, 20));
        settingsSearchSize2Label.setPreferredSize(new Dimension(20, 20));
        settingsSearchSize2Label.setForeground(Color.WHITE);
        settingsSearchSize2Label.setBorder(border);
        settingsSearchSize2Label.setFont(new Font("Lucida Console", Font.BOLD, 16));
        settingsDataPanel.add(settingsSearchSize2Label);
        
        // Add MouseListener to the label
        settingsSearchSize2Label.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
            	JOptionPane.showMessageDialog(settingsPanel, "When pulling in data, this field represents the amount\n of entries it's allowed to search for the \"specific player\" list.\nBy default the value is 5000, the higher the value the\n more processing must be done to find the data.");
            }
        });
        
        settingsDataSearchSize = new JTextField(tracker.settings.searchSize + "");
        settingsDataSearchSize.setPreferredSize(new Dimension(40, 20));
        settingsDataPanel.add(settingsDataSearchSize);
        
        ((AbstractDocument)settingsDataSearchSize.getDocument()).setDocumentFilter(new DocumentFilter()
        {
            Pattern regEx = Pattern.compile("\\d*");
    
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
            {          
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches())
                {
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });
        
        settingsBottomPanel = new JPanel();
        settingsBottomPanel.setBackground(Color.BLACK);
        settingsBottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        settingsBottomPanel.setPreferredSize(settingsPanel.getPreferredSize());
        settingsPanel.add(settingsBottomPanel);
        
        debugCheckBox = new JCheckBox("Debug Mode");
        debugCheckBox.setSelected(tracker.settings.debugMode);
        debugCheckBox.setBackground(settingsDataPanel.getBackground());
        settingsBottomPanel.add(debugCheckBox);
        
        resetSettingsButton = new JButton("Reset Default");
        resetSettingsButton.setPreferredSize(new Dimension(120, 20));
        resetSettingsButton.setSize(new Dimension(120, 20));
        settingsBottomPanel.add(resetSettingsButton);
        
        resetSettingsButton.addActionListener(new ActionListener()    // Add an ActionListener to the button
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                tracker.settings.applySettingsProfile(new SettingsProfile(tracker));
                
                colorSchema1TextField.setText(tracker.settings.colorSchemaR + "");
                colorSchema2TextField.setText(tracker.settings.colorSchemaG + "");
                colorSchema3TextField.setText(tracker.settings.colorSchemaB + "");
                fontComboBox.setSelectedItem((String) tracker.settings.font);
                settingsFontSize.setText(tracker.settings.fontSize + "");
                settingsGraphNodeSpacing.setText(tracker.settings.graphNodeSpacing + "");
                settingsDataPullSize.setText(tracker.settings.pullSize + "");
                settingsDataSearchSize.setText(tracker.settings.searchSize + "");
            }
        });
        
        applySettingsButton = new JButton("Apply");
        applySettingsButton.setPreferredSize(new Dimension(80, 20));
        applySettingsButton.setSize(new Dimension(80, 20));
        settingsBottomPanel.add(applySettingsButton);
        
        applySettingsButton.addActionListener(new ActionListener()    // Add an ActionListener to the button
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //  APPLY COLOR SWATCH HERE
                int colorR = 0, colorG = 0, colorB = 0, fontSize = 12, graphNodeSpacing = 8, pullSize = 20, searchSize = 5000;
                
                colorR = Integer.parseInt(colorSchema1TextField.getText());
                colorG = Integer.parseInt(colorSchema2TextField.getText());
                colorB = Integer.parseInt(colorSchema3TextField.getText());
                fontSize = Integer.parseInt(settingsFontSize.getText());
                graphNodeSpacing = Integer.parseInt(settingsGraphNodeSpacing.getText());
                pullSize = Integer.parseInt(settingsDataPullSize.getText());
                searchSize = Integer.parseInt(settingsDataSearchSize.getText());
                
                if(colorSchema1TextField.getText().equals(""))
                    colorSchema1TextField.setText("0");
                if(colorSchema2TextField.getText().equals(""))
                    colorSchema2TextField.setText("0");
                if(colorSchema3TextField.getText().equals(""))
                    colorSchema3TextField.setText("0");
                if(settingsFontSize.getText().equals(""))
                    settingsFontSize.setText("12");
                if(settingsGraphNodeSpacing.getText().equals(""))
                	settingsGraphNodeSpacing.setText("8");
                if(settingsDataPullSize.getText().equals(""))
                    settingsDataPullSize.setText("20");
                if(settingsDataSearchSize.getText().equals(""))
                	settingsDataSearchSize.setText("5000");
                
                if(colorR < 0)
                    colorSchema1TextField.setText("0");
                if(colorG < 0)
                    colorSchema2TextField.setText("0");
                if(colorB < 0)
                    colorSchema3TextField.setText("0");
                
                if(colorR > 255)
                    colorSchema1TextField.setText("255");
                if(colorG > 255)
                    colorSchema2TextField.setText("255");
                if(colorB > 255)
                    colorSchema3TextField.setText("255");
                if(fontSize < 8)
                    settingsFontSize.setText("8");
                if(fontSize > 90)
                    settingsFontSize.setText("90");
                if(graphNodeSpacing > 100)
                	settingsGraphNodeSpacing.setText("100");
                if(graphNodeSpacing < 1)
                	settingsGraphNodeSpacing.setText("1");
                
                if(pullSize < 0)
                    settingsDataPullSize.setText("0");
                if(pullSize > 100)
                    settingsDataPullSize.setText("100");
                if(searchSize < 100)
                	settingsDataSearchSize.setText("100");
                if(searchSize > 15000)
                	settingsDataSearchSize.setText("15000");
                
                
                colorSchemaColor = new Color(Integer.parseInt(colorSchema1TextField.getText()), Integer.parseInt(colorSchema2TextField.getText()), Integer.parseInt(colorSchema3TextField.getText()));
                tracker.settings.colorSchemaR = Integer.parseInt(colorSchema1TextField.getText());
        		tracker.settings.colorSchemaG = Integer.parseInt(colorSchema2TextField.getText());
        		tracker.settings.colorSchemaB = Integer.parseInt(colorSchema3TextField.getText());
                settingsColorSchemaPanel.setBackground(colorSchemaColor);
                
                String selectedFont = (String) fontComboBox.getSelectedItem();  //  Gets the font listed in the font drop down box options
                tracker.settings.font = selectedFont;
                tracker.settings.fontSize = Integer.parseInt(settingsFontSize.getText());
                lineFont = new Font(selectedFont, Font.PLAIN, Integer.parseInt(settingsFontSize.getText()));
                tracker.settings.graphNodeSpacing = Integer.parseInt(settingsGraphNodeSpacing.getText());
                tracker.settings.pullSize = Integer.parseInt(settingsDataPullSize.getText());
                tracker.settings.searchSize = Integer.parseInt(settingsDataSearchSize.getText());
                tracker.settings.debugMode = debugCheckBox.isSelected(); 
                
                tracker.saveData();
            }
        });
        
        settingsColorSchemaPanel.setSize(new Dimension(settingsPanel.getPreferredSize().width, settingsColorSchemaPanel.getPreferredSize().height));
        
        // SETTINGS BUTTON
        settingsButton = new JLabel(settingsIcon);
        settingsButton.setMinimumSize(new Dimension(20, 20));
        settingsButton.setMaximumSize(new Dimension(20, 20));
        settingsButton.setPreferredSize(new Dimension(20, 20));
        topLeftPanel.add(settingsButton, BorderLayout.EAST);
        
        // Add MouseListener to the label
        settingsButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(settingsPanel.isVisible())
                {
                    changelogPanel.setVisible(false);
                    settingsPanel.setVisible(false);
                    
                    xpList.setVisible(true);
                }
                else
                {
                    settingsPanel.setLocation((sizeX/2)-(settingsPanel.getWidth()/2), (sizeY/2)-(settingsPanel.getHeight()/2));
                    
                    xpList.setVisible(false);
                    changelogPanel.setVisible(false);
                    
                    settingsPanel.setVisible(true);
                }
            }
        });
        
        
        // TOP MIDDLE PANEL
        
        topMiddlePanel = new JPanel();
        topMiddlePanel.setBackground(Color.LIGHT_GRAY);
        topMiddlePanel.setPreferredSize(new Dimension(topMiddlePanel.getWidth(), 20));
        topMiddlePanel.setLayout(new BorderLayout());
        topMiddlePanel.setVisible(true);
        topPanel.add(topMiddlePanel, BorderLayout.CENTER);
        
        // "Refresh in #s" update timer text
        
        updateTimer = new JLabel("");
        updateTimer.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        //updateTimer.setOutlineColor(Color.BLACK);
        updateTimer.setForeground(Color.decode("#115511"));
        topMiddlePanel.add(updateTimer, BorderLayout.WEST);
        
        
        // Force Refresh BUTTON
        refreshButton = new JButton("Manual Reset");
        //refreshButton.setSize(refreshButton.getPreferredSize());
        refreshButton.setSize(new Dimension(200, 20));
        topMiddlePanel.add(refreshButton, BorderLayout.CENTER);
        
        refreshButton.addActionListener(new ActionListener()    // Add an ActionListener to the button
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Define the action to be performed when the button is clicked
                //tracker.pullData();
                try
                {
                    tracker.previous.clear();
                    tracker.web.list.clear();
                    tracker.web.retrieve();
                    
                    tracker.lastUpdate = tracker.lastUpdate.minusMillis(tracker.updateTimer);
                    tracker.updateFrame();
                    //tracker.printData();
                }
                
                catch(Exception ee)
                {
                    System.out.println("Manual Reset Button [Error] - " + ee);
                    ee.printStackTrace();
                }
            }
        });
        
        
        // TOP RIGHT EDGE PANEL
        topRightPanel = new JPanel();
        topRightPanel.setBackground(Color.LIGHT_GRAY);
        topRightPanel.setPreferredSize(new Dimension(210, 20));
        topRightPanel.setLayout(new BorderLayout());
        topRightPanel.setVisible(true);
        topPanel.add(topRightPanel, BorderLayout.EAST);
        
        addButton = new JButton("Add Player");
        topRightPanel.add(addButton, BorderLayout.EAST);
        
        // Add an ActionListener to the button
        addButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Define the action to be performed when the button is clicked
                if(addTextField.getText().length() >= 4)
                {
                    tracker.specificList.add(new String(addTextField.getText()));
                    JOptionPane.showMessageDialog(xpList, "Character name \"" + addTextField.getText() + "\" will now be tracked!\nPlease wait until next web data pull for list update..");
                    
                    if(xpList != null)
                    {
                        //JLabel tempLabel = new JLabel(addTextField.getText());
                        //tempLabel.setForeground(Color.WHITE);
                        //xpList.add(tempLabel);
                    }
                    
                    addTextField.setText("");
                    
                    tracker.web.recompile();
                    tracker.printData();
                }
            }
        });
        
        addTextField = new JTextField(12);
        //addTextField.setSize(128, 20);
        topRightPanel.add(addTextField, BorderLayout.WEST);
        
        
        xpList = new JPanel();
        xpList.setLayout(new BoxLayout(xpList, BoxLayout.Y_AXIS));
        //xpList.setLocation(0, 0);
        xpList.setPreferredSize(new Dimension(sizeX, sizeY));
        xpList.setBackground(Color.black);
        xpList.setVisible(true);
        this.add(xpList, BorderLayout.CENTER);
        
        Random random = new Random(System.currentTimeMillis());
        loadingChinaLabel = new JLabel();
        if(random.nextInt(2) > 0)
        	loadingChinaLabel.setIcon(chinaIcon1);
        else
        	loadingChinaLabel.setIcon(chinaIcon2);
        loadingChinaLabel.setMinimumSize(new Dimension(32, 64));
        loadingChinaLabel.setMaximumSize(new Dimension(32, 64));
        loadingChinaLabel.setPreferredSize(new Dimension(32, 64));
        
        //Border titleBorder = BorderFactory.createLineBorder(Color.WHITE, 2);
        //loadingLabel1 = new JLabel("XP Tracker");
        loadingLabel1 = new JLabel(logoIcon);
        loadingLabel1.setPreferredSize(new Dimension(360, 128));
        //loadingLabel1.setFont(new Font("Impact", Font.PLAIN, 64));
        //loadingLabel1.setBorder(titleBorder);
        //loadingLabel1.setForeground(Color.WHITE);
        
        loadingLabel2 = new JLabel("Made by China");
        loadingLabel2.setFont(new Font("Comic Sans", Font.BOLD, 10));
        loadingLabel2.setForeground(Color.WHITE);
        
        loadingLabel2Panel = new JPanel();
        loadingLabel2Panel.setLayout(new BoxLayout(loadingLabel2Panel, BoxLayout.X_AXIS));
        loadingLabel2Panel.setBackground(Color.BLACK);
        loadingLabel2Panel.add(Box.createHorizontalGlue()); // Add horizontal glue
        loadingLabel2Panel.add(loadingLabel2);
        
        loadingLabel3 = new JLabel("Loading...");
        loadingLabel3.setFont(new Font("Comic Sans", Font.BOLD, 36));
        loadingLabel3.setForeground(Color.WHITE);
        
        loadingPanel2 = new JPanel();
        loadingPanel2.setLayout(new BoxLayout(loadingPanel2, BoxLayout.Y_AXIS));
        loadingPanel2.setPreferredSize(loadingPanel2.getPreferredSize());
        loadingPanel2.setMaximumSize(new Dimension((int) (loadingLabel1.getPreferredSize().width * 1.18), (int) (loadingLabel2.getPreferredSize().height*14)));
        loadingPanel2.setBackground(Color.BLACK);
        loadingPanel2.setVisible(true);
        loadingPanel2.add(loadingLabel1);
        loadingPanel2.add(loadingLabel2Panel);
        loadingPanel2.add(loadingLabel3);
        
        loadingPanel = new JPanel();
        loadingPanel.setLayout(new BoxLayout(loadingPanel, BoxLayout.X_AXIS));
        //loadingPanel.setMinimumSize(new Dimension(sizeX, sizeY));
        loadingPanel.setPreferredSize(loadingPanel.getPreferredSize());
        loadingPanel.setBackground(Color.BLACK);
        loadingPanel.setVisible(true);
        loadingPanel.add(loadingChinaLabel);
        loadingPanel.add(loadingPanel2);
        
        xpList.add(new JLabel(" "));
        xpList.add(new JLabel(" "));
        xpList.add(new JLabel(" "));
        xpList.add(new JLabel(" "));
        xpList.add(new JLabel(" "));
        xpList.add(loadingPanel);
        
        
        
        setVisible(true);
    }

    @Override
    public void run()
    {
        running = true;

        while (running)
        {
            // Update the game logic here
            
            //timestamp);
            
            if(tracker != null)
            {
                if(xpList != null)
                {
                    for(int a=0;a<xpList.getComponentCount();a++)
                    {
                        //xpList.getComponent(a).setBounds(0, a*16, 600, 32);
                        xpList.getComponent(a).setVisible(true);
                        xpList.getComponent(a).repaint();
                    }
                    
                    //System.out.println("panel size: " + ((JPanel) this.getComponent(0)).getComponentCount());
                }
            }
            
            // Repaint the panel
            repaint();

            try
            {
                Thread.sleep(16); // Aim for around 60 FPS
            }
            
            catch (InterruptedException e)
            {
                System.out.println("GfxWindow.run() error" + e);
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.drawString(timestamp + "", 2, 13);
        
        // Draw your 2D graphics here
        //g.setColor(Color.RED);
        //g.fillRect(100, 100, 200, 150);

        //g.setColor(Color.BLUE);
        //g.fillOval(300, 300, 100, 100);
    }

    public void start()
    {
        Thread thread = new Thread(this);
        thread.start();
    }

    public void stop()
    {
        running = false;
    }
}
