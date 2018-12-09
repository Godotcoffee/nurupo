package com.nurupo.movie.spark.bean

import org.apache.spark.SparkContext
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class Spark {
  @Bean
  def sc() = new SparkContext("local[2]", "movie")
}
