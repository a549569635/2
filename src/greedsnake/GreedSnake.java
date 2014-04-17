package greedsnake;

import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.*;

public class GreedSnake implements KeyListener{
	JFrame mainFrame;
	Canvas paintCanvas;
	JLabel labelScore;
	SnakeModel snakeModel=null;
	public static final int canvasWidth=300;
	public static final int canvasHeight=400;
	public static final int nodeWidth=10;
	public static final int nodeHeight=10;
	
	public static void main(String[] args){
		GreedSnake gs=new GreedSnake();
	}
	
	public GreedSnake(){
		mainFrame=new JFrame("贪吃蛇");
		Container cp=mainFrame.getContentPane();
		labelScore=new JLabel("分数:");
		cp.add(labelScore,BorderLayout.NORTH);
		paintCanvas=new Canvas();
		paintCanvas.setSize(canvasWidth+1,canvasHeight+1);
		paintCanvas.addKeyListener(this);
		cp.add(paintCanvas,BorderLayout.CENTER);
		
		JPanel panelButtom=new JPanel();
		panelButtom.setLayout(new BorderLayout());
		
		JLabel labelHelp;
		labelHelp=new JLabel("PageUp,PageDown键调节速度（暂停时可用）;",JLabel.CENTER);
		panelButtom.add(labelHelp,BorderLayout.NORTH);
		labelHelp=new JLabel("ENTER,R和S键重新开始游戏;",JLabel.CENTER);
		panelButtom.add(labelHelp,BorderLayout.CENTER);
		labelHelp=new JLabel("空格和P键暂停游戏。",JLabel.CENTER);
		panelButtom.add(labelHelp,BorderLayout.SOUTH);
		
		cp.add(panelButtom,BorderLayout.SOUTH);
		
		mainFrame.addKeyListener(this);
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		
		begin();
	}
	public void keyPressed(KeyEvent e){
		int keyCode=e.getKeyCode();
		if(snakeModel.running){
			switch(keyCode){
			
			case KeyEvent.VK_UP:
				snakeModel.changeDirection(SnakeModel.UP);
				break;
			case KeyEvent.VK_DOWN:
				snakeModel.changeDirection(SnakeModel.DOWN);
				break;
			case KeyEvent.VK_LEFT:
				snakeModel.changeDirection(SnakeModel.LEFT);
				break;
			case KeyEvent.VK_RIGHT:
				snakeModel.changeDirection(SnakeModel.RIGHT);
				break;
			case KeyEvent.VK_PAGE_UP:
				snakeModel.speedUp();
				break;
			case KeyEvent.VK_SUBTRACT:
			case KeyEvent.VK_PAGE_DOWN:
				snakeModel.speedDown();
				break;
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_P:
				snakeModel.changePauseState();
				break;
			default:
			}
		}
		
		if(keyCode==KeyEvent.VK_R || keyCode==KeyEvent.VK_S || keyCode==KeyEvent.VK_ENTER){
			snakeModel.running=false;
			begin();
		}
	}
    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
    
    void repaint(){
	    Graphics g=paintCanvas.getGraphics();
	    g.setColor(Color.WHITE);
	    g.fillRect(0,0,canvasWidth,canvasHeight);
	    g.setColor(Color.BLACK);
	    LinkedList na=snakeModel.nodeArray;
	    Iterator it=na.iterator();
	
	    while(it.hasNext()){
		    Node n=(Node)it.next();
		    drawNode(g,n);
	    }
	
	    g.setColor(Color.RED);
	    Node n=snakeModel.food;
	    drawNode(g,n);
	    updateScore();
    }

    private void drawNode(Graphics g,Node n){
	    g.fillRect(n.x*nodeWidth,n.y*nodeHeight,nodeWidth-1,nodeHeight-1);
    }

    public void updateScore(){
	    String s="分数: "+snakeModel.score;
	    labelScore.setText(s);
    }

    void begin(){
	    if(snakeModel==null||!snakeModel.running){
		    snakeModel=new SnakeModel(this,canvasWidth/nodeWidth,
				    this.canvasHeight/nodeHeight);
		    (new Thread(snakeModel)).start();
	    }
    }
}
