package com.damienfremont.tool.picturemulticrop;

import java.awt.image.BufferedImage;
import java.util.List;

import boofcv.struct.feature.Match;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PictureModel {

	// 1
	@NonNull
	public String path;

	// 2
	public List<Match> matchs;
	public Double ratio;

	// 3
	public List<Rect> crops;

	// 4
	public List<Dest> dests;

	@Data
	@AllArgsConstructor
	public static class Rect {
		public int x;
		public int y;
		public int w;
		public int h;
	}

	@Data
	@AllArgsConstructor
	public static class Dest {
		public BufferedImage img;
	}
}
