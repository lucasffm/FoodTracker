package br.com.projetofinal.foodtracker.modelo;

/**
 * Created by Lucas on 30/09/2016.
 */
public class QrCode {
    private int id_qrcode;
    private String numero_qrcode;
    private String status;

    public QrCode() {
    }

    public QrCode(int id_qrcode, String numero_qrcode, String status) {
        this.id_qrcode = id_qrcode;
        this.numero_qrcode = numero_qrcode;
        this.status = status;
    }

    public int getId_qrcode() {
        return id_qrcode;
    }

    public void setId_qrcode(int id_qrcode) {
        this.id_qrcode = id_qrcode;
    }

    public String getNumero_qrcode() {
        return numero_qrcode;
    }

    public void setNumero_qrcode(String numero_qrcode) {
        this.numero_qrcode = numero_qrcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
