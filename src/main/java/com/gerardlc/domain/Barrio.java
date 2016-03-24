package com.gerardlc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Barrio.
 */
@Entity
@Table(name = "barrio")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "barrio")
public class Barrio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nombre")
    private String nombre;
    
    @Column(name = "nombre_csv_origen")
    private String nombreCsvOrigen;
    
    @Column(name = "nombre_csv_destino")
    private String nombreCsvDestino;
    
    @OneToMany(mappedBy = "barrioOrigen")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MigracionBarrio> migracionBarrioOrigens = new HashSet<>();

    @OneToMany(mappedBy = "barrioDestino")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MigracionBarrio> migracionBarrioDestinos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreCsvOrigen() {
        return nombreCsvOrigen;
    }
    
    public void setNombreCsvOrigen(String nombreCsvOrigen) {
        this.nombreCsvOrigen = nombreCsvOrigen;
    }

    public String getNombreCsvDestino() {
        return nombreCsvDestino;
    }
    
    public void setNombreCsvDestino(String nombreCsvDestino) {
        this.nombreCsvDestino = nombreCsvDestino;
    }

    public Set<MigracionBarrio> getMigracionBarrioOrigens() {
        return migracionBarrioOrigens;
    }

    public void setMigracionBarrioOrigens(Set<MigracionBarrio> migracionBarrios) {
        this.migracionBarrioOrigens = migracionBarrios;
    }

    public Set<MigracionBarrio> getMigracionBarrioDestinos() {
        return migracionBarrioDestinos;
    }

    public void setMigracionBarrioDestinos(Set<MigracionBarrio> migracionBarrios) {
        this.migracionBarrioDestinos = migracionBarrios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Barrio barrio = (Barrio) o;
        if(barrio.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, barrio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Barrio{" +
            "id=" + id +
            ", nombre='" + nombre + "'" +
            ", nombreCsvOrigen='" + nombreCsvOrigen + "'" +
            ", nombreCsvDestino='" + nombreCsvDestino + "'" +
            '}';
    }
}
