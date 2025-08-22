import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class DropElement extends JLayeredPane
{
	public int id;
	
	public String name;
	
	ItemData itemData;
	
	public DropElement(Tracker tracker, DropData drop)
	{
		id = drop.id;
		
		//itemData = tracker.panel.monsterPanel.itemData.get(a);
		
	    for (ItemData value : tracker.panel.monsterPanel.itemData.values())
	    {
	        if (value.id == id)
	        {
	        	itemData = value;
	        	break;
	        }
	    }
	    
	    if(itemData == null)
	    {
	    	System.out.println("Drop Panel Data was not able to find itemData for ID [" + id + "]! Skipping this...");
	    	return;
	    }
	    
		//itemData = new ArrayList<>(tracker.panel.monsterPanel.itemData.values()).get(a);
		//itemData = new ArrayList<>tracker.panel.monsterPanel.itemData(map.values()).get(index);
		name = itemData.name;
		
		if(itemData.image == null || itemData.smallImage == null)
		{
			itemData.setImage(tracker.web.getItemImage(id));
			
			// Desired maximum dimension
	        int maxWidth = 32;
	        int maxHeight = 32;
	
	        // Calculate the new dimensions while preserving aspect ratio
	        int originalWidth = itemData.image.getIconWidth();
	        int originalHeight = itemData.image.getIconHeight();
	
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
	        Image scaledImage = itemData.image.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
	        itemData.setSmallImage(new ImageIcon(scaledImage));
			//ImageIcon tempIcon = new ImageIcon(ee.image.getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));
			//ee.setSmallImage(tempIcon);
	        
	        
		}
		
		setName(name);
		
		setBackground(Color.decode("#111111"));
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(256, 32));
		setMaximumSize(new Dimension(384, 32));
		setMinimumSize(new Dimension(384, 32));
		//setBounds(0, 0, 64, 100);
		
		//JLabel xButton = new JLabel(tracker.panel.redXIcon);
		//xButton.setBounds(24, 0, 8, 8);
		//this.add(xButton, JLayeredPane.DRAG_LAYER);
		
		//xButton.addMouseListener(new MouseAdapter()
        //{
        //    @Override
        //    public void mouseClicked(MouseEvent e)
        //    {
        //        tracker.panel.monsterPanel.removeItemElement(name);
        //    }
        //});
		
		//JLabel iconImage = new JLabel(tracker.panel.monsterPanel.monsterData.get("name").im
		JLabel iconImage = new JLabel(itemData.smallImage);
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
                tracker.panel.monsterPanel.populateInfo(itemData);
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
		
		String chance = "";
		String chance2 = "";
		
		if(100 / drop.chance < 2)
			chance2 = "";
		
		else
		{
			if((100 / drop.chance) % 1 > 0)
				chance2 = "   1/" + (((int)(100/drop.chance))+1) + "*";
			else
				chance2 = "   1/" + ((int)(100/drop.chance));
		}
		
		if(drop.chance % 1 == 0)
			chance = (int) drop.chance + "%" + chance2;
		else
			chance = drop.chance + "%" + chance2;
		
		JLabel chanceLabel = new JLabel(chance);
		chanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		chanceLabel.setForeground(Color.WHITE);
		chanceLabel.setFont(tracker.panel.monsterPanel.monsterMediumFont);
		chanceLabel.setHorizontalAlignment(JLabel.LEFT);
		chanceLabel.setVerticalAlignment(JLabel.CENTER);
		chanceLabel.setBounds(34, 16, 256, 16);
		this.add(chanceLabel, MODAL_LAYER);
		
		//System.out.println("Loaded drop " + a + " with chance " + b);
	}
}
