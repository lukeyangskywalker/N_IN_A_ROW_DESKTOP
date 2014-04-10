package GamePlay;
/*
 *  PlayerView
 *  shows the player view as well as its controller
 *  
*/
import javax.swing.*;

import GamePlay.Model.GameState;

import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.*;
import java.util.*;

class PlayerView extends JPanel implements IView {

	// the model that this view is showing
	private Model model;
	private JRadioButton O;
	private JRadioButton X;
	private JRadioButton invisible;
	private JLabel status;
	PlayerView(Model model_) {
		// create UI
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		O = new JRadioButton("O first",false);
		X = new JRadioButton("X first",false);
		
		// make a invisible button for the group
		invisible = new JRadioButton("blah",false);
		
		status = new JLabel("");
		
		ButtonGroup bgroup = new ButtonGroup();
		bgroup.add(O);
		bgroup.add(X);
		bgroup.add(invisible);
		
		O.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("O selected");
				model.selected(0);
			}
		});
		
		X.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("X selected");
				model.selected(1);
			}
		});
		
		// set the model
		model = model_;
		add(O);
		add(X);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.PAGE_END;
		add(status, gbc);
		// setup the event to go to the "controller"
		// (this anonymous class is essentially the controller)		
		addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
				}
		});
	}

	// IView interface
	public void updateView() {
		System.out.println("Player: updateView");
		// just displays an 'X' for each counter value
		if ((model.get_current_state() != GameState.BEGIN) && (model.get_current_state() != GameState.SELECTED)){
			O.setEnabled(false);
			X.setEnabled(false);
			if (model.get_turn() == 0)
				status.setText("It is O's turn");
			if (model.get_turn() == 1)
				status.setText("It is X's turn");	
			
		}
		else {
			O.setEnabled(true);
			X.setEnabled(true);
			status.setText("");
			if (model.get_current_state() == GameState.BEGIN){
				invisible.setSelected(true);
			}
		}
	}
}
