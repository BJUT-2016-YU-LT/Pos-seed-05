import com.thoughtworks.pos.common.EmptyShoppingCartException;
import com.thoughtworks.pos.domains.Item;
import com.thoughtworks.pos.domains.ShoppingChart;
import com.thoughtworks.pos.services.services.InputParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ZXR on 2016/6/24.
 */
public class TestFileInputS03 {
    public static void main(String[] args) throws IOException, EmptyShoppingCartException {
        TestFileInputS03 testFileInputS03=new TestFileInputS03();
        testFileInputS03.inputandoutput();
    }
    public void inputandoutput() throws IOException, EmptyShoppingCartException {
        InputParser inputParser = new InputParser(new File("src/filesrc/data_index03.json"),new File("src/filesrc/data_list03.json"));
        ArrayList<Item> items = inputParser.parsertwofile().getItems();
        ShoppingChart shoppingChart=new ShoppingChart();
        for(Item item:items)
            shoppingChart.add(item);
        com.thoughtworks.pos.domains.Pos pos = new com.thoughtworks.pos.domains.Pos();
        System.out.println(pos.getShoppingList(shoppingChart));
    }

}