To run the application to follow the next steps:
```
1. If spark installed:
  a. Download tgz archive: 
    wget https://github.com/ilja2209/allert-buttons-analyzer/releases/download/1.0/warningbuttontracker-1.0.tgz
  b. Unpack: 
    tar -xzf warningbuttontracker-1.0.tgz
  c. Specify environment variable SPARK_HOME:
    export SPARK_HOME=<path to your spark folder. For example: /root/spark-3.0.1-bin-hadoop2.7>
  d. Execute script run.sh:
    ./run.sh
  e. As soon as the application has run the results will be saved in the results.csv file in the same directory where the script is located. 
    Also the results will be shown in the terminal
    
2. If spark is not installed:
  a. Download spark:
    wget https://apache-mirror.rbc.ru/pub/apache/spark/spark-3.0.1/spark-3.0.1-bin-hadoop2.7.tgz
  b. Unpack spark:
    tar -zxvf spark-3.0.1-bin-hadoop2.7.tgz
  c. Complete steps a-e from 1 part
  
3. To run from sources:
  a. Clone the repository:
    git clone https://github.com/ilja2209/allert-buttons-analyzer.git
  b. Go to the project directory:
    cd allert-buttons-analyzer
  c. Run tests:
    sbt test
```
    
