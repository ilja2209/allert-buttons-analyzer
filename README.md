The project analyzes the location of the alert button and calculates the place where the user of the alert button lives. Dataset contains the next columns:
```
device_id - id of user's alarm buttom
timestamp - time when event was fixed
x and y - alert button coordinates
```

In short algorithm looks like:
```
1. Group all records by device_id
2. For each group find geometric median (https://en.wikipedia.org/wiki/Geometric_median)
3. Get results as a dataset with columns: device_id, x, y
```

To run the application follow the next steps:
```
1. If spark is installed:
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
    
