package PoolGame.Memento;

import PoolGame.Items.PoolTable;

public class TableMemento {
    private PoolTable lastPoolTable;

    public TableMemento(PoolTable poolTable) {
        this.lastPoolTable = poolTable;
    }

    public PoolTable getLastState() {
        return this.lastPoolTable;
    }
}
