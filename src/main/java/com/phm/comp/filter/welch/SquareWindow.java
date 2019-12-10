/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phm.comp.filter.welch;

import java.util.Arrays;

public class SquareWindow extends Window {

	public SquareWindow(double[] data) {
		super(data);
	}

	public SquareWindow() {
		super();
	}

	@Override
	public double[] get(double windowSize) {
		int windowStart = (int) ((getData().length - windowSize) / 2);
		double[] window = Arrays.copyOfRange(getData(), windowStart, (int) (windowStart + windowSize));
		return window;
	}

	@Override
	public int getRecommandedOverlappingSize(int segmentLength) {
		return segmentLength;
	}

}