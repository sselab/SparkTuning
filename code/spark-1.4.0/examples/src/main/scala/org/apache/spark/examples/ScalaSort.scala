package org.apache.spark.examples

import java.util.Scanner

import org.apache.hadoop.io.{LongWritable,Text}
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.rdd.RDD

import scala.reflect.ClassTag

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


object ScalaSort{


  def main(args: Array[String]){
//    if (args.length != 2){
//      System.err.println(
//        s"Usage: $ScalaSort <INPUT_HDFS> <OUTPUT_HDFS>"
//      )
//      System.exit(1)
//    }
    val sparkConf = new SparkConf().setAppName("ScalaSort").setMaster("local[1]")
    val sc = new SparkContext(sparkConf)
    val zz=sc.newAPIHadoopFile[LongWritable, Text, TextInputFormat]("d:/wordcount4.txt")
    zz.map(_._2.getLength).countByValue();
    zz.map(x=>(x._1.toString,x._2.getLength)).sortByKey().count();


//   val rdd1=sc.textFile("d:/wordcount.txt")
//    //rdd1.count()
//     rdd1.flatMap(line=>line.split(" ")).map((_,1)).sortByKey().count()


    val scanner: Scanner = new Scanner(System.in)
    scanner.nextLine
    sc.stop()
  }
}
