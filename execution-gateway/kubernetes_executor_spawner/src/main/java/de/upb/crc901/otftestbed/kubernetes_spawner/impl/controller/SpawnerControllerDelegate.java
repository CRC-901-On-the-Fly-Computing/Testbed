package de.upb.crc901.otftestbed.kubernetes_spawner.impl.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.commons.admin.ExecutorDescription;
import de.upb.crc901.otftestbed.commons.admin.ExecutorLogs;
import de.upb.crc901.otftestbed.commons.admin.ListOfExecutors;
import de.upb.crc901.otftestbed.commons.rest.SimpleJSONMessage;
import de.upb.crc901.otftestbed.commons.spawner.ListOfServices;
import de.upb.crc901.otftestbed.kubernetes_spawner.generated.spring_server.api.ExecutorSpawnerApiDelegate;
import de.upb.crc901.otftestbed.kubernetes_spawner.impl.config.Config;
import de.upb.crc901.otftestbed.kubernetes_spawner.impl.model.DockerBuildComponent;
import de.upb.crc901.otftestbed.kubernetes_spawner.impl.model.KubernetesComponent;

@Controller
public class SpawnerControllerDelegate implements ExecutorSpawnerApiDelegate {

    private final DockerBuildComponent dockerComponent;

    private final KubernetesComponent kubernetesComponent;


    @Autowired
    public SpawnerControllerDelegate(DockerBuildComponent dockerComponent, KubernetesComponent kubernetesComponent) {
        Assert.notNull(dockerComponent, "dockerComponent must not be null");
        Assert.notNull(kubernetesComponent, "kubernetesComponent must not be null");

        this.dockerComponent = dockerComponent;
        this.kubernetesComponent = kubernetesComponent;
    }


    @Override
    public ResponseEntity<ExecutorDescription> getExecutor(UUID executorId) {
        ExecutorDescription executorDescription = kubernetesComponent.getExecutor(executorId);
        return new ResponseEntity<>(executorDescription, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SimpleJSONMessage> spawnExecutor(ListOfServices listOfServices) {

        UUID executorName = UUID.randomUUID();
        while(!executorName.toString().matches("[a-z]([-a-z0-9]*[a-z0-9])?")) {
            executorName = UUID.randomUUID();
        }

        // Step 1: Generate the config file
        String folderName = generateConfigFile(listOfServices, executorName);
        // Step 2: Build docker file
        String dockerTag = buildDockerFile(folderName, executorName);
        // Step 3: Deploy to Kubernetes
        String kubernetesURL = deployDockerImage(folderName, dockerTag);

        SimpleJSONMessage simpleJSONMessage = new SimpleJSONMessage(kubernetesURL);
        return new ResponseEntity<>(simpleJSONMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> destroyExecutor(UUID executorId) {
        if (kubernetesComponent.isExisting(executorId) && kubernetesComponent.destroyExecutor(executorId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @Override
    public ResponseEntity<ListOfExecutors> getExecutors() {
        return kubernetesComponent.getExecutors();
        //return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateExecutor(ExecutorDescription body, UUID executorId) {
        destroyExecutor(executorId);
        //spawnExecutor(body.getListOfServices());
        return null;
    }

    private String deployDockerImage(String folderName, String dockerTag) {
        return kubernetesComponent.deployDockerImage(folderName, dockerTag);
    }

    private String buildDockerFile(String folderName, UUID executorName) {
        return dockerComponent.buildAndUploadDockerImage(folderName, folderName, executorName.toString());
    }

    private String generateConfigFile(ListOfServices serviceList, UUID executorName) {
        String executorId = executorName.toString();

        try {
            Path configPath = FileSystems.getDefault().getPath("docker_images", executorId, "custom.vars");
            File configFile = configPath.toAbsolutePath().toFile();
            Files.deleteIfExists(configPath);
            if (configFile.getParentFile().mkdirs() && configFile.createNewFile()) {
                StringBuilder sb = new StringBuilder();

                StringJoiner servicesJoined = new StringJoiner("\",\"", "[\"", "\"]");
                servicesJoined.setEmptyValue("[]");
                for (String service : serviceList.getServices()) {
                    servicesJoined.add(service);
                }

                sb.append("SEDE_EXECUTOR_ID").append("='").append(executorName).append("'\n");
                sb.append("SEDE_GROUP_ID").append("='").append(executorName).append("'\n");
                sb.append("SEDE_GATEWAYS").append("='[\"execution-gateway.").append(System.getenv("GATEWAY_NAMESPACE")).append(":8080\"]'\n");
                sb.append("SEDE_SERVICE_STORE_LOCATION").append("='").append(Config.SEDE_INSTANCES_PATH).append("'\n");
                sb.append("SEDE_SERVICES").append("='").append(servicesJoined).append("'\n");

                Files.write(configPath, sb.toString().getBytes());

                FileUtils.copyURLToFile(Objects.requireNonNull(getClass().getClassLoader().getResource("Dockerfile")),
                        FileSystems.getDefault().getPath("docker_images", executorId, "Dockerfile").toFile());
                FileUtils.copyURLToFile(Objects.requireNonNull(getClass().getClassLoader().getResource("init.sh")),
                        FileSystems.getDefault().getPath("docker_images", executorId, "init.sh").toFile());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return executorId;
    }

//    @Override
//    public ResponseEntity<SimpleJSONMessage> deleteLogs(UUID executorId) {
//        return kubernetesComponent.deleteLogs(executorId) ? new ResponseEntity<>(new SimpleJSONMessage("OK"), HttpStatus.OK)
//                : new ResponseEntity<>(new SimpleJSONMessage("Logs couldn't deleted"), HttpStatus.OK);
//    }

    @Override
    public ResponseEntity<ExecutorLogs> getLogs(UUID executorId) {
        return kubernetesComponent.getLogs(executorId);
    }
}
