package 订单;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OrderManagementSystem {
    // 添加商品
    public static void addProduct(int productId, String productInfo, String productName, double productPrice) {
        // 判断商品是否已存在
        if (isProductExist(productId)) {
            System.out.println("商品编号 " + productId + " 已存在");
            return;
        }

        String sql = "INSERT INTO products (product_id, product_info, product_name, product_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
                //PreparedStatement 会自动处理参数化查询，将用户输入的内容作为参数传递，而不是将其作为 SQL 语句的一部分执行，从而保护数据库免受恶意注入攻击。
        {
            conn.setAutoCommit(false); // 开启事务
            stmt.setInt(1, productId);
            stmt.setString(2, productInfo);
            stmt.setString(3, productName);
            stmt.setDouble(4, productPrice);
            stmt.executeUpdate();
            conn.commit(); // 提交事务
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除商品
    public static void deleteProduct(int productId) {
        // 判断商品是否存在
        if (!isProductExist(productId)) {
            System.out.println("商品编号 " + productId + " 不存在");
            return;
        }

        String sql = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false); // 开启事务
            stmt.setInt(1, productId);
            stmt.executeUpdate();
            conn.commit(); // 提交事务
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 更新商品信息
    public static void updateProduct(int productId, String newInfo, String newName , double newPrice) {
        String sql = "UPDATE products SET product_name = ?, product_info = ?, product_price = ? WHERE product_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setString(2, newInfo);
            stmt.setDouble(3, newPrice);
            stmt.setInt(4, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询所有商品（分页查询）
    public static List<Product> getAllProducts(int page) {
        List<Product> productList = new ArrayList<>();
        int offset = (page - 1) * 10; // 计算偏移量

        String sql = "SELECT * FROM products LIMIT ?, ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, offset);
            stmt.setInt(2, 10);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productInfo = resultSet.getString("product_info");
                String productName = resultSet.getString("product_name");
                double productPrice = resultSet.getDouble("product_price");

                Product product = new Product(productId, productInfo, productName, productPrice);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }

    // 获取商品价值
    private static double getProductPrice(int productId) {
        double productPrice = 0;
        String sql = "SELECT product_price FROM products WHERE product_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                productPrice = resultSet.getDouble("product_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productPrice;
    }

    // 增加订单
    public static void createOrder(int orderId, List<Product> products) {
        double totalPrice = 0;
        for (Product product : products) {
            // 判断商品是否存在
            if (!isProductExist(product.getProductId())) {
                System.out.println("商品编号 " + product.getProductId() + " 不存在");
                return;
            }
            double productPrice = getProductPrice(product.getProductId());
            product.setProductPrice(productPrice); // 设置商品价格
            totalPrice += product.getProductPrice();
        }

        String orderSql = "INSERT INTO orders (order_id, order_time, order_price) VALUES (?, NOW(), ?)";
        String orderProductSql = "INSERT INTO order_product (order_id, product_id) VALUES (?, ?)";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(orderSql);
             PreparedStatement orderProductStmt = conn.prepareStatement(orderProductSql)) {

            conn.setAutoCommit(false); // 开启事务

            // 插入订单信息
            orderStmt.setInt(1, orderId);
            orderStmt.setDouble(2, totalPrice);
            orderStmt.executeUpdate();

            // 插入订单商品信息
            for (Product product : products) {
                orderProductStmt.setInt(1, orderId);
                orderProductStmt.setInt(2, product.getProductId());
                orderProductStmt.executeUpdate();
            }

            conn.commit(); // 提交事务

            System.out.println("订单创建成功！");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 删除订单
    public static void deleteOrder(int orderId) {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        String orderProductSql = "DELETE FROM order_product WHERE order_id = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             PreparedStatement orderProductStmt = conn.prepareStatement(orderProductSql)) {

            conn.setAutoCommit(false); // 开启事务

            // 删除订单信息
            stmt.setInt(1, orderId);
            stmt.executeUpdate();

            // 删除订单商品信息
            orderProductStmt.setInt(1, orderId);
            orderProductStmt.executeUpdate();

            conn.commit(); // 提交事务

            System.out.println("订单删除成功！");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 获取所有订单
    public static List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT orders.order_id, orders.order_time, orders.order_price, products.product_id, products.product_name " +
                "FROM orders " +
                "LEFT JOIN order_product ON orders.order_id=order_product.order_id " +
                "LEFT JOIN products ON order_product.product_id=products.product_id";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = stmt.executeQuery();

            Map<Integer, Order> orderMap = new HashMap<>();

            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                String orderTime = resultSet.getString("order_time");
                double orderPrice = resultSet.getDouble("order_price");
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("product_name");

                if (!orderMap.containsKey(orderId)) {
                    Order order = new Order(orderId, new ArrayList<>(), orderTime, orderPrice);
                    orderMap.put(orderId, order);
                }

                Product product = new Product(productId, productName);
                orderMap.get(orderId).getProducts().add(product);
            }

            orderList.addAll(orderMap.values());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderList;
    }



    // 对订单按价格排序
    public static List<Order> sortOrdersByPrice(List<Order> orders) {
        List<Order> sortedList = new ArrayList<>(orders);
        Collections.sort(sortedList, (o1, o2) -> Double.compare(o1.getOrderPrice(), o2.getOrderPrice()));
        return sortedList;
    }

    // 对订单按下单时间排序
    public static List<Order> sortOrdersByTime(List<Order> orders) {
        List<Order> sortedList = new ArrayList<>(orders);
        Collections.sort(sortedList, (o1, o2) -> o1.getOrderTime().compareTo(o2.getOrderTime()));
        return sortedList;
    }

    // 检查商品是否存在
    static boolean isProductExist(int productId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM products WHERE product_id = ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 获取商品信息字符串（用于存储到订单）
    private static String getProductInfo(List<Product> products) {
        StringBuilder productInfo = new StringBuilder();

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            productInfo.append(product.getProductId()).append(":").append(product.getProductName());
            if (i != products.size() - 1) {
                productInfo.append(",");
            }
        }

        return productInfo.toString();
    }

    // 解析商品信息字符串
    private static List<Product> parseProductInfo(String productInfo) {
        List<Product> productList = new ArrayList<>();
        String[] products = productInfo.split(",");

        for (String product : products) {
            String[] info = product.split(":");
            int productId = Integer.parseInt(info[0]);
            String productName = info[1];

            Product p = new Product(productId, productName);
            productList.add(p);
        }

        return productList;
    }
}
