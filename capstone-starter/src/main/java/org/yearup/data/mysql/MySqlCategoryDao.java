package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM Categories";

        try(Connection connection = getConnection();
        PreparedStatement ps = getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                int categoryId = rs.getInt("category_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                Category category = new Category(categoryId, name, description);
                categories.add(category);
            }
        } catch(Exception ex){
            ex.printStackTrace();
        }

        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
       String query = "SELECT * FROM categories WHERE category_id = ?";

       try(Connection connection = getConnection();
       PreparedStatement ps = connection.prepareStatement(query)){

           ps.setInt(1, categoryId);

           try (ResultSet rs = ps.executeQuery()) {
               if (rs.next()) {
                   // Extract data from the result set and create a Category object
                   int id = rs.getInt("category_id");
                   String name = rs.getString("category_name");
                   String description = rs.getString("category_description");

                   // Create and return a Category object
                   return new Category(id, name, description);
               } else {
                   // Handle the case where no category with the specified ID is found
                   return null;
               }
           }
       } catch (SQLException e) {
           // Handle any SQL-related exceptions
           e.printStackTrace(); // Consider logging the exception
           throw new RuntimeException("Error retrieving category by ID", e);
       }
    }

    @Override
    public Category create(Category category)
    {
        String query = "INSERT INTO categories (category_name, category_description) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                // Retrieve the auto-generated key (if any)
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        category.setCategoryId(generatedId); // Set the generated ID in the Category object
                        return category;
                    }
                }
            }

            // Handle the case where the insertion was not successful
            throw new RuntimeException("Error creating category. No ID generated.");

        } catch (SQLException e) {
            // Handle any SQL-related exceptions
            e.printStackTrace(); // Consider logging the exception
            throw new RuntimeException("Error creating category", e);
        }
    }

    @Override
    public void update(int categoryId, Category category)
    {
        String query = "UPDATE categories SET category_name = ?, category_description = ? WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, categoryId);  // Set the category_id in the WHERE clause

            int affectedRows = ps.executeUpdate();

            if (affectedRows <= 0) {
                // Handle the case where no category with the specified ID was found for updating
                throw new RuntimeException("No category found for updating with ID: " + categoryId);
            }

        } catch (SQLException e) {
            // Handle any SQL-related exceptions
            e.printStackTrace(); // Consider logging the exception
            throw new RuntimeException("Error updating category", e);
        }
    }

    @Override
    public void delete(int categoryId)
    {
        String query = "DELETE FROM categories WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, categoryId);  // Set the category_id in the WHERE clause

            int affectedRows = ps.executeUpdate();

            if (affectedRows <= 0) {
                // Handle the case where no category with the specified ID was found for deletion
                throw new RuntimeException("No category found for deletion with ID: " + categoryId);
            }

        } catch (SQLException e) {
            // Handle any SQL-related exceptions
            e.printStackTrace(); // Consider logging the exception
            throw new RuntimeException("Error deleting category", e);
        }
    }

    @Override
    public Category addCategory(Category category) {
        String query = "INSERT INTO categories (category_name, category_description) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                // Retrieve the auto-generated key (if any)
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        category.setCategoryId(generatedId); // Set the generated ID in the Category object
                        return category;
                    }
                }
            }

            // Handle the case where the insertion was not successful
            throw new RuntimeException("Error adding category. No ID generated.");

        } catch (SQLException e) {
            // Handle any SQL-related exceptions
            e.printStackTrace(); // Consider logging the exception
            throw new RuntimeException("Error adding category", e);
        }
    }

    @Override
    public void updateCategory(int id, Category category) {
        String query = "UPDATE categories SET category_name = ?, category_description = ? WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, id);  // Set the category_id in the WHERE clause

            int affectedRows = ps.executeUpdate();

            if (affectedRows <= 0) {
                // Handle the case where no category with the specified ID was found for updating
                throw new RuntimeException("No category found for updating with ID: " + id);
            }

        } catch (SQLException e) {
            // Handle any SQL-related exceptions
            e.printStackTrace(); // Consider logging the exception
            throw new RuntimeException("Error updating category", e);
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
