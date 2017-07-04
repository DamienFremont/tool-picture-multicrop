package com.damienfremont.tool.picturemulticrop;
import org.junit.Test;

import com.damienfremont.tool.picturemulticrop.Batch;

public class BatchTest {

	static String path = System.getProperty("user.dir");

	@Test
	public void single() {
		Batch.main(new String[] { //
				"--src=" + path + "\\src\\test\\resources\\usecase-single", //
				"--tpl=" + path + "\\src\\main\\resources\\template.jpg", //
				"--dest=" + path + "\\target\\test-classes\\usecase-single-result" });
	}

}
