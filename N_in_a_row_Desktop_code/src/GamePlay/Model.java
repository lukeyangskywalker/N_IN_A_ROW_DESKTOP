package GamePlay;
import java.util.ArrayList;

/*
 *  the model of the game, it is the brain, the master mind that calculates everything in the back end 
 */

// View interface
interface IView {
	public void updateView();
}

public class Model {	
	
	// state, 0 means menu page, 1 means to load the game
	// could be better with String or Char, but I like integer
	private int main_state = 0;
	
	// the data in the model, just a counter
	private int counter;	
	// all views of this model
	private ArrayList<IView> views = new ArrayList<IView>();
	
	// define the size of the matrix of the game, default : 3 x 3
	private static int sizex=3;
	private static int sizey=3;
	// define the winning number, 3 in a row, 4 in a row, 5 in a row etc..
	private static int n_in_a_row=3;
	
	// store the board information matrix
	private int board[][];
	
	final int BLANK = 2;  // empty
	final int PCO = 0;  // for O
	final int PCX = 1;   //  for X
	
	private int number_of_moves = 0;
	
	public enum GameState {  // to save as "GameState.java"
		   BEGIN, SELECTED, PLAYING, ILLEGAL, DRAW, X_WON, O_WON
		}
	
	private GameState current_state = GameState.BEGIN;
	
	private int turn; //0 is O, 1 is X
	private boolean boarddrawn = false;
	
	public void start_game(int x, int y, int n){
		sizex = x;
		sizey = y;
		n_in_a_row = n;
		main_state = 1;
	}
	public int get_main_state(){
		return main_state;
	}
	
	public int get_number_of_moves(){
		return number_of_moves;
	}
	
	public GameState get_current_state(){
		
		return current_state;
	}
	
	// if X or O radiobutton is selected, told by the PlayerView controller, then notify all viewers
	public void selected(int i){
		if (i == 1){
			turn = 1;
			current_state = GameState.SELECTED;
		}
		else if (i == 0){
			turn = 0;
			current_state = GameState.SELECTED;
		}
		notifyObservers();
		
	}
	
	// get info from boardview controller and modify the board matrix
	// then notify every view
	public void clicked(int x, int y){
		System.out.println("board "+x+" : "+y + " is " + board[x][y]);
		System.out.println("turn is "+turn);
		
		if (board[x][y] != BLANK){
			current_state = GameState.ILLEGAL;
		}
		else if (turn == 0){
			board[x][y] = PCO;
			turn = 1;
			number_of_moves++;
			current_state = GameState.PLAYING;
		}
		else if (turn == 1){
			board[x][y] = PCX;
			turn = 0;
			number_of_moves++;
			current_state = GameState.PLAYING;
		}
		notifyObservers();
		if (check_win() == 0)
			current_state = GameState.O_WON;
		if (check_win() == 1)
			current_state = GameState.X_WON;
		if (check_win() == 2)
			current_state = GameState.DRAW;
		notifyObservers();
	}
	
	// tells the board view the size of the game
	public int get_size_x(){
		return sizex;
	}
	public int get_size_y(){
		return sizey;
	}
	
	public int get_n_in_a_row(){
		return n_in_a_row;
	}
	private void board_init(){
		board = new int[get_size_x()][get_size_y()];
		for (int i=0; i < get_size_x(); i++)
		   for (int j=0; j < get_size_y(); j++){
			   System.out.println("setting black to "+ i + " : "+j);
			   board[i][j] = BLANK;
		   }
	}
	
	public void newgame(int x, int y, int n){
		sizex = x;
		sizey = y;
		n_in_a_row = n;
		
		board_init();
		boarddrawn = false;
		number_of_moves = 0;
		current_state = GameState.BEGIN;
		System.out.println("Model: starting a new game");
		notifyObservers();
	}
	
	// tells the player view whos turn it is
	public int get_turn(){
		return turn;
	}
	
	// tells the board view all info it needs to draw the board
	public int get_board(int x, int y){
		return board[x][y];
	}
	public boolean is_board_drawn(){
		if (boarddrawn)
			return true;
		else 
			return false;
	}
	public void set_board_drawn(){
		
		boarddrawn = true;
		notifyObservers();
	}
	public int getsizex(){
		return sizex;
	}
	public int getsizey(){
		return sizey;		
	}
	// set the view observer
	public void addView(IView view) {
		views.add(view);
		// update the view to current state of the model
		notifyObservers();
	}
		
	// notify the IView observer
	private void notifyObservers() {
			for (IView view : this.views) {
				System.out.println("Model: notify View");
				view.updateView();
			}
	}
	
	// check if anyone win or draw
	// 0  - o wins
	// 1  - x wins
	// 2  - draw
	// -1 - no winner yet
	//
	// TODO: clean up the code... probably wont be anytime soon
	
	// check each non-empty cell, in four directions:
	//		\		|		 /
	// --- , \  , 	|,		/
	//		  \		|	   /
	private int check_win(){
		if (number_of_moves <5) {
			return -1;
		}
		
		int temp;
		int counter = 1;
		// for each cell, check if it has a n_in_a_row
		for (int i=0; i < sizex; i++){
			for (int j=0; j < sizey; j++){
				if (board[i][j] == BLANK)
					continue;
				temp = board[i][j];
				counter = 1;
				// now check if in a row ->
				for (int x = i+1; x<sizex;x++){
					if (temp != board[x][j])
						break;
					temp = board[x][j];
					counter++;
				}
				if (counter >= n_in_a_row)
					return temp;
				
				// now check if in a cross row \
				int x = i+1;
				int y = j +1;
				temp = board[i][j];
				counter = 1;
				while(x<sizex && y <sizey){
					if (temp != board[x][y])
						break;
					temp = board[x][y];
					counter++;
					x++;
					y++;
				}
				if (counter >= n_in_a_row)
					return temp;
				
				// now check if in a column
				temp = board[i][j];
				counter = 1;
				for (x = j+1; x<sizey;x++){
					if (temp != board[i][x])
						break;
					temp = board[i][x];
					counter++;
				}
				if (counter >= n_in_a_row)
					return temp;
				
				
				// now check if in a cross left down /
				x = i-1;
				y = j+1;
				temp = board[i][j];
				counter = 1;
				while(x>=0 && y < sizey){
					if (temp != board[x][y])
						break;
					temp = board[x][y];
					counter++;
					x--;
					y++;
				}
				if (counter >= n_in_a_row)
					return temp;
				
			}
		}
		
		if (number_of_moves == sizex*sizey){
			return 2;
		}
		
		// if all cases fails, return -1
		return -1;
		
		//if (number_of_moves == 9)
		
	}
}
