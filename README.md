# Education Management System

## Pre-requisite

* Java 11 or later.
* PostgreSQL.
* Maven 3.6.3 or later.

## Technology Stack
* Spring Boot 2.3.4.RELEASE
* Spring Data
* Thymeleaf

## Configuration
###### Update /etc/hosts file as follows
```
vim /etc/hosts
<your-host-ip>  auth.qict.org
<your-host-ip>  user.qict.org
<your-host-ip>  education.qict.org
```
###### Update environment variables at ***dev_scripts.sh*** 
```
export JAVA_HOME=<your-jdk-folder-path>
export M2_HOME=<your-maven-folder-path>
export MAVEN_OPTS="-Xms256m -Xmx512m"
export PATH=${JAVA_HOME}/bin:${M2_HOME}/bin:${PATH}

```

###### Update database variables at ***dev_scripts.sh*** 
```
export EDUCATION_DB_URL=jdbc:postgresql://<your-host>:<db-port>/<db-name>
export EDUCATION_DB_USER=<your-db-user>
export EDUCATION_DB_PASS=<your-db-user-pass>
```

###### Update nginx proxy at ***nginx.conf*** 
```
upstream education.qict.org {
	server <your-host-ip>:8141 weight=5;
}

server { # simple load balancing
	server_name education.qict.org;
	access_log /tmp/education.log;
	proxy_read_timeout 600s;
	location / {
		proxy_pass http://education.qict.org;
	}
}

```

## Run project
Run script
```
source dev_scripts.sh
```
##
Now go to
```
http://education.qict.org/
