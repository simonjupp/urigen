package uk.ac.ebi.fgpt.urigen.web.view;

/**
 * @author Simon Jupp
 * @date 29/02/2012
 * Functional Genomics Group EMBL-EBI
 */
public class DataTableParamPojo {

    public String sEcho;
    public String sKeyword;
    public int iDisplayStart;
    public int iDisplayLength;
    public int iColumns;
    public int iSortingCols;
    public int iSortCol_3;
    public String sSortCol_3;
    public String sSearch;
    public int iSortColumnIndex;
    public String sSortDirection;
    public String sColumns;

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public String getsKeyword() {
        return sKeyword;
    }

    public void setsKeyword(String sKeyword) {
        this.sKeyword = sKeyword;
    }

    public int getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public int getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public int getiColumns() {
        return iColumns;
    }

    public void setiColumns(int iColumns) {
        this.iColumns = iColumns;
    }

    public int getiSortingCols() {
        return iSortingCols;
    }

    public void setiSortingCols(int iSortingCols) {
        this.iSortingCols = iSortingCols;
    }

    public int getiSortCol_3() {
        return iSortCol_3;
    }

    public void setiSortCol_3(int iSortCol_3) {
        this.iSortCol_3 = iSortCol_3;
    }

    public String getsSortCol_3() {
        return sSortCol_3;
    }

    public void setsSortCol_3(String sSortCol_3) {
        this.sSortCol_3 = sSortCol_3;
    }

    public DataTableParamPojo() {

    }

    public DataTableParamPojo(String sEcho, String sKeyword, int iDisplayStart, int iDisplayLength, int iColumns, int iSortingCols, int iSortCol_3, String sSortCol_3) {

        this.sEcho = sEcho;
        this.sKeyword = sKeyword;
        this.iDisplayStart = iDisplayStart;
        this.iDisplayLength = iDisplayLength;
        this.iColumns = iColumns;
        this.iSortingCols = iSortingCols;
        this.iSortCol_3 = iSortCol_3;
        this.sSortCol_3 = sSortCol_3;
    }
}
