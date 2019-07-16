package ram.munindia.ModalandAdatpters;

public class DispatchModel {

    String saleorder,barcodenumber,prepareddate,dispatcheddate,qty,dispatchedstatus;

    public DispatchModel(String saleorder, String barcodenumber, String prepareddate, String dispatcheddate, String qty, String dispatchedstatus) {
        this.saleorder = saleorder;
        this.barcodenumber = barcodenumber;
        this.prepareddate = prepareddate;
        this.dispatcheddate = dispatcheddate;
        this.qty = qty;
        this.dispatchedstatus = dispatchedstatus;
    }

    public String getSaleorder() {
        return saleorder;
    }

    public void setSaleorder(String saleorder) {
        this.saleorder = saleorder;
    }

    public String getBarcodenumber() {
        return barcodenumber;
    }

    public void setBarcodenumber(String barcodenumber) {
        this.barcodenumber = barcodenumber;
    }

    public String getPrepareddate() {
        return prepareddate;
    }

    public void setPrepareddate(String prepareddate) {
        this.prepareddate = prepareddate;
    }

    public String getDispatcheddate() {
        return dispatcheddate;
    }

    public void setDispatcheddate(String dispatcheddate) {
        this.dispatcheddate = dispatcheddate;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDispatchedstatus() {
        return dispatchedstatus;
    }

    public void setDispatchedstatus(String dispatchedstatus) {
        this.dispatchedstatus = dispatchedstatus;
    }
}
