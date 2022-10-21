package de.upb.crc901.otftestbed.admin_client.impl;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import de.upb.crc901.otftestbed.admin_client.generated.spring_server.api.AdminApiDelegate;
import de.upb.crc901.otftestbed.admin_client.generated.spring_server.model.OtfProviderDescription;
import de.upb.crc901.otftestbed.commons.admin.ExecutorDescription;
import de.upb.crc901.otftestbed.commons.admin.ExecutorLogs;
import de.upb.crc901.otftestbed.commons.admin.ListOfExecutors;
import de.upb.crc901.otftestbed.commons.admin.ListOfOtfProviders;
import de.upb.crc901.otftestbed.commons.admin.RequestDescription;
import de.upb.crc901.otftestbed.commons.admin.ServiceDescription;
import de.upb.crc901.otftestbed.commons.rest.SimpleJSONMessage;
import de.upb.crc901.otftestbed.commons.spawner.ListOfServices;
import de.upb.crc901.otftestbed.kubernetes_spawner.generated.java_client.api.ExecutorSpawnerApi;

@Component
public class AdminControllerDelegate implements AdminApiDelegate {

    private ExecutorSpawnerApi executorSpawnerApi;

    public AdminControllerDelegate() {
    	this(new ExecutorSpawnerApi());
    }

//    @Autowired
    public AdminControllerDelegate(ExecutorSpawnerApi executorSpawnerApi) {
    	executorSpawnerApi.getApiClient().setBasePath("http://kube-master.cs.upb.de:31527/api");
    	this.executorSpawnerApi = executorSpawnerApi;
    }

    @Override
    public ResponseEntity<String> createExecutor(ExecutorDescription body) {
        //TODO: If the Placeholder of the ExecutorDescription is changed, we have to handle it here
        SimpleJSONMessage simpleJSONMessage = new SimpleJSONMessage();
        try {
            simpleJSONMessage = executorSpawnerApi.spawnExecutor(body.getListOfServices());
        } catch (RestClientException e) {
            log.error("Problem by using the Api ",e);
        }
        return ResponseEntity.ok(simpleJSONMessage.getMessage());
    }

    @Override
    public ResponseEntity<String> createOtfProvider(OtfProviderDescription body) {

        return null;
    }

    @Override
    public ResponseEntity<ServiceDescription> getService(String serviceId) {
        return null;
    }


    @Override
    public ResponseEntity<Void> deleteExecutor(UUID executorId) {
        try {
            executorSpawnerApi.destroyExecutor(executorId);
        } catch (RestClientException e) {
            log.error("Problem by using the Api ",e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteExecutorLogs(UUID executorId) {
        try {
            executorSpawnerApi.deleteLogs(executorId);
        } catch (RestClientException e) {
            log.error("Problem by using the Api ",e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteOtfProvider(String otfpUuid) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteRequest(String otfpUuid, String requestId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteService(String serviceId) {
        return null;
    }

    @Override
    public ResponseEntity<ExecutorDescription> getExecutor(UUID executorId) {
        try {
            return ResponseEntity.ok(executorSpawnerApi.getExecutor(executorId));
        } catch (RestClientException e) {
            log.error("Problem by using the Api ",e);
        }
        return null;
    }

    @Override
    public ResponseEntity<ExecutorLogs> getExecutorLogs(UUID executorId) {
        try {
            return ResponseEntity.ok(executorSpawnerApi.getLogs(executorId));
        } catch (RestClientException e) {
            log.error("Problem by using the Api ",e);
        }
        return null;
    }

    @Override
    public ResponseEntity<ListOfExecutors> getExecutors() {
        try {
            return ResponseEntity.ok(executorSpawnerApi.getExecutors());
        } catch (RestClientException e) {
            log.error("Problem by using the Api ",e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OtfProviderDescription> getOtfProvider(String otfpUuid) {
        return null;
    }

    @Override
    public ResponseEntity<ListOfOtfProviders> getOtfProviders() {
        return null;
    }

    @Override
    public ResponseEntity<RequestDescription> getRequest(String otfpUuid, String requestId) {
        return null;
    }


    @Override
    public ResponseEntity<ListOfServices> getServices() {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateExecutor(ExecutorDescription body, UUID executorId) {
        try {
            executorSpawnerApi.updateExecutor(body, executorId);
        } catch (RestClientException e) {
            log.error("Problem by using the Api ",e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateOtfProvider(OtfProviderDescription body, String otfpUuid) {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateRequest(RequestDescription body, String otfpUuid, String requestId) {
        return null;
    }

}
