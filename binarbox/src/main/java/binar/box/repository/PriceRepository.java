package binar.box.repository;


import binar.box.domain.Price;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends BaseJpaRepository<Price, Long> {
}
