package nl.hannito.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Cacheable(true)
@Table(name = "difficultylevels")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DifficultyLevel.findAll", query = "SELECT d FROM DifficultyLevel d"),
    @NamedQuery(name = "DifficultyLevel.findLowercase", query = "SELECT d FROM DifficultyLevel d WHERE lower(d.difficulty) = lower(:difficulty)")})
public class DifficultyLevel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "difficulty")
    private String difficulty;

    public DifficultyLevel() {
    }

    public DifficultyLevel(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
