package com.thoughtworks.pos.services.services;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.pos.domains.Item;
import com.thoughtworks.pos.domains.ShoppingChart;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Administrator on 2015/1/2.
 */
public class InputParser {
    private File file;
    private File file2;
    private final ObjectMapper objectMapper;

    public InputParser(File file) {
        this.file = file;
        objectMapper = new ObjectMapper(new JsonFactory());
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }
    public InputParser(File file,File file2) {
        this.file = file;
        this.file2=file2;
        objectMapper = new ObjectMapper(new JsonFactory());
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }
    public ShoppingChart parser() throws IOException {
        String textInput = FileUtils.readFileToString(file);
        Item[] items = objectMapper.readValue(textInput, Item[].class);
        return BuildShoppingChart(items);
    }
    private ShoppingChart BuildShoppingChart(Item[] items) {
        ShoppingChart shoppingChart = new ShoppingChart();
        for (Item item : items) {
            shoppingChart.add(item);
        }
        return shoppingChart;
    }
  //需求3新增
    public ShoppingChart parsertwofile() throws IOException {
        int num_product=0;
        String textInput_index = FileUtils.readFileToString(file);
        String textInput_list = FileUtils.readFileToString(file2);
        JsonNode rootNode_list = objectMapper.readTree(textInput_list);
        JsonNode rootNode = objectMapper.readTree(textInput_index);
        Iterator<JsonNode> iterator_list = rootNode_list.elements();
        while (iterator_list.hasNext()) {
            num_product++;
            iterator_list.next();
        }
        String [] Barcode=new String[num_product];
        iterator_list = rootNode_list.elements();
        int i=0;
        while (iterator_list.hasNext()) {
            JsonNode jsonNode =iterator_list.next();
            Barcode[i]=jsonNode.textValue();
            i++;
        }
        rootNode=rootNode.path(0);
        Iterator<JsonNode> iterator_index = rootNode.elements();
        String textInput_sync;
        textInput_sync="[";
        while (iterator_index.hasNext()) {
            JsonNode jsonNode =iterator_index.next();
            textInput_sync+=jsonNode;

            if(!iterator_index.hasNext());
                else textInput_sync+=",";
        }
        textInput_sync+="]";
        Item[]items_index = objectMapper.readValue(textInput_sync, Item[].class);
        Item[]items=new Item[num_product];
        items[0]=items_index[0];
        items[0].setBarcode(Barcode[0]);
        int j=1;
        for( i=1;i<num_product;i++)
        {
            if(Barcode[i].equals(Barcode[i-1]))
                items[i]=items[i-1];
            else {
                items[i]=items_index[j];
                j++;
            }
            items[i].setBarcode(Barcode[i]);
        }
        return BuildShoppingChart(items);
    }
    //d单元测试
//    public static void main(String[] args) throws IOException {
//        InputParser inputParser = new InputParser(new File("src/filesrc/data_index03.json"),new File("src/filesrc/data_list03.json"));
//        inputParser.parsertwofile();
//    }


}