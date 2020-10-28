import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class BoundedBuffer implements MessageBuffer<String> {

    private ConcurrentLinkedQueue<String> queue;
    private final Semaphore availableSpots;
    private final Semaphore availableItems;


    public BoundedBuffer(int bound) {
        queue = new ConcurrentLinkedQueue<String>();
        availableSpots = new Semaphore(bound);
        availableItems = new Semaphore(0);
    }

    @Override
    public String receiveMessage() throws InterruptedException {
        availableItems.acquire();
        var  element = queue.poll();
        availableSpots.release();
        return element;
    }

	@Override
	public void sendMessage(String element) throws InterruptedException {
        availableSpots.acquire();
        queue.add(element);
        availableItems.release();
		
	}
}

