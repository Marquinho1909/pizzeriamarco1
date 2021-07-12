package sample.functional_logic;

import sample.GUI.ParentController;

import java.util.ArrayList;
import java.util.List;

public abstract class ParentService {
    public List<ParentController> observers = new ArrayList<>();

    public void addObserver(ParentController p) {
        observers.add(p);
    }

    public void notifyObservers() {
        observers.forEach(ParentController::update);
    }

}
