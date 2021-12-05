import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class Counter extends Thread{
    private Condition playPressedCondition;
    private Player player;
    private boolean stopRequested;

    public Counter(Condition playPressedCondition, Player player){
        super();
        this.playPressedCondition = playPressedCondition;
        this.player = player;
        this.stopRequested = false;
    }

    @Override
    public void run(){
        try{
            while(true){
            if (this.stopRequested){
                return;
            }
            Thread.sleep(1000);
            this.player.lock.lock();
            while (this.player.isPlaying == false){
                this.playPressedCondition.await();
            }

            player.updateTime();
            this.player.lock.unlock();
            }
        } catch (InterruptedException e){ }
    }

    public void requestStop(){
        this.stopRequested = true;
    }
}

