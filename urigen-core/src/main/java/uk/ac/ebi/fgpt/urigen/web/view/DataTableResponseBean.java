package uk.ac.ebi.fgpt.urigen.web.view;

/**
 * @author Simon Jupp
 * @date 29/02/2012
 * Functional Genomics Group EMBL-EBI
 */
public class DataTableResponseBean {

    private String sEcho;
    private String iTotalRecords;
    private String iTotalDisplayRecords;
    private String iDisplayLength;

    private String [][] aaData;


    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public String getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(String iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }


    public String getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(String iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public String getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(String iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public String[][] getAaData() {
        return aaData;
    }

    public void setAaData(String[][] aaData) {
        this.aaData = aaData;
    }

    public DataTableResponseBean(String sEcho, String iTotalRecords, String iTotalDisplayRecords, String[][] aaData) {

        this.sEcho = sEcho;
        this.iTotalRecords = iTotalRecords;
        this.iTotalDisplayRecords = iTotalDisplayRecords;
        this.aaData = aaData;
    }

    public DataTableResponseBean() {

    }
}
