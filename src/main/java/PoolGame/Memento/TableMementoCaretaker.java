package PoolGame.Memento;

import java.util.ArrayList;
import java.util.List;

public class TableMementoCaretaker {

    private List<TableMemento> timeMachine = new ArrayList<>();

    public TableMemento getSavedState() {
        return this.timeMachine.get(0);
    }

    public void updateMemento() {
        // if the game is not at the initial state
        if (this.timeMachine.size() > 1) {
            this.timeMachine.set(0, this.timeMachine.get(1));
        }
    }

    public void addMemento(TableMemento newTimeMachine) {
        if (this.timeMachine.size() > 1) {
            // at this point timeMachine.get(0) is the previous state, while 1 is the current state.
            // we will call updateMemento() when the current state is becoming the previous state.
            this.timeMachine.set(1, newTimeMachine);
        } else {
            // if the game have just started
            this.timeMachine.add(newTimeMachine);
        }
    }
}
