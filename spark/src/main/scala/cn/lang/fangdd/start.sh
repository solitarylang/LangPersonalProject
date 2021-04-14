#!/bin/bash
source /opt/Bigdata/client/bigdata_env

echo "starting execute ..."

#spark-submit \
#  --class cn.lang.fangdd.DownloadDataFromHive \
#  --master yarn \
#  --deploy-mode client \
#  hdfs://hacluster/tmp/compare/spark-1.0-SNAPSHOT.jar

beeline -u 'jdbc:hive2://10.50.23.212:10000/ods;auth=noSasl' -n root -e "show tables;"

echo "finishing downlaod ..."

spark-submit \
  --class cn.lang.fangdd.DataCompareMainClass \
  --master yarn \
  --deploy-mode client \
  hdfs://hacluster/tmp/compare/spark-1.0-SNAPSHOT.jar

echo "ending execute ..."