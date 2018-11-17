package binar.box.repository;

import binar.box.domain.Point;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends BaseJpaRepository<Point,Long>{

    Point findByXAndY(Double x, Double y);
}
