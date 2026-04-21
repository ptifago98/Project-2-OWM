package ch.hearc.ig.scl.business;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StationMeteo implements Serializable {
    static Integer num = 0;
    private Integer numero;
    private String idStation;
    private Integer timeZone;
    private Pays pays;
    private Double latitude;
    private Double longitude;
    private String nom;
    private Map<Date, Meteo> WeatherMap;

    public StationMeteo() {
        this.numero = num++;
    }


    public StationMeteo(String id, Integer timeZone, Pays pays, Double latitude, Double longitude, String nom) {
        this.numero = num++;
        this.idStation = id;
        this.timeZone = timeZone;
        this.pays = pays;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nom = nom;

        this.WeatherMap = new HashMap<>();
    }

    public String getIdStation() {
        return idStation;
    }

    public void setIdStation(String idStation) {
        this.idStation = idStation;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(Integer timeZone) {
        this.timeZone = timeZone;
    }

    public Pays getPays() {
        return pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Map<Date, Meteo> getWeatherMap() {
        return WeatherMap;
    }

    public void setWeatherMap(Map<Date, Meteo> weatherMap) {
        WeatherMap = weatherMap;
    }

    public void addWeather(Meteo meteo){
        this.WeatherMap.put(meteo.getDate(),meteo);
    }

    @Override
    public String toString() {
        return "StationMeteo {" +
                "numero=" + numero +
                ", timeZone=" + timeZone +
                ", pays=" + pays +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", nom='" + nom + '\'' +
                '}';
    }
}
