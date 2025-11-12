package com.banula.openlib.ocpi.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.File;
import java.io.FileReader;
import java.util.List;

@Slf4j
public class InfoUtils {
    public static String getDependencyVersion(String groupId, String artifactId) {
        File pomFile = new File("pom.xml");
        log.info("Attempting to read pom.xml from: " + pomFile.getAbsolutePath());
        try (FileReader reader = new FileReader(pomFile)) {
            MavenXpp3Reader mavenReader = new MavenXpp3Reader();
            Model model = mavenReader.read(reader);
            List<Dependency> dependencies = model.getDependencies();
            for (Dependency dependency : dependencies) {
                if (dependency.getGroupId().equals(groupId) && dependency.getArtifactId().equals(artifactId)) {
                    return dependency.getVersion();
                }
            }
        } catch (Exception e) {
            log.warn("It was not possible to read dependency version from pom.xml", e);
        }
        return "Unknown";
    }

    public static String getLibVersion(String groupId, String artifactId) {
        return getDependencyVersion(groupId, artifactId);
    }

    public static void logCurlCommand(String url, HttpMethod httpMethod, HttpHeaders headers, String requestBody) {

        StringBuilder curlCommand = new StringBuilder();
        curlCommand.append("curl -X ").append(httpMethod.name());

        // Add all headers
        if (headers != null) {
            headers.forEach((headerName, headerValues) -> {
                for (String headerValue : headerValues) {
                    curlCommand.append(" -H '").append(headerName).append(": ").append(escapeSingleQuotes(headerValue))
                            .append("'");
                }
            });
        }

        // Add request body if present (for POST, PUT, PATCH methods)
        if (requestBody != null && !requestBody.isEmpty() && !"null".equals(requestBody)) {
            curlCommand.append(" -d '").append(escapeSingleQuotes(requestBody)).append("'");
        }

        // Add URL
        curlCommand.append(" '").append(url).append("'");
        log.info("Executing request:\n{}", curlCommand.toString());
    }

    private static String escapeSingleQuotes(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("'", "'\\''");
    }

}
