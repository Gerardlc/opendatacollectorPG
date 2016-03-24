package com.gerardlc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A MigracionBarrio.
 */
@Entity
@Table(name = "migracion_barrio")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "migracionbarrio")
public class MigracionBarrio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "numero_personas", nullable = false)
    private Integer numeroPersonas;
    
    @Column(name = "anyo")
    private Integer anyo;
    
    @ManyToOne
    @JoinColumn(name = "barrio_origen_id")
    private Barrio barrioOrigen;

    @ManyToOne
    @JoinColumn(name = "barrio_destino_id")
    private Barrio barrioDestino;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroPersonas() {
        return numeroPersonas;
    }
    
    public void setNumeroPersonas(Integer numeroPersonas) {
        this.numeroPersonas = numeroPersonas;
    }

    public Integer getAnyo() {
        return anyo;
    }
    
    public void setAnyo(Integer anyo) {
        this.anyo = anyo;
    }

    public Barrio getBarrioOrigen() {
        return barrioOrigen;
    }

    public void setBarrioOrigen(Barrio barrio) {
        this.barrioOrigen = barrio;
    }

    public Barrio getBarrioDestino() {
        return barrioDestino;
    }

    public void setBarrioDestino(Barrio barrio) {
        this.barrioDestino = barrio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MigracionBarrio migracionBarrio = (MigracionBarrio) o;
        if(migracionBarrio.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, migracionBarrio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MigracionBarrio{" +
            "id=" + id +
            ", numeroPersonas='" + numeroPersonas + "'" +
            ", anyo='" + anyo + "'" +
            '}';
    }
}
