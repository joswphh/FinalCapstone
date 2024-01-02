package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;
@RestController
@RequestMapping("/categories")
@CrossOrigin
@PreAuthorize("permitAll()")
// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
// http://localhost:8080/categories
// add annotation to allow cross site origin requests
public class CategoriesController
{
    private CategoryDao categoryDao;
    private ProductDao productDao;

    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao){
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }
    // create an Autowired controller to inject the categoryDao and ProductDao

    // add the appropriate annotation for a get action

    @GetMapping
    @PreAuthorize("permitAll()")
    public List<Category> getAll()
    {
        // find and return all categories
        var categories = categoryDao.getAllCategories();
        return categories;
    }

    // add the appropriate annotation for a get action
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public Category getById(@PathVariable int id)
    {
        // get the category by id
        return categoryDao.getById(id);
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    @PreAuthorize("permitAll()")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        return (List<Product>) productDao.getById(categoryId);
    }

    // add annotation to call this method for a
    // POST action
    // add annotation to ensure that only an ADMIN can call this function
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Category addCategory(@RequestBody Category category)
    {
        // insert the category
        return categoryDao.addCategory(category);
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function



    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        categoryDao.updateCategory(id, category);
        // update the category by id
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(@PathVariable int id)
    {
        categoryDao.delete(id);
        // delete the category by id
    }
}
