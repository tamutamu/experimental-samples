package org.farrukh.examples.patterns.behavioral.observer.ch1;

public interface Subject {
	
	public Object getUpdate();
	
	public void notifyObserver();
	
	public void registerObserver(Observer observer);
	
	public void unRegisterObserver(Observer observer);

}
