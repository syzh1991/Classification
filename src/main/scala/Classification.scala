import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint

/**
 * Created by zhang on 2015/10/28.
 */
object Classification extends App {

  System.setProperty("hadoop.home.dir", "C:\\Users\\zhang\\Downloads\\hadoop")
  System.setProperty("HADOOP_USER_NAME", "root")

  val conf = new SparkConf()
    .setMaster("local")
    .setAppName("Classification")

  val sc = new SparkContext(conf)

  val trainDataPath = "hdfs://10.214.0.151:8020/user/syzh/gaofen/classification/label.txt"
  val testDataPath = "hdfs://10.214.0.151:8020/user/syzh/gaofen/GF1_PMS1_E119.0_N31.9_20130712_L2A0000121522-MSS1.txt"
  //my raw data
  // line -> user,featureCategoricalOne,fTwo,featureCategoricalThree,label
  val trainData = sc.textFile(trainDataPath)
    .map(line =>{
    val values = line.split(",")
    val featureVector = Vectors.dense(values.slice(0, values.length-1).map(_ .toDouble))
    val label = values(values.length-1).toInt
    LabeledPoint(label, featureVector)
  })
  trainData.cache()

  val testData = sc.textFile(testDataPath)
    .map(line =>{
    val values = line.split(",")
    val featureVector = Vectors.dense(values.slice(0, values.length).map(_ .toDouble))
    featureVector
  })
  testData.cache()

  //train data for training set, test data for valuating the model
  //val Array(trainData, testData) = rawData.randomSplit(Array(0.8, 0.2))
  //trainData.cache()
  //testData.cache()


  //tell DecisionTree the number of values of categorical feature
  val featureMap = Map[Int, Int]()

  val model = org.apache.spark.mllib.tree.DecisionTree.trainClassifier(trainData, 6, featureMap, "gini", 10, 100)

  val predict = testData.map(point => model.predict(point))
  predict.saveAsTextFile("hdfs://10.214.0.151:8020/user/syzh/gaofen/classification/out")

}
