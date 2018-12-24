package com.nurupo.movie.recommend.bean


import org.apache.spark.sql.SparkSession
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class Spark {
  @Bean
  def sparkSession(): SparkSession = SparkSession.builder.master("local[4]").appName("JavaALSExample").getOrCreate
}
