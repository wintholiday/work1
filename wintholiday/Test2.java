package 订单;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Test2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("欢迎来到商品管理系统！请输入您的要求！");
            System.out.println("1为添加商品；6为更新商品,7为删除商品;2为查询所有商品；3为创建订单；4为获取所有订单信息；5为删除订单；8为更新订单,0为退出");
            try {
                int n = scanner.nextInt();
                switch (n) {
                    case 0:
                        isRunning = false;
                        System.out.println("已退出商品管理系统！");
                        break;
                    case 1:
                        System.out.println("请输入添加商品的个数");
                        int m = scanner.nextInt();
                        for (int i = 0; i < m; i++) {
                            System.out.println("====== 添加商品 ======");
                            System.out.print("请输入商品编号：");
                            int productIdi = scanner.nextInt();
                            System.out.print("请输入介绍：");
                            String productInfo = scanner.next();
                            System.out.print("请输入商品名称：");
                            String productNamei = scanner.next();
                            System.out.print("请输入商品价格：");
                            double productPricei = scanner.nextDouble();
                            OrderManagementSystem.addProduct(productIdi, productInfo,productNamei, productPricei);
                        }
                        break;
                    case 2:
                        // 查询所有商品
                        System.out.println("\n====== 查询所有商品 ======");
                        System.out.print("请输入页码（每页10条记录）：");
                        int page = scanner.nextInt();
                        List<Product> products = OrderManagementSystem.getAllProducts(page);
                        System.out.println("所有商品：");
                        for (Product product : products) {
                            System.out.println(product.getProductId() + "\t" + product.getProductName() + "\t" + product.getProductPrice());
                        }
                        break;
                    case 3:
                        // 创建订单
                        System.out.println("\n====== 创建订单 ======,请输入订单号");
                        int i = scanner.nextInt();
                        List<Product> orderProducts = new ArrayList<>();
                        while (true) {
                            System.out.print("请输入商品编号（输入0结束）：");
                            int productId = scanner.nextInt();
                            if (productId == 0) break;
                            if (!OrderManagementSystem.isProductExist(productId)) {
                                System.out.println("商品编号 " + productId + " 不存在，请重新输入！");
                                continue;
                            }
                            Product product = new Product(productId, "");
                            orderProducts.add(product);
                        }
                        OrderManagementSystem.createOrder(i, orderProducts);
                        break;
                    case 4:
                        // 获取所有订单信息
                        System.out.println("\n====== 获取所有订单信息 ======");
                        List<Order> orders = OrderManagementSystem.getAllOrders();
                        System.out.println("所有订单：");
                        for (Order order : orders) {
                            System.out.print(order.getOrderId() + "\t");
                            for (Product product : order.getProducts()) {
                                System.out.print("(" + product.getProductId() + ":" + product.getProductName() +")");
                            }
                            System.out.println("\t" + order.getOrderTime() + "\t" + order.getOrderPrice());
                        }
                        break;
                    case 5:
                        // 删除订单
                        System.out.println("\n====== 删除订单 ======");
                        System.out.print("请输入要删除的订单编号：");
                        int orderId = scanner.nextInt();
                        OrderManagementSystem.deleteOrder(orderId);
                        break;
                    case 6:
                        // 更新商品信息
                        System.out.println("\n====== 更新商品信息 ======");
                        System.out.print("请输入要更新的商品编号：");
                        int productIdToUpdate = scanner.nextInt();
                        System.out.print("请输入新介绍：");
                        String newProductInfo = scanner.next();
                        System.out.print("请输入新商品名称：");
                        String newProductName = scanner.next();
                        System.out.print("请输入新商品价格：");
                        double newProductPrice = scanner.nextDouble();
                        OrderManagementSystem.updateProduct(productIdToUpdate, newProductInfo, newProductName, newProductPrice);
                        break;
                    case 8:
                        // 更新订单内商品种类
                        System.out.println("\n====== 更新订单内商品种类 ======");
                        System.out.print("请输入要更新的订单号：");
                        int i1 = scanner.nextInt();
                        OrderManagementSystem.deleteOrder(i1);
                        List<Product> orderProducts1 = new ArrayList<>();
                        while (true) {
                            System.out.print("请输入新商品编号（输入0结束）：");
                            int productId = scanner.nextInt();
                            if (productId == 0) break;
                            if (!OrderManagementSystem.isProductExist(productId)) {
                                System.out.println("商品编号 " + productId + " 不存在，请重新输入！");
                                continue;
                            }
                            Product product = new Product(productId, "");
                            orderProducts1.add(product);
                        }
                        OrderManagementSystem.createOrder(i1, orderProducts1);
                        break;
                    case 7:
                        // 删除商品
                        System.out.println("\n====== 删除商品 ======");
                        System.out.print("请输入要删除的商品编号：");
                        int productIdToDelete = scanner.nextInt();
                        OrderManagementSystem.deleteProduct(productIdToDelete);
                        break;
                    default:
                        //System.out.println("输入有误，请重新输入！");
                }
            } catch (InputMismatchException e) {
                System.out.println("输入有误，请输入数字！");
                scanner.nextLine(); // 清除输入缓冲区中的非数字字符
            }
        }
    }
}
