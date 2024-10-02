package diningphilosophers;

public class ChopStick {
    private static int stickCount = 0;
    private boolean iAmFree = true;
    private final int myNumber;

    public ChopStick() {
        myNumber = ++stickCount;
    }

    synchronized public boolean take() throws InterruptedException {
        long timeout=100;
        if (!iAmFree) {
            wait(timeout);
            if (!iAmFree) {
                return false;
            }
        }
        // assert iAmFree;
        iAmFree = false;
        //System.out.println("baguette " + myNumber + " prise");
        // Pas utile de faire notifyAll ici, personne n'attend qu'elle soit occupée
        return true;
    }

    synchronized public void release() {
        // assert !iAmFree;
        System.out.println("baguette " + myNumber + " relâchée");
        iAmFree = true;
        notifyAll(); // On prévient ceux qui attendent que la baguette soit libre
    }

   @Override
    public String toString() {
        return "baguette #" + myNumber;
    }
    
}
