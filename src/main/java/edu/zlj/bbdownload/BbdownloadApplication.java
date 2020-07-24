package edu.zlj.bbdownload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.awt.font.NumericShaper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
public class BbdownloadApplication {

	@Value("${thread.nums:10}")
	private int nums;
	public static void main(String[] args) {
		SpringApplication.run(BbdownloadApplication.class, args);
	}
	@Bean
	public ExecutorService getExecutor(){
		return Executors.newFixedThreadPool(nums);
	}
}
