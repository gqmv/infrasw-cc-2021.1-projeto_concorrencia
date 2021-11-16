import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class Counter extends Thread{
    private Condition playPressedCondition;
    private Player player;

    public Counter(Condition playPressedCondition, Player player){
        super();
        this.playPressedCondition = playPressedCondition;
        this.player = player;
    }

    @Override
    public void run(){
        try{
            while(true){
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
}

