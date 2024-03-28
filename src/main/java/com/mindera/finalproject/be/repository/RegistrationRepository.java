package com.mindera.finalproject.be.repository;

import com.mindera.finalproject.be.config.DynamoDbConfig;
import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.entity.Registration;
import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.ArrayList;
import java.util.List;

import static com.mindera.finalproject.be.repository.schema.TableSchemas.registrationTableSchema;

@ApplicationScoped
public class RegistrationRepository {

    DynamoDbTable<Registration> table = DynamoDbConfig.client.table("registrationTable", registrationTableSchema);

    public RegistrationRepository() {
        table.createTable();
    }
    public Registration save(Registration registration) {
        table.putItem(registration);
        return registration;
    }

    public List<Registration> getAll(){
       return table.scan().items().stream().toList();
    }

    public Registration getById(String registrationId) {
        return table.getItem(Key.builder().partitionValue(registrationId).build());
    }

    public void deleteById(String registrationId) {
        table.deleteItem(Key.builder().partitionValue(registrationId).build());
    }

    public Registration update(Registration registration) {
        table.updateItem(registration);
        return registration;
    }

    public Registration getByCompositeKey(String compositeKey) {
        return table.getItem(Key.builder().partitionValue(compositeKey).build());
    }

    public void delete(String compositeKey) {
        table.deleteItem(Key.builder().partitionValue(compositeKey).build());
    }

}
