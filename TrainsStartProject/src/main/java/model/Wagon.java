package model;

public abstract class Wagon {
    private int wagonId;
    private Wagon previousWagon;
    private Wagon nextWagon;

    public Wagon(int wagonId) {
        this.wagonId = wagonId;
    }

    public Wagon getLastWagonAttached() {
        Wagon wagon = this;
        while (wagon.hasNextWagon()){
            wagon = wagon.getNextWagon();
        }
        return wagon;
        // find the last wagon of the row of wagons attached to this wagon
        // if no wagons are attached return this wagon
    }

    public void setNextWagon(Wagon wagon) {
        // when setting the next wagon, set this wagon to be previous wagon of next wagon
        if(wagon != null){
            wagon.setPreviousWagon(this);
        }
        nextWagon = wagon;
    }

    public int getNumberOfWagonsAttached() {
        Wagon wagon = getLastWagonAttached();
        int index = 1;
        while(wagon.hasPreviousWagon()){
            wagon = wagon.getPreviousWagon();
            index++;
        }
        return index;
    }


    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    public void setPreviousWagon(Wagon previousWagon) {
        this.previousWagon = previousWagon;
    }

    public Wagon getNextWagon() {
        return nextWagon;
    }

    public int getWagonId() {
        return wagonId;
    }


    public boolean hasNextWagon() {
        return !(nextWagon == null);
    }

    public boolean hasPreviousWagon() {
        return !(previousWagon == null);
    }

    @Override
    public String toString() {
        return String.format("[Wagon %d]", wagonId);
    }
}
