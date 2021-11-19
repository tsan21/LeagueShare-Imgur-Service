package leagueshare.imgurservice.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "imgur")
public class Imgur {
    @Id
    @GeneratedValue
    private Long imgur_id;
    private String link;
    private String deletehash;

    public Imgur() {
    }

    public Imgur(String link, String deletehash) {
        this.link = link;
        this.deletehash = deletehash;
    }
}
