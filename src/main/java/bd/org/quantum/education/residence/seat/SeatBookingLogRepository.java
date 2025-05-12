package bd.org.quantum.education.residence.seat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatBookingLogRepository extends JpaRepository<SeatBookingLog, Long>, JpaSpecificationExecutor<SeatBookingLog> {
}
