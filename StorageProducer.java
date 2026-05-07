import java.util.concurrent.CountDownLatch;

public class StorageProducer implements Runnable {
	private final int itemAmount;
	private final StorageManager manager;
	// countdownlatch - це механізм синхронізації, який дозволяє одному або кільком потокам чекати, поки інший потік завершить певну операцію
	private final CountDownLatch latch;
	
	// конструктор 
	public StorageProducer (StorageManager manager, int itemAmount, CountDownLatch latch) {
		this.manager = manager;
		this.itemAmount = itemAmount;

		// CountDownLatch для очікування завершення потоку
		this.latch = latch;
		new Thread(this).start();
	}
	
	@Override
	public void run () {
		for (int i = 1; i <= itemAmount; i++) {
		
			// отримуємо дозвіл на додавання предмету
			manager.getAddItemPermit();
			// отримуємо дозвіл на доступ до сховища
			manager.getAccessPermit();
			
			// додаємо предмет
			manager.addItem("item " + i);
			System.out.println(this + " added item " + i);

			// звільняємо дозвіл на доступ до сховища
			manager.dropAccessPermit();
			// додаємо новий дозвіл на взяття предмету
			manager.addNewTakeItemPermit();
		}
		// зменшуємо лічильник CountDownLatch
		latch.countDown();
	}
}
