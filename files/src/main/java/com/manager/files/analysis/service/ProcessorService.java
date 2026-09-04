package com.manager.files.analysis.service;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import com.manager.files.analysis.model.FileMetaData;

@Component
public class ProcessorService {
    
    private boolean isHeader;

    private Map<String, FileMetaData> columnMetaData;
    private Map<Integer, String> columnKeys;

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]+");
    private static final Pattern EDGES = Pattern.compile("^-|-$");

    public ProcessorService() {
        isHeader = true;
        columnMetaData = new TreeMap<>();
        columnKeys = new TreeMap<>();
    }

    public Map<String, FileMetaData> analyse(Row row) {
        if (isHeader) {
            for (Cell cell: row) {
                FileMetaData columnData = new FileMetaData();
                String slug = toSlug(cell.toString());
                columnData.setColumnName(cell.toString());
                columnMetaData.put(slug, columnData);
                columnKeys.put(cell.getColumnIndex(), slug);
            }
            isHeader = false;
            return columnMetaData;
        }

        
        for (Cell cell: row) {
            if (cell.toString().isBlank()) {
                continue;
            }
            String slug = columnKeys.get(cell.getColumnIndex());
            FileMetaData columnData = columnMetaData.get(slug);

            columnData.setCount(columnData.getCount() + 1);
           
            if (cell == null || cell.getCellType() == CellType.BLANK) {
                columnData.setNullCount(columnData.getNullCount() + 1);
            }

            columnData.setDataType(cell.getCellType().toString());

            switch (cell.getCellType().toString().toLowerCase()) {
                case "integer":
                case "double":
                case "long":
                case "float":
                case "numeric":
                    columnData.setMin(getMinimum(columnData.getMin(), cell.getNumericCellValue()));
                    columnData.setMax(getMaximum(columnData.getMax(), cell.getNumericCellValue()));
                    columnData.setSum(columnData.getSum() + cell.getNumericCellValue());
                    break;
               
                default:
                    break;
            }

            columnMetaData.put(slug, columnData);

        }
        

        return columnMetaData;
        
    }

    public double getMinimum(double first, double second) {
        return first <= second ? first : second;
    }

    public double getMaximum(double first, double second) {
        return first >= second ? first : second;
    }

    public static String toSlug(String input) {
        if (input == null) {
            return "";
        }
        
        String noWhiteSpace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhiteSpace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        slug = EDGES.matcher(slug).replaceAll("");
        
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
 