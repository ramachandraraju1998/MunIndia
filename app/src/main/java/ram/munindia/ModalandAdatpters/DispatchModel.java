package ram.munindia.ModalandAdatpters;

public class DispatchModel {

    String productname,color,size,carton,picspercartton;

    public DispatchModel(String productname, String color, String size, String carton, String picspercartton) {
        this.productname = productname;
        this.color = color;
        this.size = size;
        this.carton = carton;
        this.picspercartton = picspercartton;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setCarton(String carton) {
        this.carton = carton;
    }

    public void setPicspercartton(String picspercartton) {
        this.picspercartton = picspercartton;
    }

    public String getProductname() {
        return productname;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public String getCarton() {
        return carton;
    }

    public String getPicspercartton() {
        return picspercartton;
    }


}
