package GamePlay;

/*
 * the is the menu view at the top of the game play
 */

import javax.swing.*;

import GamePlay.Model.GameState;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;	

class MenuView extends JPanel implements IView {

	// the view's main user interface
	private JButton button; 
	private JButton newgame;
	private JLabel message;
	// the model that this view is showing
	private Model model;
	
	MenuView(Model model_) {
		
		this.setPreferredSize(new Dimension(100, 100));
		
		// create the view UI
		button = new JButton("?");
		button.setMaximumSize(new Dimension(100, 50));
		button.setPreferredSize(new Dimension(100, 50));
		
		newgame = new JButton("newgame");
		newgame.setMaximumSize(new Dimension(100, 50));
		newgame.setPreferredSize(new Dimension(90, 30));
		
		message = new JLabel();

		// a GridBagLayout with default constraints centres
		// the widget in the window
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 1.0;
		
		this.add(newgame, gbc);
		
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.weighty = 1.0;
		this.add(message,gbc2);
		setBackground(Color.WHITE);
		// set the model 
		model = model_;
		
		// setup the event to go to the "controller"
		// (this anonymous class is essentially the controller)

		
		newgame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.start_game(3,3,3);
			}
		});
	} 

	// IView interface 
	public void updateView() {
		System.out.println("ToolBarView: updateView");
		
		System.out.println("current state is: "+model.get_current_state());
		
		
	}
} 
