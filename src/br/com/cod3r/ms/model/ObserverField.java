package br.com.cod3r.ms.model;

@FunctionalInterface
public interface ObserverField {
	
	public void ocurredEvent( Field f , EventField e);
	
}
