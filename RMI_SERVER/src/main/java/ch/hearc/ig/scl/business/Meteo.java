package ch.hearc.ig.scl.business;
import java.io.Serializable;
import java.util.Date;

public class Meteo implements Serializable {
    private Integer numero;
    private String description;
    private Date date;
    private Double temperature;
    private Double temperatureRessentie;
    private Integer pression;
    private Double humidite;
    private Double ventVitesse;
    private Double ventOrientation;
    private String icon;

    public Meteo(String description, Date date, Double temperature, Double temperatureRessentie, Integer pression, Double humidite, Double ventVitesse, Double ventOrientation, String icon) {
        this.description = description;
        this.date = date;
        this.temperature = temperature;
        this.temperatureRessentie = temperatureRessentie;
        this.pression = pression;
        this.humidite = humidite;
        this.ventVitesse = ventVitesse;
        this.ventOrientation = ventOrientation;
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Double getVentOrientation() {
        return ventOrientation;
    }

    public void setVentOrientation(Double ventOrientation) {
        this.ventOrientation = ventOrientation;
    }

    public Double getVentVitesse() {
        return ventVitesse;
    }

    public void setVentVitesse(Double ventVitesse) {
        this.ventVitesse = ventVitesse;
    }

    public Double getHumidite() {
        return humidite;
    }

    public void setHumidite(Double humidite) {
        this.humidite = humidite;
    }

    public Integer getPression() {
        return pression;
    }

    public void setPression(Integer pression) {
        this.pression = pression;
    }

    public Double getTemperatureRessentie() {
        return temperatureRessentie;
    }

    public void setTemperatureRessentie(Double temperatureRessentie) {
        this.temperatureRessentie = temperatureRessentie;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return String.format(
                "[%s] %s | %.1f°C (ressenti %.1f°C) | Humidité %.0f%% | Pression %d hPa | Vent %.1f m/s à %.0f°",
                date, description, temperature, temperatureRessentie,
                humidite, pression, ventVitesse, ventOrientation);
    }
}
