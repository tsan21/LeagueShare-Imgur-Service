package leagueshare.imgurservice.repo;

import leagueshare.imgurservice.entities.Imgur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImgurRepo  extends JpaRepository<Imgur, Long> {

}
