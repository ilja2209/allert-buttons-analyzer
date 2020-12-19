#!/bin/sh

if [[ -z "${SPARK_HOME}" ]]; then
  echo Please set environment variable SPARK_HOME
  exit
fi

$SPARK_HOME/bin/spark-submit warningbuttontracker_2.12-0.1.jar