import java.util.concurrent.CountDownLatch;

public class StorageConsumer implements Runnable {
	private final int itemsAmount;
	private final StorageManager manager;
	// countdownlatch - це механізм синхронізації, який дозволяє одному або кільком потокам чекати, поки інший потік завершить певну операцію
	private final CountDownLatch latch;
	

	// конструктор
	public StorageConsumer (StorageManager manager, int itemsAmount, CountDownLatch latch) {
		this.manager = manager;
		this.itemsAmount = itemsAmount;
		this.latch = latch;
		new Thread(this).start();
	}
	
	@Override
	public void run () {
		for (int i = 0; i < itemsAmount; i++) {
			String item;
			// отримуємо дозвіл на взяття предмету
			manager.getTakeItemPermit();
			// отримуємо дозвіл на доступ до сховища
			manager.getAccessPermit();
			
			// беремо предмет
			item = manager.takeItem();
			System.out.println(this + " taken item : " + item);
			
			// звільняємо дозвіл на доступ до сховища
			manager.dropAccessPermit();
			// додаємо новий дозвіл на додавання предмету
			manager.newAddItemPermit();
		}
		// зменшуємо лічильник CountDownLatch
		latch.countDown();
	}
}
