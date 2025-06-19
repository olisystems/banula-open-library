package com.banula.openlib.ocpi.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

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

}
