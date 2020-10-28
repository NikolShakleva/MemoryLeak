public interface MessageBuffer <T> {
    public void sendMessage(T element) throws InterruptedException;
    public T receiveMessage() throws InterruptedException;
}
