package com.greenvest.patterns.observer;


import java.util.ArrayList;
import java.util.List;

public class AlertService implements Subject {

    private List<Observer> observers = new ArrayList<>();

    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String msg) {
        for (Observer o : observers) o.update(msg);
    }
}