# Banula Open Library

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.my-oli/banula-open-library/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.my-oli/banula-open-library)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Introduction

The Banula Open Library is a comprehensive Java library designed to simplify the development of OCPI-compliant and OCN-compliant applications. It provides essential models, validators, mappers, and utilities to accelerate the development of e-mobility solutions.

## Features

### OCPI Module
- Complete set of OCPI 2.2.1 models and DTOs
- Request/response validation utilities
- Exception handling for OCPI-specific errors
- Data mappers for seamless DTO-entity conversion
- Support for all OCPI modules: CDRs, Commands, Credentials, Locations, etc.

### OCN Module
- OCN protocol implementation
- Message signing and verification
- Node communication utilities
- Session management

### Web3 Utilities
- Keccak-256 hashing for blockchain operations
- Smart contract interaction helpers
- Ethereum address validation and formatting

### Smart Locations
- Advanced location-based services
- Geo-spatial calculations
- Smart charging station discovery

## Requirements

- Java 17 or higher
- Spring Boot 3.0.2 or compatible
- Maven 3.6.3 or higher

## Installation

Add the following dependency to your project's `pom.xml`:

```xml
<dependency>
    <groupId>com.my-oli</groupId>
    <artifactId>banula-open-library</artifactId>
    <version>1.0.3</version>
</dependency>
```

For Gradle users, add to your `build.gradle`:

```groovy
implementation 'com.my-oli:banula-open-library:1.0.3'
```

## Quick Start

### OCPI Example

```java
import com.banula.ocpi.model.dto.*;
import com.banula.ocpi.service.OcpiService;

// Initialize OCPI service
OcpiService ocpiService = new OcpiService();

// Create a new location
Location location = new Location();
location.setId("LOC1");
location.setType(LocationType.ON_STREET);
location.setName("Downtown Charging");

// Save location
Location savedLocation = ocpiService.saveLocation(location);
```

### OCN Configuration Example

1. First, configure the OCN client as a Spring Bean:

```java
import com.banula.ocn.client.OcnClient;
import com.banula.ocn.client.OcnClientBuilder;
import com.banula.ocn.client.OcnCredentialHandler;
import com.banula.ocn.model.OcnCredential;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@AllArgsConstructor
public class OcnClientConfig {
    private final MongoTemplate mongoTemplate;
    private final ApplicationConfiguration config;

    @Bean
    public OcnClient ocnClient() {
        String backendUrl = config.getBackendUrl() + config.getApiPrefix() + "/2.2/versions";
        return new OcnClientBuilder()
                .setSupportsSigning(config.isSigningSupported())
                .setFrom(config.getTariffsCountryCode(), config.getTariffsPartyId())
                .setNodeUrl(config.getOcnNodeUrl())
                .setTokenB(config.getTokenB())
                .setAdminKey(config.getOcnAdminKey())
                .setPrivateKey(config.getPartyPrivateKey())
                .setUpdatePartyOnStart(config.isUpdatingParty())
                .setPartyBackendUrl(backendUrl)
                .setOcpiRoles(List.of(config.getRole()))
                .setOcnCredentialHandler(new OcnCredentialHandler() {
                    @Override
                    public OcnCredential getOcnCredential() {
                        // Implementation to fetch credentials from your database
                        return mongoTemplate.findOne(
                            new Query()
                                .addCriteria(Criteria.where("countryCode").is(config.getTariffsCountryCode()))
                                .addCriteria(Criteria.where("partyId").is(config.getTariffsPartyId())),
                            OcnCredential.class,
                            "ocn_credentials"
                        );
                    }

                    @Override
                    public void saveOcnCredential(OcnCredential ocnCredential) {
                        mongoTemplate.save(ocnCredential, "ocn_credentials");
                    }
                })
                .build();
    }
}
```

2. Perform handshake on application startup:

