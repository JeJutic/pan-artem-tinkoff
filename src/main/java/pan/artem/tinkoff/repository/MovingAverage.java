package pan.artem.tinkoff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pan.artem.tinkoff.entity.MovingAverageRecord;

public interface MovingAverage extends JpaRepository<MovingAverageRecord, Long> {

    @Modifying
    @Query("DELETE FROM MovingAverageRecord ma WHERE ma.id IN " +
            "(SELECT m.id FROM MovingAverageRecord m ORDER BY m.id LIMIT 1)")
    void deleteLatest();
}
