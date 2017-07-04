package com.damienfremont.tool.picturemulticrop;
import org.springframework.boot.SpringApplication;

public class Batch {

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run( //
				BatchConfig.class, args)));
	}
}
