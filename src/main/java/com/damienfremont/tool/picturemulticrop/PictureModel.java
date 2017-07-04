package com.damienfremont.tool.picturemulticrop;

import java.awt.image.BufferedImage;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PictureModel {
	
	// IN
	@NonNull
	private BufferedImage img;
	
	// OUT
	
}
