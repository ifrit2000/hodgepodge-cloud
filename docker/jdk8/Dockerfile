FROM debian:buster-slim

# MAINTAINER
MAINTAINER cdistc@163.com

ENV JAVA_HOME /usr/lib/jvm/jdk1.8.0_221
ENV CLASSPATH .:$JAVA_HOME/lib:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar
ENV PATH $PATH:$JAVA_HOME/bin

RUN mkdir /usr/lib/jvm/

ADD jdk-8u221-linux-x64.tar.gz /usr/lib/jvm
