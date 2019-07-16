package ram.munindia.ModalandAdatpters;

public class ScanInputModel {
    String barcode_no,product_varient_name,product_code,product_category,sub_category,color,sizes,grammage,qty,id;

    public ScanInputModel(String barcode_no, String product_varient_name, String product_code, String product_category, String sub_category, String color, String sizes, String grammage,String qty,String id) {
        this.barcode_no = barcode_no;
        this.product_varient_name = product_varient_name;
        this.product_code = product_code;
        this.product_category = product_category;
        this.sub_category = sub_category;
        this.color = color;
        this.sizes = sizes;
        this.grammage = grammage;
        this.qty=qty;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBarcode_no() {
        return barcode_no;
    }

    public void setBarcode_no(String barcode_no) {
        this.barcode_no = barcode_no;
    }

    public String getProduct_varient_name() {
        return product_varient_name;
    }

    public void setProduct_varient_name(String product_varient_name) {
        this.product_varient_name = product_varient_name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSizes() {
        return sizes;
    }

    public void setSizes(String sizes) {
        this.sizes = sizes;
    }

    public String getGrammage() {
        return grammage;
    }

    public void setGrammage(String grammage) {
        this.grammage = grammage;
    }
}
