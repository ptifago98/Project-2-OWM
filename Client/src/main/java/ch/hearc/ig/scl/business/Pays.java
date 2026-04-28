package ch.hearc.ig.scl.business;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pays implements Serializable {
    private Integer numero;
    private String name;
    private String code;

    public Pays(){

    }
    public Pays(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Pays {" +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
