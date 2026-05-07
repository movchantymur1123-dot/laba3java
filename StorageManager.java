import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class StorageManager {
	// мютекс для доступу до сховища
	private final Semaphore accessPermit = new Semaphore(1);
	// семафор для доступу до взяття предмету
	private final Semaphore itemAvailable = new Semaphore(0);
	// семафор для доступу до додавання предмету
	private final Semaphore spotsForNewItems;
	
	public ArrayList<String> storage = new ArrayList<>();
	

	public StorageManager(int storageSize) {
		// ініціалізуємо семафор для доступу до додавання предмету
		spotsForNewItems = new Semaphore(storageSize);
	}
	
	// беремо предмет зі сховища
	public String takeItem () {
		var item = storage.get(0);
		storage.remove(0);
		return item;
	}
	
	// додаємо предмет до сховища
	public void addItem(String item) {
		storage.add(item);
	}
	
	// отримуємо дозвіл на доступ до сховища
	public void getAccessPermit () {
		try {
			accessPermit.acquire();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	public void dropAccessPermit () {
		accessPermit.release();
	}
	
	// отримуємо дозвіл на взяття предмету
	public void getTakeItemPermit () {
		try {
			itemAvailable.acquire();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	// додаємо новий дозвіл на взяття предмету
	public void addNewTakeItemPermit () {
		itemAvailable.release();
	}
	
	// отримуємо дозвіл на додавання предмету
	public void getAddItemPermit() {
		try {
			spotsForNewItems.acquire();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void newAddItemPermit() {
		spotsForNewItems.release();
	}
}
