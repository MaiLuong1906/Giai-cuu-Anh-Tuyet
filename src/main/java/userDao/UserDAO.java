package userDao;

import dao.DBConnection;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {

    private static final String INSERT_USER = "INSERT INTO Users (username, email, country, role, status, password, dob) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM Users WHERE id = ?";
    private static final String UPDATE_USER = "UPDATE Users SET username = ?, email = ?, country = ?, role = ?, status = ?, password = ?, dob = ? WHERE id = ?";
    private static final String DELETE_USER = "UPDATE Users SET status = ? WHERE id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM Users WHERE status = 1";
    private static final String LOGIN = "SELECT * FROM Users WHERE username = ? AND password = ?";

    @Override
    public void insertUser(User user) throws SQLException {
        try (Connection connection = DBConnection.getConnection(); 
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.setBoolean(5, user.isStatus());
            preparedStatement.setString(6, user.getPassword());
            preparedStatement.setDate(7, user.getDob());
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new user was inserted successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User selectUser(int id) {
        User user = null;
        try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user = extractUserFromResultSet(rs);
            } else {
                System.out.println("User not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean deleteUser(int id) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_USER)) {
            statement.setBoolean(1, false);
            statement.setInt(2, id);
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_USER)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());
            statement.setString(4, user.getRole());
            statement.setBoolean(5, user.isStatus());
            statement.setString(6, user.getPassword());
            statement.setDate(7, user.getDob());
            statement.setInt(8, user.getId());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    @Override
    public User checkLogin(String username, String password) {
        User user = null;
        try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(LOGIN)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user = extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String email = rs.getString("email");
        String country = rs.getString("country");
        String role = rs.getString("role");
        boolean status = rs.getBoolean("status");
        String password = rs.getString("password");
        Date dob = rs.getDate("dob");
        return new User(id, username, email, country, role, status, password, dob);
    }
}
