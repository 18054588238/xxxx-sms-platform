FROM swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/adoptopenjdk/openjdk8:latest
# 将Jenkins工作空间中的sms-api.jar复制到镜像中的/usr/local目录。
COPY sms-api.jar /usr/local
WORKDIR /usr/local
CMD java -jar sms-api.jar