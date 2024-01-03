package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {


    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        return null;
    }

    @Override
    public List<ShoppingCartItem> getAllItemsInCart(int userId) {
        String query = "SELECT * FROM shopping_cart_items WHERE user_id = ?";
        List<ShoppingCartItem> cartItems = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    int quantity = rs.getInt("quantity");

                    // You might need to retrieve additional information about the product
                    // from the 'products' table and create a ShoppingCartItem instance
                    // with the fetched data.


                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }

        return cartItems;
    }

    @Override
    public void addProductToCart(int userId, int productId, int quantity) {

    }

    @Override
    public void updateProductInCart(int userId, int productId, int quantity) {

    }

    @Override
    public void clearCart(int userId) {

    }
}
