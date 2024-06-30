package ru.fafurin.publishing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fafurin.publishing.dto.request.CustomerRequest;
import ru.fafurin.publishing.exception.CustomerNotFoundException;
import ru.fafurin.publishing.mapper.CustomerMapper;
import ru.fafurin.publishing.entity.Customer;
import ru.fafurin.publishing.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    /**
     * Получить список всех заказчиков
     * @return список всех заказчиков
     */
    public List<Customer> getAll() {
        List<Customer> customers = repository.findAll();
        return customers.stream()
                .filter(b -> !b.isDeleted())
                .collect(Collectors.toList());
    }

    /**
     * Получить заказчика по идентификатору
     * @param id - идентификатор заказчика
     * @return заказчик или выбрасывается исключение,
     *         если заказчик не найден по идентификатору
     */
    public Customer get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    /**
     * Сохранить нового заказчика, если он не найден по email
     * @param customerRequest - данные заказчика
     * @return новый или существующий заказчик
     */
    public Customer saveIfNotExists(CustomerRequest customerRequest) {
        Customer customer = CustomerMapper.getCustomer(new Customer(), customerRequest);
        return checkCustomerExisting(customer);
    }

    /**
     * Изменить данные существующего заказчика
     * @param id - идентификатор заказчика
     * @param customerRequest - данные для изменения существующего заказчика
     * @return - измененный заказчик или выбрасывается исключение,
     *           если заказчик не найден по идентификатору
     */
    public Customer update(Long id, CustomerRequest customerRequest) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        return repository.save(
                CustomerMapper.getCustomer(customer, customerRequest));
    }

    /**
     * Безопасно удалить заказчика по идентификатору,
     * т.е. задать значение true для поля IsDeleted
     * @param id - идентификатор заказчика
     */
    public void delete(Long id) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        customer.setDeleted(true);
        repository.save(customer);
    }


    /**
     * Вернуть заказчика, если заказчик с таким email существует,
     * или вернуть нового заказчика
     * @param customer - данные заказчика
     * @return новый или существующий заказчик
     */
    private Customer checkCustomerExisting(Customer customer) {
        Optional<Customer> customerOptional = repository.findByEmail(customer.getEmail());
        return customerOptional.orElseGet(() -> repository.save(customer));
    }

}
