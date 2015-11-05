# Classification

1 Classification.scala完成的是对样本数据训练以及利用生成的模型对遥感影像进行分类的功能
  样本数据为trainDataPath = "hdfs://10.214.0.151:8020/user/syzh/gaofen/classification/label.txt"
  待分类的遥感影像数据为testDataPath = "hdfs://10.214.0.151:8020/user/syzh/gaofen/GF1_PMS1_E119.0_N31.9_20130712_L2A0000121522-MSS1.txt"
  分类后的结果存放在hdfs://10.214.0.151:8020/user/syzh/gaofen/classification/out
  
2 Vec2Pic.java实现将分类后的结果还原成图片的功能，程序写的比较死，需要注意图片的尺寸问题
