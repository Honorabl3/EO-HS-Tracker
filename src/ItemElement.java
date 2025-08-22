import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class ItemElement extends JLayeredPane
{
	int id, quantity, expTotal;
	public String name;
	
	public JLabel quantityLabel, expTotalLabel;
	
	public ItemElement(Tracker tracker, String n)
	{
		ItemData ee = tracker.panel.monsterPanel.itemData.get(n);
		name = ee.name;
		id = ee.id;
		quantity = 0;
		expTotal = 0;
		
		if(ee.image == null || ee.smallImage == null)
		{
			ee.setImage(tracker.web.getItemImage(id));
			
			// Desired maximum dimension
	        int maxWidth = 32;
	        int maxHeight = 32;
	
	        // Calculate the new dimensions while preserving aspect ratio
	        int originalWidth = ee.image.getIconWidth();
	        int originalHeight = ee.image.getIconHeight();
	
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
	        Image scaledImage = ee.image.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
	        ee.setSmallImage(new ImageIcon(scaledImage));
			//ImageIcon tempIcon = new ImageIcon(ee.image.getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));
			//ee.setSmallImage(tempIcon);
		}
		
		setName(name);
		
		setBackground(Color.decode("#111111"));
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(256, 32));
		setMaximumSize(new Dimension(384, 32));
		//setBounds(0, 0, 64, 100);
		
		//JPanel xButtonPanel = new JPanel();
		//xButtonPanel.setBackground(Color.BLACK);
		//xButtonPanel.add(Box.createHorizontalGlue());
		JLabel xButton = new JLabel(tracker.panel.redXIcon);
		//xButton.setMaximumSize(new Dimension(8, 8));
		//xButton.setPreferredSize(new Dimension(8, 8));
		xButton.setBounds(24, 0, 8, 8);
		this.add(xButton, JLayeredPane.DRAG_LAYER);
		//xButtonPanel.add(xButton);
		//this.add(xButtonPanel);
		
		xButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                tracker.panel.monsterPanel.removeItemElement(name);
            }
        });
		
		//JLabel iconImage = new JLabel(tracker.panel.monsterPanel.monsterData.get("name").im
		JLabel iconImage = new JLabel(ee.smallImage);
		//JLabel iconImage = new JLabel(ee.image);
		iconImage.setBackground(Color.BLACK);
		iconImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		iconImage.setPreferredSize(new Dimension(64, 64));
		iconImage.setBounds(0, 0, 32, 32);
		this.add(iconImage, JLayeredPane.PALETTE_LAYER);
		
		iconImage.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                tracker.panel.monsterPanel.populateInfo(ee);
            }
        });
		
		JLabel nameLabel = new JLabel(name);
		nameLabel.setFont(tracker.panel.monsterPanel.monsterMediumFont);
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		nameLabel.setHorizontalAlignment(JLabel.LEFT);
		nameLabel.setVerticalAlignment(JLabel.CENTER);
		nameLabel.setBounds(34, 4, 384, 16);
		//nameLabel.setBounds(nameLabel.getBounds().width, nameLabel.getBounds().height, nameLabel.getHorizontalTextPosition(), nameLabel.getVerticalAlignment()-10);
		this.add(nameLabel, MODAL_LAYER);
		
		quantityLabel = new JLabel("x" + quantity);
		quantityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		quantityLabel.setForeground(Color.WHITE);
		quantityLabel.setFont(tracker.panel.monsterPanel.monsterMediumFont);
		quantityLabel.setHorizontalAlignment(JLabel.LEFT);
		quantityLabel.setVerticalAlignment(JLabel.CENTER);
		quantityLabel.setBounds(34, 16, 256, 16);
		this.add(quantityLabel, MODAL_LAYER);
		
		expTotalLabel = new JLabel("+" + expTotal);
		expTotalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		expTotalLabel.setForeground(Color.WHITE);
		expTotalLabel.setFont(tracker.panel.monsterPanel.monsterMediumFont);
		expTotalLabel.setHorizontalAlignment(JLabel.LEFT);
		expTotalLabel.setVerticalAlignment(JLabel.CENTER);
		expTotalLabel.setBounds(72, 18, 96, 12);
		expTotalLabel.setVisible(false);
		this.add(expTotalLabel, MODAL_LAYER);
	}
	
	public void addData(int amt, int xp)
	{
		quantity += amt;
		expTotal += xp;
		
		update();
	}
	
	public void update()
	{
		if(expTotal > 0)
			expTotalLabel.setVisible(true);
		
		quantityLabel.setText("x" + quantity);
		expTotalLabel.setText("+" + expTotal);
	}
}
