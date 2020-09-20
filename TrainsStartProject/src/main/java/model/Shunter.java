package model;

public class Shunter {


    /* four helper methods than are used in other methods in this class to do checks */
    private static boolean isSuitableWagon(Train train, Wagon wagon) {
        // trains can only exist of passenger wagons or of freight wagons
        if(train.hasNoWagons()){
            return true;
        }
        if(train.isPassengerTrain()){
            return wagon instanceof PassengerWagon;
        }else {
            return wagon instanceof FreightWagon;
        }
    }

    private static boolean isSuitableWagon(Wagon one, Wagon two) {
        // passenger wagons can only be hooked onto passenger wagons
        if(one instanceof PassengerWagon){
            return two instanceof PassengerWagon;
        }else{
            return two instanceof FreightWagon;
        }
    }

    private static boolean hasPlaceForWagons(Train train, Wagon wagon) {
        // the engine of a train has a maximum capacity, this method checks for a row of wagons
        int totalWagons = wagon.getNumberOfWagonsAttached() + train.getNumberOfWagons();
        return train.getEngine().getMaxWagons() >= totalWagons;
    }

    private static boolean hasPlaceForOneWagon(Train train, Wagon wagon) {
        // the engine of a train has a maximum capacity, this method checks for one wagon
        return hasPlaceForWagons(train,wagon);
    }

    public static boolean hookWagonOnTrainRear(Train train, Wagon wagon) {
         /* check if Locomotive can pull new number of Wagons
         check if wagon is correct kind of wagon for train
         find the last wagon of the train
         hook the wagon on the last wagon (see Wagon class)
         adjust number of Wagons of Train */
         if(hasPlaceForWagons(train,wagon) && isSuitableWagon(train,wagon)){
             if(train.hasNoWagons()){
                 train.setFirstWagon(wagon);
                 train.resetNumberOfWagons();
                 return true;
             }else {
                 Wagon lastWagon = train.getFirstWagon().getLastWagonAttached();
                 lastWagon.setNextWagon(wagon);
                 train.resetNumberOfWagons();
                 return true;
             }
         }
         return false;
    }

    public static boolean hookWagonOnTrainFront(Train train, Wagon wagon) {
        /* check if Locomotive can pull new number of Wagons
         check if wagon is correct kind of wagon for train
         if Train has no wagons hookOn to Locomotive
         if Train has wagons hookOn to Locomotive and hook firstWagon of Train to lastWagon attached to the wagon
         adjust number of Wagons of Train */
        if(hasPlaceForWagons(train,wagon) && isSuitableWagon(train,wagon)){
            if(train.hasNoWagons()){
                train.setFirstWagon(wagon);
                train.resetNumberOfWagons();
                return true;
            }else{
                Wagon lastWagons = train.getFirstWagon();
                train.setFirstWagon(wagon);
                wagon.setNextWagon(lastWagons);
                train.resetNumberOfWagons();
                return true;
            }
        }
        return false;

    }

    public static boolean hookWagonOnWagon(Wagon first, Wagon second) {
        /* check if wagons are of the same kind (suitable)
        * if so make second wagon next wagon of first */
        if(isSuitableWagon(first,second)){
            first.setNextWagon(second);
            return true;
        }
        return false;
    }


    public static boolean detachAllFromTrain(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon with all its successor
         recalculate the number of wagons of the train */
        if(!train.hasNoWagons()){
            if(wagon.hasPreviousWagon()) {
                wagon.getPreviousWagon().setNextWagon(null);
                train.resetNumberOfWagons();
                return true;
            }else{
                train.setFirstWagon(null);
            }

        }
        return false;
    }

    public static boolean detachOneWagon(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon and hook the nextWagon to the previousWagon
         so, in fact remove the one wagon from the train
        */
        if(!train.hasNoWagons()){
            wagon.getPreviousWagon().setNextWagon(wagon.getNextWagon());
            train.resetNumberOfWagons();
            return true;
        }
        return false;

    }

    public static boolean moveAllFromTrain(Train from, Train to, Wagon wagon) {
        /* check if wagon is on train from
         check if wagon is correct for train and if engine can handle new wagons
         detach Wagon and all successors from train from and hook at the rear of train to
         remember to adjust number of wagons of trains */
        if(isSuitableWagon(to,wagon) && hasPlaceForWagons(to,wagon)) {
            for(Wagon fromWagon: from){
                if (fromWagon.getWagonId() == wagon.getWagonId()) {
                    detachAllFromTrain(from,wagon);
                    hookWagonOnTrainRear(to,wagon);
                    return true;
                }
            }
//            Wagon countWagon = from.getFirstWagon();
//            do{
//                if (countWagon.getWagonId() == wagon.getWagonId()) {
//                    detachAllFromTrain(from,wagon);
//                    hookWagonOnTrainRear(to,wagon);
//                    return true;
//                }else{
//                    countWagon = countWagon.getNextWagon();
//                }
//            }
//            while (countWagon.hasNextWagon());
        }
        return false;

    }

    public static boolean moveOneWagon(Train from, Train to, Wagon wagon) {
        // detach only one wagon from train from and hook on rear of train to
        // do necessary checks and adjustments to trains and wagon
        if(isSuitableWagon(to,wagon) && hasPlaceForWagons(to,wagon)) {
            for (Wagon wagonFrom : from) {
                if (wagonFrom.getWagonId() == wagon.getWagonId()) {
                    detachOneWagon(from,wagon);
                    wagon.setNextWagon(null);
                    wagon.setPreviousWagon(null);
                    hookWagonOnTrainRear(to,wagon);
                    return true;
                }else{
                    wagonFrom = wagonFrom.getNextWagon();
                }
            }
        }
        return false;
    }
}
