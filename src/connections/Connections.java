package connections;

import entity.Category;
import entity.Item;
import entity.Role;
import entity.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Connections {
    private static final String PROPERTIES_FILE = "database.properties";

    private static Connections instance;

    private Connections() {
    }

    public static synchronized Connections getInstance() {
        if (instance == null) {
            instance = new Connections();
        }
        return instance;
    }

    private Connection getConnection() {
        try {
            Properties properties = getDatabaseProperties();

            String database = properties.getProperty("db.name");
            String url = "jdbc:mysql://localhost:3306/" + database + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Properties getDatabaseProperties() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(PROPERTIES_FILE)) {
            properties.load(fileInputStream);
        }
        return properties;
    }

//    CATEGORY
    public Category getCategoryByName(String name) {
        Category category = null;
        try(Connection connection = getConnection()) {
            String query = "SELECT * FROM categories WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("category_id");
                String categoryName = resultSet.getString("name");
                category = new Category(id, categoryName);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }

    public Category getCategoryById(int id) {
        Category category = null;
        try(Connection connection = getConnection()) {
            String query = "SELECT * FROM categories WHERE category_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                category = new Category();
                category.setId(resultSet.getInt("category_id"));
                category.setName(resultSet.getString("name"));
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try(Connection connection = getConnection()) {
            String query = "SELECT * FROM categories ORDER BY category_id";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Category category = new Category();
                category.setId(resultSet.getInt("category_id"));
                category.setName(resultSet.getString("name"));
                categories.add(category);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    public boolean categoryExistsByName(String name) {
        Category category = getCategoryByName(name);
        return category != null;
    }

    public int saveCategory(Category category) {
        try(Connection connection = getConnection()) {
            String query = "INSERT INTO categories(name) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, category.getName());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int updateCategory(int id, String name) {
        try(Connection connection = getConnection()) {
            String query = "UPDATE categories SET name = ? WHERE category_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteCategory(String name) {
        try(Connection connection = getConnection()) {
            String query = "DELETE FROM categories WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//     USER
    public User getUserByUsername(String username) {
        User user = null;
        try(Connection connection = getConnection()) {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(Role.valueOf(resultSet.getString("role")));
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public User getUserById(int id) {
        User user = null;
        try(Connection connection = getConnection()) {
            String query = "SELECT * FROM users WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(Role.valueOf(resultSet.getString("role")));
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public boolean userExistsByUsername(String username) {
        User user = getUserByUsername(username);
        return user != null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try(Connection connection = getConnection()) {
            String query = "SELECT * FROM users";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(Role.valueOf(resultSet.getString("role")));
                users.add(user);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public List<User> searchUsersByUsername(String username) {
        List<User> users = new ArrayList<>();
        try(Connection connection = getConnection()) {
            String query = "SELECT * FROM users WHERE username LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + username + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(Role.valueOf(resultSet.getString("role")));
                users.add(user);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public int saveUser(User user) {
        try(Connection connection = getConnection()) {
            String query = "INSERT INTO users(name, username, password, role) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getRole().name());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int updateUser(int id, User user) {
        try(Connection connection = getConnection()) {
            String query = "UPDATE users SET name = ?, username = ?, password = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, id);

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteUser(String username) {
        try(Connection connection = getConnection()) {
            String query = "DELETE FROM users WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    ITEMS
    public int getNextItemId() {
        int nextId = 1;
        String query = "SELECT MAX(item_id) FROM items";
        try(Connection connection = getConnection();Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                nextId = resultSet.getInt(1) + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nextId;
    }

    public int saveItem(Item item) {
        try(Connection connection = getConnection()) {
            String query = "INSERT INTO items(name, brand, model, sku, quantity, price, category_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, item.getName());
            preparedStatement.setString(2, item.getBrand());
            preparedStatement.setString(3, item.getModel());
            preparedStatement.setString(4, item.getSku());
            preparedStatement.setInt(5, item.getQuantity());
            preparedStatement.setDouble(6, item.getPrice());
            preparedStatement.setInt(7, item.getCategory().getId());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Item getItemBySku(String sku) {
        Item item = null;
        try(Connection connection = getConnection()) {
            String query = "SELECT * FROM items WHERE sku = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, sku);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                item = new Item();
                Category category = getCategoryById(resultSet.getInt("category_id"));
                item.setId(resultSet.getInt("item_id"));
                item.setName(resultSet.getString("name"));
                item.setBrand(resultSet.getString("brand"));
                item.setModel(resultSet.getString("model"));
                item.setSku(resultSet.getString("sku"));
                item.setQuantity(resultSet.getInt("quantity"));
                item.setPrice(resultSet.getDouble("price"));
                item.setCategory(category);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return item;
    }

    public boolean ItemExistsBySku(String sku) {
        Item item = getItemBySku(sku);
        return item != null;
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        try(Connection connection = getConnection()) {
            String query = "SELECT * FROM items";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Item item = new Item();
                item.setId(resultSet.getInt("item_id"));
                item.setName(resultSet.getString("name"));
                item.setBrand(resultSet.getString("brand"));
                item.setModel(resultSet.getString("model"));
                item.setSku(resultSet.getString("sku"));
                item.setQuantity(resultSet.getInt("quantity"));
                item.setPrice(resultSet.getDouble("price"));
                item.setCategory(getCategoryById(resultSet.getInt("category_id")));
                items.add(item);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    public List<Item> searchItemsByName(String name) {
        List<Item> items = new ArrayList<>();
        try(Connection connection = getConnection()) {
            String query = "SELECT * FROM items WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + name + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Item item = new Item();
                item.setId(resultSet.getInt("item_id"));
                item.setName(resultSet.getString("name"));
                item.setBrand(resultSet.getString("brand"));
                item.setModel(resultSet.getString("model"));
                item.setSku(resultSet.getString("sku"));
                item.setQuantity(resultSet.getInt("quantity"));
                item.setPrice(resultSet.getDouble("price"));
                item.setCategory(getCategoryById(resultSet.getInt("category_id")));
                items.add(item);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    public int updateItem(String sku, Item item) {
        try(Connection connection = getConnection()) {
            String query = "UPDATE items SET name = ?, brand = ?, model = ?, quantity = ?, price = ?, category_id = ? WHERE sku = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, item.getName());
            preparedStatement.setString(2, item.getBrand());
            preparedStatement.setString(3, item.getModel());
            preparedStatement.setInt(4, item.getQuantity());
            preparedStatement.setDouble(5, item.getPrice());
            preparedStatement.setInt(6, item.getCategory().getId());
            preparedStatement.setString(7, sku);

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteItem(String sku) {
        try(Connection connection = getConnection()) {
            String query = "DELETE FROM items WHERE sku = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, sku);

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
