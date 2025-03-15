# Step 1: Choose a Base Image
FROM openjdk:21

# Step 2: Set the Working Directory
WORKDIR /app

# Step 2: Install Maven
FROM base AS build
RUN apt-get update && apt-get install -y maven

# Step 3: Build the application
RUN mvn clean package -DskipTests


# Step 3: Copy Application Files
COPY target/olx-Login-0.0.1-SNAPSHOT.jar olx-login.jar


# Step 4: Install Dependencies
#RUN pip install --no-cache-dir -r requirements.txt

# Step 5: Expose Ports
EXPOSE 8082

# Step 6: Set Environment Variables
#ENV MAIL_PASSWORD=obrljvofqymhtpbd
#ENV MAIL_USERNAME=habibullashaik9944@gmail.com
#ENV MYSQL_PASSWORD=786786786
#ENV MYSQL_USERNAME=root

# Step 7: Define the Startup Command
CMD ["java", "-jar", "olx-login.jar"]

# Step 8: Add Metadata
#LABEL maintainer="yourname@example.com"
#LABEL version="1.0"