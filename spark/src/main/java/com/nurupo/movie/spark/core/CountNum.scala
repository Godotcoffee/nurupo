package com.nurupo.movie.spark.core

import org.apache.spark.SparkContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CountNum {
  @Autowired
  var sc: SparkContext = _

  def solve(list: Seq[Integer]): Int = {
    val rdd = sc.parallelize(list)
    rdd.reduce((a, b) => a + b)
  }
}
