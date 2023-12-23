package 订单;

public class Product {
    private int productId;
    private String productInfo;
    private String productName;
    private double productPrice;

    public Product(int productId, String productInfo,String productName, double productPrice) {
        this.productId = productId;
        this.productInfo = productInfo;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public Product(int productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    public Product() {
    }

    /**
     * 获取
     * @return productId
     */
    public int getProductId() {
        return productId;
    }

    /**
     * 设置
     * @param productId
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * 获取
     * @return productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置
     * @param productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获取
     * @return productPrice
     */
    public double getProductPrice() {
        return productPrice;
    }

    /**
     * 设置
     * @param productPrice
     */
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }


    /**
     * 获取
     * @return productInfo
     */
    public String getProductInfo() {
        return productInfo;
    }

    /**
     * 设置
     * @param productInfo
     */
    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public String toString() {
        return "Product{productId = " + productId + ", productInfo = " + productInfo + ", productName = " + productName + ", productPrice = " + productPrice + "}";
    }
}
