package br.com.cod3r.ms.model;

import java.util.ArrayList;
import java.util.List;

public class Field   {
	
	private final int xAxis; //line
	private final int yAxis; //column
	
	
	private boolean open = false;
	private boolean mine = false;
	private boolean marked = false;
	
	private List<Field> neighbors = new ArrayList<>();
	private List<ObserverField> observers = new ArrayList<>();
	
	Field(int xAxis , int yAxis){
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		
	}
	
	
	public void observerRecord(ObserverField observer) {
		observers.add(observer);
	}
	
	
	private void observersNotify(EventField e ) {
		observers.stream()
		         .forEach(o -> o.ocurredEvent(this,  e));
	}
	
	
	boolean addNeighbors(Field neighbor) {
		boolean diferentxAxis = xAxis != neighbor.xAxis;
		boolean diferentyAxis = yAxis != neighbor.yAxis;
		
		boolean diagonal = diferentxAxis && diferentyAxis;
		
		int deltaX = Math.abs(xAxis - neighbor.xAxis);
		int deltaY = Math.abs(yAxis - neighbor.yAxis);
		int deltaG = deltaX + deltaY;
		
		if(deltaG == 1 && !diagonal) {
			neighbors.add(neighbor);	
			return true;
		}else if(deltaG == 2 && diagonal) {
			neighbors.add(neighbor);
			return true;
		}else {
			return false;
		}
	}
	public void switchMarking() {
		if(!open) {
			marked = !marked;
			
			if(marked) {
				observersNotify(EventField.MARK);
			}else {
				observersNotify(EventField.MARKOFF);
			}
		}
	}
	
	public boolean open() {
		if(!open && !marked) {
			open = true;
			if(mine) {
			observersNotify(EventField.EXPLODE);
			return true;
				
			}
			setOpen(true);
			
			
			if(safeNeighbors()) {
				neighbors.forEach( v -> v.open());
			}
			return true;
		}else {
			return false;
			
		}
	}
	
	public boolean safeNeighbors() {
		return neighbors.stream().noneMatch(v -> v.mine);
	}
	public boolean isMined() {
		return mine;
	}
	
	public boolean isMarked() {
		return marked;
	}
	void undermine() {
		mine = true;
		
	}
	
	 void setOpen(boolean open) {
		this.open = open;
		
		if(open) {
			observersNotify(EventField.OPEN);
		}
	}
	public boolean isOpen() {
		return open;
	}
	public boolean isClosed() {
		return !isOpen();
	}
	public int getxAxis() {
		return xAxis;
	}
	public int getyAxis() {
		return yAxis;
	}
	
	 boolean targetFinal() {
		boolean unveil = !mine && open;
		boolean secure = mine && marked;
		return unveil || secure;
	}
	public int minesInNeighborhood() {
		return (int) neighbors.stream().filter(v -> v.mine).count();
		}
	void restart() {
		open = false;
		mine = false;
		marked = false;
	    observersNotify(EventField.RESTART);
	}
	
}
