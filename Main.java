import java.util.concurrent.CountDownLatch;

public class Main {
	public static void main (String[] args) {
		Main main = new Main();
		
		int storageSize = 5;
		int totalItems = 100;
		int amountProducers = 5;
		int amountConsumers = 10;
		
		
		main.starter(storageSize, totalItems, amountProducers, amountConsumers);
	}
	
	private void starter(int storageSize, int totalItems, int amountProducers, int amountConsumers) {
		StorageManager manager = new StorageManager(storageSize);
		
		// CountDownLatch для очікування завершення всіх потоків
		int totalThreads = amountProducers + amountConsumers;
		CountDownLatch latch = new CountDownLatch(totalThreads);
		
		// кількість предметів для виробників
		int itemsPerProducer = totalItems / amountProducers;
		int producerRemainder = totalItems % amountProducers;
		
		// кількість предметів для споживачів
		int itemsPerConsumer = totalItems / amountConsumers;
		int consumerRemainder = totalItems % amountConsumers;
		
		System.out.println("=== Storage System Started ===");
		System.out.println("Storage size: " + storageSize);
		System.out.println("Total items: " + totalItems);
		System.out.println("Producers: " + amountProducers + " (each: " + itemsPerProducer + " items)");
		System.out.println("Consumers: " + amountConsumers + " (each: " + itemsPerConsumer + " items)");
		System.out.println("==============================\n");
		
		// створюємо споживачів
		for (int i = 0; i < amountConsumers; i++) {
			int items = itemsPerConsumer + (i < consumerRemainder ? 1 : 0);
			new StorageConsumer(manager, items, latch);
		}
		
		// створюємо виробників
		for (int i = 0; i < amountProducers; i++) {
			int items = itemsPerProducer + (i < producerRemainder ? 1 : 0);
			new StorageProducer(manager, items, latch);
		}
		
		try {
			// вбиваємо головний потік, поки всі потоки не завершаться
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("\n=== All items processed. Program terminated. ===");
	}
}