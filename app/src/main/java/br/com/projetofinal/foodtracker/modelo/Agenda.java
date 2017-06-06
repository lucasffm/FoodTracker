package br.com.projetofinal.foodtracker.modelo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Date;

/**
 *
 * @author Lucas
 */
public class Agenda {
    private int id_agenda;
    private Double latitude;
    private Double longitude;
    private Date data;
    private Date hora_inicio;
    private Date hora_fim;
    private Carro carro;

    public Agenda() {
    }

    public Agenda(int id_agenda, Double latitude, Double longitude, Date data, Date hora_inicio, Date hora_fim, Carro carro) {
        this.id_agenda = id_agenda;
        this.latitude = latitude;
        this.longitude = longitude;
        this.data = data;
        this.hora_inicio = hora_inicio;
        this.hora_fim = hora_fim;
        this.carro = carro;
    }

    public int getId_agenda() {
        return id_agenda;
    }

    public void setId_agenda(int id_agenda) {
        this.id_agenda = id_agenda;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(Date hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public Date getHora_fim() {
        return hora_fim;
    }

    public void setHora_fim(Date hora_fim) {
        this.hora_fim = hora_fim;
    }

    public Carro getCarro() {
        return carro;
    }

    public void setCarro(Carro carro) {
        this.carro = carro;
    }
}
