# Backend is built using Maven
FROM maven:3.9.5 as backend-build
#FROM maven:3.9.5
# Specify the working directory within the build docker container
WORKDIR /app
#Copy the ufund-api folder in the current directory to the docker container
COPY . /app/

# RUN ls -lah
# 
# RUN mvn compile
# 
# ENTRYPOINT ["mvn"]
# CMD ["exec:java"] 
#Compile the project into a Java ARchive (.jar file)
RUN mvn package

#OpenJDK containers are officially deprecated as per their overview page
#Their dockerhub page suggests (among others) eclipse-temurin as an alternative
#Using OpenJDK (or similar) as the base container results in smaller shipped container, 
#with lower attack surface
FROM eclipse-temurin:17-jre 
#Set working directory in shipped container
WORKDIR /app
#Copy jar file from build container to the shipping container
COPY --from=backend-build /app/target/ufund-api-3.0.0.jar /app/ufund-api-3.0.0.jar

RUN ls -lah /app
#Open port 8080, to allow listening to REST communication
EXPOSE 8080
#Set runtime command to explicitly run the jar file
ENTRYPOINT ["java"]
CMD ["-jar", "/app/ufund-api-3.0.0.jar"]
