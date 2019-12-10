/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phm.comp.filter.welch;

import java.util.Arrays;

public class ChunkedData {

    private double[] data;
    private int cursor;
    private int fs;
    private int chunkSize;
    private int numberOfChunk;
    private int overlapSize;
    private int chunkCursor;
    private boolean allowPartialChunks;

	// M = ( 1 - K ) R + N
    // R = ( N - M ) / ( K - 1 )
    // K = floor(((N - M)/ R) + 1)
    public ChunkedData(double[] data, int fs, int chunkSize, int overlapp) {
        initialize(data, fs, chunkSize, overlapp, false);
    }

    public ChunkedData(double[] data, int fs, int chunkSize, int overlapp, boolean allowPartialChunks) {
        initialize(data, fs, chunkSize, overlapp, allowPartialChunks);
    }

    private void initialize(double[] data, int fs, int chunkSize, int overlapp, boolean allowPartialChunks) {
        this.data = data;
        setFs(fs);
        cursor = 0;
        chunkCursor = 0;
        overlapSize = overlapp;
        this.allowPartialChunks = allowPartialChunks;
        setChunkSize(chunkSize); ///< also trigger numberOfChunk computation
    }

    public int getNumberOfChunk() {
        return numberOfChunk;
    }

    public void setNumberOfChunk(int numberOfChunk) {
        this.numberOfChunk = numberOfChunk;
        if (overlapSize > 0) {
            chunkSize = (1 - numberOfChunk) * overlapSize + data.length;
        } else {
            chunkSize = (int) Math.floor((double) data.length / (double) numberOfChunk);
        }
        if (chunkSize < fs) {
            throw new IllegalArgumentException("Impossible to have " + numberOfChunk
                    + " chunks while keeping this overlapping size of " + overlapSize
                    + " and having the chunk size at least greater equal to the sampling rate");
        }
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
        /*
         if(chunkSize < fs) {
         throw new IllegalArgumentException("chunk size have to be at least equal to sampling rate");
         }
         */
        if (overlapSize > chunkSize) {
            throw new IllegalArgumentException("Overlapping size cannot be greater thant chunksize !");
        }
        if (overlapSize > 0) {
            numberOfChunk = (int) Math.floor((((double) data.length - (double) chunkSize) / overlapSize) + (allowPartialChunks ? 1d : 0));
        } else {
            numberOfChunk = (int) Math.floor((double) data.length / (double) chunkSize);
        }
        if (numberOfChunk < 1) {
            throw new IllegalArgumentException("Chunk size too big to have at least one chunk");
        }
    }

    public int getOverlapSize() {
        return overlapSize;
    }

    public void setOverlapSize(int overlapSize) {
        this.overlapSize = overlapSize;
        if (overlapSize > chunkSize) {
            throw new IllegalArgumentException("overlapping cannot be greater than the chunk size");
        }
        if (overlapSize > 0) {
            numberOfChunk = (int) Math.floor((((double) data.length - (double) chunkSize) / overlapSize) + (allowPartialChunks ? 1d : 0));
        } else {
            numberOfChunk = (int) Math.floor((double) data.length / (double) chunkSize);
        }
        if (numberOfChunk < 1) {
            throw new IllegalArgumentException("Chunk size too big to have at least one chunk");
        }
    }

    public double[] getChunk() {
        return getChunk(chunkCursor);
    }

    public double[] getChunk(int chunckIndex) {
        chunkCursor = chunckIndex;
        return Arrays.copyOfRange(data,
                chunckIndex * (chunkSize - overlapSize),
                (chunckIndex * (chunkSize - overlapSize)) + chunkSize);
    }

    public void nextChunk() {
        chunkCursor++;
    }

    public void previousChunk() {
        chunkCursor--;
    }

    public double[] getNextChunk() {
        return getChunk(chunkCursor + 1);
    }

    public double[] getPreviousChunk() {
        return getChunk(chunkCursor - 1);
    }

    private boolean isChunkAt(int chunkIndex) {
        int start = chunkIndex * (chunkSize - overlapSize);
        int end = start + chunkSize;
        if (allowPartialChunks) {
            return (start >= 0 && start < data.length);
        } else {
            return (start >= 0 && end <= data.length);
        }

    }

    public boolean hasNextChunk() {
        return isChunkAt(chunkCursor + 1);
    }

    public boolean hasPreviousChunk() {
        return isChunkAt(chunkCursor - 1);
    }

    public int getFs() {
        return fs;
    }

    public void setFs(int fs) {
        this.fs = fs;
    }

    public double next() {
        cursor++;
        return data[cursor];
    }

    public double previous() {
        cursor--;
        return data[cursor];
    }

    public boolean hasNext() {
        return cursor < data.length - 1 && data.length > 0;
    }

    public boolean hasPrevious() {
        return cursor > 0 && data.length > 0;
    }

    public boolean isEmpty() {
        return data.length == 0;
    }

    public double[] getData() {
        return data;
    }

    public int getPosition() {
        return cursor;
    }

    public int getChunkPosition() {
        return chunkCursor;
    }
}
