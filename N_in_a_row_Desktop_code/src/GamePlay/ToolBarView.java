package GamePlay;
/*
 *  ToolBarView, shows the view of the tool bar, as well as the controller
 */

import javax.swing.*;

import GamePlay.Model.GameState;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;	

class ToolBarView extends JPanel implements IView {

	// the view's main user interface
	private JButton button; 
	private JButton newgame;
	private JLabel message;
	
	private JTextField my_x = new JTextField(5);
	private JTextField my_y = new JTextField(5);
	private JTextField my_n = new JTextField(5);
	
	// the model that this view is showing
	private Model model;
	
	ToolBarView(Model model_) {
		
		this.setPreferredSize(new Dimension(100, 100));
		
		// create the view UI
		button = new JButton("?");
		button.setMaximumSize(new Dimension(100, 50));
		button.setPreferredSize(new Dimension(100, 50));
		
		newgame = new JButton("newgame");
		newgame.setMaximumSize(new Dimension(100, 50));
		newgame.setPreferredSize(new Dimension(90, 30));
		
		message = new JLabel();
		message.setForeground(Color.red);
		// the widget in the window
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(newgame,gbc);
		gbc.gridy = 1;
		this.add(new JLabel("input numbers then"),gbc);
		gbc.gridy = 2;
		this.add(new JLabel("press newgame"),gbc);
		
		// just styling the tool bar view UI
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc.fill = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weighty = 1.0;
		gbc.weightx = 3.0;
		gbc.gridwidth = 2;
		this.add(message,gbc);
		
		gbc2.gridx = 1;
		gbc2.gridy = 0;
		gbc2.anchor = GridBagConstraints.LINE_END;
		this.add(new JLabel("Rows: "),gbc2);
		gbc2.gridx = 2;
		this.add(this.my_x,gbc2);

		gbc2.gridx = 1;
		gbc2.gridy = 1;
		this.add(new JLabel("Colunms: "),gbc2);
		gbc2.gridx = 2;
		this.add(this.my_y,gbc2);
		
		gbc2.gridx = 1;
		gbc2.gridy = 2;
		this.add(new JLabel("How many in a row: "),gbc2);
		gbc2.gridx = 2;
		this.add(this.my_n,gbc2);
		
		setBackground(Color.WHITE);
		// set the model 
		model = model_;
		
		// setup the event to go to the "controller"
		// (this anonymous class is essentially the controller)
		// tells the action to the model
		
		newgame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int x1,x2,x3;
				try{
					x1 = Integer.parseInt(my_x.getText());
					x2 = Integer.parseInt(my_y.getText());
					x3 = Integer.parseInt(my_n.getText());
					}catch (NumberFormatException e1){
						x1 = model.get_size_x();
						x2 = model.get_size_y();
						x3 = model.get_n_in_a_row();
				}
				
				model.newgame(x1,x2,x3);
			}
		});
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.newgame(5,5,5);
			}
		});
	} 

	// IView interface 
	// display the message according to the model
	public void updateView() {
		System.out.println("ToolBarView: updateView");
		System.out.println("current state is: "+model.get_current_state());
		if (model.get_current_state()==GameState.BEGIN)
			message.setText("Select which player starts (see below the board)");
		else if (model.get_current_state()==GameState.SELECTED)
			message.setText("Change which player starts, or make first move");
		else if (model.get_current_state()==GameState.PLAYING)
			message.setText(model.get_number_of_moves() + "moves");
		else if (model.get_current_state()==GameState.ILLEGAL)
			message.setText("Illegal move!");
		else if (model.get_current_state()==GameState.X_WON)
			message.setText("X wins");
		else if (model.get_current_state()==GameState.O_WON)
			message.setText("O wins");
		else if (model.get_current_state()==GameState.DRAW)
			message.setText("Game over, no winner");
		
		
	}
} 
