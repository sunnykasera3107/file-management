package com.manager.files.analysis.model;

import lombok.Data;

@Data
public class FileMetaData {
    
    private double min;
    private double max;
    private double sum;
    private long count;
    private long nullCount;
    private String columnName;
    private String dataType;

    public FileMetaData() {
        min = Double.MAX_VALUE;
        max = Double.MIN_VALUE;
        sum = 0.0;
        count = 0;
        nullCount = 0;
    }

}
