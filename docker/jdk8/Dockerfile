FROM debian:stretch-slim

# MAINTAINER
MAINTAINER cdistc@163.com

RUN mkdir /usr/lib/jvm/

ADD jdk-8u191-linux-x64.tar.gz /usr/lib/jvm

ENV JAVA_HOME /usr/lib/jvm/jdk1.8.0_191
ENV CLASSPATH .:$JAVA_HOME/lib:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar
ENV PATH $PATH:$JAVA_HOME/bin

