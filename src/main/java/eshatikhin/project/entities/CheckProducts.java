package eshatikhin.project.entities;

public class CheckProducts {
    private int check_id;
    private int product_id;
    private float quantity;

    public CheckProducts() {
    }

    public CheckProducts(int check_id, int product_id, float quantity) {
        this.check_id = check_id;
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public int getCheck_id() {
        return check_id;
    }

    public void setCheck_id(int check_id) {
        this.check_id = check_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}
