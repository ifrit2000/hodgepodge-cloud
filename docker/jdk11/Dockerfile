FROM debian:stretch-slim

# MAINTAINER
MAINTAINER cdistc@163.com

RUN mkdir /usr/lib/jvm/

ADD jdk-11.0.1_linux-x64_bin.tar.gz /usr/lib/jvm

ENV JAVA_HOME /usr/lib/jvm/jdk-11.0.1
ENV CLASSPATH ./$JAVA_HOME/lib
ENV PATH $PATH:$JAVA_HOME/bin

