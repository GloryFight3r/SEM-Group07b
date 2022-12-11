package pizzeria.order.domain.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pizzeria.order.domain.order.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    private final transient StoreRepository storeRepo;

    @Autowired
    public StoreService(StoreRepository storeRepo){
        this.storeRepo = storeRepo;
    }

    public Store addStore(Store store) throws StoreAlreadyExistException {
        if (store == null || storeRepo.existsById(store.getId())) {
            throw new StoreAlreadyExistException();
        }

        // TODO validate location attribute

        return storeRepo.save(store);
    }

    public boolean editStore(Store store) throws StoreDoesNotExistException {
        Optional<Store> optionalStore = storeRepo.findById(store.getId());

        if (optionalStore.isEmpty()) {
            throw new StoreDoesNotExistException();
        }

        storeRepo.save(store);
        return true;
    }

    public boolean deleteStore(Long storeId) throws StoreDoesNotExistException {
        Optional<Store> optionalStore = storeRepo.findById(storeId);

        if (optionalStore.isEmpty()) {
            throw new StoreDoesNotExistException();
        }

        return storeRepo.deleteStoreById(storeId) != 0;
    }

    public static class StoreDoesNotExistException extends Exception {
        @Override
        public String getMessage(){
            return "The store with the id provided does not exist";
        }
    }

    public static class StoreAlreadyExistException extends Exception {
        @Override
        public String getMessage(){
            return "There already exists a store with the same id";
        }
    }
}