```java
import com.banula.ocn.client.OcnClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OcnStartupListener implements ApplicationListener<ApplicationReadyEvent> {
    private final OcnClient ocnClient;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            ocnClient.shakeHands();
            log.info("OCN handshake completed successfully");
        } catch (Exception ex) {
            log.error("OCN handshake failed: {}", ex.getMessage(), ex);
        }
    }
}
```

3. Sending messages through OCN:

```java
import com.banula.ocn.client.OcnClient;
import com.banula.ocn.client.OcnEndpoints;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ocn")
@RequiredArgsConstructor
public class OcnMessageController {
    private final OcnClient ocnClient;
    private final ApplicationConfiguration config;

    @PostMapping("/send-message")
    public YourResponseType sendMessage(@RequestBody YourRequestType request) {
        return ocnClient.executeOcpiOperation(
            OcnEndpoints.YOUR_ENDPOINT,
            request,
            config.getTargetPartyId(),
            config.getTargetCountryCode(),
            YourResponseType.class,
            HttpMethod.POST,
            new ArrayList<>()
        );
    }
}
```

## Documentation

For detailed documentation, please refer to:
- [OCPI Module Documentation](docs/ocpi/README.md)
- [OCN Module Documentation](docs/ocn/README.md)
- [Web3 Utilities](docs/web3/README.md)
- [Smart Locations](docs/smart-locations/README.md)

## Contributing

We welcome contributions to the Banula Open Library! Here's how you can help:

1. **Report Bugs**: File an issue describing the bug and steps to reproduce.
2. **Suggest Enhancements**: Propose new features or improvements.
3. **Submit Pull Requests**: Follow these steps:
   - Fork the repository
   - Create a feature branch (`git checkout -b feature/amazing-feature`)
   - Commit your changes (`git commit -m 'Add some amazing feature'`)
   - Push to the branch (`git push origin feature/amazing-feature`)
   - Open a Pull Request

### Development Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/olisystems/banula-open-library.git
   cd banula-open-library
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run tests:
   ```bash
   mvn test
   ```

## How to Deploy on Maven Central

### Prerequisites

Before deploying, ensure you have:
- GPG keys configured for signing artifacts
- Maven Central credentials in `~/.m2/settings.xml` with server id `central`
- Incremented the version number in `pom.xml` (Maven Central doesn't allow redeploying the same version)

### Deployment Steps

1. **Update the version** in `pom.xml`:
   ```xml
   <version>0.1.18</version> <!-- Increment from previous version -->
   ```

2. **Deploy to Maven Central** using the `release` profile:
   ```bash
   ./mvnw -P release -DskipTests clean deploy
   ```

   The `release` profile:
   - Skips the fat jar assembly to reduce bundle size
   - Includes only the standard jar, sources, and javadoc
   - Signs all artifacts with GPG
   - Automatically uploads and publishes to Maven Central

3. **Verify deployment**:
   - Check the build output for `BUILD SUCCESS`
   - Look for confirmation: `Deployment was successfully published`
   - The artifact will be available on Maven Central within 15-30 minutes

### Troubleshooting

- **"Broken pipe" errors**: Usually caused by oversized bundles. The `release` profile prevents this by skipping the fat jar.
- **"Already exists" errors**: Increment the version number in `pom.xml`.
- **Authentication errors**: Verify your `~/.m2/settings.xml` has valid credentials for server id `central`.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Support

For support, please contact:
- GitHub Issues: [https://github.com/olisystems/banula-open-library/issues](https://github.com/olisystems/banula-open-library/issues)

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/olisystems/banula-open-library/tags).

## Acknowledgments
- OCPI 2.2.1 Specification
- OCN Protocol Documentation
- All contributors who have helped shape this project

## Authors
- Diego Rosales <diego.rosales@my-oli.com>
- Matheus Rosendo <matheus.rosendo@my-oli.com>
- Elton Saraci <elton.saraci@my-oli.com>
