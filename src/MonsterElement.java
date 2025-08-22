import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class MonsterElement extends JLayeredPane
{
	public int id, quantity, quantityParty, expTotal;
	String name = "";
	
	MonsterData monsterData;
	
	public JLabel quantityLabel, quantityPartyLabel, expTotalLabel;
	
	static ImageIcon partySoloIcon = new ImageIcon("EO-HS-Tracker/images/party_solo.png");
	static ImageIcon partyGroupIcon = new ImageIcon("EO-HS-Tracker/images/party_group.png");
	
	public MonsterElement(Tracker tracker, String n)
	{
		monsterData = tracker.panel.monsterPanel.monsterData.get(n);
		name = monsterData.name;
		id = monsterData.id;
		quantity = 0;
		quantityParty = 0;
		expTotal = 0;
		
		//prepImages(tracker.panel.monsterPanel.monsterData.get(n));
		
		if(monsterData.image == null || monsterData.smallImage == null)
		{
			monsterData.setImage(tracker.web.getMonsterImage(id));
			
			// Desired maximum dimension
	        int maxWidth = 64;
	        int maxHeight = 64;
	
	        // Calculate the new dimensions while preserving aspect ratio
	        int originalWidth = monsterData.image.getIconWidth();
	        int originalHeight = monsterData.image.getIconHeight();
	
	        // Calculate aspect ratio
	        double aspectRatio = (double) originalWidth / originalHeight;
	
	        // Determine the new dimensions
	        int newWidth, newHeight;
	
	        if (originalWidth > originalHeight)
	        {
	            newWidth = maxWidth;
	            newHeight = (int) (maxWidth / aspectRatio);
	        }
	        
	        else
	        {
	            newHeight = maxHeight;
	            newWidth = (int) (maxHeight * aspectRatio);
	        }
	
	        // Scale the image
	        Image scaledImage = monsterData.image.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
	        monsterData.setSmallImage(new ImageIcon(scaledImage));
			//ImageIcon tempIcon = new ImageIcon(ee.image.getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));
			//ee.setSmallImage(tempIcon);
		}
		
		setName(name);
		
		setBackground(Color.decode("#111111"));
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(64, 100));
		//setBounds(0, 0, 64, 100);
		
		//JPanel xButtonPanel = new JPanel();
		//xButtonPanel.setBackground(Color.BLACK);
		//xButtonPanel.add(Box.createHorizontalGlue());
		JLabel xButton = new JLabel(tracker.panel.redXIcon);
		//xButton.setMaximumSize(new Dimension(8, 8));
		//xButton.setPreferredSize(new Dimension(8, 8));
		xButton.setBounds(56, 8, 8, 8);
		this.add(xButton, JLayeredPane.DRAG_LAYER);
		//xButtonPanel.add(xButton);
		//this.add(xButtonPanel);
		
		xButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                tracker.panel.monsterPanel.removeMonsterElement(name);
            }
        });
		
		//JLabel iconImage = new JLabel(tracker.panel.monsterPanel.monsterData.get("name").im
		JLabel iconImage = new JLabel(monsterData.smallImage);
		//JLabel iconImage = new JLabel(ee.image);
		iconImage.setBackground(Color.BLACK);
		iconImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		iconImage.setPreferredSize(new Dimension(64, 64));
		iconImage.setBounds(0, 0, 64, 64);
		this.add(iconImage, JLayeredPane.PALETTE_LAYER);
		
		iconImage.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                tracker.panel.monsterPanel.populateInfo(monsterData);
            }
        });
		
		JLabel nameLabel = new JLabel(name);
		nameLabel.setFont(tracker.panel.monsterPanel.monsterSmallFont);
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		nameLabel.setVerticalAlignment(JLabel.TOP);
		nameLabel.setBounds(0, 1, 64, 32);
		//nameLabel.setBounds(nameLabel.getBounds().width, nameLabel.getBounds().height, nameLabel.getHorizontalTextPosition(), nameLabel.getVerticalAlignment()-10);
		this.add(nameLabel, MODAL_LAYER);
		
		quantityLabel = new JLabel("x" + quantity, partySoloIcon, JLabel.LEFT);
		quantityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		quantityLabel.setForeground(Color.WHITE);
		quantityLabel.setFont(tracker.panel.monsterPanel.monsterMediumFont);
		quantityLabel.setHorizontalAlignment(JLabel.CENTER);
		quantityLabel.setVerticalAlignment(JLabel.CENTER);
		quantityLabel.setBounds(0, 64, 64, 12);
		this.add(quantityLabel, MODAL_LAYER);
		
		quantityPartyLabel = new JLabel("x" + quantity, partyGroupIcon, JLabel.LEFT);
		quantityPartyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		quantityPartyLabel.setForeground(Color.WHITE);
		quantityPartyLabel.setFont(tracker.panel.monsterPanel.monsterMediumFont);
		quantityPartyLabel.setHorizontalAlignment(JLabel.CENTER);
		quantityPartyLabel.setVerticalAlignment(JLabel.CENTER);
		quantityPartyLabel.setBounds(0, 76, 64, 12);
		this.add(quantityPartyLabel, MODAL_LAYER);
		
		expTotalLabel = new JLabel("+" + expTotal);
		expTotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		expTotalLabel.setForeground(Color.WHITE);
		expTotalLabel.setFont(tracker.panel.monsterPanel.monsterMediumFont);
		expTotalLabel.setHorizontalAlignment(JLabel.CENTER);
		expTotalLabel.setVerticalAlignment(JLabel.CENTER);
		expTotalLabel.setBounds(0, 88, 64, 12);
		this.add(expTotalLabel, MODAL_LAYER);
		
		//JPanel tempPanel
		//this.add(tempPanel);
	}
	
	//	Because exp can change on the NPC you slay (low-level 4x boost situations, or in a party), I have decided to not assume the EXP will ever remain the same static amount.
	public void addData(int amt, int xp, boolean party)
	{
		//	Check if the gained XP is less than the amount.. (detecting if in a party)
		if(xp < monsterData.exp && xp > (monsterData.exp / 2))
			quantityParty += amt;
		else
			quantity += amt;
		expTotal += xp;
		
		update();
	}
	
	public void update()
	{
		quantityLabel.setText("x" + quantity);
		if(quantityParty > 0)
		{
			if(!quantityPartyLabel.isVisible())
			{
				quantityPartyLabel.setVisible(true);
			}
			
			quantityPartyLabel.setText("x" + quantityParty);
		}
		expTotalLabel.setText("+" + expTotal);
	}
}
