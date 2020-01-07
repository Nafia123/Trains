package model;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Train implements Iterable<Wagon>{
    private Locomotive engine;
    private Wagon firstWagon;
    private String destination;
    private String origin;
    private int numberOfWagons;

    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;
    }

    public Wagon getFirstWagon() {
        return firstWagon;
    }

    public void setFirstWagon(Wagon firstWagon) {
        this.firstWagon = firstWagon;
    }



    public int getNumberOfWagons() {
        return numberOfWagons;
    }


    /* three helper methods that are usefull in other methods */

    public boolean hasNoWagons() {
        return (firstWagon == null);
    }

    public boolean isPassengerTrain() {
        return firstWagon instanceof PassengerWagon;
    }

    public boolean isFreightTrain() {
        return firstWagon instanceof FreightWagon;
    }

    public void resetNumberOfWagons() {
       /*  when wagons are hooked to or detached from a train,
         the number of wagons of the train should be reset
         this method does the calculation */

       if(getFirstWagon() == null){
           numberOfWagons = 0;
       }else {
           numberOfWagons = getFirstWagon().getNumberOfWagonsAttached();
       }
    }

    public int getPositionOfWagon(int wagonId) {
        // find a wagon on a train by id, return the position (first wagon had position 1)
        // if not found, than return -1
        int index = 1;
        Wagon wagon = firstWagon;
        do {
            if(wagon.getWagonId() == wagonId){
                return index;
            }
            wagon = wagon.getNextWagon();
            index++;
        }
        while(wagon.hasNextWagon());
        if(wagon.getWagonId() == wagonId){
            return index;
        }
        return -1;
    }


    public Wagon getWagonOnPosition(int position) throws IndexOutOfBoundsException {
        /* find the wagon on a given position on the train
         position of wagons start at 1 (firstWagon of train)
         use exceptions to handle a position that does not exist */
        if(position < numberOfWagons || position > 1){
            Wagon wagon = firstWagon;
            int index = 1;
            if(position == index){
                return wagon;
            }
            while(wagon.hasNextWagon()){
                wagon = firstWagon.getNextWagon();
                if(position == index){
                    return wagon;
                }
                index++;
            }
        }
        throw new IndexOutOfBoundsException("OOPS");
    }

    public int getNumberOfSeats() {
        /* give the total number of seats on a passenger train
         for freight trains the result should be 0 */
        int amount = 0;
        if(isPassengerTrain()){
            PassengerWagon wagon = (PassengerWagon) firstWagon;
            while (wagon.hasNextWagon()){
                wagon = (PassengerWagon) wagon.getNextWagon();
                amount += wagon.getNumberOfSeats();
            }
            amount += wagon.getNumberOfSeats();
            return amount;
        }else{
            return amount;
        }
    }

    public int getTotalMaxWeight() {
        /* give the total maximum weight of a freight train
         for passenger trains the result should be 0 */
        int weight = 0;
        if(isFreightTrain()){
            FreightWagon wagon = (FreightWagon) firstWagon;
            while (wagon.hasNextWagon()){
                wagon = (FreightWagon) wagon.getNextWagon();
                weight += wagon.getMaxWeight();
            }
            return weight;
        }else{
            return weight;
        }
    }

    public Locomotive getEngine() {
        return engine;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(engine.toString());
        Wagon next = this.getFirstWagon();
        while (next != null) {
            result.append(next.toString());
            next = next.getNextWagon();
        }
        result.append(String.format(" with %d wagons and %d seats from %s to %s", numberOfWagons, getNumberOfSeats(), origin, destination));
        return result.toString();
    }

    @Override
    public Iterator iterator() {
        return new CustomIterator<>(this.getFirstWagon());
    }

    @Override
    public void forEach(Consumer action) {
        for (Object o : this) {
            action.accept(o);
        }
    }

    @Override
    public Spliterator spliterator() {
        return null;
    }


    class CustomIterator<Wagon> implements Iterator<Wagon> {
        Wagon current;

        public CustomIterator(Wagon wagon){
            current = wagon;
        }

        // Checks if the next element exists
        public boolean hasNext() {
            return current != null;
        }

        // moves the cursor/iterator to next element
        public Wagon next() {
            current = current.getNextWagon();
            return current;
        }

        // Used to remove an element. Implement only if needed
        public void remove() {
            // Default throws UnsupportedOperationException.
            throw new UnsupportedOperationException();
        }
    }

}
