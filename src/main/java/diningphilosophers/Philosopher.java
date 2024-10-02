package diningphilosophers;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Philosopher extends Thread {
    private final static int delai = 1000;
    private final ChopStick myLeftStick;
    private final ChopStick myRightStick;
    private boolean running = true;

    public Philosopher(String name, ChopStick left, ChopStick right) {
        super(name);
        myLeftStick = left;
        myRightStick = right;
    }

    private void think() throws InterruptedException {
        System.out.println("M."+this.getName()+" pense... ");
        sleep(delai+new Random().nextInt(delai+1));
        System.out.println("M."+this.getName()+" arrête de penser");
    }

    private void eat() throws InterruptedException {
        System.out.println("M."+this.getName() + " mange...");
        sleep(delai+new Random().nextInt(delai+1));
        //System.out.println("M."+this.getName()+" arrête de manger");
    }

    @Override
    public void run() {
        while (running) {
            try {
                think();
                boolean leftTaken = false;
                boolean rightTaken = false;

                // Aléatoirement prendre la baguette de gauche puis de droite ou l'inverse
                switch (new Random().nextInt(2)) {
                    case 0:
                        leftTaken = myLeftStick.take();
                        if (leftTaken) {
                            think(); // pour augmenter la probabilité d'interblocage
                            rightTaken = myRightStick.take();
                        }
                        break;
                    case 1:
                        rightTaken = myRightStick.take();
                        if (rightTaken) {
                            think(); // pour augmenter la probabilité d'interblocage
                            leftTaken = myLeftStick.take();
                        }
                }

                if (leftTaken && rightTaken) {
                    eat();
                    myLeftStick.release();
                    myRightStick.release();
                } else {
                    if (leftTaken) {
                        myLeftStick.release();
                    }
                    if (rightTaken) {
                        myRightStick.release();
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger("Table").log(Level.SEVERE, "{0} Interrupted", this.getName());
            }
        }
    }

    // Permet d'interrompre le philosophe "proprement" :
    // Il relachera ses baguettes avant de s'arrêter
    public void leaveTable() {
        running = false;
    }

}
