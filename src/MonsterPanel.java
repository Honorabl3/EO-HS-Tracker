import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class MonsterPanel extends JPanel
{
	public Tracker tracker;
	
	//	Data for Recharged stuff
	public LinkedHashMap<String, ItemData> itemData;
	public LinkedHashMap<String, MonsterData> monsterData;
	
	public ChatLogReader chatLogReader; 
	
	
	public JPanel scrollItemPanelList, infoPanel, scrollBottomPanelList;
	public JLabel aboveMonsterListXPTotal, bossTimerLabel;
	
	//public JLabel panel1Label1, panel1Label2, panel1Label3, panel1Label4, panel1Label5;
	
	public JCheckBox active, bossTimerActive;
	
	
	//	FONTS
	public Font monsterSmallFont, monsterMediumFont;
	
	
	public int totalEXP;
	public int totalEXPColor;
	public int monsterSortMode;
	public int monsterBackColor;
	//	active XP Tracker
	
	//	TIMER VARIABLES/OBJECTS
	
	public JFrame XPHRPopFrame;
	public Point popInitialClick;	//	The dragging mouse position when moving the popout widget..
	public Color transparent;
	public JLabel panel3XPTotal, panel3XPHR, panel3Time;
	
	public int timerTotalEXP;
	public int timerTotalEXPColor;
	public double timerXPHR;
	private Instant timerStartTime, timerPauseTime, timerBossInstant;
    private Duration timerTotalPausedTime, timerBossTime;
    private boolean timerRunning;
	//public Time timeCount;
	
	
	public MonsterPanel(Tracker t)
	{
		tracker = t;
		itemData = new LinkedHashMap<String, ItemData>();
		monsterData = new LinkedHashMap<String, MonsterData>();
		chatLogReader = new ChatLogReader(tracker);
		
		monsterSmallFont = new Font("Courier New", Font.PLAIN, 9);
        monsterMediumFont = new Font("Courier New", Font.PLAIN, 11);
		
		totalEXP = 0;
		totalEXPColor = 2;
		monsterSortMode = 0;	//	0 quantity, 1 by exp count, 
		monsterBackColor = 1;
		
		XPHRPopFrame = null;
		timerTotalEXP = 0;
		timerTotalEXPColor = 2;
		timerXPHR = 0;
		timerStartTime = Instant.now();
		timerPauseTime = Instant.now();
		timerTotalPausedTime = Duration.ZERO;
		timerRunning = false;
		
		ImageIcon arrow_u = new ImageIcon("EO-HS-Tracker/images/arrow_u.png");
		ImageIcon arrow_d = new ImageIcon("EO-HS-Tracker/images/arrow_d.png");
		ImageIcon arrow_l = new ImageIcon("EO-HS-Tracker/images/arrow_l.png");
		ImageIcon arrow_r = new ImageIcon("EO-HS-Tracker/images/arrow_r.png");
		ImageIcon sortIcon = new ImageIcon("EO-HS-Tracker/images/sort2.png");
		ImageIcon backColorIcon = new ImageIcon("EO-HS-Tracker/images/back_color.png");
		ImageIcon timerPlayIcon = new ImageIcon("EO-HS-Tracker/images/timer_play.png");
		ImageIcon timerPauseIcon = new ImageIcon("EO-HS-Tracker/images/timer_pause.png");
		ImageIcon timerStopIcon = new ImageIcon("EO-HS-Tracker/images/timer_stop.png");
		ImageIcon timerPopIcon = new ImageIcon("EO-HS-Tracker/images/popout.png");
		
		this.setName("Monster Log");
		
		try
		{
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
			this.setSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
			this.setBackground(Color.BLACK);
			
			//	Top Panel
			
			JPanel topPanel = new JPanel();
			topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
			topPanel.setBackground(Color.decode("#000011"));
			//topPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
			//topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
			//topPanel.setPreferredSize(new Dimension(this.getPreferredSize().width, this.getPreferredSize().height));
			//topPanel.setMinimumSize(new Dimension(this.getPreferredSize().width, this.getPreferredSize().height));
			this.add(topPanel);
			
			//	Panel 2
			
			JPanel itemPanel = new JPanel();
			itemPanel.setMinimumSize(new Dimension(256, 128));
			itemPanel.setMaximumSize(new Dimension(384, Integer.MAX_VALUE));
			itemPanel.setPreferredSize(new Dimension(256, 128));
			itemPanel.setBackground(Color.BLACK);
			itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
			topPanel.add(itemPanel);
			
			scrollItemPanelList = new JPanel();
			scrollItemPanelList.setMaximumSize(new Dimension(384, 128));
			scrollItemPanelList.setBackground(Color.BLACK);
			scrollItemPanelList.setLayout(new BoxLayout(scrollItemPanelList, BoxLayout.Y_AXIS));
			
			JScrollPane scrollItemPanelListScrollPane = new JScrollPane(scrollItemPanelList);
			scrollItemPanelListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollItemPanelListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollItemPanelListScrollPane.setBorder(null);
			
	        JScrollBar scrollItemPanelListBar = scrollItemPanelListScrollPane.getVerticalScrollBar();
	        scrollItemPanelListBar.setUnitIncrement(16); // Adjust this value to control the speed (higher values scroll faster)
	        scrollItemPanelListBar.setBlockIncrement(64); // Adjust this value to control how much is scrolled on page up/down
	        scrollItemPanelListBar.setUI(new BasicScrollBarUI()
			{
	            @Override
	            protected void configureScrollBarColors()
	            {
	                this.thumbColor = Color.decode("#111111"); // Scroll thumb color
	                this.trackColor = Color.BLACK; // Scroll track color
	            }
	        });
	        itemPanel.add(scrollItemPanelListScrollPane);
			
			
	        //	Middle Panel
	        
	        JPanel middlePanel = new JPanel();
	        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
	        middlePanel.setBackground(Color.BLACK);
	        //middlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	        //middlePanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	        //middlePanel.setPreferredSize(new Dimension(256, 128));
	        topPanel.add(middlePanel);
	        
	        infoPanel = new JPanel();
	        //infoPanel.setPreferredSize(new Dimension());
	        //infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
	        infoPanel.setBackground(Color.BLACK);
	        //infoPanel.add(Box.createHorizontalGlue());
	        middlePanel.add(infoPanel);
	        
	        JScrollPane infoPanelScrollPane = new JScrollPane(infoPanel);
	        infoPanelScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	        infoPanelScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	        infoPanelScrollPane.setBorder(null);
			
	        JScrollBar infoPanelListBar = infoPanelScrollPane.getVerticalScrollBar();
	        infoPanelListBar.setUnitIncrement(16); // Adjust this value to control the speed (higher values scroll faster)
	        infoPanelListBar.setBlockIncrement(64); // Adjust this value to control how much is scrolled on page up/down
	        infoPanelListBar.setUI(new BasicScrollBarUI()
			{
	            @Override
	            protected void configureScrollBarColors()
	            {
	                this.thumbColor = Color.decode("#111111"); // Scroll thumb color
	                this.trackColor = Color.BLACK; // Scroll track color
	            }
	        });
	        middlePanel.add(infoPanelScrollPane);
	        
			
			//	Panel 3 WIDGETS
	
			JPanel panel3 = new JPanel();
			panel3.setMinimumSize(new Dimension(250, 150));
			panel3.setMaximumSize(new Dimension(250, 150));
			panel3.setPreferredSize(new Dimension(250, 150));
			panel3.setBackground(Color.BLACK);
			panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
			topPanel.add(panel3);
			
			JPanel panel3Panel1 = new JPanel();
			panel3Panel1.setAlignmentX(Component.LEFT_ALIGNMENT);
			panel3Panel1.setBackground(Color.decode("#111111"));
			panel3Panel1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
			panel3.add(panel3Panel1);
			
			JLabel hideWidgetsButton = new JLabel(arrow_r)
			{
				@Override
				public boolean contains(int x, int y)
				{
			        Icon icon = getIcon();
			        if (icon != null)
			        {
			            // Get the bounds of the icon inside the label
			            int iconWidth = icon.getIconWidth();
			            int iconHeight = icon.getIconHeight();

			            // Check if the click is within the icon's bounds
			            return (x >= 0 && x < iconWidth) && (y >= 0 && y < iconHeight);
			        }
			        // If no icon is present, fallback to the default behavior
			        return super.contains(x, y);
			    }
			};
			hideWidgetsButton.setMaximumSize(new Dimension(20, 20));
			panel3Panel1.add(hideWidgetsButton);
			
			// Add MouseListener to the label
			hideWidgetsButton.addMouseListener(new MouseAdapter()
	        {
	            @Override
	            public void mouseClicked(MouseEvent e)
	            {
	            	if(hideWidgetsButton.getIcon() == arrow_r)
	            	{
	            		panel3.setMinimumSize(new Dimension(32, 150));
	            		panel3.setPreferredSize(new Dimension(32, 150));
	            		panel3.setMaximumSize(new Dimension(32, 150));
	            		hideWidgetsButton.setIcon(arrow_l);
	            		
	            		panel3.revalidate();
	    				panel3.repaint();
	            	}
	            	
	            	else
	            	{
	            		panel3.setMinimumSize(new Dimension(250, 150));
	            		panel3.setPreferredSize(new Dimension(250, 150));
	            		panel3.setMaximumSize(new Dimension(250, 150));
	            		hideWidgetsButton.setIcon(arrow_r);
	            		
	            		panel3.revalidate();
	    				panel3.repaint();
	            	}
	            }
	        });
			
			active = new JCheckBox("ENABLE LOG");
			active.setSelected(false);
			active.setBackground(Color.BLACK);
			active.setForeground(Color.WHITE);
			panel3Panel1.add(active);
			
			active.addActionListener(new ActionListener()
			{
	            @Override
	            public void actionPerformed(ActionEvent e)
	            {
	                // Check if the checkbox is selected or not
	                if (active.isSelected())
	                {
	                    if(tracker.settings.chatLogDirectory.equalsIgnoreCase("No directory selected.."))
						{
	                    	active.setSelected(false);
	                    	
	                    	tracker.panel.settingsPanel.setLocation((tracker.panel.sizeX/2)-(tracker.panel.settingsPanel.getWidth()/2), (tracker.panel.sizeY/2)-(tracker.panel.settingsPanel.getHeight()/2));
	                        
	                    	tracker.panel.xpList.setVisible(false);
	                    	tracker.panel.changelogPanel1.setVisible(false);
	                    	tracker.panel.monsterPanel.setVisible(false);
	                        
	                    	tracker.panel.settingsPanel.setVisible(true);
	                    	
	                    	tracker.panel.settingsMonsterPanel.setBorder(tracker.panel.thickRedStrokeBorder);
	                    	//JOptionPane.showMessageDialog(tracker.panel.settingsPanel, "No directory to chatlog selected, please check settings..");
						}
	                }
	            }
	        });
			
			JButton clearDataButton = new JButton("Dump Data");
			clearDataButton.setPreferredSize(new Dimension(100, 20));
			panel3Panel1.add(clearDataButton);
			
			// Add MouseListener to the label
			clearDataButton.addMouseListener(new MouseAdapter()
	        {
	            @Override
	            public void mouseClicked(MouseEvent e)
	            {
	            	removeAllItemElements();
	            	removeAllMonsterElements();
	            	
	            	totalEXP = 0;
	            	
	            	update();
	            	
	            	itemPanel.invalidate();
            		itemPanel.repaint();
	            	
            		scrollBottomPanelList.invalidate();
            		scrollBottomPanelList.repaint();
	            }
	        });
			
			transparent = new Color(0, 0, 0, 0);
			
			JPanel XPHRPanel = new JPanel();
			XPHRPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			XPHRPanel.setLayout(new BoxLayout(XPHRPanel, BoxLayout.Y_AXIS));
			XPHRPanel.setBackground(transparent);
			panel3.add(XPHRPanel);
			
			JPanel XPHRTopPanel = new JPanel();
			XPHRTopPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			XPHRTopPanel.setLayout(new BoxLayout(XPHRTopPanel, BoxLayout.X_AXIS));
			XPHRTopPanel.setBackground(transparent);
			XPHRPanel.add(XPHRTopPanel);
			
			JPanel XPHRBottomPanel = new JPanel();
			XPHRBottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			XPHRBottomPanel.setLayout(new BoxLayout(XPHRBottomPanel, BoxLayout.X_AXIS));
			XPHRBottomPanel.setBackground(transparent);
			XPHRPanel.add(XPHRBottomPanel);
			
			XPHRTopPanel.add(Box.createHorizontalGlue());
			
			JLabel playTimerButton = new JLabel(timerPlayIcon)
			{
				@Override
				public boolean contains(int x, int y)
				{
			        Icon icon = getIcon();
			        if (icon != null)
			        {
			            // Get the bounds of the icon inside the label
			            int iconWidth = icon.getIconWidth();
			            int iconHeight = icon.getIconHeight();

			            // Check if the click is within the icon's bounds
			            return (x >= 0 && x < iconWidth) && (y >= 0 && y < iconHeight);
			        }
			        // If no icon is present, fallback to the default behavior
			        return super.contains(x, y);
			    }
			};
			playTimerButton.setMaximumSize(new Dimension(20, 20));
			//playTimerButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			XPHRTopPanel.add(playTimerButton);
			
			// Add MouseListener to the label
			playTimerButton.addMouseListener(new MouseAdapter()
	        {
	            @Override
	            public void mouseClicked(MouseEvent e)
	            {
	            	if(playTimerButton.getIcon() == timerPlayIcon)
	            	{
	            		timerResume();
	            		playTimerButton.setIcon(timerPauseIcon);
	            	}
	            	
	            	else
	            	{
	            		timerPause();
	            		playTimerButton.setIcon(timerPlayIcon);
	            	}
	            }
	        });
			
			JLabel restartTimerButton = new JLabel(timerStopIcon);
			restartTimerButton.setPreferredSize(new Dimension(20, 20));
			restartTimerButton.setToolTipText("Reset Timer & XP");
			XPHRTopPanel.add(restartTimerButton);
			
			// Add MouseListener to the label
			restartTimerButton.addMouseListener(new MouseAdapter()
	        {
	            @Override
	            public void mouseClicked(MouseEvent e)
	            {
	            	if(playTimerButton.getIcon() == timerPauseIcon)
	            		playTimerButton.setIcon(timerPlayIcon);
	            	
	            	timerReset();
	            	timerPause();
	            }
	        });
			
			XPHRTopPanel.add(Box.createHorizontalGlue());
			
			panel3Time = new JLabel("" + timerGetElapsedTime());
			panel3Time.setForeground(Color.GREEN);
			//panel3Time.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
			XPHRTopPanel.add(panel3Time);
			
			XPHRTopPanel.add(Box.createHorizontalGlue());
			
			JLabel popoutButton = new JLabel(timerPopIcon);
			popoutButton.setForeground(Color.GREEN);
			XPHRTopPanel.add(popoutButton);
			
			// Add MouseListener to the label
			popoutButton.addMouseListener(new MouseAdapter()
	        {
	            @Override
	            public void mouseClicked(MouseEvent e)
	            {
	            	if(XPHRPopFrame == null)
	            	{
		            	XPHRPopFrame = new JFrame("XP/HR Widget");
		            	XPHRPopFrame.setUndecorated(true);
		            	XPHRPopFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this frame
		            	XPHRPopFrame.setSize(XPHRPanel.getPreferredSize());
		            	XPHRPopFrame.setMinimumSize(new Dimension(160, XPHRPanel.getPreferredSize().height));
		            	XPHRPopFrame.setLocationRelativeTo(tracker.frame);  // Center the new frame relative to the main frame
		            	XPHRPopFrame.setAlwaysOnTop(true);
		            	XPHRPopFrame.setBackground(transparent);
		                
		                XPHRPopFrame.add(XPHRPanel);
		                
		                // Make the new frame visible
		                XPHRPopFrame.setVisible(true);
		                
		                XPHRPopFrame.addMouseListener(new MouseAdapter()
		                {
		                    public void mousePressed(MouseEvent e)
		                    {
		                        // Get the initial mouse position relative to the frame's location
		                    	popInitialClick = e.getPoint();
		                    }
		                });

		                XPHRPopFrame.addMouseMotionListener(new MouseAdapter()
		                {
		                    public void mouseDragged(MouseEvent e) {
		                        // Get the location of the JFrame
		                        int thisX = XPHRPopFrame.getLocation().x;
		                        int thisY = XPHRPopFrame.getLocation().y;

		                        // Calculate how far the mouse moved from the initial click point
		                        int xMoved = e.getX() - popInitialClick.x;
		                        int yMoved = e.getY() - popInitialClick.y;

		                        // Move the frame by the distance moved
		                        int newX = thisX + xMoved;
		                        int newY = thisY + yMoved;

		                        // Update the frame's location
		                        XPHRPopFrame.setLocation(newX, newY);
		                    }
		                });
	            	}
	            	
	            	else
	            	{
	            		panel3.add(XPHRPanel);
	            		XPHRPopFrame.dispose();
	            		XPHRPopFrame = null;
	            		
	            		panel3.invalidate();
	            		panel3.repaint();
	            	}
	            }
	        });
			
			
			XPHRBottomPanel.add(Box.createHorizontalGlue());
			
			panel3XPHR = new JLabel(timerXPHR + " XP/HR");
			panel3XPHR.setForeground(Color.WHITE);
			XPHRBottomPanel.add(panel3XPHR);
			
			XPHRBottomPanel.add(Box.createHorizontalGlue());
			
			panel3XPTotal = new JLabel("+0");
			panel3XPTotal.setForeground(Color.WHITE);
			XPHRBottomPanel.add(panel3XPTotal);
			
			panel3XPTotal.addMouseListener(new MouseAdapter()
	        {
	            @Override
	            public void mouseClicked(MouseEvent e)
	            {
	            	timerTotalEXPColor++;
	            	
	            	if(timerTotalEXPColor > 7)
	            		timerTotalEXPColor = 0;
	            	
	            	switch(timerTotalEXPColor)
	            	{
	            		case 0:
	            			panel3XPTotal.setForeground(Color.RED);
	            			break;
	            		case 1:
	            			panel3XPTotal.setForeground(Color.MAGENTA);
	            			break;
	            		case 2:
	            			panel3XPTotal.setForeground(Color.WHITE);
	            			break;
	            		case 3:
	            			panel3XPTotal.setForeground(Color.BLUE);
	            			break;
	            		case 4:
	            			panel3XPTotal.setForeground(Color.CYAN);
	            			break;
	            		case 5:
	            			panel3XPTotal.setForeground(Color.GREEN);
	            			break;
	            		case 6:
	            			panel3XPTotal.setForeground(Color.YELLOW);
	            			break;
	            		case 7:
	            			panel3XPTotal.setForeground(Color.PINK);
	            			break;
	            	}
	            }
	        });
			
			XPHRBottomPanel.add(Box.createHorizontalGlue());
			
			JPanel BossTimerPanel = new JPanel();
			BossTimerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			BossTimerPanel.setLayout(new BoxLayout(BossTimerPanel, BoxLayout.Y_AXIS));
			BossTimerPanel.setBackground(transparent);
			BossTimerPanel.setVisible(false);	//	Turned off for github submission
			panel3.add(BossTimerPanel);
			
			bossTimerActive = new JCheckBox("BOSS TIMER");
			bossTimerActive.setSelected(false);
			bossTimerActive.setBackground(Color.BLACK);
			bossTimerActive.setForeground(Color.WHITE);
			bossTimerActive.setVisible(false);	//	Turned off for github submission
			BossTimerPanel.add(bossTimerActive);
			
			bossTimerLabel = new JLabel("0:00");
			bossTimerLabel.setForeground(Color.WHITE);
			bossTimerLabel.setVisible(false);	//	Turned off for github submission
			BossTimerPanel.add(bossTimerLabel);
			
			//this.add(Box.createVerticalStrut(96));
			
			//	Panel 1 - MONSTER LOG (x250 Ant Soldiers Killed etc)
			
			JPanel panelBottom = new JPanel();
			panelBottom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 128));
			panelBottom.setPreferredSize(new Dimension(this.getPreferredSize().width, 128));
			panelBottom.setMinimumSize(new Dimension(200, 128));
			panelBottom.setBackground(Color.BLACK);
			panelBottom.setLayout(new BoxLayout(panelBottom, BoxLayout.Y_AXIS));
			this.add(panelBottom);
			
			JPanel aboveMonsterList = new JPanel();
			aboveMonsterList.setBackground(Color.decode("#111111"));
			aboveMonsterList.setLayout(new FlowLayout());
			aboveMonsterList.setMaximumSize(new Dimension(Integer.MAX_VALUE, 12));
			panelBottom.add(aboveMonsterList);
			
			JLabel aboveMonsterListXPTotalBefore = new JLabel("Total XP");
			aboveMonsterListXPTotalBefore.setForeground(Color.WHITE);
			aboveMonsterListXPTotalBefore.setFont(monsterMediumFont);
			aboveMonsterList.add(aboveMonsterListXPTotalBefore);
			
			aboveMonsterListXPTotal = new JLabel("+" + totalEXP);
			aboveMonsterListXPTotal.setForeground(Color.WHITE);
			aboveMonsterListXPTotal.setFont(monsterMediumFont);
			aboveMonsterList.add(aboveMonsterListXPTotal);
			
			aboveMonsterListXPTotal.addMouseListener(new MouseAdapter()
	        {
	            @Override
	            public void mouseClicked(MouseEvent e)
	            {
	            	totalEXPColor++;
	            	
	            	if(totalEXPColor > 7)
	            		totalEXPColor = 0;
	            	
	            	switch(totalEXPColor)
	            	{
	            		case 0:
	            			aboveMonsterListXPTotal.setForeground(Color.RED);
	            			break;
	            		case 1:
	            			aboveMonsterListXPTotal.setForeground(Color.MAGENTA);
	            			break;
	            		case 2:
	            			aboveMonsterListXPTotal.setForeground(Color.WHITE);
	            			break;
	            		case 3:
	            			aboveMonsterListXPTotal.setForeground(Color.BLUE);
	            			break;
	            		case 4:
	            			aboveMonsterListXPTotal.setForeground(Color.CYAN);
	            			break;
	            		case 5:
	            			aboveMonsterListXPTotal.setForeground(Color.GREEN);
	            			break;
	            		case 6:
	            			aboveMonsterListXPTotal.setForeground(Color.YELLOW);
	            			break;
	            		case 7:
	            			aboveMonsterListXPTotal.setForeground(Color.PINK);
	            			break;
	            	}
	            }
	        });
			
			aboveMonsterList.add(Box.createHorizontalGlue());
			JLabel sortButton = new JLabel(sortIcon);
			sortButton.setMaximumSize(new Dimension(64, 12));
			aboveMonsterList.add(sortButton);
			
			sortButton.addMouseListener(new MouseAdapter()
	        {
	            @Override
	            public void mouseClicked(MouseEvent e)
	            {
	            	if(monsterSortMode == 0)
	            		monsterSortMode = 1;
	            	else
	            		monsterSortMode = 0;
	            	
	            	resortMonsterList();
	            }
	        });
			
			JLabel backColorButton = new JLabel(backColorIcon);
			backColorButton.setMaximumSize(new Dimension(64, 12));
			aboveMonsterList.add(backColorButton);
			
			backColorButton.addMouseListener(new MouseAdapter()
	        {
	            @Override
	            public void mouseClicked(MouseEvent e)
	            {
	            	monsterBackColor++;
	            	
	            	if(monsterBackColor > 7)
	            		monsterBackColor = 0;
	            	
	            	switch(monsterBackColor)
	            	{
	            		case 0:
	            			scrollBottomPanelList.setBackground(Color.decode("#000000"));
	            			break;
	            		case 1:
	            			scrollBottomPanelList.setBackground(Color.decode("#111111"));
	            			break;
	            		case 2:
	            			scrollBottomPanelList.setBackground(Color.decode("#222222"));
	            			break;
	            		case 3:
	            			scrollBottomPanelList.setBackground(Color.decode("#333333"));
	            			break;
	            		case 4:
	            			scrollBottomPanelList.setBackground(Color.decode("#220000"));
	            			break;
	            		case 5:
	            			scrollBottomPanelList.setBackground(Color.decode("#002200"));
	            			break;
	            		case 6:
	            			scrollBottomPanelList.setBackground(Color.decode("#000022"));
	            			break;
	            		case 7:
	            			scrollBottomPanelList.setBackground(Color.decode("#120011"));
	            			break;
	            	}
	            	
	            	//scrollBottomPanelList.revalidate();
	        		//scrollBottomPanelList.repaint();
	            }
	        });
			aboveMonsterList.add(Box.createHorizontalGlue());
			
			scrollBottomPanelList = new JPanel();
			scrollBottomPanelList.setMaximumSize(new Dimension(500, 100));
			scrollBottomPanelList.setBackground(Color.BLACK);
			scrollBottomPanelList.setLayout(new FlowLayout());
			scrollBottomPanelList.setBackground(Color.decode("#333333"));
			
			JScrollPane scrollBottomPanelListScrollPane = new JScrollPane(scrollBottomPanelList);
			scrollBottomPanelListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
			scrollBottomPanelListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollBottomPanelListScrollPane.setBorder(null);
			
	        JScrollBar scrollBottomPanelListBar = scrollBottomPanelListScrollPane.getHorizontalScrollBar();
	        scrollBottomPanelListBar.setPreferredSize(new Dimension(0, 0));
	        scrollBottomPanelListBar.setMinimumSize(new Dimension(0, 0));
	        scrollBottomPanelListBar.setMaximumSize(new Dimension(0, 0));
	        scrollBottomPanelListBar.setUnitIncrement(16); // Adjust this value to control the speed (higher values scroll faster)
	        scrollBottomPanelListBar.setBlockIncrement(64); // Adjust this value to control how much is scrolled on page up/down
	        scrollBottomPanelListBar.setUI(new BasicScrollBarUI()
			{
	        	@Override
	            protected void configureScrollBarColors() {
	                this.thumbColor = new Color(0, 0, 0, 0); // Invisible thumb
	                this.trackColor = new Color(0, 0, 0, 0); // Invisible track
	                this.maximumThumbSize = new Dimension(0, 0);
	            }
	        	
	        	@Override
	            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
	                // Do nothing to prevent track from being painted
	            }

	            @Override
	            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
	                // Do nothing to prevent thumb from being painted
	            }
	        	
	            @Override
	            protected JButton createDecreaseButton(int orientation) {
	                return createInvisibleButton();
	            }

	            @Override
	            protected JButton createIncreaseButton(int orientation) {
	                return createInvisibleButton();
	            }

	            private JButton createInvisibleButton() {
	                JButton button = new JButton();
	                button.setPreferredSize(new Dimension(0, 0)); // Make the buttons 0 size
	                button.setMinimumSize(new Dimension(0, 0));
	                button.setMaximumSize(new Dimension(0, 0));
	                return button;
	            }
	        });
	        panelBottom.add(scrollBottomPanelListScrollPane);
			
			//JPanel panelBottomPanel2 = new JPanel();
			//panelBottomPanel2.setBackground(Color.BLACK);
			//panelBottomPanel2.setMaximumSize(new Dimension(panelBottom.getPreferredSize().width, 20));
			//panelBottom.add(panelBottomPanel2);
			
			
			/*
			//	Panel 1
			
			JPanel panel1 = new JPanel();
			panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
			panel1.setMinimumSize(new Dimension(250, 150));
			panel1.setMaximumSize(new Dimension(250, 150));
			panel1.setPreferredSize(new Dimension(250, 150));
			panel1.setBackground(Color.BLACK);
			this.add(panel1);
			
			panel1Label1 = new JLabel();
			panel1Label1.setFont(new Font("Impact", Font.PLAIN, 16));
			panel1Label1.setText("Panel: " + this.getName());
			panel1Label1.setForeground(Color.WHITE);
			panel1.add(panel1Label1);
			
			//	Panel 2
			
			JPanel panel2 = new JPanel();
			panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
			panel2.setMinimumSize(new Dimension(300, 150));
			panel2.setMaximumSize(new Dimension(300, 150));
			panel2.setPreferredSize(new Dimension(300, 150));
			this.add(panel2);
			
			scrollPanelList = new JPanel();
			scrollPanelList.setBackground(Color.BLACK);
			
			JScrollPane scrollPane = new JScrollPane(scrollPanelList);
			panel2.add(scrollPane);
			
			JButton clearDataButton = new JButton("Dump All Data");
			clearDataButton.setPreferredSize(new Dimension(100, 20));
			panel2.add(clearDataButton);
			
			//	Panel 3
			
			JPanel panel3 = new JPanel();
			panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
			panel3.setMinimumSize(new Dimension(250, 150));
			panel3.setMaximumSize(new Dimension(250, 150));
			panel3.setPreferredSize(new Dimension(250, 150));
			panel3.setBackground(Color.BLACK);
			this.add(panel3);
			
			active = new JCheckBox("Enable Processing");
			active.setSelected(true);
			active.setForeground(Color.WHITE);
			active.setBackground(Color.BLACK);
			panel3.add(active);
			
			//	Panel 4
			
			JPanel panel4 = new JPanel();
			panel4.setLayout(new BoxLayout(panel4, BoxLayout.Y_AXIS));
			panel4.setMinimumSize(new Dimension(250, 150));
			panel4.setMaximumSize(new Dimension(250, 150));
			panel4.setPreferredSize(new Dimension(250, 150));
			panel4.setBackground(Color.BLACK);
			this.add(panel4);*/
		}
		
		catch(Exception e)
		{
			System.out.println("[Error] MonsterPanel.MonsterPanel() - " + e);
            e.printStackTrace();
		}
	}
	
	public void addItem(String n, int amt, int xp)
	{
		if(active.isSelected())	//	If Enabled
		{
			for (int a = 0; a < scrollItemPanelList.getComponentCount(); a++)
			{
				ItemElement child = (ItemElement) scrollItemPanelList.getComponent(a);
				
				if(child.getName().equalsIgnoreCase(n))
				{
					child.addData(amt, xp);
					
					//	Sort the list of item based off the quantity..
					scrollItemPanelList.remove(child);
					int slot = 0;
					for(int b = 0; b < scrollItemPanelList.getComponentCount(); b++)
					{
						ItemElement child2 = (ItemElement) scrollItemPanelList.getComponent(b);
						if(child.quantity >= child2.quantity)
						{
							slot = b;
							break;
						}
					}
					scrollItemPanelList.add(child, slot);
					
					scrollBottomPanelList.revalidate();
					scrollBottomPanelList.repaint();
					
					return;
				}
	        }
			
			//	The rest of the code (after the for-loop) will run in the event that NO already existing GUI elements for that type of monster have been found.
			
			ItemElement tempData = new ItemElement(tracker, n);
			tempData.addData(amt, xp);
			scrollItemPanelList.add(tempData);
			
			scrollBottomPanelList.revalidate();
			scrollBottomPanelList.repaint();
		}
	}
	
	public void addKill(String n, int amt, int xp, boolean party)
	{
		if(active.isSelected())	//	If Enabled
		{
			for (int a = 0; a < scrollBottomPanelList.getComponentCount(); a++)
			{
				MonsterElement child = (MonsterElement) scrollBottomPanelList.getComponent(a);
				
				if(child.getName().equalsIgnoreCase(n))
				{
					child.addData(amt, xp, party);
					
					
					//	Sort the list of monsters based off the quantity OR exp..
					scrollBottomPanelList.remove(child);
					int slot = 0;
					for(int b = 0; b < scrollBottomPanelList.getComponentCount(); b++)
					{
						MonsterElement child2 = (MonsterElement) scrollBottomPanelList.getComponent(b);
						
						if(monsterSortMode == 0)	//	Sort by Quantity
						{
							if(child.quantity + child.quantityParty >= child2.quantity + child2.quantityParty)
							{
								slot = b;
								break;
							}
						}
						
						//	Sort by EXP
						else// if(sortMode == 1)
						{
							if(child.expTotal >= child2.expTotal)
							{
								slot = b;
								break;
							}
						}
					}
					scrollBottomPanelList.add(child, slot);
					
					totalEXP += amt * xp;
					
					if(timerRunning)
						timerTotalEXP += amt * xp;
					
					return;
				}
	        }
			
			//	The rest of the code (after the for-loop) will run in the event that NO already existing GUI elements for that type of monster have been found.
			
			MonsterElement tempData = new MonsterElement(tracker, n);
			tempData.addData(amt, xp, party);
			scrollBottomPanelList.add(tempData);
			
			totalEXP += amt * xp;
			
			if(timerRunning)
				timerTotalEXP += amt * xp;
		}
	}
	
	/*public MonsterData getMonsterDataFromArray(String s)
	{
		for(int a=0;a<monsterData.size();a++)
		{
			if(monsterData.get(a).name.equals(s))
				return monsterData.get(a);
		}
		
		return null;
	}
	
	public void updateMonsterLog()
	{
		if(active.isSelected())	//	If Enabled
		{
			
		}
	}*/
	
	public void removeItemElement(String n)
	{
		for(int a=0;a<scrollBottomPanelList.getComponentCount();a++)
		{
			ItemElement child = (ItemElement) scrollItemPanelList.getComponent(a);
			
			if(child.getName().equalsIgnoreCase(n))
			{
				scrollItemPanelList.remove(a);
				break;
			}
		}
	}
	
	public void removeAllItemElements()
	{
		scrollItemPanelList.removeAll();
	}
	
	public void removeMonsterElement(String n)
	{
		for(int a=0;a<scrollBottomPanelList.getComponentCount();a++)
		{
			MonsterElement child = (MonsterElement) scrollBottomPanelList.getComponent(a);
			
			if(child.getName().equalsIgnoreCase(n))
			{
				scrollBottomPanelList.remove(a);
				break;
			}
		}
		
		revalidate();
		repaint();
	}
	
	public void removeAllMonsterElements()
	{
		scrollBottomPanelList.removeAll();
	}
	
	public void populateInfo(ItemData itemNode)
	{
		if(infoPanel.getComponentCount() > 0)
			infoPanel.removeAll();
		
		JPanel data = new JPanel();
		data.setLayout(new BoxLayout(data, BoxLayout.Y_AXIS));
		data.setBackground(Color.BLACK);
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		top.setBackground(Color.BLACK);
		top.setAlignmentX(Component.LEFT_ALIGNMENT);
		data.add(top);
		
		JLabel imageLabel = new JLabel(itemNode.smallImage);
		top.add(imageLabel);
		
		JPanel namePlate = new JPanel();
		namePlate.setBackground(Color.BLACK);
		namePlate.setLayout(new BoxLayout(namePlate, BoxLayout.Y_AXIS));
		namePlate.setAlignmentX(Component.TOP_ALIGNMENT);
		top.add(namePlate);
		
		JPanel namePlateTop = new JPanel();
		namePlateTop.setBackground(Color.BLACK);
		namePlateTop.setLayout(new BoxLayout(namePlateTop, BoxLayout.X_AXIS));
		namePlateTop.setAlignmentX(Component.LEFT_ALIGNMENT);
		namePlate.add(namePlateTop);
		
		JLabel nameLabel = new JLabel(" " + itemNode.name);
		nameLabel.setForeground(Color.WHITE);
		namePlateTop.add(nameLabel);
		//top.add(Box.createHorizontalGlue());
		
		switch(itemNode.unique_id)
		{
			case 3:
				nameLabel.setForeground(Color.decode("#6F3096"));
				break;
			case 26:
				nameLabel.setForeground(Color.decode("#ED820E"));
				break;
			case 43:
				nameLabel.setForeground(Color.decode("#9966CC"));
				break;
			default:
				nameLabel.setForeground(Color.WHITE);
				break;
		}
		
		
		
		infoPanel.add(data);
	}
	
	public void populateInfo(MonsterData monsterNode)
	{
		if(infoPanel.getComponentCount() > 0)
			infoPanel.removeAll();
		
		JPanel data = new JPanel();
		data.setLayout(new BoxLayout(data, BoxLayout.Y_AXIS));
		data.setBackground(Color.BLACK);
		data.setAlignmentX(Component.LEFT_ALIGNMENT);
		//data.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		top.setBackground(Color.BLACK);
		top.setAlignmentX(Component.LEFT_ALIGNMENT);
		data.add(top);
		
		JLabel imageLabel = new JLabel(monsterNode.smallImage);
		top.add(imageLabel);
		
		JPanel namePlate = new JPanel();
		namePlate.setBackground(Color.BLACK);
		namePlate.setLayout(new BoxLayout(namePlate, BoxLayout.Y_AXIS));
		namePlate.setAlignmentX(Component.TOP_ALIGNMENT);
		top.add(namePlate);
		
		JPanel namePlateTop = new JPanel();
		namePlateTop.setBackground(Color.BLACK);
		namePlateTop.setLayout(new BoxLayout(namePlateTop, BoxLayout.X_AXIS));
		namePlateTop.setAlignmentX(Component.LEFT_ALIGNMENT);
		namePlate.add(namePlateTop);
		
		JLabel nameLabel = new JLabel(" " + monsterNode.name);
		nameLabel.setForeground(Color.WHITE);
		namePlateTop.add(nameLabel);
		//top.add(Box.createHorizontalGlue());
		
		if(monsterNode.boss)
		{
			JLabel bossLabel = new JLabel(" (BOSS)");
			bossLabel.setForeground(Color.RED);
			namePlateTop.add(bossLabel);
		}
		
		else if(monsterNode.child)
		{
			JLabel bossLabel = new JLabel(" (CHILD)");
			bossLabel.setForeground(Color.YELLOW);
			namePlateTop.add(bossLabel);
		}
		
		JLabel levelLabel = new JLabel(" Lvl. " + monsterNode.level + "   EXP: " + monsterNode.exp);
		levelLabel.setForeground(Color.WHITE);
		namePlate.add(levelLabel);
		
		//JLabel explLabel = new JLabel(" EXP: " + monsterNode.exp);
		//explLabel.setForeground(Color.WHITE);
		//namePlate.add(explLabel);
		
		JLabel healthLabel = new JLabel(" HP: " + monsterNode.health + "   Dmg: " + monsterNode.min_damage + "~" + monsterNode.max_damage);
		healthLabel.setForeground(Color.WHITE);
		namePlate.add(healthLabel);
		
		//top.add(Box.createHorizontalGlue());
		
		JPanel middlePlate = new JPanel();
		middlePlate.setBackground(Color.BLACK);
		middlePlate.setLayout(new BoxLayout(middlePlate, BoxLayout.X_AXIS));
		middlePlate.setAlignmentX(Component.LEFT_ALIGNMENT);
		data.add(middlePlate);
		
		JPanel middlePlateLeft = new JPanel();
		middlePlateLeft.setBackground(Color.BLACK);
		middlePlateLeft.setLayout(new BoxLayout(middlePlateLeft, BoxLayout.Y_AXIS));
		middlePlateLeft.setAlignmentX(Component.LEFT_ALIGNMENT);
		middlePlate.add(middlePlateLeft);
		
		//JPanel middlePlateTop = new JPanel();
		//middlePlateTop.setBackground(Color.BLACK);
		//middlePlateTop.setLayout(new BoxLayout(namePlateTop, BoxLayout.X_AXIS));
		//middlePlate.add(middlePlateTop);
		
		//middlePlateTop.add()
		
		JLabel accuracyLabel = new JLabel(" Accuracy: " + monsterNode.accuracy);
		accuracyLabel.setForeground(Color.WHITE);
		middlePlateLeft.add(accuracyLabel);
		
		JLabel evasionLabel = new JLabel(" Evasion: " + monsterNode.evasion);
		evasionLabel.setForeground(Color.WHITE);
		middlePlateLeft.add(evasionLabel);
		
		JLabel armorLabel = new JLabel(" Armor: " + monsterNode.armor);
		armorLabel.setForeground(Color.WHITE);
		middlePlateLeft.add(armorLabel);
		
		JLabel critLabel = new JLabel(" Crit Chance: " + monsterNode.critical_chance);
		critLabel.setForeground(Color.WHITE);
		middlePlateLeft.add(critLabel);
		
		middlePlate.add(Box.createHorizontalGlue());
		
		//data.add(Box.createHorizontalGlue());
		
		if(monsterNode.drops.size() > 0)
		{
			//JPanel scrollDropPanelList = new JPanel();
			//scrollDropPanelList.setMaximumSize(new Dimension(384, 128));
			//scrollDropPanelList.setBackground(Color.BLACK);
			//scrollDropPanelList.setLayout(new BoxLayout(scrollDropPanelList, BoxLayout.X_AXIS));
			
			/*JPanel itemPanel = new JPanel();
			itemPanel.setMinimumSize(new Dimension(256, 128));
			itemPanel.setMaximumSize(new Dimension(384, Integer.MAX_VALUE));
			itemPanel.setPreferredSize(new Dimension(256, 128));
			itemPanel.setBackground(Color.BLACK);
			itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
			topPanel.add(itemPanel);
			
			scrollItemPanelList = new JPanel();
			scrollItemPanelList.setMaximumSize(new Dimension(384, 128));
			scrollItemPanelList.setBackground(Color.BLACK);
			scrollItemPanelList.setLayout(new BoxLayout(scrollItemPanelList, BoxLayout.Y_AXIS));*/
			
			JPanel dropPlate = new JPanel();
			dropPlate.setMinimumSize(new Dimension(256, 120));
			dropPlate.setMaximumSize(new Dimension(384, 120));
			dropPlate.setPreferredSize(new Dimension(256, 120));
			dropPlate.setBackground(Color.BLACK);
			dropPlate.setLayout(new BoxLayout(dropPlate, BoxLayout.Y_AXIS));
			dropPlate.setAlignmentX(Component.LEFT_ALIGNMENT);
			data.add(dropPlate);
			
			JPanel dropPlateList = new JPanel();
			dropPlateList.setMaximumSize(new Dimension(384, 120));
			dropPlateList.setBackground(Color.BLACK);
			dropPlateList.setLayout(new BoxLayout(dropPlateList, BoxLayout.Y_AXIS));
			
			JScrollPane scrollDropPanelListScrollPane = new JScrollPane(dropPlateList);
			scrollDropPanelListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollDropPanelListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollDropPanelListScrollPane.setBorder(null);
			
	        JScrollBar scrollDropPanelListBar = scrollDropPanelListScrollPane.getVerticalScrollBar();
	        scrollDropPanelListBar.setUnitIncrement(16); // Adjust this value to control the speed (higher values scroll faster)
	        scrollDropPanelListBar.setBlockIncrement(64); // Adjust this value to control how much is scrolled on page up/down
	        scrollDropPanelListBar.setUI(new BasicScrollBarUI()
			{
	            @Override
	            protected void configureScrollBarColors()
	            {
	                this.thumbColor = Color.decode("#111111"); // Scroll thumb color
	                this.trackColor = Color.BLACK; // Scroll track color
	            }
	        });
	        dropPlate.add(scrollDropPanelListScrollPane);
	        
	        for(int a=0;a<monsterNode.drops.size();a++)
	        {
	        	DropElement tempDropPanel = new DropElement(tracker, monsterNode.drops.get(a));
	        	tempDropPanel.setVisible(true);
	        	dropPlateList.add(tempDropPanel);
	        }
	        
	        data.add(dropPlate);
		}
		
		infoPanel.add(data);
		
		infoPanel.revalidate();
		infoPanel.repaint();
	}
	
	public void update()
	{
		aboveMonsterListXPTotal.setText("+" + String.format("%,d", totalEXP));
		
		//	Timer
		panel3XPTotal.setText("+" + String.format("%,d", timerTotalEXP));
		panel3Time.setText("" + timerGetElapsedTime());
		
		if (timerRunning)
		{
            // Calculate total elapsed time excluding paused time
            Duration elapsedTime = Duration.between(timerStartTime, Instant.now()).minus(timerTotalPausedTime);
            long elapsedSeconds = elapsedTime.getSeconds();

            // Avoid division by zero
            if (elapsedSeconds > 0)
            {
                // Calculate XP per hour (XP/HR) based on total XP and elapsed time
                timerXPHR = (timerTotalEXP / (double) elapsedSeconds) * 3600; // 3600 seconds in an hour
                panel3XPHR.setText(String.format("%.1f", timerXPHR/1000) + "K XP/HR");
            }
        }
		
		else
		{
            // If paused, use time until pause for XP/HR
            Duration elapsedTime = Duration.between(timerStartTime, timerPauseTime).minus(timerTotalPausedTime);
            long elapsedSeconds = elapsedTime.getSeconds();

            if (elapsedSeconds > 0)
            {
                timerXPHR = (timerTotalEXP / (double) elapsedSeconds) * 3600;
                panel3XPHR.setText(String.format("%.1f", timerXPHR/1000) + "K XP/HR");
            }
        }
		
		
		//	If XPHR Panel is popped out into a moveable frame..
		if(XPHRPopFrame != null)
		{
			System.out.println("Calling XPHRPopFrame Update");
			for(int a = 0; a < XPHRPopFrame.getComponentCount(); a++)
			{
				XPHRPopFrame.getComponent(a).revalidate();
				XPHRPopFrame.getComponent(a).repaint();
			}
		}
		
		if(bossTimerActive.isSelected()) // if boss timer active
		{
			Duration temp = Duration.between(timerStartTime, Instant.now()).minus(timerTotalPausedTime);
	        
			//timerBossTime = new Duration();
			long hours = temp.toHours();
	        long minutes = (temp.toMinutes() % 60);  // Extract minutes, ignoring hours
	        long seconds = (temp.getSeconds() % 60);
	        
	        String string = String.format("%dh%dm%ds", hours, minutes, seconds);
	        
			bossTimerLabel.setText("" + string);
		}
	}
	
	public void resortMonsterList()
	{
		//	Sort the list of monsters based off the quantity or exp..
		
		Component[] elementArray = scrollBottomPanelList.getComponents();
		scrollBottomPanelList.removeAll();
		
		for (Component component : elementArray)
		{
			MonsterElement element = (MonsterElement) component;
            if(monsterSortMode == 0)	//	Sort by Quantity
            {
            	int place = 0;
            	
            	for(int a = 0; a < scrollBottomPanelList.getComponentCount(); a++)
            	{
            		MonsterElement child = (MonsterElement) scrollBottomPanelList.getComponent(a);
            		if(element.quantity + element.quantityParty >= child.quantity + child.quantityParty)
            		{
            			//place = a;
            			break;
            		}
            		
            		place++;
            	}
            	
            	scrollBottomPanelList.add(element, place);
            }
            
            else if(monsterSortMode == 1)	//	Sort by EXP total
            {
            	int place = 0;
            	
            	for(int a = 0; a < scrollBottomPanelList.getComponentCount(); a++)
            	{
            		MonsterElement child = (MonsterElement) scrollBottomPanelList.getComponent(a);
            		//System.out.println("a[" + a + "] place[" + place + "] element[" + element.name + "] MORE XP than child[" + child.name + "]:" + (element.expTotal > child.expTotal));
            		if(element.expTotal >= child.expTotal)
            		{
            			//place = a;
            			break;
            		}
            		
            		place++;
            	}
            	
            	//System.out.println("Adding in " + element.name + " at " + place + " place!");
            	scrollBottomPanelList.add(element, place);
            }
        }
		
		scrollBottomPanelList.revalidate();
		scrollBottomPanelList.repaint();
	}
	
	public void timerStart()
	{
        if (!timerRunning)
        {
            timerStartTime = Instant.now();
            timerRunning = true;
            timerTotalPausedTime = Duration.ZERO;
            
            panel3Time.setForeground(Color.GREEN);
        }
    }

    public void timerPause()
    {
        if (timerRunning)
        {
            timerPauseTime = Instant.now();
            timerRunning = false;
            
            panel3Time.setForeground(Color.RED);
        }
    }

    public void timerResume()
    {
        if (!timerRunning && timerPauseTime != null)
        {
            timerTotalPausedTime = timerTotalPausedTime.plus(Duration.between(timerPauseTime, Instant.now()));
            timerRunning = true;
            
            panel3Time.setForeground(Color.GREEN);
        }
    }

    public String timerGetElapsedTime()
    {
    	Duration temp = null;
        if (timerRunning)
        	temp = Duration.between(timerStartTime, Instant.now()).minus(timerTotalPausedTime);
        else
        	temp = Duration.between(timerStartTime, timerPauseTime).minus(timerTotalPausedTime);
        
        long hours = temp.toHours();
        long minutes = (temp.toMinutes() % 60);  // Extract minutes, ignoring hours
        long seconds = (temp.getSeconds() % 60);
        
        return String.format("%dh%dm%ds", hours, minutes, seconds);
    }

    public void timerReset()
    {
    	timerTotalEXP = 0;
        timerStartTime = Instant.now();
        timerPauseTime = null;
        timerTotalPausedTime = Duration.ZERO;
        timerRunning = true;
        
        panel3Time.setForeground(Color.WHITE);
    }
}
