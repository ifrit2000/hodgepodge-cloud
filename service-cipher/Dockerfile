FROM cd871127/jdk11:v1

# MAINTAINER
MAINTAINER cdistc@163.com

RUN groupadd -r app && useradd -r -g app app
RUN mkdir -p /data/conf && mkdir -p /data/log
ADD build/libs/service-cipher.jar /data
RUN chown -R app:app /data


VOLUME ["/data/conf","/data/log"]

WORKDIR /data

EXPOSE 8080

CMD ["java","-jar","service-cipher.jar"]