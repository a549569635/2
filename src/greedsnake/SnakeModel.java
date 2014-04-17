package greedsnake;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.JOptionPane;

class SnakeModel implements Runnable{
	GreedSnake gs;
	boolean[][] matrix;
	LinkedList nodeArray=new LinkedList();
	Node food;
	int MAX_X;
	int MAX_Y;
	
	int direction=2;
	boolean running=false;
	int timeInterval=200;
	double speedChangeRate=0.75;
	boolean paused=false;
	int score=0;
	int countMove=0;
	
	public static final int UP=1;
	public static final int DOWN=3;
	public static final int LEFT=2;
	public static final int RIGHT=4;
	
	public SnakeModel(GreedSnake gs,int MAX_X,int MAX_Y){
		this.gs=gs;
		this.MAX_X=MAX_X;
		this.MAX_Y=MAX_Y;
		matrix=new boolean[MAX_X][];
		for(int i=0;i<MAX_X;++i){
			matrix[i]=new boolean[MAX_Y];
			Arrays.fill(matrix[i],false);
		}
		
		int initArrayLength;
		if(MAX_X>20){
			initArrayLength = 10;
		}else{
			initArrayLength = MAX_X/2;
		}
		
		for(int i=0;i<initArrayLength;++i){
			int x=MAX_X/2+i;
			int y=MAX_Y/2;
			nodeArray.addLast(new Node(x,y));
			matrix[x][y]=true;
		}
		
		food=createFood();
		matrix[food.x][food.y]=true;
	}
	
	public boolean changeDirection(int newDirection){
		if(paused){
			return false;
		}
		if(direction%2!=newDirection%2){
			direction=newDirection;
		}else if(direction == newDirection){
			speedUp();
		}else{
			speedDown();
		}
		return true;
	}
	
	public boolean moveOn(){
		Node n=(Node)nodeArray.getFirst();
		int x=n.x;
		int y=n.y;
		
		switch(direction){
		case UP:
			y--;
			break;
		case DOWN:
			y++;
			break;
		case LEFT:
			x--;
			break;
		case RIGHT:
			x++;
			break;
		}
		
		int countWin = 0;
		if((0<=x&&x<MAX_X)&&(0<=y&&y<MAX_Y)){
			if(matrix[x][y]){
				if(x==food.x&&y==food.y){
					nodeArray.addFirst(food);
					int scoreGet=(7000+300*nodeArray.size()-200*countMove)/timeInterval;
					if(scoreGet>0){
						score += scoreGet;
					}else{
						score += 10;
					}
					countWin++;
					if(countWin == 5){
						speedUp();
						countWin = 0;
					}
					countMove=0;
					food=createFood();
					matrix[food.x][food.y]=true;
					return true;
				}
				else return false;
			}else{
				nodeArray.addFirst(new Node(x,y));
				matrix[x][y]=true;
				n=(Node)nodeArray.removeLast();
				matrix[n.x][n.y]=false;
				countMove++;
				return true;
			}
		}
		return false;
	}
	
	public void run(){
		running=true;
		while(running){
			try{
				Thread.sleep(timeInterval);
			}catch(Exception e){
				break;
			}
			
			if(!paused){
				if(moveOn()){
					gs.repaint();
			    }
			    else{
				    JOptionPane.showMessageDialog(null,"GAME OVER",
						    "Game Over",JOptionPane.INFORMATION_MESSAGE);
				    break;
			    }
		    }
	    }
	    running=false;
    }
	
	private Node createFood(){
		int x=0;
		int y=0;
		
		do{
			Random r=new Random();
			x=r.nextInt(MAX_X);
			y=r.nextInt(MAX_Y);
		}while(matrix[x][y]);
		
		return new Node(x,y);
	}
	
	public void speedUp(){
		timeInterval*=speedChangeRate;
	}
	
	public void speedDown(){
		if(timeInterval<200){
			timeInterval/=speedChangeRate;
		}
	}
	
	public void changePauseState(){
		paused=!paused;
	}
}
