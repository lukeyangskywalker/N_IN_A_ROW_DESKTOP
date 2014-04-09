package GamePlay;

import javax.swing.*;
import javax.swing.event.*;

import GamePlay.Model.GameState;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

/**
 * A view of the game board, and its controller
 * 
 */
public class BoardView extends JComponent implements IView {
	private Model model;

	private int cell_size = 60;
	private int strock_size = 2;
	private int offx;
	private int offy;
	
	private int rows;
	private int cols;
	
	// for animation
	private int frame1 = 0;
	private int frame2 = 0;
	private Timer timer = new Timer(1,new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e){
			frame1 ++ ;
			repaint();
		}
	});
	public void stopAnimation() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }

    public void startAnimation() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }
	
	// IView interface
	public void updateView() {
		System.out.println("BoardView : updateView");
		
		repaint();
	}

	// To format numbers consistently in the text fields.
	private static final NumberFormat formatter = NumberFormat
			.getNumberInstance();

	public BoardView(Model aModel) {
		super();
		this.model = aModel;
		rows = model.get_size_x();
		cols = model.get_size_y();
		
		this.layoutView();
		this.registerControllers();
	}
	
	private void update_row_col(){
		rows = model.get_size_x();
		cols = model.get_size_y();
	}

	/** How should it look on the screen? */
	private void layoutView() {
		
		this.setPreferredSize(new Dimension(200, 200));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	/** Register event Controllers for mouse clicks and motion. */
	private void registerControllers() {
		MouseInputListener mil = new MController();
		this.addMouseListener(mil);
		this.addMouseMotionListener(mil);
	}

	// paint board animation
	
	private void paintboard(Graphics2D g2){
		g2.setColor(Color.gray);
		
		int x = get_position_x();
		int y = get_position_y();
		int i = frame1;
		int j = frame2;
		
		// print rows
		if (i < (cols*cell_size)){
			for (int a = 1; a < rows; a++){
				g2.drawLine(x, y+(a*cell_size), x+i, y+(a*cell_size));
		
			}
		}
		// print cols if rows are already printed
		else if(j < (rows*cell_size)){
		
			for (int i1 = 1; i1<rows;i1++){
				g2.drawLine(x, y+(i1*cell_size), x+(cols*cell_size), y+(i1*cell_size));
			}
			
			for (int b = 1; b < cols; b++){
				g2.drawLine(x+(b*cell_size), y, x+(b*cell_size), y+j);
			}
			frame2 ++ ;
		}
		
		else{
			timer.stop();
			model.set_board_drawn();
		}
	}
	/** Paint the board, and handle resize */
	public void paintComponent(Graphics g) {
		update_row_col();
		super.paintComponent(g);
		Insets insets = this.getInsets();
		g.setColor(new Color(236,231,208));
		g.fillRect(0, 0, getWidth(), getHeight());
		int x = get_position_x();
		int y = get_position_y();
		
		fix_cell_size();
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(strock_size));
		// Draw the board first
		if (!model.is_board_drawn()){
			startAnimation();
			paintboard(g2);
		}
		
		else if (model.is_board_drawn()){
			frame1 = 0;
			frame2 = 0;
			g2.setColor(Color.gray);
		// draw rows line
		
		for (int i = 1; i<rows;i++){
			g2.drawLine(x, y+(i*cell_size), x+(cols*cell_size), y+(i*cell_size));
		}
		// draw cols line
		for (int i = 1; i<cols;i++){
			g2.drawLine(x+(i*cell_size), y, x+(i*cell_size), y+(rows*cell_size));
		}
		
		for (int i = 0; i <rows; i++){
			for (int j = 0; j < cols; j++){
				if (model.get_board(i,j) == 0){
					drawO(g2, x+(i*cell_size), y+(j*cell_size));
				}
				else if (model.get_board(i, j) == 1){
					drawX(g2, x+(i*cell_size), y+(j*cell_size));
				}
			}
			
		}

		}


	}
	
	

	// calculate the size of each cell based on the window size
	private void fix_cell_size(){
		int w = getWidth();
		int h = getHeight();
		if (w>=h){
			cell_size = h/rows;
		}
		else {
			cell_size = w/cols;
		}
		
		strock_size = cell_size /20;
		
	}
		
	// get position of the board offset based on the current view size
	private int get_position_x(){
		int ret = getWidth() - cols*cell_size;
		return ret/2;
	}
	private int get_position_y(){
		return (getHeight() - rows*cell_size)/2;
	}
	
	// draw O and X
	
	private void drawO(Graphics2D g, int x, int y){
		g.setColor(Color.black);
		int offset = cell_size/10;
		g.drawOval(x+offset, y+offset, cell_size-(offset*2), cell_size-(offset*2));
		
	}
	
	private void drawX(Graphics2D g, int x, int y){
		g.setColor(Color.black);
		int offset = cell_size/10;
		g.drawLine(x+offset, y+offset, x+cell_size-offset, y+cell_size-offset);
		g.drawLine(x+cell_size-offset, y+offset, x+offset, y+cell_size-offset);
		
	}
	
	/**************************************************
	 * below is the controller part
	 * 
	 * 
	 *
	 **************************************************/
	
	private class MController extends MouseInputAdapter {
		/*
		 * Select or deselect the triangle.
		 */
		private boolean canDragX = false;

		private boolean canDragY = false;

		public void mouseClicked(MouseEvent e) {
			//selected = onRightSide(e.getX(), e.getY());
			if (model.get_current_state()==GameState.SELECTED ||
				model.get_current_state()==GameState.PLAYING ||
				model.get_current_state()==GameState.ILLEGAL
				){
				int x = e.getX();
				int y = e.getY();
			
				int offx = get_position_x();
				int offy = get_position_y();
			
				int zonex = -1;
				int zoney = -1;
				fix_cell_size();
			
				// find out which cell the clicked occured, return -1 if not in any cell
				for (int i = 0; i <cols; i ++){
					if (x >= offx+(i*cell_size) && x < offx+((i+1)*cell_size)){
						zonex = i;				
					}
				}
			
				for (int i = 0; i <rows; i ++){
				
					if (y >= offy+(i*cell_size) && y < offy+((i+1)*cell_size)){
						zoney = i;				
					}
				}
				if (zonex >=0 && zoney >=0){
					System.out.println("Mouse clicked on zone "+zonex+" : "+zoney);
					model.clicked(zonex,zoney);
			}
			}
		}

	} // MController
} // GraphicalView

