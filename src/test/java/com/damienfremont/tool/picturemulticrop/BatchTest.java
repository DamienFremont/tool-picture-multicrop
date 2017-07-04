package com.damienfremont.tool.picturemulticrop;
import org.junit.Test;

import com.damienfremont.tool.picturemulticrop.Batch;

public class BatchTest {

	static String path = System.getProperty("user.dir");

	@Test
	public void single() {
		Batch.main(new String[] { //
//				"--src=src/test/resources/single", //
				"--src=" + path + "\\src\\test\\resources\\single", //
				"--dest=" + path + "\\target\\test-class\\single-result" });
	}

}
