package lk.ijse.dep7.pos.dao;

import lk.ijse.dep7.pos.dto.ItemDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    private Connection connection;

    public ItemDAO(Connection connection){
        this.connection = connection;
    }

    public void saveItem(ItemDTO item) {
        try {
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO item (code, description, unit_price, qty_on_hand) VALUES (?,?,?,?)");
            pstm.setString(1, item.getCode());
            pstm.setString(2, item.getDescription());
            pstm.setBigDecimal(3, item.getUnitPrice());
            pstm.setInt(4, item.getQtyOnHand());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save the Item");
        }
    }

    public boolean existsItem(String code) {
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT code FROM item WHERE code=?");
            pstm.setString(1, code);
            return pstm.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException("Failed operation");
        }
    }

    public void updateItem(ItemDTO item) {
        try {
            PreparedStatement pstm = connection.prepareStatement("UPDATE item SET description=?, unit_price=?, qty_on_hand=? WHERE code=?");
            pstm.setString(1, item.getDescription());
            pstm.setBigDecimal(2, item.getUnitPrice());
            pstm.setInt(3, item.getQtyOnHand());
            pstm.setString(4, item.getCode());
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update the Item " + item.getCode(), e);
        }
    }

    public void deleteItem(String code) {
        try {
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM item WHERE code=?");
            pstm.setString(1, code);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete the Item " + code, e);
        }
    }

    public ItemDTO findItem(String code) {
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM item WHERE code=?");
            pstm.setString(1, code);
            ResultSet rst = pstm.executeQuery();
            rst.next();
            return new ItemDTO(code, rst.getString("description"), rst.getBigDecimal("unit_price"), rst.getInt("qty_on_hand"));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find the Item " + code, e);
        }
    }

    public List<ItemDTO> findAllItems(){
        try {
            List<ItemDTO> itemList = new ArrayList<>();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM item");

            while (rst.next()) {
                itemList.add(new ItemDTO(rst.getString("code"), rst.getString("description"), rst.getBigDecimal("unit_price"), rst.getInt("qty_on_hand")));
            }

            return itemList;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find Items", e);
        }
    }

    public List<ItemDTO> findAllItems(int page, int size){
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM item LIMIT ? OFFSET ?;");
            stm.setObject(1, size);
            stm.setObject(2, size * (page - 1));

            ResultSet rst = stm.executeQuery();
            List<ItemDTO> itemList = new ArrayList<>();

            while (rst.next()) {
                itemList.add(new ItemDTO(rst.getString("code"),
                        rst.getString("description"),
                        rst.getBigDecimal("unit_price"),
                        rst.getInt("qty_on_hand")));
            }
            return itemList;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch Items", e);
        }
    }

    public String getLastItemCode(){
        try {
            ResultSet rst = connection.createStatement().executeQuery("SELECT code FROM item ORDER BY code DESC LIMIT 1;");

            if (rst.next()) {
                return rst.getString("code");
            } else {
                return null;
            }
        }catch (Exception e){
            throw new RuntimeException("Failed operation");
        }
    }
    
}
