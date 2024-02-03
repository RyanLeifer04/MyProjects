# U-Fund: Public Schools Funding
# Modify this document to expand any and all sections that are applicable for a better understanding from your users/testers/collaborators (remove this comment and other instructions areas for your FINAL release)

An online U-Fund system built in Java 17=> and ___ _replace with other platform requirements_ ___

## Team

- Mohammed Fareed
- Ryan Leifer
- Kenny Casey
- Blizzard Finnegan
- Neav Ziv


## Prerequisites

### Bare-metal configuration
- Java >=17 (Make sure to have correct `JAVA_HOME` setup in your environment)
- Maven
- NodeJS/`npm` >= 18
    - Angular >=16

### Docker configuration
- Docker >=20
- Docker Compose >= 2.0
    - Docker Compose may work properly on older versions, but that is not supported at this time


## How to run it

### Bare-metal Configuration

1. Clone the repository and go to the `ufund-api` directory.
2. Execute `mvn compile exec:java` in the terminal.
3. Move to the `ufund-ui` directory.
4. Execute `npm install && ng serve` in the terminal.
5. Open your browser and navigate to http://localhost:4200

### Container Configuration
In addition to the traditional stack, this project can also be run and/or deployed with a Docker containerization as well.

To start the service, simply run `docker compose up -d` in the root of this repository to start your containers.

To access the new service, open your browser and navigate to http://localhost:8080.

For security purposes, it is recommended that this be put behind a reverse proxy such as NginxProxyManager or Caddy. A simple Caddy example has been provided within `docker-compose.proxy.yml`. Note that your domain and email must both be specified to proxy to a public-facing URL.


## Known bugs and disclaimers
(It may be the case that your implementation is not perfect.)

Document any known bug or nuisance.
If any shortcomings, make clear what these are and where they are located.

## How to test it

The Maven build script provides hooks for run unit tests and generate code coverage
reports in HTML.

To run tests on all tiers together do this:

1. Execute `mvn clean test jacoco:report`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/index.html`

To run tests on a single tier do this:

1. Execute `mvn clean test-compile surefire:test@tier jacoco:report@tier` where `tier` is one of `controller`, `model`, `persistence`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/{controller, model, persistence}/index.html`

To run tests on all the tiers in isolation do this:

1. Execute `mvn exec:exec@tests-and-coverage`
2. To view the Controller tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
3. To view the Model tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
4. To view the Persistence tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`

*(Consider using `mvn clean verify` to attest you have reached the target threshold for coverage)


## How to generate the Design documentation PDF

1. Access the `PROJECT_DOCS_HOME/` directory
2. Execute `mvn exec:exec@docs`
3. The generated PDF will be in `PROJECT_DOCS_HOME/` directory


## How to setup/run/test program
1. Tester, first obtain the Acceptance Test plan
2. IP address of target machine running the app
3. Execute ________
4. ...
5. ...

## License

MIT License

See LICENSE for details.
