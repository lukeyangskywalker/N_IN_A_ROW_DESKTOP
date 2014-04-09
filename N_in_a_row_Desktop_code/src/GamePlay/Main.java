package GamePlay;

/*
 * this is the entry class
 * the start game screen. 
 * you can pick 1) new game 2) option
 */

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.*;	

public class Main{

	
	private JPanel cardHolder;
	private CardLayout cards;
	private static final String main_card = "A";
	private static final String game_card = "B";
	private static final String option_card = "C";

	private class Selector implements ActionListener{
		String card;
		Selector(String card){
			this.card = card;}
		@Override
		public void actionPerformed(ActionEvent e) {
			cards.show(cardHolder, card);
		}
	}
		
	private void run(){
		
		JFrame frame = new JFrame("tic tac toe");
		
		// create all panels

		//JPanel main_game = new JPanel();
		
		// create Model and initialize it
		Model model = new Model();
		model.newgame(3, 3, 3);
		// create View, tell it about model (and controller)
		MenuView menu = new MenuView(model);
		model.addView(menu);

		ToolBarView view = new ToolBarView(model);
		model.addView(view);
		
		// create board view ...
		BoardView view2 = new BoardView(model);
		model.addView(view2);
		
		// create player view ...
		PlayerView view3 = new PlayerView(model);
		model.addView(view3);
		
		// create the window
		// make the menu panel

		JLabel game_name = new JLabel("N in a row");
		JPanel main_menu = new JPanel(new GridBagLayout());
		JButton my_new_game = new JButton("NEW GAME");
		JButton option = new JButton("OPTIONS");
		my_new_game.addActionListener(new Selector(game_card));
		
		main_menu.add(game_name);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = 1.0;
		main_menu.add(my_new_game,gbc);
		gbc.gridy = 2;
		main_menu.add(option,gbc);
		
		
		JPanel main_game = new JPanel(new BorderLayout());

		frame.getContentPane().add(main_game);
		main_game.add(view, BorderLayout.PAGE_START);
		main_game.add(view2,BorderLayout.CENTER);
		//p.add(board);
		
		cardHolder = new JPanel();
		cards = new CardLayout();
		cardHolder.setLayout(cards);
		cardHolder.add(main_menu, main_card);
		cardHolder.add(main_game, game_card);
		frame.add(cardHolder);
		
		main_game.add(view3, BorderLayout.PAGE_END);
		
		//model.start_game(3,3,3);
		
		frame.setPreferredSize(new Dimension(500,500));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static void main(String[] args){	

		new Main().run();
	} 
}
