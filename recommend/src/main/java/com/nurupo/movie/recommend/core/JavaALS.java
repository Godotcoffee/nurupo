package com.nurupo.movie.recommend.core;

import com.nurupo.movie.recommend.dao.UserRepository;
import com.nurupo.movie.recommend.entity.User;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// $example on$
// $example off$
@Component
public class JavaALS {

    @Autowired
    private SparkSession spark;

    // $example on$
    public static class Rating implements Serializable {
        private int userId;
        private int movieId;
        private float rating;
        private long timestamp;

        public Rating() {}

        public Rating(int userId, int movieId, float rating, long timestamp) {
            this.userId = userId;
            this.movieId = movieId;
            this.rating = rating;
            this.timestamp = timestamp;
        }

        public int getUserId() {
            return userId;
        }

        public int getMovieId() {
            return movieId;
        }

        public float getRating() {
            return rating;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public static Rating parseRating(String str) {
            String[] fields = str.split("::");
            if (fields.length != 4) {
                throw new IllegalArgumentException("Each line must contain 4 fields");
            }
            int userId = Integer.parseInt(fields[0]);
            int movieId = Integer.parseInt(fields[1]);
            float rating = Float.parseFloat(fields[2]);
            long timestamp = Long.parseLong(fields[3]);
            return new Rating(userId, movieId, rating, timestamp);
        }
    }
    // $example off$

    public List<User> getRecommend() throws Exception {
        List<User> list=new ArrayList<>();

        Resource resource = new ClassPathResource("data/sample_movielens_ratings.txt");
        // $example on$
        JavaRDD<Rating> ratingsRDD = spark
                .read().textFile(resource.getFile().getAbsolutePath()).javaRDD()
                .map(Rating::parseRating);
        Dataset<Row> ratings = spark.createDataFrame(ratingsRDD, Rating.class);
        Dataset<Row>[] splits = ratings.randomSplit(new double[]{0.8, 0.2});
        Dataset<Row> training = splits[0];
        Dataset<Row> test = splits[1];

        // Build the recommendation model using ALS on the training data
        ALS als = new ALS()
                .setMaxIter(5)
                .setRegParam(0.01)
                .setUserCol("userId")
                .setItemCol("movieId")
                .setRatingCol("rating");
        ALSModel model = als.fit(training);

        // Evaluate the model by computing the RMSE on the test data
        // Note we set cold start strategy to 'drop' to ensure we don't get NaN evaluation metrics
        model.setColdStartStrategy("drop");
        Dataset<Row> predictions = model.transform(test);

        RegressionEvaluator evaluator = new RegressionEvaluator()
                .setMetricName("rmse")
                .setLabelCol("rating")
                .setPredictionCol("prediction");
        Double rmse = evaluator.evaluate(predictions);
        System.out.println("Root-mean-square error = " + rmse);

        // Generate top 10 movie recommendations for each user
        Dataset<Row> userRecs = model.recommendForAllUsers(10);

        Dataset<Row> recMovie = userRecs.withColumn("movierat", functions.explode(functions.col("recommendations")))
                .withColumn("movieId", functions.col("movierat").getField("movieId"))
                .withColumn("rating", functions.col("movierat").getField("rating"))
                .drop(functions.col("recommendations"))
                .drop(functions.col("movierat"));
//        recMovie.show();

        Iterator<Row> iterator=recMovie.toLocalIterator();
        while(iterator.hasNext()){
            Row row = iterator.next();
            User user=new User();
            String userId= row.get(0).toString();
            System.out.println(userId);
            String movieId= row.get(1).toString();
            user.setUserId(userId);
            user.setMovieId(movieId);
           list.add( user);
        }
        return list;
    }
}