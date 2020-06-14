package br.com.cod3r.ms.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
public class Board implements ObserverField {

	private final int lines;
	private final int columns;
	private final int mines;
	
	private final List<Field> fields = new ArrayList<>();
	private final List<Consumer<EventResult>> observers = new ArrayList<>();
	

	public Board(int lines, int columns, int mines) {
		this.lines = lines;
		this.columns = columns;
		this.mines = mines;
	
		createFields();
		associateNeighbors();
		draftMines();
	   }
	
	public void forEach(Consumer< Field > function) {
		fields.forEach(function);
	}
	
	public void observersRecord(Consumer<EventResult> observer) {
		observers.add(observer);
	}
	
	private void observersNotify(boolean result) {
		observers.stream()
		.forEach(o -> o.accept(new EventResult(result)));
	}
	
	public void open(int line, int column ) {
	
			fields.parallelStream()
		      .filter(c -> c.getxAxis() == line && c.getyAxis() == column)
		      .findFirst()
		      .ifPresent( c -> c.open());
			}
		
	public void switchMarking(int line, int column ) {
		fields.parallelStream()
		.filter(c -> c.getxAxis() == line && c.getyAxis() == column)
		.findFirst()
		.ifPresent( c -> c.switchMarking());
	}
	
	
	
	private void createFields() {
		for (int l = 0; l < lines; l++) {
			for (int c = 0; c < columns; c++) {
				Field field = new Field(l, c);
				field.observerRecord(this);
				fields.add(field);
			}
			
		}
		
	}
	
	private void associateNeighbors() {
		for(Field f1: fields) {
			for(Field f2: fields) {
				f1.addNeighbors(f2);
			}
		}
	}
	private void draftMines() {
		long armedMines = 0;
		
		Predicate<Field> mine = f -> f.isMined();
		
		do {
			int aleatory =(int) (Math.random() * fields.size());
			fields.get(aleatory).undermine();
			armedMines = fields.stream().filter(mine).count();
		}while(armedMines < mines);
		
	}
	public boolean targetFinal() {
		return fields.stream().allMatch(f -> f.targetFinal()); 
	}
	public void restart() {
		fields.stream().forEach(f -> f.restart());
		draftMines();
	}
	
	
	
	public int getLines() {
		return lines;
	}

	public int getColumns() {
		return columns;
	}

	@Override
	public void ocurredEvent(Field f, EventField e) {
		if(e == EventField.EXPLODE) {
			showMines();
			observersNotify(false);
		}else if(targetFinal()) {
			observersNotify(true);
		}
		
	}
	
	private void showMines() {
		fields.stream()
		.filter(f -> f.isMined())
		.filter(f -> !f.isMarked())
		.forEach(f -> f.setOpen(true));
	}
}
